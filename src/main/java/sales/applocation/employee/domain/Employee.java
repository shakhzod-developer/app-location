package sales.applocation.employee.domain;

import sales.applocation.shared.role.Role;

public class Employee {

    private final EmployeeId id;

    private String username;

    private String phone;

    private String password;

    private final Role role;

    private boolean online;

    public Employee(EmployeeId id, String username, String phone, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.online = false;
    }

    public static Employee create(EmployeeId id, String username, String phone, String password) {
        return new Employee(id, username, phone, password, Role.DELIVERY_MAN);
    }


    public void goOnline() {
        this.online = true;
    }

    public void goOffline() {
        this.online = false;
    }

    public EmployeeId getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public Role getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public boolean isOnline() {
        return online;
    }
}
