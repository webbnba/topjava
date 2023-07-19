package ru.javawebinar.topjava.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.Arrays;

public class JdbcUtil {
    @Autowired
    private Environment env;

    public JdbcUtil(Environment env) {
        this.env = env;
    }

    public boolean isJdbcProfile() {
        return Arrays.stream(env.getActiveProfiles()).anyMatch(env -> env.equalsIgnoreCase("Jdbc"));
    }
}
