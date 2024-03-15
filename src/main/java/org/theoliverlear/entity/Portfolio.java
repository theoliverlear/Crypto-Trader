package org.theoliverlear.entity;

import java.util.ArrayList;

public class Portfolio {
    private ArrayList<PortfolioAsset> assets;
    public Portfolio() {
        this.assets = new ArrayList<>();
    }
    public ArrayList<PortfolioAsset> getAssets() {
        return this.assets;
    }
    public void setAssets(ArrayList<PortfolioAsset> assets) {
        this.assets = assets;
    }
}
