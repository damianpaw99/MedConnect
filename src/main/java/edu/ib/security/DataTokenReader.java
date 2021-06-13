package edu.ib.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


public class DataTokenReader{

    private String signingKey;

    public DataTokenReader(String signingKey){
        this.signingKey=signingKey;
    }
    public String readRole(String token){
        if(token==null) return null;
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);

        return claimsJws.getBody().get("role").toString().split("_")[1];
    }
    public Long readPesel(String token){
        if (token==null) return null;
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);

        return Long.parseLong(claimsJws.getBody().get("sub").toString());
    }
}
