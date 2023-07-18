package ru.viz.clinic.request.data.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.viz.clinic.request.data.entity.Authority;
import ru.viz.clinic.request.data.entity.User;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long>, JpaSpecificationExecutor<User> {
    List<Authority> findByUsername(String username);
}
