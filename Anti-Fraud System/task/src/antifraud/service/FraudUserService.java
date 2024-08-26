package antifraud.service;

import antifraud.domain.dto.FraudUserDTO;
import antifraud.domain.dto.UserAccessDTO;
import antifraud.domain.enums.UserRoles;
import antifraud.domain.model.FraudUser;
import antifraud.domain.model.RegistrationRequest;
import antifraud.exception.ConflictException;
import antifraud.exception.NotFoundException;
import antifraud.repository.FraudUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FraudUserService {

    @Autowired
    private FraudUserRepository fraudUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserRoles getRole(String role){
        if (role.equals("ADMINISTRATOR")){
            return UserRoles.ADMINISTRATOR;
        }
        else if (role.equals("MERCHANT")){
            return UserRoles.MERCHANT;
        }
        else if (role.equals("SUPPORT")){
            return UserRoles.SUPPORT;
        }
        return null;
    }



    public FraudUserDTO registerUser(RegistrationRequest request) {
        if (fraudUserRepository.existsByUsername(request.username())) {
            throw new RuntimeException("User already exists");
        }

        FraudUser user = new FraudUser();
        user.setName(request.name());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));

        boolean isFirstUser = fraudUserRepository.count() == 0;
        if (isFirstUser) {
            user.setRole(UserRoles.ADMINISTRATOR);
            user.setLocked(false);
        } else {
            user.setRole(UserRoles.MERCHANT);
            user.setLocked(true);
        }

        FraudUser savedUser = fraudUserRepository.save(user);
        return new FraudUserDTO(savedUser.getId(), savedUser.getName(), savedUser.getUsername(), savedUser.getRole());
    }

    public FraudUserDTO changeRole(FraudUserDTO fraudUserDTO) {
        String username = fraudUserDTO.getUsername();
        String role = fraudUserDTO.getRole().toString();

        if (role == null || username == null || (!role.equals("SUPPORT") && !role.equals("MERCHANT"))) {
            throw new IllegalArgumentException("Invalid role or username");
        }

        Optional<FraudUser> optionalUser = fraudUserRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            FraudUser fraudUser = optionalUser.get();
            System.out.println(fraudUser.getRole());
            if (role.equals(fraudUser.getRole().toString())) {
                throw new ConflictException("Role already assigned to the user");
            } else {
                fraudUser.setRole(getRole(fraudUserDTO.getRole().toString()));
                fraudUserRepository.save(fraudUser);
                return new FraudUserDTO(fraudUser.getId(), fraudUser.getName(), fraudUser.getUsername(), fraudUser.getRole());
            }
        } else {
            throw new NotFoundException("User not found");
        }
    }

    public String changeUserAccess(UserAccessDTO accessDTO) {
        String username = accessDTO.getUsername();
        String operation = accessDTO.getOperation();

        if (username == null || operation == null) {
            throw new IllegalArgumentException("Invalid username or operation");
        }

        Optional<FraudUser> optionalUser = fraudUserRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        FraudUser user = optionalUser.get();

        if (user.getRole() == UserRoles.ADMINISTRATOR) {
            throw new IllegalArgumentException("Administrator cannot be locked");
        }

        if ("LOCK".equals(operation)) {
            user.setLocked(true);
            fraudUserRepository.save(user);
            return "User " + username + " locked!";
        } else if ("UNLOCK".equals(operation)) {
            user.setLocked(false);
            fraudUserRepository.save(user);
            return "User " + username + " unlocked!";
        } else {
            throw new IllegalArgumentException("Invalid operation");
        }
    }

}
