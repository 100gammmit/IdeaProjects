package com.spdrtr.nklcb;

import com.spdrtr.nklcb.repository.ArticleRepository;
import com.spdrtr.nklcb.service.Crawling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class NklcbApplication {

    public static void main(String[] args) {
        SpringApplication.run(NklcbApplication.class, args);
    }

}
