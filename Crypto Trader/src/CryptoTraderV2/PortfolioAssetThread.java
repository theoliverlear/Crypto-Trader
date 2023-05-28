package CryptoTraderV2;

import java.io.IOException;

public class PortfolioAssetThread implements Runnable {
    PortfolioAsset portfolioAsset;
    public PortfolioAssetThread(PortfolioAsset portfolioAsset) {
        this.portfolioAsset = portfolioAsset;

    }


    @Override
    public void run() {
        do {
            try {
                this.portfolioAsset.poll();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } while (true);
    }
}
