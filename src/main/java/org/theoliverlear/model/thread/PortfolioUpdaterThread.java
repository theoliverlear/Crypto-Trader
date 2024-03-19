package org.theoliverlear.model.thread;

import org.theoliverlear.entity.Portfolio;

public class PortfolioUpdaterThread extends Thread {
    Portfolio portfolio;
    Portfolio lastPortfolio;
    public PortfolioUpdaterThread(Portfolio portfolio) {
        this.portfolio = portfolio;
        this.lastPortfolio = portfolio;
    }
    @Override
    public void run() {
        while (true) {
            try {
                if (!this.portfolio.equals(this.lastPortfolio)) {
                    this.portfolio.updateValues();
                    this.lastPortfolio = this.portfolio;
                }
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}
