package com.vertx.demo.repository;

import com.vertx.demo.entity.Category;
import com.vertx.demo.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    public List<Category> findAll() {
        List<Category> categoryList = new ArrayList<>();
        try {
            Connection connection = DBConnection.getConnection();
            if (connection != null) {

                Statement statement = connection.createStatement();
                String sql = "SELECT * FROM category";
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    Long id = Long.valueOf(resultSet.getString("category_id"));
                    String name = resultSet.getString("name");
                    Category category = new Category(id, name);
                    categoryList.add(category);
                }
                return categoryList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryList;
    }

    public Category findById(Long id) {
        Category category = new Category();
        try {
            Connection connection = DBConnection.getConnection();
            if (connection != null) {

                Statement statement = connection.createStatement();
                String sql = "SELECT * FROM category where category_id = " + id;
                ResultSet resultSet = statement.executeQuery(sql);

                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    category = new Category(id, name);
                    return category;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return category;
    }

    public Category add(Category category) {
        try {
            Connection connection = DBConnection.getConnection();
            if (connection != null) {

                Statement statement = connection.createStatement();
                String sql = "INSERT into category value (" + category.getCategoryId() + "," +"'"+category.getName()+"'" +")";
                System.out.println("sql: "+ sql);
                int rs = statement.executeUpdate(sql);
                if (rs > 0) {
                    return category;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
