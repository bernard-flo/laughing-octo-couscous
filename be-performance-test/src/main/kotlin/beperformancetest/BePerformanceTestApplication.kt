package beperformancetest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class BePerformanceTestApplication

fun main(args: Array<String>) {
    runApplication<BePerformanceTestApplication>(*args)
}
