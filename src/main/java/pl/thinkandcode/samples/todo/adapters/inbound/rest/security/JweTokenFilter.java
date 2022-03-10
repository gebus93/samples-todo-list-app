package pl.thinkandcode.samples.todo.adapters.inbound.rest.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JweTokenFilter extends OncePerRequestFilter {

    private final JweTokenManager tokenManager;

    public JweTokenFilter(JweTokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Get JWE token and validate
        final String token = header.split(" ")[1].trim();
        if (!tokenManager.validate(token)) {
            chain.doFilter(request, response);
            return;
        }

        var authenticatedUser = tokenManager.decodeToken(token);
        var authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, null, List.of());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private boolean isEmpty(String string) {
        return string == null || string.isBlank();
    }

}