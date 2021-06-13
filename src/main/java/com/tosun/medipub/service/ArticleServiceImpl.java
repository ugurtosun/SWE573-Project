package com.tosun.medipub.service;

import com.tosun.medipub.model.Article;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class ArticleServiceImpl implements ArticleService{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ArrayList<Article> getArticles(ArrayList<String> articleIDList) {

        String subQuery = "";

        for (int i = 0; i < articleIDList.size(); i++){
            if(i != 0){
                subQuery += ",";
            }
            subQuery += articleIDList.get(i);
        }

        String query = "select *\n" +
                "from public.articles\n" +
                "where article_id = ANY (?)";

        Connection connection = null;
        try {
            connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            Array array = preparedStatement.getConnection().createArrayOf("VARCHAR", articleIDList.toArray());
            preparedStatement.setArray(1, array);
            preparedStatement.addBatch();
            ResultSet result = preparedStatement.executeQuery();

            ArrayList<Article> articleList = new ArrayList<>();

            while (result.next()) {

                Article article = new Article(result.getString(1), result.getString(2), result.getString(7));
                article.setJournal(result.getString(3));
                article.setPublishDate(result.getDate(4));
                article.setRevisionDate(result.getDate(5));
                article.setAuthorList(convertArrayToArraylist(result.getString("authors")));
                article.setKeywords(convertArrayToArraylist(result.getString("keywords")));
                articleList.add(article);
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> convertArrayToArraylist(String str){

        str = str.replace("{","").replace("}", "").replace("\"", "");
        if(!str.isEmpty()){
            return new ArrayList<String>(Arrays.asList(str.split(",")));
        }
        return null;
    }

    @Override
    public boolean tagArticle() {
        return false;
    }
}
