package com.example.workerManagers.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.netty.http.client.HttpClient;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class WebClientConfig {

    @Value("${fastapi.server.url}")
    private String fastApiServerUrl;

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 600000) // Connection timeout: 10 minutes
                .responseTimeout(Duration.ofMinutes(10)) // Response timeout: 10 minutes
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .doOnConnected(conn -> 
                    conn.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.MINUTES))
                        .addHandlerLast(new WriteTimeoutHandler(10, TimeUnit.MINUTES))
                );

        return WebClient.builder()
                .baseUrl(fastApiServerUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(logRequest())
                .filter(logResponse())
                .filter(handleErrors())
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> 
                values.forEach(value -> log.debug("{}={}", name, value))
            );
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.debug("Response status: {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }

    private ExchangeFilterFunction handleErrors() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().is5xxServerError()) {
                return clientResponse.bodyToMono(String.class)
                    .flatMap(body -> {
                        String errorMessage;
                        if (clientResponse.statusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                            errorMessage = "AI 서버가 현재 사용 불가능합니다. 잠시 후 다시 시도해주세요.";
                        } else {
                            errorMessage = String.format("AI 서버 오류가 발생했습니다 (상태 코드: %s)", 
                                clientResponse.statusCode());
                        }
                        log.error("Server error: {} - {}", clientResponse.statusCode(), errorMessage);
                        return Mono.error(new RuntimeException(errorMessage));
                    });
            }
            return Mono.just(clientResponse);
        });
    }
} 