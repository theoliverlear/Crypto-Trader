package CryptoTraderV2.Coinbase;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataParser {
    String rawData;
    String data;
    TreeMap<String, String> jsonMap;
    public DataParser(String rawData) {
        this.rawData = rawData;
        this.data = parse();
        this.jsonMap = new TreeMap<>();
        this.jsonMap = parseJson();
    }
    public String parse() {
        StringBuilder dataSB = new StringBuilder();
        // Parenthesis are the groups
        //String regexPattern = "\"([^\"]+)\":\\s*\"([^\"]*)\"";
        //String[] regexPatterns = {"\"([^\"]+)\":\\s*\"([^\"]*)\"", "\"([^\"]+)\":\\{\"([^\"]*)\""};
        String[] regexPatterns = {"\"(\\w+)\":\\s*(\"[^\"]*\"|[^\",\\n}]+)"};
        for (String regexPattern : regexPatterns) {
            Pattern pattern = Pattern.compile(regexPattern);
            Matcher matcher = pattern.matcher(this.rawData);
            while (matcher.find()) {
                dataSB.append(matcher.group(1) + " : " + matcher.group(2).replace("\"", "") + "\n");
            }
        }
        return dataSB.toString();
    }
    public TreeMap<String, String> parseJson() {
        TreeMap<String, String> jsonMap = new TreeMap<>();
        for (String line : this.data.split("\n")) {
            String[] keyValue = line.split(" : ");
            jsonMap.put(keyValue[0].trim()
                    .replace("{", "")
                    .replace("}", ""),
                    keyValue[1].trim()
                            .replace("{", "")
                            .replace("}", ""));
        }
        return jsonMap;
    }

    public String getData() {
        return this.data;
    }
    public String getRawData() {
        return this.rawData;
    }

    public TreeMap<String, String> getJsonMap() {
        return this.jsonMap;
    }
}
