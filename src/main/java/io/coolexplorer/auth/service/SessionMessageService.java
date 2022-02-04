package io.coolexplorer.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.coolexplorer.auth.message.SessionMessage;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface SessionMessageService {
    void createSession(Long accountId, String value, Long expiration);
    SessionMessage.SessionInfo getSession(Long accountId) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException;
    void updateSession(Long accountId, String appendedValue, Long expiration);
    void deleteSession(Long accountId);
}
