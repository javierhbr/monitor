
## Build

```bash
./gradlew clean build
```

## Run
Java
```bash
java -jar build/libs/monitor-1.0.jar
```

Shell script on background.
```bash
nohup ./startMonitor.sh & 
```

### Context

Stack: Spring boot
I made 2 scheduled jobs using a thread executor to collect de statuses and then every minute calculate metrics os the past minutes


- `MagnificentClient` : contains the logic to ping the URL and process the response, also it's logging that information for monitoring purpose.
- `MagnificentMetrics`: collects the metrics sent them by checkHealthMonitor execution and stores it per minute. 
 
 
- `MonitorScheduledTasks`:  contain the scheduling task to perform the checkHealthMonitor action and trigger the metrics calculator every minute.


##### Configurations:


from the application.properties it's possible to configure the recurrence of checkHealthMonitor to the app (magnificent), the name of the app, the valid status to consider available.

```properties
monitor.magnificent.status.delay=4000
monitor.magnificent.url=http://localhost:12345
monitor.magnificent.status=200
monitor.magnificent.application=magnificent
```


I started late, so I haven't time to implement 2 more things:
- Separate logs for metrics and statuses, My idea was used logback or log4j to implement separate files and rolling policy.
- Unit test. I'm not proud to deliver something without it.