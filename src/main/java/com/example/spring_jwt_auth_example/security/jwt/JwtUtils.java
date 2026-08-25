package com.example.spring_jwt_auth_example.security.jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtUtils {

    @Value("${p.jwt.secret}")
    private String jwtSecret;
}
