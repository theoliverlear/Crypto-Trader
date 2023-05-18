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
    public CryptoTraderDatabase() throws IOException {
        // Test Currency for now
        this.shib = new Currency("Shiba-Inu", "SHIB", "https://api.coinbase.com/v2/prices/SHIB-USD/spot");
        this.btc = new Currency("Bitcoin", "BTC", "https://api.coinbase.com/v2/prices/BTC-USD/spot");
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

    public Currency getSHIB() {
        return this.shib;
    }
    public Currency getBTC() {
        return this.btc;
    }
    public Connection getConnection() {
        return this.connection;
    }

    public static void main(String[] args) throws IOException {
        CryptoTraderDatabase cryptoTraderDatabase = new CryptoTraderDatabase();

        double shibValue = cryptoTraderDatabase.getSHIB().getUpdatedValue();
        String query1 = "DECLARE @shib_value DECIMAL(14, 8); SET @shib_value = " + shibValue + "; UPDATE CurrencyValues SET exchange_rate = @shib_value WHERE currency_code = 'SHIB'";
        try {
            PreparedStatement statement = cryptoTraderDatabase.getConnection().prepareStatement(query1);
            try {
                statement.execute();
            } finally {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String shibValueFormat = cryptoTraderDatabase.getSHIB().getFormattedValue().trim();
        String query2 = "DECLARE @shib_value_format VARCHAR(255); SET @shib_value_format = " + "'" + shibValueFormat + "'" + "; UPDATE CurrencyValues SET exchange_rate_formatted = @shib_value_format WHERE currency_code = 'SHIB'";
        try {
            PreparedStatement statement = cryptoTraderDatabase.getConnection().prepareStatement(query2);
            try {
                statement.execute();
            } finally {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        double btcValue = cryptoTraderDatabase.getBTC().getUpdatedValue();
        String query3 = "DECLARE @btc_value DECIMAL(14, 8); SET @btc_value = " + btcValue + " UPDATE CurrencyValues SET exchange_rate = @btc_value WHERE currency_code = 'BTC'";
        try {
            PreparedStatement statement = cryptoTraderDatabase.getConnection().prepareStatement(query3);
            try {
                statement.execute();
            } finally {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        String btcValueFormat = cryptoTraderDatabase.getBTC().getFormattedValue().trim();
        String query4 = "DECLARE @btc_value_format VARCHAR(255); SET @btc_value_format = " + "'" + btcValueFormat + "'" + "; UPDATE CurrencyValues SET exchange_rate_formatted = @btc_value_format WHERE currency_code = 'BTC'";
        try {
            PreparedStatement statement = cryptoTraderDatabase.getConnection().prepareStatement(query4);
            try {
                statement.execute();
            } finally {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
