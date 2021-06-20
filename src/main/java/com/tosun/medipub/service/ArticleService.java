package com.tosun.medipub.service;

import com.tosun.medipub.model.Article;

import java.util.ArrayList;

public interface ArticleService {

    ArrayList<Article> getArticles(ArrayList<String> articleIDList);

}
