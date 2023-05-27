package CryptoTraderV2;

public class PortfolioAssetThread implements Runnable {
    PortfolioAsset portfolioAsset;
    public PortfolioAssetThread(PortfolioAsset portfolioAsset) {
        this.portfolioAsset = portfolioAsset;

    }


    @Override
    public void run() {
        do {
            this.portfolioAsset.poll();
        } while (true);
    }
}
