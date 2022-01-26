package io.coolexplorer.auth.service.impl;

import io.coolexplorer.auth.message.JwtTokenMessage;
import io.coolexplorer.auth.service.JwtTokenMessageService;
import io.coolexplorer.auth.service.callback.JwtTokenFutureCallback;
import io.coolexplorer.auth.topic.JwtTokenTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtTokenMessageServiceImpl implements JwtTokenMessageService {
    private final KafkaTemplate<String, Object> kafkaJwtTokenTemplate;

    @Override
    public void creteJwtTokenCache(JwtTokenMessage.CreateMessage message) {
        LOGGER.debug("topic = {}, payload = {}", JwtTokenTopic.TOPIC_CREATE_JWT_TOKEN, message);

        ListenableFuture<SendResult<String, Object>> listenableFuture = kafkaJwtTokenTemplate.send(JwtTokenTopic.TOPIC_CREATE_JWT_TOKEN, message);

        listenableFuture.addCallback(new JwtTokenFutureCallback(message));
    }

    @Override
    public void getJwtTokenCache(JwtTokenMessage.RetrieveMessage message) {
        LOGGER.debug("topic = {}, payload = {}", JwtTokenTopic.TOPIC_RETRIEVE_JWT_TOKEN, message);

        ListenableFuture<SendResult<String, Object>> listenableFuture = kafkaJwtTokenTemplate.send(JwtTokenTopic.TOPIC_RETRIEVE_JWT_TOKEN, message);

        listenableFuture.addCallback(new JwtTokenFutureCallback(message));
    }

    @Override
    public void updateJwtTokenCache(JwtTokenMessage.UpdateMessage message) {
        LOGGER.debug("topic = {}, payload = {}", JwtTokenTopic.TOPIC_UPDATE_JWT_TOKEN, message);

        ListenableFuture<SendResult<String, Object>> listenableFuture = kafkaJwtTokenTemplate.send(JwtTokenTopic.TOPIC_UPDATE_JWT_TOKEN, message);

        listenableFuture.addCallback(new JwtTokenFutureCallback(message));
    }

    @Override
    public void deleteJwtTokenCache(JwtTokenMessage.DeleteMessage message) {
        LOGGER.debug("topic = {}, payload = {}", JwtTokenTopic.TOPIC_DELETE_JWT_TOKEN, message);

        ListenableFuture<SendResult<String, Object>> listenableFuture = kafkaJwtTokenTemplate.send(JwtTokenTopic.TOPIC_DELETE_JWT_TOKEN, message);

        listenableFuture.addCallback(new JwtTokenFutureCallback(message));
    }
}
