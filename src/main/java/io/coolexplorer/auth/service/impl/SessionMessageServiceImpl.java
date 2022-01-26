package io.coolexplorer.auth.service.impl;

import io.coolexplorer.auth.message.SessionMessage;
import io.coolexplorer.auth.service.SessionMessageService;
import io.coolexplorer.auth.service.callback.SessionFutureCallback;
import io.coolexplorer.auth.topic.SessionTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionMessageServiceImpl implements SessionMessageService {
    private final KafkaTemplate<String, Object> kafkaSessionTemplate;

    @Override
    public void createSession(SessionMessage.CreateMessage message) {
        LOGGER.debug("topic = {}, payload = {}", SessionTopic.TOPIC_CREATE_SESSION, message);

        ListenableFuture<SendResult<String, Object>> listenableFuture = kafkaSessionTemplate.send(SessionTopic.TOPIC_CREATE_SESSION, message);

        listenableFuture.addCallback(new SessionFutureCallback(message));
    }
}
