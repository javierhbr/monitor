package com.challenge.magnificent;

import com.challenge.domain.HealthStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Component
public class MagnificentClient {

    private static final Logger logger = LoggerFactory.getLogger(MagnificentClient.class);

    private RestTemplate restTemplate;

    @Value("${monitor.magnificent.url}")
    private String magnificentUrl;

    @Value("${monitor.magnificent.application}")
    private String applicationName;

    public MagnificentClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public HealthStatus checkHealth(){
        logger.trace("calling {}",magnificentUrl);
        HealthStatus status = null;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(magnificentUrl, String.class);
            status = HealthStatus.builder()
                    .application(applicationName)
                    .dateTime(LocalDateTime.now())
                    .status(response.getStatusCodeValue())
                    .url(magnificentUrl)
                    .build();

            logger.info("{}", status);

        }catch (HttpServerErrorException error){
            status = HealthStatus.builder()
                    .application(applicationName)
                    .dateTime(LocalDateTime.now())
                    .status(error.getRawStatusCode())
                    .url(magnificentUrl)
                    .build();
            logger.info("{}",status);
        }
        return status;

    }
}
