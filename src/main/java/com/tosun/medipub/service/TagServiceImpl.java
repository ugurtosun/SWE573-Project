package com.tosun.medipub.service;

import com.tosun.medipub.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@Service
public class TagServiceImpl implements TagService{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public boolean createTag(String customTagName, String customDescription, String wikiTagName
            , String wikiID, String  wikiURL, String articleID) {

        Tag tag = new Tag(customTagName);
        if(wikiTagName != null){
            tag.setLabel(wikiTagName);
            tag.setUrl(wikiURL);
        }
        tag.setCustomDescription(customDescription);
        tag.setArticleID(articleID);
        tag.setWikiID(wikiID);

        tag = writeTagToDB(tag);
        if(tag != null){
            return true;
        }
        return false;
    }

    public Tag writeTagToDB(Tag tag){

        String query = "INSERT INTO tags(tag_name, description, wiki_name, wiki_url, wiki_id, article_id)\n" +
                        "VALUES (?,?,?,?,?,?) RETURNING tag_id";

        Connection connection = null;

        try {
            connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,tag.getCustomTagName());
            preparedStatement.setString(2,tag.getCustomDescription());
            preparedStatement.setString(3,tag.getLabel());
            preparedStatement.setString(4,tag.getUrl());
            preparedStatement.setString(5,tag.getWikiID());
            preparedStatement.setString(6, tag.getArticleID());

            preparedStatement.addBatch();
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            if(resultSet.next()) {
                int tagID = resultSet.getInt(1);
                tag.setTagID(Integer.toString(tagID));
            }
            connection.close();
            return tag;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
