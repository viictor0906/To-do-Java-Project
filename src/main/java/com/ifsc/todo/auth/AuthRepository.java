package com.ifsc.todo.auth;

import java.lang.foreign.Linker.Option;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.catalina.User;

import com.ifsc.todo.repository.UserRepository;

public class AuthRepository {
    private final Map<String, SessionInfo> tokenStore = new ConcurrentHashMap<>();

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthRepository(JwtUtil jwtUtil, UserRepository userRepository){
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }
    
    public Optional<String> login(String username, String password){
        return userRepository.findByUsername(username)
            .filter(u -> u.getPassword().equals(password))
            .map(u->{
                String jwt = jwtUtil.generateToken(u.getUsername(), u.getRoles());
                tokenStore.put(jwt, new SessionInfo(u.getUsername(), Instant.now()));
                return jwt;
            });
    }

    public Optional<String> validate(String token){
        if(token == null || token.isBlank()) return Optional.empty();
        try{
            SessionInfo sessionInfo = tokenStore.get(token);
            if(sessionInfo == null) return Optional.empty();
            return Optional.ofNullable(jwtUtil.getSubject(token));
        }catch(Exception e){
            return Optional.empty();
        }
    }

    public void logout(String token){
        if(token != null){
            tokenStore.remove(token);
        }
    }

    public boolean register(String username, String password){
        if(username == null || password == null) return false;

        if(userRepository.existsByUserName(username)) return false;

        User u = new User();
        u.setUsername(username);
        u.setPassword(password);
        u.setRole("USER");

        userRepository.save(u);
        return true;
    }

    public String getRoleByUsername(String username){
        return userRepository.findByUsername(username)
            .map(User::getRole)
            .orElse(null);
    }

    private record SessionInfo(String username, Instant authentificatedAt) {}
}
