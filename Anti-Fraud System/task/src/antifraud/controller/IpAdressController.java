package antifraud.controller;


import antifraud.domain.dto.IpAdressDTO;
import antifraud.exception.NotFoundException;
import antifraud.service.IpAdressService;
import lombok.Getter;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/antifraud/suspicious-ip")
public class IpAdressController {
    @Autowired
    private IpAdressService ipAdressService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IpAdressDTO> registerIpAdress(@RequestBody IpAdressDTO ipAdressDTO){
        if (ipAdressDTO.getIp() == null || ipAdressDTO.getIp().isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        try {
            IpAdressDTO ipAdressDTO1 = ipAdressService.registerIpAdress(ipAdressDTO.getIp());
            return ResponseEntity.ok(ipAdressDTO1);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(409).build();
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "/{ip}")
    public ResponseEntity<Map<String, String>> deleteIpAdress(@PathVariable String ip){
        try {
            return ResponseEntity.ok(Map.of("status", ipAdressService.deleteIpAdress(ip)));
        } catch (NotFoundException e){
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<IpAdressDTO>> getAllIps(){
        return ResponseEntity.ok().body(ipAdressService.getIpAdresses());
    }
}
