package antifraud.domain.dto;

import antifraud.domain.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class ResultDTO {
    private TransactionStatus result;
    private String info;

    public ResultDTO(TransactionStatus result, String info) {
        this.result = result;
        this.info = info;
    }

    public TransactionStatus getResult() {
        return result;
    }

    public void setResult(TransactionStatus result) {
        this.result = result;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


}
