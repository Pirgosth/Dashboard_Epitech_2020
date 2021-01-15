package io.github.dashboard.utils;

import org.bouncycastle.util.Times;

import java.sql.*;
import java.util.List;

public class SqlUtils {

    private static PreparedStatement generateStatement(String request, List<Object> parameters, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(request, Statement.RETURN_GENERATED_KEYS);
        int idx = 1;
        for (Object obj : parameters) {
            if (obj instanceof Integer)
                preparedStatement.setInt(idx, (Integer) obj);
            else if (obj instanceof String)
                preparedStatement.setString(idx, (String)obj);
            else if(obj instanceof Timestamp){
                preparedStatement.setTimestamp(idx, (Timestamp)obj);
            }
            idx++;
        }
        return preparedStatement;
    }

    public static ResultSet executeInsert(String request, List<Object> parameters) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/dashboard" , "dashboard", "qps4<PAsj:e*");
        PreparedStatement stmt = generateStatement(request, parameters, connection);
        stmt.executeQuery();
        ResultSet rs = stmt.getGeneratedKeys();
        stmt.close();
        connection.close();
        return rs;
    }

    public static ResultSet executeSelect(String request, List<Object> parameters) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/dashboard" , "dashboard", "qps4<PAsj:e*");
        PreparedStatement stmt = generateStatement(request, parameters, connection);
        ResultSet rs = stmt.executeQuery();
        stmt.close();
        connection.close();
        return rs;
    }

    public static void executeUpdate(String request, List<Object> parameters) throws SQLException{
        Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/dashboard" , "dashboard", "qps4<PAsj:e*");
        PreparedStatement stmt = generateStatement(request, parameters, connection);
        stmt.executeQuery();
        stmt.close();
        connection.close();
    }

    public static void executeDelete(String request, List<Object> parameters) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/dashboard" , "dashboard", "qps4<PAsj:e*");
        PreparedStatement stmt = generateStatement(request, parameters, connection);
        stmt.executeQuery();
        stmt.close();
        connection.close();
    }
}