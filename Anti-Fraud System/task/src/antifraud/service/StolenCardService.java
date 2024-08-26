package antifraud.service;

import antifraud.domain.dto.StolenCardDTO;
import antifraud.domain.model.StolenCard;
import antifraud.exception.NotFoundException;
import antifraud.repository.StolenCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StolenCardService {

    @Autowired
    private StolenCardRepository stolenCardRepository;


    public static boolean isValidLuhn(String number) {
        int n = number.length();
        int total = 0;
        boolean even = true;
        for (int i = n - 2; i >= 0; i--) {
            int digit = number.charAt(i) - '0';
            if (digit < 0 || digit > 9) {
                return false;
            }
            if (even) {
                digit <<= 1;
            }
            even = !even;
            total += digit > 9 ? digit - 9 : digit;
        }
        int checksum = number.charAt(n - 1) - '0';
        return (total + checksum) % 10 == 0;
    }


    public StolenCardDTO addCard(String card){
        if (!isValidLuhn(card)){
            throw new IllegalArgumentException("Wrong format!");
        }
        if (stolenCardRepository.existsByNumber(card)){
            throw new NotFoundException("Already exists!");
        }
        StolenCard stolenCard = stolenCardRepository.save(new StolenCard(card));
        return new StolenCardDTO(stolenCard.getId(), stolenCard.getNumber());
    }

    public String deleteCard(String card){
        if (!isValidLuhn(card)){
            throw new IllegalArgumentException("Wrong format!");
        }
        if (!stolenCardRepository.existsByNumber(card)){
            throw new NotFoundException("Card doesnt exist!");
        }

        stolenCardRepository.delete(stolenCardRepository.findByNumber(card));
        return "Card "+card+" successfully removed!";
    }

    public List<StolenCardDTO> listCards(){
        return (stolenCardRepository.findAllByOrderByIdAsc()).stream().map(card -> new StolenCardDTO(card.getId(), card.getNumber())).toList();
    }
}
