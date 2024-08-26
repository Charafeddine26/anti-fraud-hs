package antifraud.controller;

import antifraud.domain.dto.IpAdressDTO;
import antifraud.domain.dto.StolenCardDTO;
import antifraud.exception.NotFoundException;
import antifraud.service.StolenCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/antifraud/stolencard")
public class StolenCardController {

    @Autowired
    private StolenCardService stolenCardService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StolenCardDTO> addCard(@RequestBody StolenCardDTO number){
        if (number.getNumber() == null || number.getNumber().isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        try {
            return ResponseEntity.ok(stolenCardService.addCard(number.getNumber()));
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e){
            return ResponseEntity.status(409).build();
        }
    }

    @DeleteMapping("/{number}")
    public ResponseEntity<Map<String, String>> deleteCard(@PathVariable String number){
        try {
            return ResponseEntity.ok(Map.of("status", stolenCardService.deleteCard(number)));
        } catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<StolenCardDTO>> getAllIps(){
        return ResponseEntity.ok().body(stolenCardService.listCards());
    }
}
