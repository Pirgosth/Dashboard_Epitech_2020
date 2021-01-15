package io.github.dashboard.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class OnStartup implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
