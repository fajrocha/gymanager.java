package com.faroc.gymanager.common.infrastructure.interceptors;

import com.faroc.gymanager.common.domain.exceptions.EventualConsistencyException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@WebFilter(urlPatterns = "/*")
@Slf4j
public class EventualConsistencyMiddleware implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (EventualConsistencyException ex) {
            log.error("Failed to fulfill request, system in inconsistent state.", ex);
            // Some logic to handle eventual consistency exceptions.
        }
    }
}
