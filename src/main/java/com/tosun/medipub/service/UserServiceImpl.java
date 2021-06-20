package com.tosun.medipub.service;

import com.tosun.medipub.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public boolean createUser(User user) {

        String query = "INSERT INTO users (user_name, email_address, pass) VALUES (?,?, crypt(?, gen_salt('bf'))) RETURNING user_id";

        Connection connection = null;

        try {
            connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,user.getUserName());
            preparedStatement.setString(2,user.getEmailAddress());
            preparedStatement.setString(3, user.getPassword());

            preparedStatement.addBatch();
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            if(resultSet.next()) {
                int userID = resultSet.getInt(1);
                user.setUserID(Integer.toString(userID));

            }
            connection.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean login(User user){

        String query = "SELECT user_id FROM users " +
                "WHERE user_name = (?) " +
                "AND pass = crypt((?), pass)";

        Connection connection = null;
        boolean isSuccess = false;

        try {
            connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,user.getUserName());
            preparedStatement.setString(2,user.getPassword());

            preparedStatement.addBatch();
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();

            if(resultSet.next()) {
                int userID = resultSet.getInt(1);
                user.setUserID(Integer.toString(userID));
                isSuccess = true;
            }
            connection.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        return isSuccess;
    }
}
