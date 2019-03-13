package me.bishisimo.springbootjdbc;

import me.bishisimo.springbootjdbc.doMain.User;
import me.bishisimo.springbootjdbc.server.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class JdbcController {
    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserServer userServer;

    @GetMapping("/district/get")
    public Map<String, Object> getDistrict(@RequestParam(value = "id", defaultValue = "1") int id) {
        Map<String, Object> data = new HashMap<String, Object>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from district where id=?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                data.put("id", resultSet.getInt("id"));
                data.put("name", resultSet.getString("name"));
                data.put("parent_id", resultSet.getInt("parent_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    @GetMapping("/user/get")
    public Map<String, Object> getUser(@RequestParam(value = "id", defaultValue = "1") int id) {
        Map<String, Object> data = new HashMap<String, Object>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from user where id=?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                data.put("id", resultSet.getInt("id"));
                data.put("name", resultSet.getString("name"));
                data.put("age", resultSet.getInt("age"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    @PostMapping("/user/add")
    public Map<String, Object> addUser(@RequestBody User user) {
        Map<String, Object> data = new HashMap<>();
        data.put("success",userServer.save(user));
        return data;
    }

    @GetMapping("/user")
    public List<Map<String,Object>> getUsers(){
        return userServer.getAll();
    }

    @GetMapping("/user/remove")
    public Map<String, Object> removeUserByName(@RequestParam(value = "name") String name){
        Map<String, Object> data = new HashMap<>();
        data.put("success",userServer.removeByName(name));
        return data;
    }
}