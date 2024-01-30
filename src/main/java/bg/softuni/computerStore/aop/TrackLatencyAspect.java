package bg.softuni.computerStore.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
public class TrackLatencyAspect {
    @Around(value = "@annotation(TrackLatency)") //around - before and after execution of the methods annotated
    public Object trackLatency(ProceedingJoinPoint pjp, TrackLatency TrackLatency) throws Throwable {
        String latencyId = TrackLatency.latency();
        DateTimeFormatter formatterToString = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object obj = pjp.proceed();
        stopWatch.stop();

        long actualLatency = stopWatch.getLastTaskTimeMillis();
        FileWriter myWriter = new FileWriter("src/main/java/bg/softuni/computerStore/logs/logfile.log", true);
        myWriter.write(String.format("%s The latency for %s is: %dms%n", LocalDateTime.now().format(formatterToString), latencyId, actualLatency));
        myWriter.close();
        return obj;   //for rest json consuming scenarios
    }
}

