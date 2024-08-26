package antifraud.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class StolenCardDTO {
    private Long id;
    private String number;

    public StolenCardDTO(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    public StolenCardDTO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
