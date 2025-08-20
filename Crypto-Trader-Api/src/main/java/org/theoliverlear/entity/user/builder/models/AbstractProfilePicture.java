package org.theoliverlear.entity.user.builder.models;

import org.theoliverlear.entity.user.ProductUser;
import org.theoliverlear.entity.user.ProfilePicture;
import org.theoliverlear.model.BuilderFactory;

public abstract class AbstractProfilePicture implements BuilderFactory<ProfilePicture> {
    public abstract AbstractProfilePicture fileName(String fileName);
    public abstract AbstractProfilePicture fileType(String fileType);
    public abstract AbstractProfilePicture fileData(byte[] fileData);
    public abstract AbstractProfilePicture user(ProductUser user);
}
