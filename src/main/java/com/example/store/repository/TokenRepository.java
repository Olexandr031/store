package com.example.store.repository;

import com.example.store.model.AuthenticationToken;
import com.example.store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<AuthenticationToken, Integer> {
     AuthenticationToken findTokenByUser(User user);
     AuthenticationToken findTokenByToken(String token);
}
