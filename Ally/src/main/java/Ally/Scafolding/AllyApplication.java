package Ally.Scafolding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "Ally.Scafolding")
public class AllyApplication {

	public static void main(String[] args) {
		SpringApplication.run(AllyApplication.class, args);
		System.out.println(" Backend Ally iniciado correctamente en http://localhost:8081");
	}

}
