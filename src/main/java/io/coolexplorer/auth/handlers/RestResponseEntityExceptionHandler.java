package io.coolexplorer.auth.handlers;

import io.coolexplorer.auth.dto.ErrorResponse;
import io.coolexplorer.auth.enums.ErrorCode;
import io.coolexplorer.auth.exceptions.user.UserDataIntegrityViolationException;
import io.coolexplorer.auth.exceptions.user.UserException;
import io.coolexplorer.auth.exceptions.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.auth.login.AccountException;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class RestResponseEntityExceptionHandler {
    private final MessageSourceAccessor errorMessageSourceAccessor;

    @ExceptionHandler(AccountException.class)
    protected ResponseEntity<ErrorResponse> handleUserException(UserException e) {
        LOGGER.error(e.getLocalizedMessage(), e);

        ErrorResponse response = createUserErrorResponse(e);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse createUserErrorResponse(UserException e) {
        ErrorResponse errorResponse = null;

        if (e instanceof UserNotFoundException) {
            errorResponse = createErrorMessage(ErrorCode.USER_NOT_FOUND);
        } else if (e instanceof UserDataIntegrityViolationException) {
            errorResponse = createErrorMessage(ErrorCode.USER_DATA_VIOLATION);
        }

        return errorResponse;
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ErrorResponse> handlerAuthenticationException(AuthenticationException e) {
        LOGGER.error(e.getLocalizedMessage(), e);

        ErrorResponse response = createAuthenticationReponse(e);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse createAuthenticationReponse(AuthenticationException e) {
        ErrorResponse errorResponse = null;

        if (e instanceof BadCredentialsException) {
            errorResponse = createErrorMessage(ErrorCode.AUTH_BAD_CREDENTIALS);
        } else if (e instanceof UsernameNotFoundException) {
            errorResponse = createErrorMessage(ErrorCode.AUTH_USER_NOT_FOUND);
        }

        return errorResponse;
    }

    private ErrorResponse createErrorMessage(ErrorCode errorCode) {
        return new ErrorResponse()
                .setCode(errorCode)
                .setDescription(getMessage(errorCode));
    }

    private String getMessage(ErrorCode errorCode) {
        return errorMessageSourceAccessor.getMessage(errorCode.getMessageKey());
    }
}
