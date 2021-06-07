package com.tosun.medipub.model;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Article {

    private String PMID;
    private String title;
    private String journal;
    private String ISSN;
    private Date publishDate;
    private Date revisionDate;
    private String language;
    private String articleAbstract;
    private ArrayList<String> authorList;
    private ArrayList<String> keywords;

    public Article() {
    }

    public Article(String PMID, String title, String articleAbstract) {
        this.PMID = PMID;
        this.title = title;
        this.articleAbstract = articleAbstract;
    }

    public String getPMID() {
        return PMID;
    }

    public void setPMID(String PMID) {
        this.PMID = PMID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getISSN() {
        return ISSN;
    }

    public void setISSN(String ISSN) {
        this.ISSN = ISSN;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(Date revisionDate) {
        this.revisionDate = revisionDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getArticleAbstract() {
        return articleAbstract;
    }

    public void setArticleAbstract(String articleAbstract) {
        this.articleAbstract = articleAbstract;
    }

    public ArrayList<String> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(ArrayList<String> authorList) {
        this.authorList = authorList;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public void writeToDB(JdbcTemplate jdbcTemplate) throws SQLException {

        String query = "INSERT INTO articles (article_id, title, journal, publish_date, revision_date, lang, abstract, authors, keywords )" +
                " VALUES(?,?,?,?,?,?,?,?,?) ON CONFLICT DO NOTHING";

        Connection connection = null;
        try {
            connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, this.PMID);
            preparedStatement.setString(2, this.title);
            preparedStatement.setString(3, this.journal);
            preparedStatement.setDate(4, new java.sql.Date(this.publishDate.getTime()));
            preparedStatement.setDate(5, new java.sql.Date(this.revisionDate.getTime()));
            preparedStatement.setString(6, this.language);
            preparedStatement.setString(7, this.articleAbstract);

            Array array = connection.createArrayOf("VARCHAR", this.authorList.toArray());
            preparedStatement.setArray(8, array);

            Array array2 = connection.createArrayOf("VARCHAR", this.keywords.toArray());
            preparedStatement.setArray(9, array2);
            //System.out.println(preparedStatement.executeUpdate());
            preparedStatement.addBatch();
            preparedStatement.close();
            connection.close();

        } catch (SQLException throwables) {
            if(connection != null){
                connection.close();
            }
            throwables.printStackTrace();
        }
    }
}
