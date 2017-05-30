package com.cstu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<CstuUser, Long> {
}
