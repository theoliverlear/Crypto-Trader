package org.theoliverlear.entity.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.theoliverlear.entity.Identifiable;

@Getter
@Setter
@Entity
@Builder
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
    private User user;
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
    public ProfilePicture(String fileName, byte[] fileData, User user) {
        super();
        this.fileName = fileName;
        this.fileData = fileData;
        this.fetchFileType();
        this.user = user;
    }
    public void fetchFileType() {
        String fileExtension = this.fileName.substring(this.fileName.lastIndexOf(".") + 1);
        switch (fileExtension) {
            case "jpg", "jpeg" -> this.fileType = "image/jpeg";
            case "png" -> this.fileType = "image/png";
            case "gif" -> this.fileType = "image/gif";
            case "bmp" -> this.fileType = "image/bmp";
            case "webp" -> this.fileType = "image/webp";
            case "svg" -> this.fileType = "image/svg+xml";
            default -> throw new IllegalArgumentException("Invalid file type");
        }
    }
    public String setFileName(String fileName) {
        this.fetchFileType();
        return this.fileName = fileName;
    }
}
