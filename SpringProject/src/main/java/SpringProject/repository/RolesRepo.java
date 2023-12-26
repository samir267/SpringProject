package SpringProject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import SpringProject.models.Roles;

public interface RolesRepo extends JpaRepository<Roles,Long>{

    Optional<Roles>findByName(String name);
    
    
    
}