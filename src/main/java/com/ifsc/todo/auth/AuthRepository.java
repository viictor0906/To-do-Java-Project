package com.ifsc.todo.auth;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.ifsc.todo.entities.user.UserEntity;
import com.ifsc.todo.repositories.UserRepository;

public class AuthRepository 
{
    private final Map<String, SessionInfo> tokenStore = new ConcurrentHashMap<>();
    private final LoginUtil loginUtil;
    private final UserRepository userRepository;

    public AuthRepository(LoginUtil loginUtil, UserRepository userRepository)
    {
        this.loginUtil = loginUtil;
        this.userRepository = userRepository;
    }
    
    public Optional<String> userLogin(String username, String password)
    {
        return userRepository
            .findByUsername(username)
            .filter(userFilter -> userFilter.getPassword()
            .equals(password))
            .map(userFilter ->
            {
                String loginJwt = loginUtil.generateToken(userFilter.getUsername(), userFilter.getUserRole());
                tokenStore.put(loginJwt, new SessionInfo(userFilter.getUsername(), Instant.now()));
                return loginJwt;
            });
    }

    public Optional<String> userValidate(String loginToken)
    {
        if(loginToken == null || loginToken.isBlank()) return Optional.empty();
        try
        {
            SessionInfo sessionInfo = tokenStore.get(loginToken);
            if(sessionInfo == null) return Optional.empty();
            return Optional.ofNullable(loginUtil.getSubject(loginToken));
        }
        catch(Exception error)
        {
            return Optional.empty();
        }
    }

    public void userLogout(String loginToken)
    {
        if(loginToken != null)
        {
            tokenStore.remove(loginToken);
        }
    }

    public boolean userRegister(String username, String password)
    {
        if(username == null || password == null) return false;

        if(userRepository.existsByUserName(username)) return false;

        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setUserRole("USER");

        userRepository.save(newUser);
        return true;
    }

    public String getRoleByUsername(String username)
    {
        return userRepository.findByUsername(username)
            .map(UserEntity::getUserRole)
            .orElse(null);
    }

    private record SessionInfo(String username, Instant authentificatedAt){}
}
