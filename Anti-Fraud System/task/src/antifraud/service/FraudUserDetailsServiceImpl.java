package antifraud.service;

import antifraud.domain.model.FraudUser;
import antifraud.domain.model.FraudUserAdapter;
import antifraud.repository.FraudUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FraudUserDetailsServiceImpl implements UserDetailsService {

    private final FraudUserRepository fraudUserRepository;

    public FraudUserDetailsServiceImpl(FraudUserRepository fraudUserRepository) {
        this.fraudUserRepository = fraudUserRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        FraudUser user = fraudUserRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        return new FraudUserAdapter(user);
    }
}
