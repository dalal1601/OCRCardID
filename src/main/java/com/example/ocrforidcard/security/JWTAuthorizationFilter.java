package com.example.ocrforidcard.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
//vérifier l'authenticité du JWT contenu dans l'en-tête Authorization.
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader("authorization");
        if(jwt == null || !jwt.startsWith(SecurityParams.PREFIX)){
            filterChain.doFilter(request,response); //permet de transmettre la requête aux autres filtres
            return;
        }
       jwt = jwt.substring(SecurityParams.PREFIX.length());
        //Vérification et décryptage du JWT
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SecurityParams.SECRET)).build();
        DecodedJWT decodedJWT = verifier.verify(jwt);

        String username = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaims().get("roles").asList(String.class);
//Convertir les rôles en autorités Spring pour gerer les permissions
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for(String role:roles)
            authorities.add(new SimpleGrantedAuthority(role));

        UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(
                username,null,authorities);
        SecurityContextHolder.getContext().setAuthentication(user);
        filterChain.doFilter(request,response);

    }
}
