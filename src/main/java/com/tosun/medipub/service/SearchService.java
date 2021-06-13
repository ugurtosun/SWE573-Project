package com.tosun.medipub.service;

import java.util.ArrayList;
import java.util.Date;

public interface SearchService {

    ArrayList<String> searchArticles(ArrayList<String> keywords, boolean isCombined);

    ArrayList<String> searchArticlesAdvanced(ArrayList<String> keywords, boolean isCombined, ArrayList<String> field, Date startDate, Date endDate);


}
