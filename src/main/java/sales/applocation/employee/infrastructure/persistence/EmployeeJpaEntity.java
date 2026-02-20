package sales.applocation.employee.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.util.UUID;

@Entity
@Table(name = "employee")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeJpaEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column(unique = true)
    private String username;

    private String password;

    private String phone;

    private String role;

    private boolean online;

    public EmployeeJpaEntity(
            UUID id,
            String username,
            String password,
            String phone,
            String role,
            boolean online
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.online = online;
    }

    @Transient
    private boolean isNew = true;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PostPersist
    void markNotNew() {
        this.isNew = false;
    }

}
