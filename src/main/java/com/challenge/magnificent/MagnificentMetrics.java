package com.challenge.magnificent;

import com.challenge.domain.HealthStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class MagnificentMetrics {

    private static final Logger log = LoggerFactory.getLogger(MagnificentMetrics.class);

    @Value("${monitor.magnificent.status}")
    private int okStatus;

    private LocalDateTime lastMetricCalculated = LocalDateTime.now().minusMinutes(1);

    private ConcurrentHashMap<String, List<HealthStatus>> lastMinuteMetrics;

    public void addStatus(HealthStatus status){
        if (lastMinuteMetrics == null){
            lastMinuteMetrics = new ConcurrentHashMap<>();
        }

        String dateAndMinuteKey = dateToString(status.getDateTime());
        List<HealthStatus> statuses = Optional.ofNullable(lastMinuteMetrics.get(dateAndMinuteKey))
                .orElse(new CopyOnWriteArrayList<>());

        statuses.add(status);
        lastMinuteMetrics.put(dateAndMinuteKey, statuses);
    }

    private String  dateToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
        return dateTime.format(formatter);
    }


    public void calculateMetrics() {
        float totalStatuses = 0;
        float totalOk = 0;
        float totalError = 0;
        float avgOk = 0;
        float avgError = 0;

        String lastDateCalculatedKey = dateToString(lastMetricCalculated);
        if (lastMinuteMetrics!= null && lastMinuteMetrics.containsKey(lastDateCalculatedKey)){
            List<HealthStatus> statuses = lastMinuteMetrics.get(lastDateCalculatedKey);
            totalStatuses = statuses.size();

            for(HealthStatus status : statuses){
                 if(status.getStatus() == okStatus){
                        totalOk++;
                    }else {
                        totalError++;
                    }
            }

            avgOk = (100 * totalOk) / totalStatuses ;
            avgError = (100 * totalError) / totalStatuses ;
            log.info("metricsMinute:{}; total:{}; totalOk:{}; totalError:{}; avgOk:{} ; avgError:{}"
                    , lastDateCalculatedKey, totalStatuses, totalOk, totalError, avgOk, avgError);
            lastMinuteMetrics.remove(lastDateCalculatedKey);
        }

        lastMetricCalculated = lastMetricCalculated.plusMinutes(1);
    }
}
