package app.rest.controller.security;

import lombok.*;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class AuthResponse {
    private final String jwtToken;
}
