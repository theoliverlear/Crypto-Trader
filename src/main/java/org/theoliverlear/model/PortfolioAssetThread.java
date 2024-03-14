package org.theoliverlear.model;

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
                Thread.currentThread().sleep(5000);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while (true);
    }
}
