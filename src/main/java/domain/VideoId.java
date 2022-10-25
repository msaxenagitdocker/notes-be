package domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class VideoId implements Serializable {
    private String refId;
    private String videoId;
}
