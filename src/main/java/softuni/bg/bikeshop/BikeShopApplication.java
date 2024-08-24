package softuni.bg.bikeshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import softuni.bg.bikeshop.service.EmailSenderService;
import softuni.bg.bikeshop.service.impl.EmailSenderServiceImpl;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableScheduling
public class BikeShopApplication {
    public static void main(String[] args) {
		SpringApplication.run(BikeShopApplication.class, args);
	}

}
