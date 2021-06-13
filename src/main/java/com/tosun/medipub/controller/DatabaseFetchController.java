package com.tosun.medipub.controller;

import com.tosun.medipub.service.ArticleFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("fetch")
public class DatabaseFetchController {

    ArticleFetchService articleFetchService;

    public DatabaseFetchController(){ }

    @Autowired
    public DatabaseFetchController(ArticleFetchService articleFetchService){
        this.articleFetchService = articleFetchService;
    }

    @GetMapping(path = "/articleIDs")
    public String fetchArticleIDs(){

        if(articleFetchService.fetchArticleIDs() == 0){
            return "success";
        };
        return "failed";
    }

    @GetMapping(path = "/articles")
    public String fetchArticles(){

        articleFetchService.fetchArticles();

        return "failed";
    }
}
