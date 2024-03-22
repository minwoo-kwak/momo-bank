package com.ssafy.user.config;

import com.ssafy.user.common.exception.ApiException;
import com.ssafy.user.common.exception.CustomException;
import com.ssafy.user.common.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.stream.Collectors;

@Configuration
class RestTemplateConfig {




    @Bean
    public RestTemplate restTemplate () {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .additionalInterceptors(clientHttpRequestInterceptor())
                .build();
    }



    public ClientHttpRequestInterceptor clientHttpRequestInterceptor() {
        return (request, body, execution) -> {
            RetryTemplate retryTemplate = new RetryTemplate();
            retryTemplate.setRetryPolicy(new SimpleRetryPolicy(2, Collections.singletonMap(HttpServerErrorException.class, true)));
            try {
                return retryTemplate.execute(context -> execution.execute(request, body));
//            } catch (Throwable throwable) {
//
//                throw new RuntimeException(throwable);
//
//            }
            } catch (HttpServerErrorException e) {
                throw new ApiException(ErrorResponse.builder()
                                .status(e.getStatusCode().value())
                                .message(e.getMessage())
                                .build());
            }
        };
    }


//
//        @Slf4j
//        static class LoggingInterceptor implements ClientHttpRequestInterceptor {
//
//            @Override
//            public ClientHttpResponse intercept(HttpRequest req, byte[] body, ClientHttpRequestExecution ex) throws IOException {
//                final String sessionNumber = makeSessionNumber();
//                printRequest(sessionNumber, req, body);
//                ClientHttpResponse response = ex.execute(req, body);
//                printResponse(sessionNumber, response);
//                return response;
//            }
//
//
//            private String makeSessionNumber() {
//                return Integer.toString((int) (Math.random() * 1000000));
//            }
//
//            private void printRequest(final String sessionNumber, final HttpRequest req, final byte[] body) {
//                log.info("[{}] URI: {}, Method: {}, Headers:{}, Body:{} ",
//                        sessionNumber, req.getURI(), req.getMethod(), req.getHeaders(), new String(body, StandardCharsets.UTF_8));
//            }
//
//            private void printResponse(final String sessionNumber, final ClientHttpResponse res) throws IOException {
//                String body = new BufferedReader(new InputStreamReader(res.getBody(), StandardCharsets.UTF_8)).lines()
//                        .collect(Collectors.joining("\n"));
//
//                log.info("[{}] Status: {}, Headers:{}, Body:{} ",
//                        sessionNumber, res.getStatusCode(), res.getHeaders(), body);
//            }
//
//
//        }
//
//
//
//

}