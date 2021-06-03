package edu.ib.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JwtFilter extends BasicAuthenticationFilter {

    public JwtFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Value("${jwt.login.token.key}")
    private String signingKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if(cookies!=null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("token")) {
                    token = cookies[i].getValue();
                    break;
                }
            }
        }
        if (token != null) {
            UsernamePasswordAuthenticationToken authResult = getAuthorizationToken(token);
            SecurityContextHolder.getContext().setAuthentication(authResult);
        } else if (SecurityContextHolder.getContext().getAuthentication() != null) {
            SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthorizationToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);

        String role = claimsJws.getBody().get("role").toString();
        String username = claimsJws.getBody().getSubject();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role);

        return new UsernamePasswordAuthenticationToken(username, null, Collections.singleton(simpleGrantedAuthority));
    }
}