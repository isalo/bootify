package com.cykor.hub.base.rest;

import com.cykor.hub.base.domain.User;
import com.cykor.hub.base.model.AuthenticationRequest;
import com.cykor.hub.base.model.AuthenticationResponse;
import com.cykor.hub.base.model.AuthenticationSocialRequest;
import com.cykor.hub.base.model.JwtUserDetails;
import com.cykor.hub.base.repos.RoleRepository;
import com.cykor.hub.base.repos.UserRepository;
import com.cykor.hub.base.service.JwtSocialUserDetailsService;
import com.cykor.hub.base.service.JwtTokenService;
import com.cykor.hub.base.service.JwtUserDetailsService;
import com.cykor.hub.base.util.UserRoles;
import jakarta.validation.Valid;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;


@RestController
@Slf4j
public class AuthenticationResource {

    private final AuthenticationProvider authenticationProvider;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtSocialUserDetailsService jwtSocialUserDetailsService;
    private final JwtTokenService jwtTokenService;
    private final Environment environment;
    private final UserRepository userRepository;
    private final String baseHost;
    private final RoleRepository roleRepository;
    private final RestClient googleClient = RestClient.builder()
            .baseUrl("https://oauth2.googleapis.com/")
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build();
    private final RestClient facebookClient = RestClient.builder()
            .baseUrl("https://graph.facebook.com/v23.0/")
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build();

    public AuthenticationResource(final AuthenticationProvider authenticationProvider,
            final JwtUserDetailsService jwtUserDetailsService,
            final JwtSocialUserDetailsService jwtSocialUserDetailsService,
            final JwtTokenService jwtTokenService, final Environment environment,
            final UserRepository userRepository, @Value("${app.baseHost}") final String baseHost,
            final RoleRepository roleRepository) {
        this.authenticationProvider = authenticationProvider;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtSocialUserDetailsService = jwtSocialUserDetailsService;
        this.jwtTokenService = jwtTokenService;
        this.environment = environment;
        this.userRepository = userRepository;
        this.baseHost = baseHost;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(
            @RequestBody @Valid final AuthenticationRequest authenticationRequest) {
        try {
            authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (final BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        final JwtUserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken(jwtTokenService.generateToken(userDetails, "direct", null));
        return authenticationResponse;
    }

    private AuthenticationResponse synchronizeUserAndGetToken(final String loginType,
            final String subject, final Instant expiresAt) {
        User user = userRepository.findByExternalLogin(subject);
        if (user == null) {
            log.info("adding new user after successful authentication: {}", subject);
            user = new User();
            user.setExternalLogin(subject);
            user.setLoginType(loginType);
            // assign default role
            user.setRoles(Collections.singleton(roleRepository.findByName(UserRoles.ROLE_ADMIN)));
        } else {
            log.info("updating existing user after successful authentication: {}", subject);
        }
        userRepository.save(user);

        final JwtUserDetails userDetails = jwtSocialUserDetailsService.loadUserByUsername(subject);
        final Duration validity = Duration.between(Instant.now(), expiresAt);
        final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAccessToken(jwtTokenService.generateToken(userDetails, loginType, validity));
        return authenticationResponse;
    }

    @PostMapping("/authenticateGoogle")
    public AuthenticationResponse authenticateGoogle(
            @RequestBody @Valid final AuthenticationSocialRequest authenticationSocialRequest) {
        log.info("exchanging google code");
        final String providerId = "google";
        final String clientId = environment.getProperty("app." + providerId + ".client-id");
        final String clientSecret = environment.getProperty("app." + providerId + ".client-secret");
        final RestClient.ResponseSpec accessTokenSpec = googleClient.post()
                .uri("token")
                .body(Map.of("client_id", clientId, "client_secret", clientSecret,
                "redirect_uri", baseHost + "/completeLogin?provider=google", "grant_type", "authorization_code",
                "code", authenticationSocialRequest.getCode()))
                .retrieve();
        final Map<String, Object> accessTokenResponse = accessTokenSpec.body(new ParameterizedTypeReference<>() {
        });

        log.info("validating google access token");
        final RestClient.ResponseSpec tokeninfoSpec = googleClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("tokeninfo")
                    .queryParam("id_token", accessTokenResponse.get("id_token"))
                    .build())
                .retrieve();
        final Map<String, Object> tokeninfoResponse = tokeninfoSpec.body(new ParameterizedTypeReference<>() {
        });
        if (!clientId.equals(tokeninfoResponse.get("aud"))) {
            log.warn("google app id not matching");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        final String subject = tokeninfoResponse.get("sub").toString();
        final Instant expiresAt = Instant.ofEpochSecond(Long.parseLong(tokeninfoResponse.get("exp").toString()));
        if (expiresAt.isBefore(Instant.now())) {
            log.warn("google token has expired");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return synchronizeUserAndGetToken(providerId, subject, expiresAt);
    }

    @PostMapping("/authenticateFacebook")
    public AuthenticationResponse authenticateFacebook(
            @RequestBody @Valid final AuthenticationSocialRequest authenticationSocialRequest) {
        log.info("exchanging facebook code");
        final String providerId = "facebook";
        final String clientId = environment.getProperty("app." + providerId + ".client-id");
        final String clientSecret = environment.getProperty("app." + providerId + ".client-secret");
        final RestClient.ResponseSpec accessTokenSpec = facebookClient.post()
                .uri("oauth/access_token")
                .body(Map.of("client_id", clientId, "client_secret", clientSecret,
                "redirect_uri", baseHost + "/completeLogin?provider=facebook",
                "code", authenticationSocialRequest.getCode()))
                .retrieve();
        final Map<String, Object> accessTokenResponse = accessTokenSpec.body(new ParameterizedTypeReference<>() {
        });

        log.info("validating facebook access token");
        final String accessToken = clientId + "|" + clientSecret;
        final RestClient.ResponseSpec debugTokenSpec = facebookClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("debug_token")
                    .queryParam("access_token", accessToken)
                    .queryParam("input_token", accessTokenResponse.get("access_token"))
                    .build())
                .retrieve();
        final Map<String, Object> debugTokenResponse = debugTokenSpec.body(new ParameterizedTypeReference<>() {
        });
        @SuppressWarnings("unchecked") final Map<String, Object> data = ((Map<String, Object>)debugTokenResponse.get("data"));
        if (!Boolean.TRUE.equals(data.get("is_valid"))) {
            log.warn("facebook token invalid");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (!clientId.equals(data.get("app_id"))) {
            log.warn("facebook app id not matching");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        final String subject = data.get("user_id").toString();
        final Instant expiresAt = Instant.ofEpochSecond(((Long)data.get("expires_at")));
        return synchronizeUserAndGetToken(providerId, subject, expiresAt);
    }

}
