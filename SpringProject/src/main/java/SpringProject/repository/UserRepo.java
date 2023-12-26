package SpringProject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SpringProject.models.User;

@Repository
public interface UserRepo extends JpaRepository<User,Long>{

    Optional<User>findByEmail(String email);
    Boolean existsByEmail(String email);
    
}