package com.ufund.api.ufundapi.DAO;

import java.io.IOException;

import com.ufund.api.ufundapi.Model.Authenticator;

public interface AuthDAO {
    Authenticator getAuth(String session_key) throws IOException;

    void addAuth(Authenticator userAuth) throws IOException;

    void removeAuth(String key) throws IOException;
}
