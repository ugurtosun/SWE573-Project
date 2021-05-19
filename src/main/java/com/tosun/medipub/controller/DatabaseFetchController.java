package com.tosun.medipub.controller;

import com.tosun.medipub.service.ArticleFetchService;
import com.tosun.medipub.service.UserService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

@RestController
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

        return "failed";
    }
}
