package com.faroc.gymanager.infrastructure.interceptors;

import com.faroc.gymanager.domain.shared.exceptions.EventualConsistencyException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
