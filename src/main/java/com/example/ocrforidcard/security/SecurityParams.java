package com.example.ocrforidcard.security;

public class SecurityParams {
    public static final long EXPIRATION_TIME = 3*24*60*60*1000;
    //Clé secrète utilisée pour signer et vérifier les tokens JWT.
    public static final String SECRET = System.getenv("JWT_SECRET_KEY"); // Load from environment variable
    //Préfixe utilisé dans l'en-tête HTTP Authorization pour transporter le token.
    public static final String PREFIX="Bearer ";

    // Private constructor to prevent instantiation
    private SecurityParams() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }
}
