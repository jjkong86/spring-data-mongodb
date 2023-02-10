package com.me.springdata.mongodb.config.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringJoiner;

@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
@WebFilter(filterName = "RequestCachingFilter", urlPatterns = "/*")
@Log4j2
public class RequestCachingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        CachedHttpServletRequest cachedHttpServletRequest = new CachedHttpServletRequest(request);
        StringJoiner stringJoiner = new StringJoiner("\n");
        String strCurrentLine = "";
        BufferedReader reader = cachedHttpServletRequest.getReader();
        while ((strCurrentLine = reader.readLine()) != null) {
            stringJoiner.add(strCurrentLine);
        }

        log.info("REQUEST DATA: {}", stringJoiner.toString());

        filterChain.doFilter(cachedHttpServletRequest, response);
    }
}
