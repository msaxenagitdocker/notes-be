package domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ImageId implements Serializable {
    private String wordId;
    private String imageId;
}
