package com.shop.user_service.security;

import com.shop.user_service.Entity.User;
import com.shop.user_service.repository.UserRepository;
import com.shop.user_service.service.JWTService;
import com.shop.user_service.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization"); //Extract the Authorization Header:

        //this token is actually concatinated with Bearer space("Bearer ")
        //Check for a Bearer Token:
        if(requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        String token = requestTokenHeader.split("Bearer ")[1]; // Extract JWT token from the header
        Long userId = jwtService.generateUserIdFromToken(token); //generate the token

        //Check if userId is Present and the Security Context is Empty
        if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            //Retrieve the User Entity
            User user = userRepository.getUserById(userId);

            //Create an Authentication Token
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user, null, null);  //put the user in spring security context holder and in other field we used 'null' for only testing purpose

            //Set Authentication Details
            authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            //Set Authentication in Security Context
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        //Continue the Filter Chain
        filterChain.doFilter(request,response);
    }
}
