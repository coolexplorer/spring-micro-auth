package io.coolexplorer.auth.service;

import io.coolexplorer.auth.message.SessionMessage;

public interface SessionMessageService {
    void createSession(SessionMessage.CreateMessage sessionDto);
}
