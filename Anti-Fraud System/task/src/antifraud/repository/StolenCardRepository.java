package antifraud.repository;

import antifraud.domain.dto.StolenCardDTO;
import antifraud.domain.model.StolenCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StolenCardRepository extends JpaRepository<StolenCard, Long> {
    boolean existsByNumber(String number);

    StolenCard findByNumber(String number);

    List<StolenCard> findAllByOrderByIdAsc();
}
