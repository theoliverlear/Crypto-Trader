package CryptoTraderV2.CoinbaseV2;

import CryptoTraderV2.CoinbaseV2.FileDataRetriever;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CoinbaseCentral {
    static String header = "-".repeat(50);
            //String requestPath = "/v2/accounts";
        //String requestPath = "/v2/user/auth";
        //String requestPath = "/v2/transaction_summary";
        //String requestPath = "/v2/exchange-rates?currency=USD";
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String baseURL = "https://api.coinbase.com";
        String methodCall = "GET";
        String requestPath = "/v2/user";


        String body = "";
        String hashAlgorithm = "HmacSHA256";
        FileDataRetriever keyRetriever = new FileDataRetriever(1, "C:\\Users\\olive\\OneDrive\\Documents\\Key Folder\\CoinbaseAPI.txt");
        FileDataRetriever secretRetriever = new FileDataRetriever(2, "C:\\Users\\olive\\OneDrive\\Documents\\Key Folder\\CoinbaseAPI.txt");
        String apiKey = keyRetriever.getData();
        String apiSecret = secretRetriever.getData();
        CoinbaseConnection connection = new CoinbaseConnection(baseURL,
                                                               methodCall,
                                                               apiKey,
                                                               apiSecret,
                                                               hashAlgorithm,
                                                               requestPath,
                                                               body);
        connection.connect();
        DataParser parser = new DataParser(connection.getPageData());
        System.out.println(parser.getRawData());
        System.out.println(header);

        String path2 = "/v2/accounts";
        CoinbaseConnection connection2 = new CoinbaseConnection(baseURL,
                                                                methodCall,
                                                                apiKey,
                                                                apiSecret,
                                                                hashAlgorithm,
                                                                path2,
                                                                body);
        connection2.connect();
        DataParser parser2 = new DataParser(connection2.getPageData());
        System.out.println(parser2.getRawData());
        String id = parser2.getValue("id");
        System.out.println(id);
        CoinbaseAccount account = new CoinbaseAccount(id);
        System.out.println(header);

        String path3 = "/v2/accounts/" + account.getId();
        CoinbaseConnection connection3 = new CoinbaseConnection(baseURL,
                                                                methodCall,
                                                                apiKey,
                                                                apiSecret,
                                                                hashAlgorithm,
                                                                path3,
                                                                body);
        connection3.connect();
        DataParser parser3 = new DataParser(connection3.getPageData());
        System.out.println(parser3.getRawData());
        System.out.println(header);

        String path4 = "/v2/accounts/" + account.getId() + "/transactions";
        CoinbaseConnection connection4 = new CoinbaseConnection(baseURL,
                                                                methodCall,
                                                                apiKey,
                                                                apiSecret,
                                                                hashAlgorithm,
                                                                path4,
                                                                body);
        connection4.connect();
        DataParser parser4 = new DataParser(connection4.getPageData());
        System.out.println(parser4.getRawData());
        System.out.println(header);

        System.out.println(parser4.getData());
        String transId = parser4.getValue("id");
        System.out.println(transId);
        parser4.getJsonMap().forEach((k, v) -> System.out.println(k + " : " + v));
//        String path5 = "/v2/accounts/" + account.getId() + "/transactions/" + transId;
//        CoinbaseConnection connection5 = new CoinbaseConnection(baseURL,
//                                                                methodCall,
//                                                                apiKey,
//                                                                apiSecret,
//                                                                hashAlgorithm,
//                                                                path5,
//                                                                body);
//        connection5.connect();
//        DataParser parser5 = new DataParser(connection5.getPageData());
//        System.out.println(parser5.getRawData());
//        System.out.println(header);


//        String id = parser.getValue("id");
//        System.out.println(id);
//        CoinbaseAccount account = new CoinbaseAccount(id);
//        System.out.println(parser.getRawData());
//        System.out.println(header);
//
//        String path2 = "/v2/accounts/" + account.getId();
//        CoinbaseConnection connection2 = new CoinbaseConnection(baseURL,
//                                                                methodCall,
//                                                                apiKey,
//                                                                apiSecret,
//                                                                hashAlgorithm,
//                                                                path2,
//                                                                body);
//        connection2.connect();
//        DataParser parser2 = new DataParser(connection2.getPageData());
//        System.out.println(parser2.getRawData());

    }
}
