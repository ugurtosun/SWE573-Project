package com.tosun.medipub.controller;

import com.tosun.medipub.model.Article;
import com.tosun.medipub.service.ArticleFetchService;
import com.tosun.medipub.service.ArticleService;
import com.tosun.medipub.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/search")
public class ArticleSearchController {

    SearchService searchService;
    ArticleService articleService;

    public ArticleSearchController() {}

    @Autowired
    public ArticleSearchController(SearchService searchService, ArticleService articleService) {
        this.searchService = searchService;
        this.articleService = articleService;
    }

    @GetMapping(path = "/searchArticle")
    public ArrayList<Article> searchArticles(@RequestParam(name = "keywords", required = true) ArrayList<String> keywords
                                        , @RequestParam(name = "isAdvanced", required = true) boolean isAdvanced
                                        , @RequestParam(name = "isCombined", required = true) boolean isCombined
                                        , @RequestParam(name = "fields", required = false) ArrayList<String> fields
                                        , @RequestParam(name = "startDate", required = false) Date startDate
                                        , @RequestParam(name = "endDate", required = false) Date endDate){

        if(fields.get(0).equals("all")){
            //return searchService.searchArticles(keywords, isCombined);
            return articleService.getArticles(searchService.searchArticles(keywords, isCombined));
        }else{
            return articleService.getArticles(searchService.searchArticlesAdvanced(keywords, isCombined,
                                                                                      fields, startDate, endDate));
        }
    }

    @GetMapping(path = "/getArticle")
    public ArrayList<Article> getArticles(@RequestParam(name = "articleIDs", required = true) ArrayList<String> articleIDs){

        return articleService.getArticles(articleIDs);
        //return null;
    }
}
