package domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "video")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@IdClass(VideoId.class)
public class Video {
    @Id
    @Column(name="refId")
    private String refId;

    @Id
    @Column(name="videoId")
    private String videoId;

    @Column(name="video")
    private String video;

    @Column(name="datetime")
    private Date dateTime;

    @Column(name="description")
    private String description;
}
