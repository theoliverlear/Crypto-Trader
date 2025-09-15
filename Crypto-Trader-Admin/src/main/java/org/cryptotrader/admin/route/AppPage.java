package org.cryptotrader.admin.route;

public enum AppPage {
    ADMIN_USERS("Admin Users"),
    AUTH("Authorize"),
    DASHBOARD("Dashboard"),
    EMAIL("Email"),
    TABLES("Tables");
    public final String pageName;
    AppPage(String pageName) {
        this.pageName = pageName;
    }
}
