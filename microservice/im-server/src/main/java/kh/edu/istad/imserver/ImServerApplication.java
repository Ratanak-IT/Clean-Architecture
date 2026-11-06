package kh.edu.istad.imserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class ImServerApplication {
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    public static void main(String[] args) {
        SpringApplication.run(ImServerApplication.class, args);
    }

}
