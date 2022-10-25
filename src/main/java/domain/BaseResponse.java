package domain;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder @ToString @EqualsAndHashCode
public class BaseResponse {
    private String status;
    private int code;
}
