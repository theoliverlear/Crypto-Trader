package org.theoliverlear.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "profile_pictures")
public class ProfilePicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
        this.fileName = "";
        this.fileData = new byte[0];
        this.user = null;
    }
    public ProfilePicture(String fileName, byte[] fileData) {
        this.fileName = fileName;
        this.fileData = fileData;
        this.fetchFileType();
        this.user = null;
    }
    public ProfilePicture(String fileName, byte[] fileData, User user) {
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
