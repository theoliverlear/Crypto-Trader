package CryptoTraderV2;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;


public class CryptoTraderDatabase {
    Connection connection;
    Currency shib;
    Currency btc;
    Currency currency;
    public CryptoTraderDatabase(Currency currency) throws IOException {
        // Test Currency for now
        this.currency = currency;
//        this.shib = new Currency("Shiba-Inu", "SHIB", "https://api.coinbase.com/v2/prices/SHIB-USD/spot");
//        this.btc = new Currency("Bitcoin", "BTC", "https://api.coinbase.com/v2/prices/BTC-USD/spot");
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(new File("C:\\Users\\olive\\OneDrive\\Documents\\Key Folder\\CryptoTraderLogin.txt"));
        String username = "";
        String password = "";
        while (scanner.hasNext()) {
            username = scanner.next();
            password = scanner.next();
        }
        String connectionUrl = "jdbc:sqlserver://crypto-trader-server.database.windows.net:1433;database=CryptoTrader;user={" + username + "};password={" + password + "};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;authentication=ActiveDirectoryPassword";
        //String connectionUrl = "jdbc:sqlserver://crypto-trader-server.database.windows.net:1433;database=CryptoTrader;encrypt=true;trustServerCertificate=false;loginTimeout=30;Authentication=ActiveDirectoryIntegrated";
        try {
            this.connection = DriverManager.getConnection(connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (this.connection != null) {
            System.out.println("Connected to database!");
        }
        else {
            System.out.println("Failed to connect to database!");
        }
    }

//    public Currency getSHIB() {
//        return this.shib;
//    }
//    public Currency getBTC() {
//        return this.btc;
//    }
    public Currency getCurrency() {
        return this.currency;
    }
    public Connection getConnection() {
        return this.connection;
    }
    public void commandQuery(String command) {
        try {
            PreparedStatement statement = this.getConnection().prepareStatement(command);
            try {
                statement.execute();
            } finally {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//    public void SHIBMethod() {
//        double shibValue = this.getSHIB().getUpdatedValue();
//        String query1 = "DECLARE @shib_value DECIMAL(14, 8); SET @shib_value = " + shibValue + "; UPDATE CurrencyValue SET exchange_rate = @shib_value WHERE currency_code = 'SHIB';";
//        query1 += "UPDATE CurrencyValue SET time_updated = SWITCHOFFSET(GETDATE(), '-05:00')  WHERE currency_code = 'SHIB';";
//        this.commandQuery(query1);
//
//        String shibValueFormat = this.getSHIB().getFormattedValue().trim();
//        String query2 = "DECLARE @shib_value_format VARCHAR(255); SET @shib_value_format = " + "'" + shibValueFormat + "'" + "; UPDATE CurrencyValue SET exchange_rate_formatted = @shib_value_format WHERE currency_code = 'SHIB';";
//        this.commandQuery(query2);
//        String query5 = "INSERT INTO CurrencyValueHistory (time_event, currency_code, currency_name, exchange_rate, exchange_rate_formatted)";
//        query5 += "VALUES (SWITCHOFFSET(GETDATE(), '-05:00'), 'SHIB', 'Shiba-Inu', " + shibValue + ", " + "'" + shibValueFormat + "'" + ")";
//        this.commandQuery(query5);
//    }
    public void updateCurrency() {
        double exchangeRate = this.getCurrency().getUpdatedValue();
        String query3 = "DECLARE @exchange_rate_value DECIMAL(14, 8); SET @exchange_rate_value = " + exchangeRate + " UPDATE CurrencyValue SET exchange_rate = @exchange_rate_value WHERE currency_code = '" + this.getCurrency().getCurrencyCode() + "';";
        query3 += "UPDATE CurrencyValue SET time_updated = SWITCHOFFSET(GETDATE(), '-05:00') WHERE currency_code = '" + this.getCurrency().getCurrencyCode() + "';";
        this.commandQuery(query3);


        String formattedExchangeRate = this.getCurrency().getFormattedValue().trim();
        String query4 = "DECLARE @exchange_rate_value_formatted VARCHAR(255); SET @exchange_rate_value_formatted = " + "'" + formattedExchangeRate + "'" + "; UPDATE CurrencyValue SET exchange_rate_formatted = @exchange_rate_value_formatted WHERE currency_code = '" + this.getCurrency().getCurrencyCode() + "';";
        this.commandQuery(query4);



        String query6 = "INSERT INTO CurrencyValueHistory (time_event, currency_code, currency_name, exchange_rate, exchange_rate_formatted)";
        query6 += "VALUES (SWITCHOFFSET(GETDATE(), '-05:00'), '" + this.getCurrency().getCurrencyCode() + "', '" + this.getCurrency().getName()+ "', " + exchangeRate + ", " + "'" + formattedExchangeRate + "'" + ")";
        this.commandQuery(query6);
    }
    public void updateCurrencyInterval() {
        double exchangeRate = this.getCurrency().getUpdatedValue();

        String formattedExchangeRate = this.getCurrency().getFormattedValue().trim();

        String query6 = "INSERT INTO CurrencyValueHistoryInterval (time_event, currency_code, currency_name, exchange_rate, exchange_rate_formatted)";
        query6 += "VALUES (SWITCHOFFSET(GETDATE(), '-05:00'), '" + this.getCurrency().getCurrencyCode() + "', '" + this.getCurrency().getName()+ "', " + exchangeRate + ", " + "'" + formattedExchangeRate + "'" + ")";
        this.commandQuery(query6);
    }
    public static void main(String[] args) throws IOException {




    }
}
