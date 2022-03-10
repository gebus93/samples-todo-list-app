package pl.thinkandcode.samples.todo.adapters.inbound.rest.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Slf4j
public class HttpTrafficLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var responseCacheWrapperObject = new ContentCachingResponseWrapper(response);
        boolean isFirstRequest = !isAsyncDispatch(request);
        if (isFirstRequest) {
            log.info(createRequestMessage(request));
        }
        try {
            filterChain.doFilter(request, responseCacheWrapperObject);
            responseCacheWrapperObject.copyBodyToResponse();
        } finally {
            if (!isAsyncStarted(request)) {
                log.info(createResponseMessage(request, responseCacheWrapperObject));
            }
        }

    }

    private String createRequestMessage(HttpServletRequest request) {
        StringBuilder msg = new StringBuilder();
        msg.append("Request: ")
                .append(createUrlMessage(request))
                .append(", content-length: ")
                .append(request.getContentLength());

        var clientInfo = getClientInfo(request);
        if (!clientInfo.isBlank()) {
            msg.append(' ').append('[').append(clientInfo).append(']');
        }
        return msg.toString();
    }

    private String createResponseMessage(HttpServletRequest request, ContentCachingResponseWrapper response) {
        var contentLength = Optional.ofNullable(response.getHeader(HttpHeaders.CONTENT_LENGTH))
                .orElseGet(() -> String.valueOf(response.getContentSize()));

        return "Response: " + createUrlMessage(request) +
                ", status: " + response.getStatus() +
                ", content-length: " + contentLength;
    }

    private String createUrlMessage(HttpServletRequest request) {
        StringBuilder msg = new StringBuilder();
        msg.append(request.getMethod()).append(' ');
        msg.append(request.getRequestURI());

        String queryString = request.getQueryString();
        if (queryString != null) {
            msg.append('?').append(queryString);
        }

        return msg.toString();

    }

    private String getClientInfo(HttpServletRequest request) {
        StringBuilder msg = new StringBuilder();
        String client = request.getRemoteAddr();
        if (StringUtils.hasLength(client)) {
            msg.append(", client=").append(client);
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            msg.append(", session=").append(session.getId());
        }
        String user = request.getRemoteUser();
        if (user != null) {
            msg.append(", user=").append(user);
        }

        return msg.isEmpty() ? "" : msg.substring(2);
    }
}
