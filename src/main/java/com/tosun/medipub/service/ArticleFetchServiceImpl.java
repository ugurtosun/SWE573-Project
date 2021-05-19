package com.tosun.medipub.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

@Service
public class ArticleFetchServiceImpl implements ArticleFetchService{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int fetchArticleIDs(){

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS articles_lookup(\n" +
                "article_id varchar(255) NOT NULL, PRIMARY KEY (article_id)\n)");

        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(
                URI.create("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&RetMax=50000&term=brain&RetStart=1"))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String xmlResponse = response.body();
            JSONObject jsonResponse = XML.toJSONObject(xmlResponse);
            JSONObject jsonResponse1 = jsonResponse.getJSONObject("eSearchResult");

            //System.out.println("Total number of articles " + (int) jsonResponse1.get("Count")); //2065705
            JSONArray jsonResponse2 = jsonResponse1.getJSONObject("IdList").getJSONArray("Id");

            String query = "INSERT INTO articles_lookup(article_id) VALUES(?) ON CONFLICT DO NOTHING";
            Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            for (int i = 0; i < jsonResponse2.length(); i++) {
                preparedStatement.setString(1,jsonResponse2.get(i).toString());
                preparedStatement.addBatch();
            }
            return 0;

        } catch (IOException | InterruptedException | JSONException | SQLException e) {
            e.printStackTrace();
            return 99;
        }
    }

    @Override
    public int fetchArticles(){

        return 0; //TODO:Implement method
    }
}
