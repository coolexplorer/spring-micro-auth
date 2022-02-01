package io.coolexplorer.auth.service.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
public class SessionFutureCallback implements ListenableFutureCallback<SendResult<String, Object>> {
    private final Object message;

    public SessionFutureCallback(Object message) {
        this.message = message;
    }

    @Override
    public void onFailure(Throwable ex) {
        LOGGER.debug("Unable to send message=[{}] due to : {}", message, ex.getMessage());
    }

    @Override
    public void onSuccess(SendResult<String, Object> result) {
        LOGGER.debug("Sent message=[{}] with offset=[{}]", message, result.getRecordMetadata().offset());
    }
}
