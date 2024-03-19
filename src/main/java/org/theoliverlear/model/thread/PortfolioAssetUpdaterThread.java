package org.theoliverlear.model.thread;


import org.theoliverlear.entity.PortfolioAsset;

public class PortfolioAssetUpdaterThread extends Thread {
    PortfolioAsset portfolioAsset;
    PortfolioAsset lastPortfolioAsset;
    public PortfolioAssetUpdaterThread(PortfolioAsset portfolioAsset) {
        this.portfolioAsset = portfolioAsset;
        this.lastPortfolioAsset = portfolioAsset;
    }
    @Override
    public void run() {
        while (true) {
            try {
                if (!this.portfolioAsset.equals(this.lastPortfolioAsset)) {
                    this.portfolioAsset.updateValues();
                    this.lastPortfolioAsset = this.portfolioAsset;
                }
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
