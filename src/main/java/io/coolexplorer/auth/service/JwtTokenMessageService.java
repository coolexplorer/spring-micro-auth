package io.coolexplorer.auth.service;

import io.coolexplorer.auth.message.JwtTokenMessage;

public interface JwtTokenMessageService {
    void creteJwtTokenCache(JwtTokenMessage.CreateMessage message);
    void getJwtTokenCache(JwtTokenMessage.RetrieveMessage message);
    void updateJwtTokenCache(JwtTokenMessage.UpdateMessage message);
    void deleteJwtTokenCache(JwtTokenMessage.DeleteMessage message);
}
