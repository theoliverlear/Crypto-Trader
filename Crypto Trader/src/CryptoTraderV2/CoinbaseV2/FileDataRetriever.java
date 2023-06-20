package CryptoTraderV2.CoinbaseV2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileDataRetriever {
    int targetLineIndex;
    String filePath;
    String data;
    public FileDataRetriever(int targetLineIndex, String filePath) {
        this.targetLineIndex = targetLineIndex;
        this.filePath = filePath;
        this.data = "";
        this.fetchData();
    }
    public void fetchData() {
        String line = "";
        try {
            int counter = 0;
            Scanner fileReader = new Scanner(new File(this.filePath));
            while (fileReader.hasNext()) {
                line = fileReader.nextLine();
                if (counter == this.targetLineIndex) {
                    this.data = line;
                }
                counter++;
            }
        } catch (FileNotFoundException err) {
            err.printStackTrace();
        }
    }
    public String getData() {
        return this.data;
    }
}
