package com.FoodDelivery.Project.Services;

import com.FoodDelivery.Project.Entity.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.UserCredentialsDataSourceAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

     String secretKey="";

    public JWTService() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGenerator.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate HMAC key", e);
        }
    }


    public String generateToken(UserDetails userDetails) {

        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_Customer"); // Default role if not found

        System.out.println("in generate token");

        Map<String,Object> claims=new HashMap<>();
        claims.put("role",role);
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+(30 * 60 * 1000)))
                .and()
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);

    }


    public Object extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }


    public Object extractRole(String token)
    {
        return extractClaim(token,claims->claims.get("role",String.class));
    }


    private <T> T extractClaim(String token, Function<Claims,T>  claimsResolver) {
        Claims claims= extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token)
    {
        Claims claims= Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        System.out.println(claims);
        return claims;
    }


    public boolean validateToken(String token, UserDetails userDetails,String requiredRole) {


        System.out.println("validation of token");

        String userName = (String) extractUsername(token);

        String role=(String)extractRole(token);
        System.out.println(userName);

        boolean isExpired =isTokenExpired(token);

        boolean isUsernameValid = userName.equals(userDetails.getUsername());
        boolean isRoleValid = role.equals(requiredRole);

        return isUsernameValid && isRoleValid && !isExpired;

    }

    private boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }




}
