package CryptoTraderV2.CoinbaseV2;


import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataParser {
    String rawData;
    String data;
    String dataBlock;
    TreeMap<String, String> jsonMap;
    public DataParser(String rawData) {
        this.rawData = rawData;
        this.data = parse();
        this.jsonMap = new TreeMap<>();
        this.jsonMap = this.parseJson();
        this.dataBlock = this.parseDataPoint();
    }
    // Parenthesis are the groups
    //String regexPattern = "\"([^\"]+)\":\\s*\"([^\"]*)\"";
    //String[] regexPatterns = {"\"([^\"]+)\":\\s*\"([^\"]*)\"",
    //                          "\"([^\"]+)\":\\{\"([^\"]*)\""};
    public String parse() {
        StringBuilder dataSB = new StringBuilder();
        String[] regexPatterns = {"\"(\\w+)\":\\s*(\"[^\"]*\"|[^\",\\n}]+)"};
        for (String regexPattern : regexPatterns) {
            Pattern pattern = Pattern.compile(regexPattern);
            Matcher matcher = pattern.matcher(this.rawData);
            int groupCount = matcher.groupCount();
            if (groupCount == 2) {
                while (matcher.find()) {
                    dataSB.append(matcher.group(1) + " : " + matcher.group(2)
                                                                    .replace("\"", "") + "\n");
                }
            }
        }
        return dataSB.toString();
    }
        //String[] regexPatterns = {"\"(\\w+)\":\\s*(\"[^\"]*\"|[^\",\\n}]+)"};
    //dataSB.append(matcher.group(1) + " : " + matcher.group(2).replace("\"", "") + "\n");
    public String parseDataPoint() {
        StringBuilder dataSB = new StringBuilder();
        String regexPattern = "\"id\".*?(\\}\\},\\{|(ls\":(true|false)\\}))";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(this.rawData);
        while (matcher.find()) {
            dataSB.append((matcher.group(0) + "\n"));
        }
        return dataSB.toString();
    }
    public TreeMap<String, String> parseJson() {
        TreeMap<String, String> jsonMap = new TreeMap<>();
        for (String line : this.data.split("\n")) {
            String[] keyValue = line.split(":");
            jsonMap.put(keyValue[0].trim()
                    .replace("{", "")
                    .replace("}", ""),
                    keyValue[1].trim()
                            .replace("{", "")
                            .replace("}", ""));
        }
        return jsonMap;
    }
    public String getValue(String key) {
        return this.jsonMap.getOrDefault(key, null);
    }
    public String getData() {
        return this.data;
    }
    public String getRawData() {
        return this.rawData;
    }
    public String getDataBlock() {
        return this.dataBlock;
    }
    public TreeMap<String, String> getJsonMap() {
        return this.jsonMap;
    }
}
