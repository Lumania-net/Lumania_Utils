package net.lumania.utils.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.lumania.utils.config.ConfigManager;

/**
 * Utility class to establish a HikariCP MariaDB connection
 * @author sparkofyt
 * @version 1.0
 * @since 1.0
 */
public class DataSourceFactory {

    /**
     * Method to get an HikariDataSource based on an ConfigManager File
     * @param config The ConfigManger/File to read Credentials from
     * @return An HikariDataSource
     */
    public static HikariDataSource create(ConfigManager config) {
        HikariConfig hConfig = new HikariConfig();
        hConfig.setJdbcUrl("jdbc:mariadb://" + config.getString("Host") + ":" + config.getInt("Port") + "/" + config.getString("Database"));
        hConfig.setUsername(config.getString("User"));
        hConfig.setPassword(config.getString("Password"));
        hConfig.addDataSourceProperty("cachePrepStmts", "true");
        hConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return new HikariDataSource(hConfig);
    }

}
