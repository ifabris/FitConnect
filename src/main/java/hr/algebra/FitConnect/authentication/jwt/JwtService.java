package hr.algebra.FitConnect.authentication.jwt;

import hr.algebra.FitConnect.feature.user.RoleType;
import hr.algebra.FitConnect.feature.user.User;
import hr.algebra.FitConnect.feature.user.UserRepo;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    @Value("604800")
    private Long accessTokenValiditySeconds;

    @Value("${security.authentication.jwt.base64-secret}")
    private String secretKey;

    private final UserRepo userRepo;

    private Set<String> invalidatedTokens = new HashSet<>();

    public boolean authenticate(String token) {
        // If JWT is invalid, user can not be authenticated
        if (isJwtInvalid(token)) {
            return false;
        }
        // If JWT is valid, store authentication in Spring security context
        User applicationUser = getCustomerFromJwt(token);
        saveAuthentication(applicationUser);

        return true;
    }

    public String createJwt(User jwtCustomer) {
        // Get the role as a string from the Role entity
        String role = String.valueOf(jwtCustomer.getRole().getRoleName());  // Assuming Role has a getRoleName method

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .setSubject(jwtCustomer.getUsername())
                .claim("id", jwtCustomer.getUserId())
                .claim("role", role)  // Use the string representation of the role
                .setExpiration(
                        new Date(Instant.now().plusSeconds(accessTokenValiditySeconds).toEpochMilli()))
                .setIssuedAt(new Date())
                .compact();
    }


    private boolean isJwtInvalid(String jwtToken) {
        try {
            if(invalidatedTokens.contains(jwtToken)){
                return true;
            }
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return false;
        } catch (MalformedJwtException e) {
            log.debug("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.debug("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.debug("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.debug("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e.getMessage());
        }
        return true;
    }

    public User getCustomerFromJwt(String jwtToken) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken).getBody();

        User customer =
                userRepo
                        .findById(Integer.valueOf(claims.get("id").toString()))
                        .orElseThrow(() -> new RuntimeException("User not found from JWT"));

        return customer;
    }

    public Integer getCustomerIdFromJwt(String jwtToken) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken).getBody();
        return Integer.valueOf(claims.get("id").toString());
    }

    public List<String> getCustomerRolesFromJwt(String jwtToken) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken).getBody();
        return Arrays.asList(claims.get("roles").toString().split(","));
    }

    public String getUsernameFromJwt(String jwtToken) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken).getBody();
        return claims.getSubject();
    }

    private void saveAuthentication(User applicationUser) {
        // Create a SimpleGrantedAuthority from the user's role
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(applicationUser.getRole().getRoleName().name());

        // Create the authentication token
        Authentication authentication = new UsernamePasswordAuthenticationToken(applicationUser, null, List.of(authority));

        // Set the authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }




    public void invalidateToken(String token){
        invalidatedTokens.add(token);
    }
}
