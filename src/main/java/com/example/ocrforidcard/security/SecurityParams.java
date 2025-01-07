package com.example.ocrforidcard.security;

public class SecurityParams {
    public static final long EXPIRATION_TIME = 3*24*60*60*1000;
    //Clé secrète utilisée pour signer et vérifier les tokens JWT.
    public static final String SECRET = "lamyDalila!2024&";
    //Préfixe utilisé dans l'en-tête HTTP Authorization pour transporter le token.
    public static final String PREFIX="Bearer ";
}
