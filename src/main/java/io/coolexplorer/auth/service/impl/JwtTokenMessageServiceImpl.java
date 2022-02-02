package io.coolexplorer.auth.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.coolexplorer.auth.message.JwtTokenMessage;
import io.coolexplorer.auth.model.Account;
import io.coolexplorer.auth.security.JwtTokenProvider;
import io.coolexplorer.auth.service.JwtTokenMessageService;
import io.coolexplorer.auth.service.callback.JwtTokenFutureCallback;
import io.coolexplorer.auth.topic.JwtTokenTopic;
import io.coolexplorer.auth.utils.DateTimeUtils;
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

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtTokenMessageServiceImpl implements JwtTokenMessageService {
    private final KafkaTemplate<String, Object> kafkaJwtTokenTemplate;
    private final ReplyingKafkaTemplate<String, Object, String> jwtTokenReplyingKafkaTemplate;
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void creteJwtTokenCache(Account account) {
        Date expireDate = jwtTokenProvider.getExpiredDate(account.getJwtToken());

        JwtTokenMessage.CreateMessage createMessage = JwtTokenMessage.CreateMessage.from(account, expireDate);

        LOGGER.debug("topic = {}, payload = {}", JwtTokenTopic.TOPIC_CREATE_JWT_TOKEN, createMessage);

        ListenableFuture<SendResult<String, Object>> listenableFuture = kafkaJwtTokenTemplate.send(JwtTokenTopic.TOPIC_CREATE_JWT_TOKEN, createMessage);

        listenableFuture.addCallback(new JwtTokenFutureCallback(createMessage));
    }

    @Override
    public JwtTokenMessage.JwtTokenInfo getJwtTokenCache(JwtTokenMessage.RequestMessage message) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException {
        LOGGER.debug("topic = {}, payload = {}", JwtTokenTopic.TOPIC_REQUEST_JWT_TOKEN, message);

        ProducerRecord<String, Object> record = new ProducerRecord<>(JwtTokenTopic.TOPIC_REQUEST_JWT_TOKEN, message);
        RequestReplyFuture<String, Object, String> replyFuture = jwtTokenReplyingKafkaTemplate.sendAndReceive(record);

        SendResult<String, Object> sendResult = replyFuture.getSendFuture().get(10, TimeUnit.SECONDS);
        ConsumerRecord<String, String> consumerRecord = replyFuture.get(10, TimeUnit.SECONDS);

        JwtTokenMessage.JwtTokenInfo jwtTokenInfo = objectMapper.readValue(consumerRecord.value(), JwtTokenMessage.JwtTokenInfo.class);

        LOGGER.debug("JwtTokenInfo : {}", jwtTokenInfo);
        return jwtTokenInfo;
    }

    @Override
    public void updateJwtTokenCache(Account account) {
        Date expireDate = jwtTokenProvider.getExpiredDate(account.getJwtToken());

        JwtTokenMessage.UpdateMessage updateMessage = JwtTokenMessage.UpdateMessage.from(account, expireDate);

        LOGGER.debug("topic = {}, payload = {}", JwtTokenTopic.TOPIC_UPDATE_JWT_TOKEN, updateMessage);

        ListenableFuture<SendResult<String, Object>> listenableFuture = kafkaJwtTokenTemplate.send(JwtTokenTopic.TOPIC_UPDATE_JWT_TOKEN, updateMessage);

        listenableFuture.addCallback(new JwtTokenFutureCallback(updateMessage));
    }

    @Override
    public void deleteJwtTokenCache(Long accountId) {
        JwtTokenMessage.DeleteMessage deleteMessage = new JwtTokenMessage.DeleteMessage();
        deleteMessage.setAccountId(accountId);

        LOGGER.debug("topic = {}, payload = {}", JwtTokenTopic.TOPIC_DELETE_JWT_TOKEN, deleteMessage);

        ListenableFuture<SendResult<String, Object>> listenableFuture = kafkaJwtTokenTemplate.send(JwtTokenTopic.TOPIC_DELETE_JWT_TOKEN, deleteMessage);

        listenableFuture.addCallback(new JwtTokenFutureCallback(deleteMessage));
    }
}
