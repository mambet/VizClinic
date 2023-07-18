package ru.viz.clinic.request.data.service;

import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.viz.clinic.request.data.Role;
import ru.viz.clinic.request.data.entity.User;

@Service
public class UserService {

    private final UserRepository repository;
     private final JdbcUserService userDetailsManager;

    private final AuthenticationManager authenticationManager;


    public UserService(UserRepository repository, JdbcUserService userDetailsManager, AuthenticationManager authenticationManager) {

        this.repository = repository;
        this.userDetailsManager = userDetailsManager;
        this.authenticationManager = authenticationManager;
    }

    public void createUser(User user, Role role)
    {
        userDetailsManager.createUser(org.springframework.security.core.userdetails.User.builder()
                .disabled(false)
                .password(user.getPassword())
                .username(user.getUsername())
                .authorities(new SimpleGrantedAuthority("ROLE_" + role))
                .build());
        repository.save(user);
    }
    public Optional<User> get(Long id) {

        return repository.findById(id);
    }

    public User getByName(String name) {
        return repository.findByUsername(name).get(0);
    }

    public User update(User user) {
        return repository.save(user);
    }
    public void updateUserDetails(UserDetails oldUser, UserDetails userDetails, String oldPass) {
        this.authenticationManager
                .authenticate(UsernamePasswordAuthenticationToken.unauthenticated(oldUser.getUsername(), oldPass));
        userDetailsManager.updateUser(userDetails);
        Authentication authentication = userDetailsManager.createNewAuthentication(SecurityContextHolder.getContext().getAuthentication(), Strings.EMPTY);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<User> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<User> list(Pageable pageable, Specification<User> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
