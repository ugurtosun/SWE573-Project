package com.tosun.medipub.service;

import com.tosun.medipub.model.Article;
import com.tosun.medipub.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
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
                article.setTags(getArticleTags(article.getPMID()));
                articleList.add(article);
            }
            connection.close();
            return articleList;
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

    public ArrayList<Tag> getArticleTags(String articleID){

        String query = "select *\n" +
                "from public.tags\n" +
                "where article_id = ?";

        Connection connection = null;
        try {
            connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, articleID);
            preparedStatement.addBatch();
            ResultSet result = preparedStatement.executeQuery();

            ArrayList<Tag> tagList = new ArrayList<>();

            while (result.next()) {

                Tag tag = new Tag();
                tag.setArticleID(articleID);
                tag.setTagID(result.getString(1));
                tag.setCustomTagName(result.getString(2));
                tag.setCustomDescription(result.getString(3));
                tag.setLabel(result.getString(4));
                tag.setUrl(result.getString(5));
                tag.setWikiID(result.getString(6));
                tagList.add(tag);
            }
            connection.close();
            return tagList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
