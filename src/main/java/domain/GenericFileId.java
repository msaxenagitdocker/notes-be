package domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GenericFileId implements Serializable {
    private String id;
    private String linkId;
}

