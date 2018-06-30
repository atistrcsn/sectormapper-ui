package demo;

import io.github.atistrcsn.sectormapper.Config;
import io.github.atistrcsn.sectormapper.SectorMapper;
import io.github.atistrcsn.sectormapper.SectorMapperImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;

@SpringBootApplication
public class App {

    @Bean
    public Config getConfig() {
        return new Config(
                null,
                new File(getClass().getResource("/map.svg").getPath()),
                "sector-overlay-"
        );
    }

    @Bean
    public SectorMapper getMapper() {
        return new SectorMapperImpl(getConfig());
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }
}
