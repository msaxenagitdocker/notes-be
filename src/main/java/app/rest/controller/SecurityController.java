package app.rest.controller;

import app.rest.controller.security.AuthRequest;
import app.rest.controller.security.AuthResponse;
import app.rest.controller.security.JwtUtil;
import app.rest.controller.security.MyUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
@Slf4j
@RestController
@RequestMapping("/securityController")
public class SecurityController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     *
     * @return
     */
    @PostMapping(value = "/authentication")
    public ResponseEntity<?> authentication(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUserName());

        final String jwtToken = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }
}
