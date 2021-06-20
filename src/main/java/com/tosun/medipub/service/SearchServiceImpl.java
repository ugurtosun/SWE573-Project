package com.tosun.medipub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ArrayList<String> searchArticles(ArrayList<String> keywordsRaw, boolean isCombined) {

        String[] keywordsList = keywordsRaw.get(0).strip().split(" ");
        ArrayList<String> keywords = new ArrayList<String>(Arrays.asList(keywordsList));
        String subQuery = "";

        for(int i=0; i<keywords.size(); i++){
            if(i != 0){
                if(isCombined){
                    subQuery += "&";
                }else{
                    subQuery += "|";
                }
            }
            subQuery = subQuery + "'" + keywords.get(i) + "'";
        } //TODO:Beautify this block

        //make combined keywords in abstract, title, keywords,  authors
        String query = "select article_id\n" +
                "from public.articles\n" +
                "where to_tsvector(abstract) @@ to_tsquery(?)\n" +
                "union\n" + "select article_id\n" +
                "from public.articles\n" +
                "where to_tsvector(title) @@ to_tsquery(?)\n" +
                "union\n" + "select article_id\n" +
                "from public.articles\n" +
                "where array_to_tsvector(keywords) @@ to_tsquery(?)\n" +
                "union\n" + "select article_id\n" +
                "from public.articles\n" +
                "where array_to_tsvector(authors) @@ to_tsquery(?)";
        Connection connection = null;

        try {
            connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,subQuery);
            preparedStatement.setString(2,subQuery);
            preparedStatement.setString(3,subQuery);
            preparedStatement.setString(4,subQuery);
            preparedStatement.addBatch();
            ResultSet result = preparedStatement.executeQuery();

            ArrayList<String> articleList = new ArrayList<>();

            int counter = 0;
            while (result.next() & counter < 50) {  //TODO: rethink
                counter++;
                String articleID = result.getString(1);
                articleList.add(articleID);
            }

            connection.close();
            return articleList;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<String> searchArticlesAdvanced(ArrayList<String> keywordsRaw, boolean isCombined, ArrayList<String> fields, Date startDate, Date endDate) {

        String[] keywordsList = keywordsRaw.get(0).strip().split(" ");
        ArrayList<String> keywords = new ArrayList<String>(Arrays.asList(keywordsList));
        String subQuery = "";

        for(int i=0; i<keywords.size(); i++){
            if(i != 0){
                if(isCombined){
                    subQuery += "&";
                }else{
                    subQuery += "|";
                }
            }
            subQuery = subQuery + "'" + keywords.get(i) + "'";
        } //TODO:Beautify this block

        String query = "";
        int counter = 0;

        for(String field:fields){

            if(counter != 0){
                query += "union\n";
            }

            switch (field) {
                case "title":
                    query += "select article_id\n" +
                            "from public.articles\n" +
                            "where to_tsvector(title) @@ to_tsquery(?)\n";
                    break;
                case "abstract":
                    query += "select article_id\n" +
                            "from public.articles\n" +
                            "where to_tsvector(abstract) @@ to_tsquery(?)\n";
                    break;
                case "journal":
                    query += "select article_id\n" +
                            "from public.articles\n" +
                            "where to_tsvector(journal) @@ to_tsquery(?)\n";
                    break;
                case "authors":
                    query += "select article_id\n" +
                            "from public.articles\n" +
                            "where array_to_tsvector(authors) @@ to_tsquery(?)\n";
                    break;
                case "keywords":
                    query += "select article_id\n" +
                            "from public.articles\n" +
                            "where array_to_tsvector(keywords) @@ to_tsquery(?)\n";
                    break;
                default:
                    System.out.println("Wrong field name");
                    break;
            }
            counter++;
        }

        Connection connection = null;

        try {
            connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            for(int i =1; i < fields.size() + 1; i++){
                preparedStatement.setString(i,subQuery);
            }

            preparedStatement.addBatch();
            ResultSet result = preparedStatement.executeQuery();
            ArrayList<String> articleList = new ArrayList<>();

            while (result.next()) {
                String articleID = result.getString(1);
                articleList.add(articleID);
            }

            connection.close();
            return articleList;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
