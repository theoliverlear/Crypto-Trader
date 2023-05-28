package CryptoTraderV2;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;


public class CryptoTraderDatabase {
    Connection connection;
    Currency shib;
    Currency btc;
    Currency currency;
    public CryptoTraderDatabase(Currency currency) throws IOException {
        this.currency = currency;
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
                //statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public double getValueFromPortfolioDatabase(String table, String columnName) throws SQLException {
        double amount = 0.00;
        String command = "SELECT " + columnName + " FROM " + table + " WHERE currency_code = '" + this.getCurrency().getCurrencyCode() + "'";
        Statement statement = this.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(command);
        if (resultSet.next()) {
            amount = resultSet.getDouble(columnName);
        }
        return amount;
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
    public void updateCurrency() throws IOException {
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
    public void updateCurrencyInterval() throws IOException {
        double exchangeRate = this.getCurrency().getUpdatedValue();

        String formattedExchangeRate = this.getCurrency().getFormattedValue().trim();

        String query6 = "INSERT INTO CurrencyValueHistoryInterval (time_event, currency_code, currency_name, exchange_rate, exchange_rate_formatted)";
        query6 += "VALUES (SWITCHOFFSET(GETDATE(), '-05:00'), '" + this.getCurrency().getCurrencyCode() + "', '" + this.getCurrency().getName()+ "', " + exchangeRate + ", " + "'" + formattedExchangeRate + "'" + ")";
        this.commandQuery(query6);
    }
    public static void main(String[] args) throws IOException {
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

        BTCThread.start();
        SHIBThread.start();
        ETHThread.start();
        DOGEThread.start();
        LTCThread.start();
        ADAThread.start();
        SOLThread.start();
        POLYThread.start();
        LINKThread.start();
        XLMThread.start();
        DOTThread.start();

        BTCIntervalThread.start();
        SHIBIntervalThread.start();
        ETHIntervalThread.start();
        DOGEIntervalThread.start();
        LTCIntervalThread.start();
        ADAIntervalThread.start();
        SOLIntervalThread.start();
        POLYIntervalThread.start();
        LINKIntervalThread.start();
        XLMIntervalThread.start();
        DOTIntervalThread.start();
    }
}
