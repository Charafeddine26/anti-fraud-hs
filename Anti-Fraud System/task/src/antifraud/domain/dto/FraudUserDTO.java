package antifraud.domain.dto;

import antifraud.domain.enums.UserRoles;

public class FraudUserDTO {

    private Integer id;
    private String name;
    private String username;
    private UserRoles role;

    public FraudUserDTO() {
    }

    public FraudUserDTO(Integer id, String name, String username, UserRoles role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.role = role;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

}
