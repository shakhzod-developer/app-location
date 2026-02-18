package sales.applocation.users.domain;

import sales.applocation.shared.role.Role;

import java.util.Objects;


public class User {

    private final UserId id;

    private String username;

    private String password;

    private String phone;

    private final Role role;

    public User(UserBuilder builder) {
        this.id = Objects.requireNonNull(builder.id);
        this.phone = Objects.requireNonNull(builder.phone);
        this.username = Objects.requireNonNull(builder.username);
        this.password = Objects.requireNonNull(builder.password);
        this.role = Role.USER;
    }

    public UserId getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }


   public static class UserBuilder{
        private UserId id;
        private String username;
        private String password;
        private String phone;
        private Role role;

        public UserBuilder id(UserId id){
            this.id = id;
            return this;
        }

        public UserBuilder username(String username){
            this.username = username;
            return this;
        }

        public UserBuilder password(String password){
            this.password = password;
            return this;
        }

        public UserBuilder phone(String phone){
            this.phone = phone;
            return this;
        }

        public UserBuilder role(Role role){
            this.role = role;
            return this;
        }

        public User build(){
            return new User(this);
        }
   }
}
