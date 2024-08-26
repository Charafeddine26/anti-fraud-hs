package antifraud.service;

import antifraud.domain.dto.ResultDTO;
import antifraud.domain.dto.TransactionDTO;
import antifraud.domain.enums.TransactionStatus;
import antifraud.domain.model.Transaction;
import antifraud.repository.IpAdressRepository;
import antifraud.repository.RegionRepository;
import antifraud.repository.StolenCardRepository;
import antifraud.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class TransactionService {

    private final IpAdressRepository ipAddressRepository;
    private final StolenCardRepository stolenCardRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(IpAdressRepository ipAddressRepository, StolenCardRepository stolenCardRepository) {
        this.ipAddressRepository = ipAddressRepository;
        this.stolenCardRepository = stolenCardRepository;
    }

    public ResultDTO createTransaction(TransactionDTO transactionDTO) {
        Set<String> infoSet = new TreeSet<>();

        if (!regionRepository.existsByCode(transactionDTO.getRegion())) {
            throw new IllegalArgumentException("Region is not valid");
        }

        // Validate IP and card number first
        boolean isSuspiciousIp = ipAddressRepository.existsByIp(transactionDTO.getIp());
        boolean isStolenCard = stolenCardRepository.existsByNumber(transactionDTO.getNumber());
        if (isStolenCard) {
            infoSet.add("card-number");
        }
        if (isSuspiciousIp) {
            infoSet.add("ip");
        }


        LocalDateTime currentDate = transactionDTO.getDate();
        LocalDateTime oneHourAgo = currentDate.minusHours(1);

        List<Transaction> transactionsInLastHour = transactionRepository.findByNumberAndDateBetween(
                transactionDTO.getNumber(), oneHourAgo, currentDate);
        transactionsInLastHour.add(saveTransaction(transactionDTO, TransactionStatus.ALLOWED)); // Add the current transaction to the list

        long uniqueIps = transactionsInLastHour.stream()
                .map(Transaction::getIp)
                .distinct()
                .count();

        long uniqueRegions = transactionsInLastHour.stream()
                .map(Transaction::getRegion)
                .distinct()
                .count();

        if (uniqueIps > 2) {
            infoSet.add("ip-correlation");
        }

        if (uniqueRegions > 2) {
            infoSet.add("region-correlation");
        }

        // Determine result
        TransactionStatus result;
        if (transactionDTO.getAmount() > 1500 || uniqueIps > 3 || uniqueRegions > 3 || isSuspiciousIp || isStolenCard) {
            if (transactionDTO.getAmount() > 1500) {
                infoSet.add("amount");
            }
            result = TransactionStatus.PROHIBITED;
        } else if (transactionDTO.getAmount() > 200 || uniqueIps == 3 || uniqueRegions == 3) {
            if (transactionDTO.getAmount() > 200) {
                infoSet.add("amount");
            }
            result = TransactionStatus.MANUAL_PROCESSING;
        } else {
            result = TransactionStatus.ALLOWED;
        }

        // Update transaction status
        Transaction transaction = transactionsInLastHour.get(transactionsInLastHour.size() - 1);
        transaction.setResult(result);
        transactionRepository.save(transaction);

        // Prepare response
        String info = infoSet.isEmpty() ? "none" : String.join(", ", infoSet);
        return new ResultDTO(result, info);
    }

    private Transaction saveTransaction(TransactionDTO dto, TransactionStatus status) {
        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setIp(dto.getIp());
        transaction.setNumber(dto.getNumber());
        transaction.setRegion(dto.getRegion());
        transaction.setDate(dto.getDate());
        transaction.setResult(status);
        return transactionRepository.save(transaction);
    }

    private int countUniqueIpsLastHour(String cardNumber, String currentIp, LocalDateTime currentDate) {
        LocalDateTime oneHourAgo = currentDate.minusHours(1);
        return transactionRepository.countDistinctIpByNumberAndIpNotAndDateBetween(cardNumber, currentIp, oneHourAgo, currentDate);
    }

    private int countUniqueRegionsLastHour(String cardNumber, String currentRegion, LocalDateTime currentDate) {
        LocalDateTime oneHourAgo = currentDate.minusHours(1);
        return transactionRepository.countDistinctRegionByNumberAndRegionNotAndDateBetween(cardNumber, currentRegion, oneHourAgo, currentDate);
    }
}
