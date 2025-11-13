package com.ufund.api.ufundapi.Model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Authenticator {
    @JsonProperty("session_key") 
    String session_key;
    
    @JsonProperty("username") 
    String username;
    
    @JsonProperty("expiry") 
    LocalDateTime expiry;

    public Authenticator(@JsonProperty("session_key") String session_key, @JsonProperty("username") String username, @JsonProperty("expiry") LocalDateTime expiry) {
        this.session_key = session_key;
        this.username = username;
        this.expiry = expiry;
    }

    /**
     * Generates a new Authentication for a given username
     * 
     * @param username the username of the user to authenticate
     * @return The authentication object
     */
    public static Authenticator generate(String username) {
        return new Authenticator(UUID.randomUUID().toString(), username, LocalDateTime.now().plusDays(7));
    }

    public String getsession_key() {
        return session_key;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

}
