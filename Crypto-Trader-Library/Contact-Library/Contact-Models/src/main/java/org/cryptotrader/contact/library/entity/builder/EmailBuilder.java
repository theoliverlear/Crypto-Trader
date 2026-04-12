package org.cryptotrader.contact.library.entity.builder;

import org.cryptotrader.contact.library.entity.CryptoTraderMailer;
import org.cryptotrader.contact.library.entity.Email;
import org.cryptotrader.contact.library.entity.EmailType;
import org.cryptotrader.contact.library.entity.builder.models.AbstractEmail;

public class EmailBuilder extends AbstractEmail {
    private CryptoTraderMailer mailer;
    private String toAddress;
    private String subject;
    private String body;
    private EmailType type;

    public EmailBuilder() {
        this.mailer = CryptoTraderMailer.SUPPORT;
        this.type = EmailType.OTHER;
    }

    @Override
    public AbstractEmail cryptoTraderMailer(CryptoTraderMailer mailer) {
        this.mailer = mailer;
        return this;
    }

    @Override
    public AbstractEmail toAddress(String toAddress) {
        this.toAddress = toAddress;
        return this;
    }

    @Override
    public AbstractEmail subject(String subject) {
        this.subject = subject;
        return this;
    }

    @Override
    public AbstractEmail body(String body) {
        this.body = body;
        return this;
    }

    @Override
    public AbstractEmail type(EmailType type) {
        this.type = type;
        return this;
    }

    @Override
    public Email build() {
        if (this.toAddress == null || this.toAddress.isEmpty()) {
            throw new IllegalStateException("Recipient address must be provided");
        }
        if (this.subject == null || this.subject.isEmpty()) {
            throw new IllegalStateException("Subject must be provided");
        }
        if (this.body == null || this.body.isEmpty()) {
            throw new IllegalStateException("Body must be provided");
        }
        return new Email(this.mailer, this.toAddress, this.subject, this.body, this.type);
    }
}
