package me.bishisimo.springbootjdbc.doMain;

import me.bishisimo.springbootjdbc.server.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

@Service
@EnableTransactionManagement
public class UserServerImp implements UserServer {
    private Boolean statue;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public boolean save(User user) {
        Boolean statue;
        statue = jdbcTemplate.execute("insert into user (name, age) values(?,?)",
                (PreparedStatementCallback<Boolean>) ps -> {
                    ps.setString(1, user.getName());
                    ps.setInt(2, user.getAge());
                    return ps.executeUpdate() > 0;
                });
        return checkStatue();
    }

    @Override
    public boolean removeByName(String name) {
        statue =jdbcTemplate.execute("delete from user where name = ?",
                (PreparedStatementCallback<Boolean>) ps -> {
                    ps.setString(1, name);
                    return ps.executeUpdate() > 0;
                });
        return checkStatue();
    }

    private boolean checkStatue(){
        if (statue == null) {
            statue = false;
        }
        return statue;
    }

    @Override
    public List<Map<String, Object>> getAll() {
        return jdbcTemplate.execute("select * from user",
                (PreparedStatementCallback<List<Map<String, Object>>>) ps -> {
                    ResultSet resultSet = ps.executeQuery();
                    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                    int columnCount = resultSetMetaData.getColumnCount();
                    List<String> columnNames = new ArrayList<>(columnCount);
                    for (int i = 1; i <= columnCount; i++) {
                        columnNames.add(resultSetMetaData.getColumnName(i));
                    }
                    List<Map<String, Object>> data = new LinkedList<>();
                    while (resultSet.next()) {
                        Map<String, Object> columnData = new LinkedHashMap<>();
                        for (String columnName : columnNames) {
                            Object columnValue = resultSet.getObject(columnName);
                            columnData.put(columnName, columnValue);
                        }
                        data.add(columnData);
                    }
                    return data;
                });
    }
}
