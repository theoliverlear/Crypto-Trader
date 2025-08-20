package org.theoliverlear.admin.route;

public enum AppPage {
    ADMIN("Admin"),
    AUTH("Authorize"),
    TABLE("Table");
    public final String pageName;
    AppPage(String pageName) {
        this.pageName = pageName;
    }
}
