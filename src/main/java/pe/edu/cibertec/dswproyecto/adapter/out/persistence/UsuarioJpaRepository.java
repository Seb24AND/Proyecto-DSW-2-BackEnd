package pe.edu.cibertec.dswproyecto.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioJpaEntity, Integer> {

    Optional<UsuarioJpaEntity> findByUsername(String username);

    boolean existsByUsername(String username);
}