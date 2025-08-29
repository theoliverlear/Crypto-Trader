package org.cryptotrader.promo;

import org.cryptotrader.promo.models.github.issue.Issue;
import org.cryptotrader.promo.models.openai.prompt.IssuePrompts;
import org.cryptotrader.promo.models.openai.Summarizer;
import org.cryptotrader.promo.models.publish.Publisher;

public class IssuePromo {
    public static void main(String[] args) {
        Issue issue = Issue.fromSystem();
        Summarizer summarizer = new Summarizer();
        String summary = summarizer.summarize(issue.toString(),
                                              IssuePrompts.SYSTEM.getText(),
                                              IssuePrompts.USER.getText());
        Publisher.publish(summary);
    }
}
