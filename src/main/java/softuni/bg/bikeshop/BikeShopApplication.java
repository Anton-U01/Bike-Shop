package softuni.bg.bikeshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class BikeShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(BikeShopApplication.class, args);
	}

}
