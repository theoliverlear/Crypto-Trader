package org.theoliverlear.repository.db;
@Deprecated
public class DatabaseManager {
    DatabaseConnection dbConnection;
    public DatabaseManager() {
        this.dbConnection = new DatabaseConnection();
    }

    public void queryCommand(SqlCommand command) {

    }
}
