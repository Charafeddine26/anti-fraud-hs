package antifraud.domain.enums;

public enum UserRoles {
    MERCHANT,
    ADMINISTRATOR,
    SUPPORT;

    public boolean isEmpty() {
        return this == null || this.toString().isEmpty();
    }
}
