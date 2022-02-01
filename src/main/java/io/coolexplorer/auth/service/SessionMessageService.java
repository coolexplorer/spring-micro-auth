package io.coolexplorer.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.coolexplorer.auth.message.SessionMessage;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface SessionMessageService {
    void createSession(SessionMessage.CreateMessage message);
    SessionMessage.SessionInfo retrieveSession(SessionMessage.RetrieveMessage message) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException;
    void updateSession(SessionMessage.UpdateMessage message);
    void deleteSession(SessionMessage.DeleteMessage message);
}
