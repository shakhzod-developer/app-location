package sales.applocation.shared.role;

public enum Role {
    ADMIN,
    DELIVERY_MAN,
    USER;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
