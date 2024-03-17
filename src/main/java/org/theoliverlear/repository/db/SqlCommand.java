package org.theoliverlear.repository.db;
@Deprecated
public class SqlCommand {
    String command;
    public SqlCommand() {
        this.command = "";
    }
    public SqlCommand(String command) {
        this.command = command;
    }
    public SqlCommand insertIntoCommand(String table, String values) {
        this.command = "INSERT INTO " + table + " VALUES " + values;
        return this;
    }
    public SqlCommand selectCommand(String table, String column, String condition) {
        this.command = "SELECT " + column + " FROM " + table + " WHERE " + condition;
        return this;
    }
    public SqlCommand updateCommand(String table, String column, String value, String condition) {
        this.command = "UPDATE " + table + " SET " + column + " = " + value + " WHERE " + condition;
        return this;
    }

    public String getCommand() {
        return this.command;
    }
    public void setCommand(String command) {
        this.command = command;
    }
}
