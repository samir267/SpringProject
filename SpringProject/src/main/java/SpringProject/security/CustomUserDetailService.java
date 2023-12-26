package SpringProject.security;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import SpringProject.models.Roles;
import SpringProject.models.User;
import SpringProject.repository.UserRepo;
@Service

public class CustomUserDetailService implements UserDetailsService{

    private UserRepo userRepo;
    
     @Autowired
    public CustomUserDetailService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user=userRepo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("user not found"));
        return (UserDetails) new User(user.getId(),user.getFull_name(),user.getEmail(),user.getPassword(),user.getPhone(),user.getAddress(),user.getRole(),user.getRoles());  
         
     }

private Collection<GrantedAuthority> mapRolesToAuthorities(List<Roles> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }    
}