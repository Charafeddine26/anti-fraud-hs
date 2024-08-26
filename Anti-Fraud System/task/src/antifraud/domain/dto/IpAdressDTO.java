package antifraud.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class IpAdressDTO {
    private Long id;
    private String ip;

    public IpAdressDTO(Long id, String ip) {
        this.id = id;
        this.ip = ip;
    }

    public IpAdressDTO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
