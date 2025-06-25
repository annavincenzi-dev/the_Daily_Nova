package dev.annavincenzi.the_daily_nova.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.annavincenzi.the_daily_nova.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
