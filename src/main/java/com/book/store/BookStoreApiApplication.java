package com.book.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class BookStoreApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApiApplication.class, args);
    }

}
