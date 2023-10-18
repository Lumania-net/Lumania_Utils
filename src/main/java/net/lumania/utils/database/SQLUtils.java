package net.lumania.utils.database;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * SQL Utility Class
 * @author sparkofyt
 * @version 1.0
 * @since 1.0
 */
public class SQLUtils {

    /**
     * Method to Execute SQL Operations from a Resource File
     * @param plugin The Plugin to read the SQL File Resource from
     * @param dataSource The DataSource to establish Database Connection
     * @param sqlFile The Resource SQL File Name
     * @since 1.0
     */
    public static void executeSQLFile(Plugin plugin, HikariDataSource dataSource, String sqlFile) {
        String sql = null;
        try(InputStream is = plugin.getResource(sqlFile)) {
            if(is == null) return;
            sql = new String(is.readAllBytes());
        } catch (IOException exception) {
            plugin.getSLF4JLogger().error("Error while reading '"+sqlFile+"'", exception);
        }

        if(sql == null) return;
        String[] queries = sql.split(";");

        try(Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            for(String query : queries) {
                if(query.isBlank()) continue;

                try(PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.execute();
                } catch (SQLException exception) {
                    plugin.getSLF4JLogger().error("Error while executing query: '" + query + "'", exception);
                }
            }
        } catch (SQLException exception) {
            plugin.getSLF4JLogger().error("Error while creating connection", exception);
        }
    }

}
