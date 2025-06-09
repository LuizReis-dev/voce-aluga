package com.cefet.vocealuga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class VoceAlugaApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoceAlugaApplication.class, args);
    }
}
