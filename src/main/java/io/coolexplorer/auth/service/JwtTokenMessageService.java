package io.coolexplorer.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.coolexplorer.auth.message.JwtTokenMessage;
import io.coolexplorer.auth.model.Account;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface JwtTokenMessageService {
    void creteJwtTokenCache(Account account);
    JwtTokenMessage.JwtTokenInfo getJwtTokenCache(JwtTokenMessage.RequestMessage message) throws ExecutionException, InterruptedException, TimeoutException, JsonProcessingException;
    void updateJwtTokenCache(Account account);
    void deleteJwtTokenCache(Long accountId);
    JwtTokenMessage.ValidateResultMessage listenValidateJwtToken(ConsumerRecord<String, String> record, Acknowledgment ack) throws JsonProcessingException;
}
