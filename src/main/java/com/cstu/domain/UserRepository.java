package com.cstu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<CstuUser, Long> {
    CstuUser findByLogin(String login);
}
