package antifraud.repository;

import antifraud.domain.model.FraudUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FraudUserRepository extends JpaRepository<FraudUser, Integer> {
    Optional<FraudUser> findByUsername(String username);

    boolean existsByUsername(String username);

    List<FraudUser> findAllByOrderByIdAsc();
}
