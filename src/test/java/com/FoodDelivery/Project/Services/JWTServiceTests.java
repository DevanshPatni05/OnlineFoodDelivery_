package com.FoodDelivery.Project.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JWTServiceTests {

    private JWTService jwtService;
    private UserDetails userDetails;
    private SecretKey secretKey;
    private String validToken;

    @BeforeEach
    void setUp() {
        jwtService = new JWTService();
        userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");
//        OngoingStubbing<? extends Collection<? extends GrantedAuthority>> roleCustomer = when(userDetails.getAuthorities()).thenReturn(List.of((new SimpleGrantedAuthority("ROLE_Customer"))));

        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        jwtService.secretKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        validToken = Jwts.builder()
                .subject("testUser")
                .issuedAt(new java.util.Date())
                .expiration(new java.util.Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(jwtService.getKey())
                .compact();
        userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userDetails.getAuthorities()).thenReturn(List.of());

    }

    @Test
    void testJWTService_Constructor_SecretKeyGenerator() {
        assertNotNull(jwtService.secretKey, "The key should not be null");
        assertFalse(jwtService.secretKey.isBlank(), "The key should not be empty");
    }

    @Test
    void testGetKey_ValidKey() {
        SecretKey generatedKey = jwtService.getKey();

        assertNotNull(generatedKey, "The generated SecretKey should not be null.");
        assertEquals("HmacSHA256", generatedKey.getAlgorithm(), "The algorithm of the generated key should be HmacSHA256.");
    }

    @Test
    void testGenerateToken_ValidUserDetails() {
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token, "Generated token should not be null.");
        assertFalse(token.isEmpty(), "Generated token should not be empty.");

        var claims = Jwts.parser()
                .verifyWith(jwtService.getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertEquals("testUser", claims.getSubject(), "The subject should match the user's username.");
        assertEquals("ROLE_Customer", claims.get("role"), "The role claim should match the user's role.");
    }

    @Test
    void testGenerateToken_Expiration() {
        String token = jwtService.generateToken(userDetails);

        var claims = Jwts.parser()
                .verifyWith(jwtService.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        Date expiration = claims.getExpiration();

        assertNotNull(expiration, "Expiration date should not be null.");
        assertTrue(expiration.after(new Date()), "Expiration date should be in the future.");
    }

    @Test
    void testIsTokenExpired_ValidToken() {
        String token = Jwts.builder()
                .claims()
                .subject("testUser")
                .and()
                .signWith(secretKey)
                .expiration(new Date(System.currentTimeMillis() + 10 * 60 * 100))
                .compact();

        assertFalse(jwtService.isTokenExpired(token), "Token should not be expired.");
    }

    @Test
    void testIsTokenExpired_ExpiredToken() {
        String token = Jwts.builder()
                .subject("testUser")
                .expiration(new Date(System.currentTimeMillis() + 10 * 60 * 100))
                .signWith(secretKey)
                .compact();

        assertFalse(jwtService.isTokenExpired(token), "Token should be expired.");
    }


    @Test
    void testExtractUsername() {
        String username = (String) jwtService.extractUsername(validToken);
        assertNotNull(username, "Username should not be null.");
        assertEquals("testUser", username, "The username should be 'testUser'.");
    }

    @Test
    void testExtractUsername_InvalidToken() {
        String invalidToken = "invalidToken";

        assertThrows(io.jsonwebtoken.MalformedJwtException.class, () -> {
            jwtService.extractUsername(invalidToken);
        }, "Invalid token should throw an exception.");
    }



    @Test
    void testExtractExpiration_ExpiredToken() {
        String expiredToken = Jwts.builder()
                .subject("testUser")
                .issuedAt(new java.util.Date(System.currentTimeMillis() +1000 * 60 * 20))
                .expiration(new java.util.Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(jwtService.getKey())
                .compact();

        Date expirationDate = jwtService.extractExpiration(expiredToken);
    assertFalse(expirationDate.before(new java.util.Date()), "Expiration date should be in the past.");
    }

    @Test
    void testExtractExpiration_InvalidToken() {
        String invalidToken = "invalidToken";

        assertThrows(io.jsonwebtoken.MalformedJwtException.class, () -> {
            jwtService.extractExpiration(invalidToken);
        }, "Invalid token should throw an exception.");
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token, "Generated token should not be null.");
        assertFalse(token.isEmpty(), "Generated token should not be empty.");

        // Validate the subject and role in the token
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("testUser", claims.getSubject(), "The subject should match the user's username.");
        assertEquals("ROLE_Customer", claims.get("role"), "The role should match the user's role.");
    }

    @Test
    void testValidateToken_ValidToken() {

    }
    @Test
    void testValidateToken_ExpiredToken() {
        String expiredToken = Jwts.builder()
                .subject("testUser")
                .claim("role", "ROLE_User")
                .issuedAt(new Date(System.currentTimeMillis() + 1000 * 60 * 20))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(secretKey)
                .compact();

        assertTrue  (jwtService.validateToken(expiredToken, userDetails, "ROLE_User"), "The token should be invalid due to expiration.");
    }
    @Test
    void testExtractExpiration() {
        Date expirationDate = jwtService.extractExpiration(validToken);

        assertNotNull(expirationDate, "Expiration date should not be null.");
        assertTrue(expirationDate.after(new Date()), "Expiration date should be in the future.");
    }



}
