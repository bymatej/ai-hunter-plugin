package com.bymatej.minecraft.plugins.aihunter.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Stream;

import static java.lang.ClassLoader.getSystemClassLoader;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.lines;
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
            String dbUri = CommonUtils.getPluginReference().getConfig().getString("db_uri");
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

    private static String getSqlFilePath(String filePath) {
        // todo: this is broken
        //System.out.println("AAAAAAAAAA");
        //System.out.println(DbUtils.class.getClassLoader().getResource(filePath));
        //URL resource = DbUtils.class.getClassLoader().getResource(filePath);
        //try {
        //    assert resource != null;
        //    File f = new File(resource.toURI());
        //} catch (URISyntaxException e) {
        //    e.printStackTrace();
        //}
        //System.out.println("BBBBBBBBBB");
        URL sqlFileUrl = DbUtils.class.getClassLoader().getResource(filePath);
        if (sqlFileUrl != null && isNotBlank(sqlFileUrl.getFile())) {
            return sqlFileUrl.getPath();
        }

        return null;
    }

    private static String getSqlFileContent(String filePath) {
        String sqlFilePath = getSqlFilePath(filePath);
        if (isNotBlank(sqlFilePath)) {
            StringBuilder contentBuilder = new StringBuilder();
            try (Stream<String> stream = lines(Paths.get(filePath), UTF_8)) {
                stream.forEach(s -> contentBuilder.append(s).append("\n"));
            } catch (IOException e) {
                CommonUtils.log(SEVERE, "Error reading the table creation SQL file", e);
            }

            return contentBuilder.toString();
        }

        return null;
    }

}
