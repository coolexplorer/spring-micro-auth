package io.coolexplorer.auth.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.coolexplorer.auth.message.SessionMessage;
import io.coolexplorer.auth.service.SessionMessageService;
import io.coolexplorer.auth.service.callback.SessionFutureCallback;
import io.coolexplorer.auth.topic.JwtTokenTopic;
import io.coolexplorer.auth.topic.SessionTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionMessageServiceImpl implements SessionMessageService {
    private final KafkaTemplate<String, Object> kafkaSessionTemplate;
    private final ReplyingKafkaTemplate<String, Object, String> sessionReplyingKafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void createSession(Long accountId, String value, Long expiration) {
        SessionMessage.CreateMessage message = new SessionMessage.CreateMessage();
        message.setAccountId(accountId);
        message.setValues(value);
        message.setExpiration(expiration);
        LOGGER.debug("topic = {}, payload = {}", SessionTopic.TOPIC_CREATE_SESSION, message);

        ListenableFuture<SendResult<String, Object>> listenableFuture = kafkaSessionTemplate.send(SessionTopic.TOPIC_CREATE_SESSION, message);

        listenableFuture.addCallback(new SessionFutureCallback(message));
    }

    @Override
    public SessionMessage.SessionInfo getSession(Long accountId) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
        SessionMessage.RequestMessage message = new SessionMessage.RequestMessage();
        message.setAccountId(accountId);

        LOGGER.debug("topic = {}, payload = {}", JwtTokenTopic.TOPIC_REQUEST_JWT_TOKEN, message);

        ProducerRecord<String, Object> record = new ProducerRecord<>(SessionTopic.TOPIC_REQUEST_SESSION, message);
        RequestReplyFuture<String, Object, String> replyFuture = sessionReplyingKafkaTemplate.sendAndReceive(record);

        SendResult<String, Object> sendResult = replyFuture.getSendFuture().get(10, TimeUnit.SECONDS);
        ConsumerRecord<String, String> consumerRecord = replyFuture.get(10, TimeUnit.SECONDS);

        SessionMessage.SessionInfo sessionInfo = objectMapper.readValue(consumerRecord.value(), SessionMessage.SessionInfo.class);

        LOGGER.debug("Session : {}", sessionInfo);

        return sessionInfo;
    }

    @Override
    public void updateSession(Long accountId, String appendValue, Long expiration) {
        SessionMessage.UpdateMessage message = new SessionMessage.UpdateMessage();
        message.setAccountId(accountId);
        message.setValues(appendValue);
        message.setExpiration(expiration);
        LOGGER.debug("topic = {}, payload = {}", SessionTopic.TOPIC_UPDATE_SESSION, message);

        ListenableFuture<SendResult<String, Object>> listenableFuture = kafkaSessionTemplate.send(SessionTopic.TOPIC_UPDATE_SESSION, message);

        listenableFuture.addCallback(new SessionFutureCallback(message));
    }

    @Override
    public void deleteSession(Long accountId) {
        SessionMessage.DeleteMessage message = new SessionMessage.DeleteMessage();
        message.setAccountId(accountId);

        LOGGER.debug("topic = {}, payload = {}", SessionTopic.TOPIC_DELETE_SESSION, message);

        ListenableFuture<SendResult<String, Object>> listenableFuture = kafkaSessionTemplate.send(SessionTopic.TOPIC_DELETE_SESSION, message);

        listenableFuture.addCallback(new SessionFutureCallback(message));
    }
}
