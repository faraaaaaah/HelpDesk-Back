package com.youtube.crud.repo;

import com.youtube.crud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<String> findPasswordByEmail(String email);

}
