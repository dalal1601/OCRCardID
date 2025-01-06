package com.example.ocrforidcard.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.ocrforidcard.dao.entities.User;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter { // gérer l'authentification via nom d'utilisateur et mot de passe.
    //interface qui gère l'authentification des utilisateurs
    private AuthenticationManager authenticationManager;
    //initialise l'authenticationManager
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //request : Représente la requête HTTP contenant les informations d'identification, response : Représente la réponse HTTP qui sera envoyée au client.
        User  user = null;
        try{
            user=new ObjectMapper().readValue(request.getInputStream(),User.class);
            //objectMapper:biblio pour deserialser(convertir data json en objt java) ->ici type user
        } catch(JsonParseException e){
            e.printStackTrace();
        } catch (JsonMappingException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword()
        ));
        //nom et psw recuperes de l'objet user
    }

    //générer un jeton JWT
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //chain : Permet de continuer le traitement des filtres dans la chaîne si nécessaire.
        //authResult : has infoss sur user authentifié (les rôles, les informations de connexion).
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)
                authResult.getPrincipal();
        //searching for the roles
        List<String> roles = new ArrayList<>();
        user.getAuthorities().forEach(var ->{
            roles.add(var.getAuthority());
        });
        // Création du JWT :
        String jwt = JWT.create()
                .withSubject(user.getUsername())
                .withArrayClaim("roles",roles.toArray(new String[roles.size()]))
                .withExpiresAt(new Date(System.currentTimeMillis()+SecurityParams.EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SecurityParams.SECRET));
        response.addHeader("authorization",jwt);
    }
}
