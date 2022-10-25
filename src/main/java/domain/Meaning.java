package domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "meaning")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Meaning {
    @Id
    @Column(name="id")
    private String id;

    @Column(name="word")
    private String word;

    @Column(name="meaning")
    private String meaning;

    @Column(name="datetime")
    private Date date;

    @Column(name="priority")
    private int priority;
}
