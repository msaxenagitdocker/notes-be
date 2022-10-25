package domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "image")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Builder @ToString
@IdClass(ImageId.class)
public class Image {
    @Id
    @Column(name="wordId")
    private String wordId;

    @Id
    @Column(name="imageId")
    private String imageId;

    @Column(name="image")
    private String image;

    @Column(name="datetime")
    private Date dateTime;
}
