package com.tosun.medipub.service;

import com.tosun.medipub.model.request.UserRequest;
import com.tosun.medipub.model.response.UserRest;

public interface ArticleFetchService {

    int fetchArticleIDs();

    int fetchArticles();

}
