package com.bymatej.minecraft.plugins.aihunter.utils;

import com.bymatej.minecraft.plugins.aihunter.data.HunterData;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

import static com.bymatej.minecraft.plugins.aihunter.utils.CommonUtils.*;
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

    public static void storeHunterData(HunterData hunter) {
        // todo: refactor and move the query to file: https://stackoverflow.com/a/12745238
        executeSqlQuery(String.format("INSERT INTO hunter (name, death_location_x, death_location_y, death_location_z, number_of_times_died, hunt_start_time)\n" +
                        "VALUES (%s, %s, %s, %s, %s, NULL);",
                hunter.getName(),
                hunter.getDeathLocationX(),
                hunter.getDeathLocationY(),
                hunter.getDeathLocationZ(),
                hunter.getNumberOfTimesDied()));
    }

    public static void removeHunter(String hunterName) {
        // todo: refactor and move the query to file: https://stackoverflow.com/a/12745238
        executeSqlQuery("DELETE FROM hunter WHERE name = '" + hunterName + "';");
    }

    public static void updateHunterCoordinates(HunterData hunter) {
        // todo: refactor and move the query to file: https://stackoverflow.com/a/12745238
        String query = String.format("UPDATE hunter SET death_location_x = %s, death_location_y = %s, death_location_z = %s WHERE name = %s;",
                hunter.getDeathLocationX(),
                hunter.getDeathLocationY(),
                hunter.getDeathLocationZ(),
                hunter.getName());
        executeSqlQuery(query);
    }

    private static Connection getConnection() {
        try {
            String dbUri = getPluginReference().getConfig().getString("db_uri");
            if (isNotBlank(dbUri)) {
                Class.forName("org.sqlite.JDBC");
                return DriverManager.getConnection(dbUri);
            }
        } catch (SQLException | ClassNotFoundException e) {
            log(SEVERE, "Error connecting to the DB.", e);
        }

        return null;
    }

    private static void executeSqlQuery(String query) {
        log("Executing the following query: \n\n" + query);
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
            log(SEVERE, "Failed to create table.", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                log(SEVERE, "Failed to create table.", e);
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
