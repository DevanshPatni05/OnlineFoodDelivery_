package com.FoodDelivery.Project.Configuration;

import com.FoodDelivery.Project.Services.AdminServices;
import com.FoodDelivery.Project.Services.CustomerServices;
import com.FoodDelivery.Project.Services.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        String role = null;

        // Extract JWT from Authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = (String) jwtService.extractUsername(token);
            role = (String) jwtService.extractRole(token); // Extract role from the JWT
            System.out.println("Extracted Username: " + username);
            System.out.println("Extracted Role: " + role);
        }

        // Proceed only if username is not null and no existing authentication is found
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = getUserDetails(username, role, response);
            if (userDetails == null) {
                // UserDetails retrieval failed, exit the filter chain
                return;
            }

            // Validate the JWT token and its role
            if (jwtService.validateToken(token, userDetails,role) && isRoleValid(role, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token or Role");
                return;
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Helper method to retrieve UserDetails from CustomerServices or AdminServices based on role.
     */
    private UserDetails getUserDetails(String username, String role, HttpServletResponse response) throws IOException {
        UserDetails userDetails = null;

        try {
            if ("ROLE_Customer".equals(role)) {
                userDetails = context.getBean(CustomerServices.class).loadUserByUsername(username);
            } else if ("ROLE_Admin".equals(role)) {
                userDetails = context.getBean(AdminServices.class).loadUserByUsername(username);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unauthorized Role");
                return null;
            }
        } catch (Exception e) {
            System.out.println("User not found for role " + role + ": " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
        }

        return userDetails;
    }

    /**
     * Validates if the extracted role from JWT matches the roles in UserDetails.
     */
    private boolean isRoleValid(String role, UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
    }
}
