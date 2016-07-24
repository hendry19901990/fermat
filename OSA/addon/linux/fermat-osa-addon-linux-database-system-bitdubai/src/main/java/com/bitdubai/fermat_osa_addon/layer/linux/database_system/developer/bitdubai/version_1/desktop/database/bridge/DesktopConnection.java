package com.bitdubai.fermat_osa_addon.layer.linux.database_system.developer.bitdubai.version_1.desktop.database.bridge;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * The class <code>com.bitdubai.fermat_osa_addon.layer.linux.database_system.developer.bitdubai.version_1.desktop.database.bridge.DesktopConnection</code>
 * bla bla bla.
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 22/07/2016.
 */
public class DesktopConnection {

    private final String databasePath;

    public DesktopConnection(String databasePath) {
        this.databasePath = databasePath;
    }

    public Connection getConnection() {

        try {
            //SQLite Driver
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
            connection.setAutoCommit(true);

            return connection;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
