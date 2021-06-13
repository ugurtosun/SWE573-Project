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
    public Tag createTag(String customTagName, String wikiName, String wikiURL, String articleID) {

        Tag tag = new Tag(customTagName);
        if(wikiName != null){
            tag.setWikiTagName(wikiName);
            tag.setWikiURL(wikiURL);
        }
        //TODO: tag keywords service will be implemented
        tag = writeTagToDB(tag);
        if(tag != null && articleID != null){
            if(updateArticleTag(articleID, tag.getTagID(), false) == 1){
                return tag;
            }
        }
        return null;
    }

    @Override
    public boolean deleteTag() {
        //TODO: implement delete tag
        return false;
    }

    public Tag writeTagToDB(Tag tag){

        String query = "INSERT INTO tags(tag_name, wiki_name, wiki_url, tag_keywords)\n" +
                        "VALUES (?,?,?, null) RETURNING tag_id";

        Connection connection = null;

        try {
            connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,tag.getCustomTagName());
            preparedStatement.setString(2,tag.getWikiTagName());
            preparedStatement.setString(3,tag.getWikiURL());

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

    public int updateArticleTag(String articleID, String tagID, boolean isRemove){

        String query;

        if(!isRemove){
            query = "UPDATE articles SET tags = array_append(tags,(?)) WHERE article_id = (?)";
        }else{
            query = "UPDATE articles SET tags = array_remove(tags,(?)) WHERE article_id = (?)";
        }

        Connection connection = null;
        int resultCode = -1;

        try {
            connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,tagID);
            preparedStatement.setString(2,articleID);

            preparedStatement.addBatch();
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            if(resultSet.next()) {
                resultCode= resultSet.getInt(1);
            }
            connection.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultCode;
    }
}
