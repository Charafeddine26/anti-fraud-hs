package antifraud.service;

import antifraud.domain.dto.IpAdressDTO;
import antifraud.domain.model.IpAdress;
import antifraud.exception.NotFoundException;
import antifraud.repository.IpAdressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IpAdressService
{
    @Autowired
    private IpAdressRepository ipAdressRepository;


    public static boolean isValidIPv4(String ip) {
        String[] parts = ip.split("\\.");
        if (parts.length != 4) {
            return false;
        }
        for (String part : parts) {
            try {
                int num = Integer.parseInt(part);
                if (num < 0 || num > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    public IpAdressDTO registerIpAdress(String ip){
        if (ipAdressRepository.existsByIp(ip)){
            throw new IllegalArgumentException("IP already exists");
        }
        if (!isValidIPv4(ip)){
            throw new RuntimeException("Invalid IP");
        }
        IpAdress ipAdress = new IpAdress();
        ipAdress.setIp(ip);
        ipAdressRepository.save(ipAdress);
        return new IpAdressDTO(ipAdress.getId(), ip);
    }

    public String deleteIpAdress(String ip){
        if (!isValidIPv4(ip)){
            throw new RuntimeException("Invalid IP");
        }
        if (!ipAdressRepository.existsByIp(ip)){
            throw new NotFoundException("IP doesn't exist");
        }
        ipAdressRepository.delete((ipAdressRepository.findByIp(ip)));
        return "IP "+ip+" successfully removed!";
    }

    public List<IpAdressDTO> getIpAdresses(){
        return (ipAdressRepository.findAllByOrderByIdAsc()).stream().map(ip -> new IpAdressDTO(ip.getId(), ip.getIp())).toList();
    }
}
