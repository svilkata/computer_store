package bg.softuni.computerStore.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
public class TrackLatencyAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrackLatencyAspect.class);

    @Around(value = "@annotation(TrackLatency)") //around - before and after execution of the methods annotated
    public Object trackLatency(ProceedingJoinPoint pjp, TrackLatency TrackLatency) throws Throwable {
        String latencyId = TrackLatency.latency();
        DateTimeFormatter formatterToString = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object obj = pjp.proceed();
        stopWatch.stop();

        long actualLatency = stopWatch.getLastTaskTimeMillis();
        String logMessage = String.format("%s The latency for %s is: %dms%n", LocalDateTime.now().format(formatterToString), latencyId, actualLatency);

        // When running the app with docker-compose locally, the docker image is built from a jar file and there is a problem with FileWriter
//        FileWriter myWriter = new FileWriter("src/main/java/bg/softuni/computerStore/logs/logfile.log", true);
//        myWriter.write(logMessage);
//        myWriter.close();

        LOGGER.info(logMessage);

        return obj;   //for rest json consuming scenarios
    }
}

