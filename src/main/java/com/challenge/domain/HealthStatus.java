package com.challenge.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HealthStatus {

    private LocalDateTime dateTime;
    private String application;
    private String url;
    private int status;


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("dateTime:").append(dateTime);
        sb.append("; application:'").append(application).append('\'');
        sb.append("; url:'").append(url).append('\'');
        sb.append("; status:").append(status);
        return sb.toString();
    }
}
