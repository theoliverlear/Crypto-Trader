package org.theoliverlear.model.deprecated;
import org.theoliverlear.coinbasev2.FileDataRetriever;

import java.io.IOException;
import java.sql.*;

public class CryptoTraderDatabase {
    Connection connection;
    Currency currency;
    String connectionUrl;
    public CryptoTraderDatabase(Currency currency) throws IOException {
        this.currency = currency;
        try {
            String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            Class.forName(driver);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        String path = "C:\\Users\\olive\\OneDrive\\Documents\\" +
                      "Key Folder\\CryptoTraderLogin.txt";
        FileDataRetriever fdrUsername = new FileDataRetriever(0, path);
        FileDataRetriever fdrPassword = new FileDataRetriever(1, path);
        String username = fdrUsername.getData();
        String password = fdrPassword.getData();
        String serverPrefix = "jdbc:sqlserver://";
        String server = "crypto-trader-server.database.windows.net:1433;";
        String database = "database=CryptoTrader;";
        String user = "user={" + username + "};password={" + password + "};";
        String ending = "encrypt=true;trustServerCertificate=false;" +
                        "hostNameInCertificate=*.database.windows.net;" +
                        "loginTimeout=30;authentication=ActiveDirectoryPassword" +
                        ";autoReconnect=true";
        this.connectionUrl = serverPrefix + server + database + user + ending;
        try {
            this.connection = DriverManager.getConnection(this.connectionUrl);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (this.connection != null) {
            System.out.println("Connected to database!");
        } else {
            System.out.println("Failed to connect to database!");
        }
    }
    public void reconnect() {
        try {
            this.connection = DriverManager.getConnection(this.connectionUrl);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public Currency getCurrency() {
        return this.currency;
    }
    public Connection getConnection() {
        return this.connection;
    }
    public void commandQuery(String command) {
        try {
            try (PreparedStatement statement = this.getConnection().prepareStatement(command)) {
                try {
                    statement.execute();
                } finally {
                    //statement.close();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            this.reconnect();
        }
    }
    public double getValueFromPortfolioDatabase(String table, String columnName) throws SQLException {
        double amount = 0.00;
        String command = "SELECT " + columnName + " FROM " + table +
                         " WHERE currency_code = '" + this.getCurrency().getCurrencyCode()
                       + "'";
        ResultSet resultSet;
        try (Statement statement = this.getConnection().createStatement()) {
            resultSet = statement.executeQuery(command);
            if (resultSet.next()) {
                amount = resultSet.getDouble(columnName);
            }
        }

        return amount;
    }

    public void updateCurrency() throws IOException {
        double exchangeRate = this.getCurrency().getUpdatedValue();
        String query3 = "DECLARE @exchange_rate_value DECIMAL(14, 8); SET @exchange_rate_value = "
                        + exchangeRate + " UPDATE CurrencyValue SET exchange_rate = @exchange_rate_value " +
                        "WHERE currency_code = '" + this.getCurrency().getCurrencyCode() + "';";
        query3 += "UPDATE CurrencyValue SET time_updated = SWITCHOFFSET(GETDATE(), '-05:00') " +
                  "WHERE currency_code = '" + this.getCurrency().getCurrencyCode() + "';";
        this.commandQuery(query3);

        String formattedExchangeRate = this.getCurrency().getFormattedValue().trim();
        String query4 = "DECLARE @exchange_rate_value_formatted VARCHAR(255); " +
                        "SET @exchange_rate_value_formatted = " + "'" + formattedExchangeRate +
                        "'" + "; UPDATE CurrencyValue SET exchange_rate_formatted = @exchange_rate_value_formatted " +
                        "WHERE currency_code = '" + this.getCurrency().getCurrencyCode() + "';";
        this.commandQuery(query4);

        String query6 = "INSERT INTO CurrencyValueHistory (time_event, currency_code, currency_name, exchange_rate, exchange_rate_formatted)";
        query6 += "VALUES (SWITCHOFFSET(GETDATE(), '-05:00'), '" + this.getCurrency().getCurrencyCode() + "', '"
                  + this.getCurrency().getName()+ "', " + exchangeRate + ", " + "'" + formattedExchangeRate + "'" + ")";
        this.commandQuery(query6);
    }
    public void updateCurrencyInterval() throws IOException {
        double exchangeRate = this.getCurrency().getUpdatedValue();
        String formattedExchangeRate = this.getCurrency().getFormattedValue().trim();
        String query6 = "INSERT INTO CurrencyValueHistoryInterval (time_event, " +
                "currency_code, currency_name, exchange_rate, exchange_rate_formatted)";
        query6 += "VALUES (SWITCHOFFSET(GETDATE(), '-05:00'), '" + this.getCurrency().getCurrencyCode() +
                "', '" + this.getCurrency().getName()+ "', " + exchangeRate +
                ", " + "'" + formattedExchangeRate + "'" + ")";
        this.commandQuery(query6);
    }
    public static void main(String[] args) throws IOException {
        /*
        Currency bitcoin = new Currency("Bitcoin", "BTC", "https://api.coinbase.com/v2/prices/BTC-USD/spot");
        Currency shiba = new Currency("Shiba Inu", "SHIB", "https://api.coinbase.com/v2/prices/SHIB-USD/spot");
        Currency ethereum = new Currency("Ethereum", "ETH", "https://api.coinbase.com/v2/prices/ETH-USD/spot");
        Currency doge = new Currency("Dogecoin", "DOGE", "https://api.coinbase.com/v2/prices/DOGE-USD/spot");
        Currency litecoin = new Currency("Litecoin", "LTC", "https://api.coinbase.com/v2/prices/LTC-USD/spot");
        Currency cardano = new Currency("Cardano", "ADA", "https://api.coinbase.com/v2/prices/ADA-USD/spot");
        Currency solana = new Currency("Solana", "SOL", "https://api.coinbase.com/v2/prices/SOL-USD/spot");
        Currency polygon = new Currency("Polygon", "MATIC", "https://api.coinbase.com/v2/prices/MATIC-USD/spot");
        Currency chainlink = new Currency("Chainlink", "LINK", "https://api.coinbase.com/v2/prices/LINK-USD/spot");
        Currency stellarLumens = new Currency("Stellar Lumens", "XLM", "https://api.coinbase.com/v2/prices/XLM-USD/spot");
        Currency polkadot = new Currency("Polkadot", "DOT", "https://api.coinbase.com/v2/prices/DOT-USD/spot");
        */

        Currency bitcoin = new Currency(OfficialCurrency.BTC);
        Currency shiba = new Currency(OfficialCurrency.SHIB);
        Currency ethereum = new Currency(OfficialCurrency.ETH);
        Currency doge = new Currency(OfficialCurrency.DOGE);
        Currency litecoin = new Currency(OfficialCurrency.LTC);
        Currency cardano = new Currency(OfficialCurrency.ADA);
        Currency solana = new Currency(OfficialCurrency.SOL);
        Currency polygon = new Currency(OfficialCurrency.MATIC);
        Currency chainlink = new Currency(OfficialCurrency.LINK);
        Currency stellarLumens = new Currency(OfficialCurrency.XLM);
        Currency polkadot = new Currency(OfficialCurrency.DOT);

        CurrencyThread bitcoinThread = new CurrencyThread(bitcoin);
        CurrencyThread shibaThread = new CurrencyThread(shiba);
        CurrencyThread ethereumThread = new CurrencyThread(ethereum);
        CurrencyThread dogeThread = new CurrencyThread(doge);
        CurrencyThread litecoinThread = new CurrencyThread(litecoin);
        CurrencyThread cardanoThread = new CurrencyThread(cardano);
        CurrencyThread solanaThread = new CurrencyThread(solana);
        CurrencyThread polygonThread = new CurrencyThread(polygon);
        CurrencyThread chainlinkThread = new CurrencyThread(chainlink);
        CurrencyThread stellarLumensThread = new CurrencyThread(stellarLumens);
        CurrencyThread polkadotThread = new CurrencyThread(polkadot);

        CurrencyIntervalThread bitcoinIntervalThread = new CurrencyIntervalThread(bitcoin);
        CurrencyIntervalThread shibaIntervalThread = new CurrencyIntervalThread(shiba);
        CurrencyIntervalThread ethereumIntervalThread = new CurrencyIntervalThread(ethereum);
        CurrencyIntervalThread dogeIntervalThread = new CurrencyIntervalThread(doge);
        CurrencyIntervalThread litecoinIntervalThread = new CurrencyIntervalThread(litecoin);
        CurrencyIntervalThread cardanoIntervalThread = new CurrencyIntervalThread(cardano);
        CurrencyIntervalThread solanaIntervalThread = new CurrencyIntervalThread(solana);
        CurrencyIntervalThread polygonIntervalThread = new CurrencyIntervalThread(polygon);
        CurrencyIntervalThread chainlinkIntervalThread = new CurrencyIntervalThread(chainlink);
        CurrencyIntervalThread stellarLumensIntervalThread = new CurrencyIntervalThread(stellarLumens);
        CurrencyIntervalThread polkadotIntervalThread = new CurrencyIntervalThread(polkadot);

        Thread BTCThread = new Thread(bitcoinThread);
        Thread SHIBThread = new Thread(shibaThread);
        Thread ETHThread = new Thread(ethereumThread);
        Thread DOGEThread = new Thread(dogeThread);
        Thread LTCThread = new Thread(litecoinThread);
        Thread ADAThread = new Thread(cardanoThread);
        Thread SOLThread = new Thread(solanaThread);
        Thread POLYThread = new Thread(polygonThread);
        Thread LINKThread = new Thread(chainlinkThread);
        Thread XLMThread = new Thread(stellarLumensThread);
        Thread DOTThread = new Thread(polkadotThread);

        Thread BTCIntervalThread = new Thread(bitcoinIntervalThread);
        Thread SHIBIntervalThread = new Thread(shibaIntervalThread);
        Thread ETHIntervalThread = new Thread(ethereumIntervalThread);
        Thread DOGEIntervalThread = new Thread(dogeIntervalThread);
        Thread LTCIntervalThread = new Thread(litecoinIntervalThread);
        Thread ADAIntervalThread = new Thread(cardanoIntervalThread);
        Thread SOLIntervalThread = new Thread(solanaIntervalThread);
        Thread POLYIntervalThread = new Thread(polygonIntervalThread);
        Thread LINKIntervalThread = new Thread(chainlinkIntervalThread);
        Thread XLMIntervalThread = new Thread(stellarLumensIntervalThread);
        Thread DOTIntervalThread = new Thread(polkadotIntervalThread);

        Thread[] threads = {BTCThread, SHIBThread, ETHThread, DOGEThread,
                LTCThread, ADAThread, SOLThread, POLYThread, LINKThread,
                XLMThread, DOTThread, BTCIntervalThread, SHIBIntervalThread,
                ETHIntervalThread, DOGEIntervalThread, LTCIntervalThread,
                ADAIntervalThread, SOLIntervalThread, POLYIntervalThread,
                LINKIntervalThread, XLMIntervalThread, DOTIntervalThread};
        for (Thread thread : threads) {
            thread.start();
        }
    }
}
