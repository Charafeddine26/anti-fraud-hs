package antifraud;

import antifraud.domain.model.Region;
import antifraud.repository.RegionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//
public class AntiFraudApplication {
    public static void main(String[] args) {
        SpringApplication.run(AntiFraudApplication.class, args);
    }

     @Bean
    public CommandLineRunner init(RegionRepository regionRepository) {
        return args -> {
            regionRepository.save(new Region(1L, "EAP", "East Asia and Pacific"));
            regionRepository.save(new Region(2L, "ECA", "Europe and Central Asia"));
            regionRepository.save(new Region(3L, "HIC", "High-Income countries"));
            regionRepository.save(new Region(4L, "LAC", "Latin America and the Caribbean"));
            regionRepository.save(new Region(5L, "MENA", "The Middle East and North Africa"));
            regionRepository.save(new Region(6L, "SA", "South Asia"));
            regionRepository.save(new Region(7L, "SSA", "Sub-Saharan Africa"));
        };
}
}
