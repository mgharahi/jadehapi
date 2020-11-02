package ir.jadeh.api.services;

import ir.jadeh.api.securityConfiguration.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserAuthentication {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public String login(String username, String roleName) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null);
        //SecurityContextHolder.getContext().setAuthentication(authentication);
        authenticationManager.authenticate(
                authentication
        );

        String token = jwtTokenProvider
                .createToken(username, Arrays.asList(roleName));

        return token;

    }

    public String login(String user) {
        return login(user, "USER_ROLE");
    }
}
