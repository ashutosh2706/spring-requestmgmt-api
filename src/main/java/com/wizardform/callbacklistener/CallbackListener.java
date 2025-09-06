package com.wizardform.callbacklistener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
public class CallbackListener implements CommandLineRunner {
    @Value("${spring.application.name}")
    String application;
    @Value("${server.port}")
    String port;

    public static void main(String[] args) {
        new SpringApplicationBuilder(CallbackListener.class)
                .profiles("client")
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.printf("%s started on port: %s\n", this.application, this.port);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((requests) -> requests.requestMatchers("/client-callback/**")
                .permitAll()
                .anyRequest().authenticated());
        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
