package mortar.euroshopper.eCommerceApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.util.ArrayList;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Account account = adminRepository.findByEmail(email);

        if (account == null) {
            throw new UsernameNotFoundException("No such account: " + account);
        }

        return new org.springframework.security.core.userdetails.User(
                account.getEmail(),
                account.getPassword(),
                true,
                true,
                true,
                true,
                account.getRoles().stream()
                        .map(string -> new SimpleGrantedAuthority(string))
                        .collect(Collectors.toCollection(ArrayList::new)));
    }

}
