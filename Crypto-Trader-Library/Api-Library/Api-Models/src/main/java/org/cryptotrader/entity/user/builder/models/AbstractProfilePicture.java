package org.cryptotrader.entity.user.builder.models;

import org.cryptotrader.entity.user.ProductUser;
import org.cryptotrader.entity.user.ProfilePicture;
import org.cryptotrader.model.BuilderFactory;

public abstract class AbstractProfilePicture implements BuilderFactory<ProfilePicture> {
    public abstract AbstractProfilePicture fileName(String fileName);
    public abstract AbstractProfilePicture fileType(String fileType);
    public abstract AbstractProfilePicture fileData(byte[] fileData);
    public abstract AbstractProfilePicture user(ProductUser user);
}
