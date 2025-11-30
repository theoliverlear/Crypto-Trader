package org.cryptotrader.api.library.entity.user.builder.models;

import org.cryptotrader.api.library.entity.user.ProductUser;
import org.cryptotrader.api.library.entity.user.ProfilePicture;
import org.cryptotrader.universal.library.model.BuilderFactory;

public abstract class AbstractProfilePicture implements BuilderFactory<ProfilePicture> {
    public abstract AbstractProfilePicture fileName(String fileName);
    public abstract AbstractProfilePicture fileType(String fileType);
    public abstract AbstractProfilePicture fileData(byte[] fileData);
    public abstract AbstractProfilePicture user(ProductUser user);
}
