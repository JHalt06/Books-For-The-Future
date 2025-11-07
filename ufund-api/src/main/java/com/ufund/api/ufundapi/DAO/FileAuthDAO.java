package com.ufund.api.ufundapi.DAO;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.Model.Authenticator;

@Repository
public class FileAuthDAO implements AuthDAO {
    /**
     * Map of user Authenticators in pairs --> (str: session_key : Authenticator auth)
     */
    private final Map<String, Authenticator> auths;
    private final ObjectMapper objectMapper;
    private final String filename;

    @Autowired
    public FileAuthDAO(ObjectMapper objectMapper, @Value("${auths.filepath}") String filename) throws IOException {
        this.auths = new HashMap<>();
        this.objectMapper = objectMapper;
        this.filename = filename;
        loadAuths();
    }

    @Override
    public Authenticator getAuth(String session_key) throws IOException {
        synchronized (auths) {
            return auths.get(session_key);
        }
    }

    @Override
    public void addAuth(Authenticator userAuth) throws IOException {
        synchronized (auths) {
            auths.put(userAuth.getsession_key(), userAuth);
            saveAuths();
        }
    }

    @Override
    public void removeAuth(String session_key) throws IOException {
        synchronized (auths) {
            auths.remove(session_key);
            saveAuths();
        }
    }
    
    private void loadAuths() throws IOException {
        this.auths.clear();
        Authenticator[] arr = objectMapper.readValue(new File(filename), Authenticator[].class);
        for (Authenticator auth : arr) {
            if (auth.getExpiry().isAfter(LocalDateTime.now())) {
                auths.put(auth.getsession_key(), auth);
            }
        }
    }

    private void saveAuths() throws IOException {
        objectMapper.writeValue(new File(filename), auths.values());
    }
}
