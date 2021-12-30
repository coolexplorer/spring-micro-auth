package io.coolexplorer.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.coolexplorer.auth.dto.ErrorResponse;
import io.coolexplorer.auth.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final MessageSourceAccessor errorMessageSourceAccessor;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponse errorResponse;
        final String expired = (String)request.getAttribute(ErrorCode.JWT_TOKEN_EXPIRED.toString());

        if (expired != null) {
            errorResponse = new ErrorResponse()
                    .setCode(ErrorCode.JWT_TOKEN_EXPIRED)
                    .setDescription(errorMessageSourceAccessor.getMessage(ErrorCode.JWT_TOKEN_EXPIRED.getMessageKey()));
        } else {
            errorResponse = new ErrorResponse()
                    .setCode(ErrorCode.JWT_TOKEN_INVALID)
                    .setDescription(errorMessageSourceAccessor.getMessage(ErrorCode.JWT_TOKEN_INVALID.getMessageKey()));
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        response.flushBuffer();
    }
}
