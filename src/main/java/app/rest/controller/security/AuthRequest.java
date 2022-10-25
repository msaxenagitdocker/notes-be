package app.rest.controller.security;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
@ToString
@EqualsAndHashCode
public class AuthRequest {
    private String userName;
    private String password;
}
