package antifraud.domain.dto;

public class FraudUserDeletionDTO {
    private String username;
    private String status;

    public FraudUserDeletionDTO() {
    }

    public FraudUserDeletionDTO(String username, String status) {
        this.username = username;
        this.status = status;
    }

    public FraudUserDeletionDTO(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
