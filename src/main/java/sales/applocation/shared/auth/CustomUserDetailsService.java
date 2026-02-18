package sales.applocation.shared.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sales.applocation.employee.domain.Employee;
import sales.applocation.employee.domain.EmployeeRepository;
import sales.applocation.users.domain.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findsByUsername(username);
        if (user != null) {
            return new CustomUserDetails(
                    user.getId().id(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getRole(),
                    false
            );
        }


        Employee emp = (Employee) employeeRepository.findsByUsername(username);
        if (emp != null) {
            return new CustomUserDetails(
                    emp.getId().id(),
                    emp.getUsername(),
                    emp.getPassword(),
                    emp.getRole(),
                    true
            );
        }
        throw new UsernameNotFoundException("Identity not found: " + username);
    }
}
