package me.bishisimo.springbootjdbc.server;

import me.bishisimo.springbootjdbc.doMain.User;

import java.util.List;
import java.util.Map;

public interface UserServer {
    boolean save(User user);
    List<Map<String, Object>> getAll();
    boolean removeByName(String name);
}
