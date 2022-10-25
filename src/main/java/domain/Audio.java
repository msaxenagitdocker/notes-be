package domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "audio")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@IdClass(AudioId.class)
public class Audio {
    @Id
    @Column(name="refId")
    private String refId;

    @Id
    @Column(name="audioId")
    private String audioId;

    @Column(name="audio")
    private String audio;

    @Column(name="datetime")
    private Date dateTime;

    @Column(name="description")
    private String description;
}

