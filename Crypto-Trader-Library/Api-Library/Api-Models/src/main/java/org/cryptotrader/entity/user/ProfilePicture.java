package org.cryptotrader.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.cryptotrader.entity.Identifiable;
import org.cryptotrader.entity.user.builder.ProfilePictureBuilder;

@Getter
@Setter
@Entity
@Table(name = "profile_pictures")
public class ProfilePicture extends Identifiable {
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "file_type")
    private String fileType;
    @Lob
    @Column(name = "file_data")
    private byte[] fileData;
    @OneToOne
    @JoinColumn(name = "user_id")
    private ProductUser user;
    public ProfilePicture() {
        super();
        this.fileName = "";
        this.fileData = new byte[0];
        this.user = null;
    }
    public ProfilePicture(String fileName, byte[] fileData) {
        super();
        this.fileName = fileName;
        this.fileData = fileData;
        this.fetchFileType();
        this.user = null;
    }
    public ProfilePicture(String fileName, byte[] fileData, ProductUser user) {
        super();
        this.fileName = fileName;
        this.fileData = fileData;
        this.fetchFileType();
        this.user = user;
    }
    public void fetchFileType() {
        this.fileType = getFileType(this.fileName);
    }
    public static String getFileType(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return switch (fileExtension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "bmp" -> "image/bmp";
            case "webp" -> "image/webp";
            case "svg" -> "image/svg+xml";
            default -> throw new IllegalArgumentException("Invalid file type");
        };
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
        this.fetchFileType();
    }
    public static ProfilePictureBuilder builder() {
        return new ProfilePictureBuilder();
    }
}
