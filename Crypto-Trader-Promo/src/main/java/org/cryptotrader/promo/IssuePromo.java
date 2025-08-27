package org.cryptotrader.promo;

import org.cryptotrader.promo.models.openai.IssuePrompts;
import org.cryptotrader.promo.models.openai.Summarizer;
import org.cryptotrader.promo.models.openai.issue.Issue;
import org.cryptotrader.promo.models.twitter.Tweeter;

public class IssuePromo {
    public static void main(String[] args) {
//        Issue issue = new Issue("As a user, I want to be able to browse supported currencies.",
//                """
//                        - [ ] Add support for more currencies.
//                        - [ ] Create dedicated page for currencies.
//                        - [ ] Create back-end connections to get currencies.
//                        
//                        Points: 10""",
//                "backend, frontend, testing, new feature");
//        Issue issue = new Issue(
//                "As a user, I want to be able to import my portfolio data from Coinbase.",
//                """
//                        As a user, I want to be able to import my portfolio data from Coinbase.
//                        - [ ] Connect Coinbase classes and services to a portfolio builder.
//                        - [ ] Create a web interface with OAuth to connect Coinbase accounts.
//                        - [ ] Create back-end communication routing for requests from the user and OAuth.
//                        
//                        Points: 9""",
//                "Back-end, Complex, External Services, Front-end"
//        );
//        Issue issue = new Issue(
//          "As a user, I want to be able to delete my account and all my data through a web interface.",
//                """
//                        As a user, I want to be able to delete my account and all my data through a web interface.
//                        - [ ] Add a delete account button to the account webpage.
//                        - [ ] Add an "Are you sure?" prompt when deleting the account.
//                        - [ ] Add mapping to the back-end for deleting a user account.
//                        - [ ] Add services that delete all data associated with a user's ID in the database.
//                        
//                        Points: 5""",
//                "Back-end, Database, Front-end, Moderate Complexity"
//        );
        Issue issue = Issue.fromSystem();
        System.out.println(issue);
        Summarizer summarizer = new Summarizer();
        String summary = summarizer.summarize(issue.toString(),
                                              IssuePrompts.SYSTEM.getText(),
                                              IssuePrompts.USER.getText());
        System.out.println(summary);
        String mode = System.getenv("RELEASEBOT_MODE");
        boolean shouldPost = mode != null && (mode.equalsIgnoreCase("prod") || mode.equalsIgnoreCase("post"));

        if (shouldPost) {
            Tweeter tweeter = new Tweeter();
            tweeter.tweet(summary);
        } else {
            System.out.println("[INFO] Dry run mode (RELEASEBOT_MODE=" + mode + "): not posting to X.");
        }
    }
}
