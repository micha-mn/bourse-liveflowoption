package com.data.synchronisation.springboot;

import java.time.Duration;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.client.RestTemplate;

import com.data.synchronisation.springboot.data.advice.RestTemplateErrorHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
//import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	 @Bean
	  public RestTemplate restTemplate(RestTemplateBuilder builder) {
	    return builder.errorHandler(new RestTemplateErrorHandler())
	        .setConnectTimeout(Duration.ofMillis(300000)).setReadTimeout(Duration.ofMillis(300000))
	        .build();
	  }
	 @PostConstruct
	    public void init() {
	        // Set the JVM's default timezone to UTC
	        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	        System.out.println("Default timezone set to: " + TimeZone.getDefault().getID());
	    }
	
	/* 
    @Bean
    WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }

    @Bean
    ApplicationListener<ApplicationReadyEvent> ready(AvailabilityClient client) {
        return applicationReadyEvent -> {
            for (var console : "ps5,xbox,ps4,switch".split(",")) {
                Flux.range(0, 20).delayElements(Duration.ofMillis(100)).subscribe(i ->
                        client
                                .checkAvailability(console)
                                .subscribe(availability ->
                                        log.info("console: {}, availability: {} ", console, availability.isAvailable())));
            }
        };
    }*/
}

/*
@Data
@AllArgsConstructor
@NoArgsConstructor
class Availability {
    private boolean available;
    private String console;
}

@Component
@RequiredArgsConstructor
class AvailabilityClient {

    private final WebClient webClient;
    private static final String URI = "http://localhost:8083/availability/{console}";

    Mono<Availability> checkAvailability(String console) {
        return this.webClient
                .get()
                .uri(URI, console)
                .retrieve()
                .bodyToMono(Availability.class)
                .onErrorReturn(new Availability(false, console));
    }

}
*/