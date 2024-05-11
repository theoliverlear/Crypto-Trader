//package org.theoliverlear.update;
//
//import org.theoliverlear.entity.Portfolio;
//import org.theoliverlear.model.thread.PortfolioUpdaterThread;
//import org.theoliverlear.model.thread.ThreadManager;
//
//import java.util.ArrayList;
//
//public class PortfolioUpdater {
//    ArrayList<Portfolio> portfolios;
//    ArrayList<Thread> portfolioUpdaters;
//    ThreadManager portfolioUpdaterThreadManager;
//    public PortfolioUpdater(ArrayList<Portfolio> portfolios) {
//        this.portfolios = portfolios;
//        this.portfolioUpdaters = new ArrayList<>();
//        this.startPortfolioUpdaters();
//    }
//    public void startPortfolioUpdaters() {
//        for (final Portfolio portfolio : this.portfolios) {
//            this.portfolioUpdaters.add(new PortfolioUpdaterThread(portfolio));
//        }
//        this.portfolioUpdaterThreadManager = new ThreadManager(this.portfolioUpdaters);
//        this.portfolioUpdaterThreadManager.startThreads();
//    }
//
//    public ArrayList<Portfolio> getPortfolios() {
//        return this.portfolios;
//    }
//    public ArrayList<Thread> getPortfolioUpdaters() {
//        return this.portfolioUpdaters;
//    }
//}
