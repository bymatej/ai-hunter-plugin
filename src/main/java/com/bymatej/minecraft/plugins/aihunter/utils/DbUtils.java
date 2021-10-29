package com.bymatej.minecraft.plugins.aihunter.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

import static com.bymatej.minecraft.plugins.aihunter.utils.CommonUtils.getPluginReference;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Level.SEVERE;
import static org.apache.commons.lang.StringUtils.isNotBlank;

public class DbUtils {

    public static void createTable() {
        executeSqlQuery(getSqlFileContent(getSqlFilePathString("createTable.sql")));
    }

    public static void dropTable() {
        executeSqlQuery(getSqlFileContent(getSqlFilePathString("dropTable.sql")));
    }

    private static Connection getConnection() {
        try {
            String dbUri = getPluginReference().getConfig().getString("db_uri");
            if (isNotBlank(dbUri)) {
                Class.forName("org.sqlite.JDBC");
                return DriverManager.getConnection(dbUri);
            }
        } catch (SQLException | ClassNotFoundException e) {
            CommonUtils.log(SEVERE, "Error connecting to the DB.", e);
        }

        return null;
    }

    private static void executeSqlQuery(String query) {
        CommonUtils.log("Executing the following query: \n\n" + query);
        Connection connection = getConnection();
        Statement statement = null;

        try {
            if (connection != null && isNotBlank(query)) {
                statement = connection.createStatement();
                statement.execute(query);
                statement.close();
                connection.close();
            }
        } catch (final SQLException e) {
            CommonUtils.log(SEVERE, "Failed to create table.", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                CommonUtils.log(SEVERE, "Failed to create table.", e);
            }
        }
    }

    private static String getSqlFilePathString(String fileName) {
        return "sql" + File.separatorChar + fileName;
    }

    private static String getSqlFileContent(String filePath) {
        if (isNotBlank(filePath)) {
            return new BufferedReader(new InputStreamReader(requireNonNull(getPluginReference().getResource(filePath))))
                    .lines()
                    .collect(Collectors.joining("\n"));

        }

        return null;
    }

}
