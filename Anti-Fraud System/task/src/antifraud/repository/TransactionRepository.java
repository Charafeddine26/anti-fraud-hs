package antifraud.repository;

import antifraud.domain.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    int countDistinctIpByNumberAndIpNotAndDateBetween(String number, String ip, LocalDateTime start, LocalDateTime end);
    int countDistinctRegionByNumberAndRegionNotAndDateBetween(String number, String region, LocalDateTime start, LocalDateTime end);
    List<Transaction> findByNumberAndDateBetween(String number, LocalDateTime startDate, LocalDateTime endDate);

}
