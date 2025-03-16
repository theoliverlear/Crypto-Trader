package org.theoliverlear.entity.user.builder;

import org.theoliverlear.entity.user.ProfilePicture;
import org.theoliverlear.entity.user.User;
import org.theoliverlear.entity.user.builder.models.AbstractProfilePicture;

public class ProfilePictureBuilder extends AbstractProfilePicture {
    private String fileName;
    private String fileType;
    private byte[] fileData;
    private User user;
    public ProfilePictureBuilder() {
        super();
        this.fileName = "";
        this.fileData = new byte[0];
        this.user = null;
    }

    @Override
    public ProfilePictureBuilder fileName(String fileName) {
        this.fileName = fileName;
        this.fileType = ProfilePicture.getFileType(fileName);
        return this;
    }

    @Override
    public AbstractProfilePicture fileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    @Override
    public AbstractProfilePicture fileData(byte[] fileData) {
        this.fileData = fileData;
        return this;
    }

    @Override
    public AbstractProfilePicture user(User user) {
        this.user = user;
        return this;
    }

    @Override
    public ProfilePicture build() {
        return new ProfilePicture(this.fileName, this.fileData, this.user);
    }
}
