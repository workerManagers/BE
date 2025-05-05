package com.example.workerManagers.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Value("${fastapi.server.url}")
    private String fastApiServerUrl;

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 600000) // Connection timeout: 10 minutes
                .responseTimeout(Duration.ofMinutes(10)) // Response timeout: 10 minutes
                .doOnConnected(conn -> 
                    conn.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.MINUTES))
                        .addHandlerLast(new WriteTimeoutHandler(10, TimeUnit.MINUTES))
                );

        return WebClient.builder()
                .baseUrl(fastApiServerUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
} 