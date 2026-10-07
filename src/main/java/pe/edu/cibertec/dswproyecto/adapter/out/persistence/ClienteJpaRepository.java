package pe.edu.cibertec.dswproyecto.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteJpaRepository extends JpaRepository<ClienteJpaEntity, Integer> {

    Optional<ClienteJpaEntity> findByUsuarioId(Integer usuarioId);
}
