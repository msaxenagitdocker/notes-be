package domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "genericfile")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Builder @ToString
@IdClass(GenericFileId.class)
public class GenericFile {
    @Id
    @Column(name="id")
    private String id;

    @Id
    @Column(name="linkId")
    private String linkId;

    @Column(name="fileData")
    private String fileData;

    @Column(name="fileName")
    private String fileName;

    @Column(name="type")
    private String type;

    @Column(name="datetime")
    private Date dateTime;
}

