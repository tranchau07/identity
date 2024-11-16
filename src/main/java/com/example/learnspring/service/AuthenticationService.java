package com.example.learnspring.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.learnspring.Entity.InvalidateToken;
import com.example.learnspring.Entity.User;
import com.example.learnspring.dto.request.AuthenticationRequest;
import com.example.learnspring.dto.request.IntrospectRequest;
import com.example.learnspring.dto.request.LogoutRequest;
import com.example.learnspring.dto.request.RefreshRequest;
import com.example.learnspring.dto.response.AuthenticationResponse;
import com.example.learnspring.dto.response.IntrospectResponse;
import com.example.learnspring.exception.AppException;
import com.example.learnspring.exception.ErrorCode;
import com.example.learnspring.reponsitory.InvalidateTokenRepository;
import com.example.learnspring.reponsitory.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    UserRepository userRepository;
    InvalidateTokenRepository invalidateTokenRepository;

    @NonFinal
    protected static final String SIGN_KEY = "XQdRWYpCWYtrOz8nMFfmjOLFfpO/Tn/Z/jpbrZ8e7uRrpmH/PhvLBavRgQsebAkr";

    //    @NonFinal
    //    @Value("${jwt.valid-duration}")
    //    protected long VALID_DURATION;
    //
    //    @NonFinal
    //    @Value("${jwt.refreshable-duration}")
    //    protected long REFRESHABLE_DURATION;

    public AuthenticationResponse authenticated(AuthenticationRequest request) {
        var user = userRepository
                .findByUserName(request.getUserName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean auth = passwordEncoder.matches(request.getPassword(), user.getPassword());
        return AuthenticationResponse.builder()
                .authenticated(auth)
                .token(generateToken(user))
                .build();
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    private String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName())
                .issuer("http://localhost:8080")
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(50, ChronoUnit.SECONDS)))
                .claim("scope", buildScope(user))
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGN_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot sign JWT", e);
            throw new RuntimeException(e);
        }
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {

        try {
            var jwtToken = verifyToken(request.getToken(), false);

            String jit = jwtToken.getJWTClaimsSet().getJWTID();
            Date expirationTime = jwtToken.getJWTClaimsSet().getExpirationTime();

            InvalidateToken invalidateToken =
                    InvalidateToken.builder().id(jit).expiryDate(expirationTime).build();
            invalidateTokenRepository.save(invalidateToken);
        } catch (AppException e) {
            log.error("Cannot logout JWT", e);
        }
    }

    public AuthenticationResponse refresh(RefreshRequest request) throws ParseException, JOSEException {
        var jwtToken = verifyToken(request.getToken(), true);

        String jit = jwtToken.getJWTClaimsSet().getJWTID();
        Date expirationTime = jwtToken.getJWTClaimsSet().getExpirationTime();

        InvalidateToken invalidateToken =
                InvalidateToken.builder().id(jit).expiryDate(expirationTime).build();
        invalidateTokenRepository.save(invalidateToken);

        var username = jwtToken.getJWTClaimsSet().getSubject();

        User user =
                userRepository.findByUserName(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));

        return AuthenticationResponse.builder()
                .authenticated(true)
                .token(generateToken(user))
                .build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGN_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = (isRefresh)
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(120, ChronoUnit.SECONDS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        log.info("Token expiration time: {}", expirationTime);
        log.info("Current time: {}", new Date());

        if (!(verified && expirationTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (invalidateTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!user.getRoles().isEmpty()) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(user.getRoles())) {
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
        }
        return stringJoiner.toString();
    }
}
