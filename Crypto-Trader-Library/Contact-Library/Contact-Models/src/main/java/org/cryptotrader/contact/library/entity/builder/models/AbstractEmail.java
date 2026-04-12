package org.cryptotrader.contact.library.entity.builder.models;

import org.cryptotrader.contact.library.entity.CryptoTraderMailer;
import org.cryptotrader.contact.library.entity.Email;
import org.cryptotrader.contact.library.entity.EmailType;
import org.cryptotrader.universal.library.model.BuilderFactory;

public abstract class AbstractEmail implements BuilderFactory<Email> {
    public abstract AbstractEmail cryptoTraderMailer(CryptoTraderMailer mailer);
    public abstract AbstractEmail toAddress(String toAddress);
    public abstract AbstractEmail subject(String subject);
    public abstract AbstractEmail body(String body);
    public abstract AbstractEmail type(EmailType type);
    public abstract Email build();
}
