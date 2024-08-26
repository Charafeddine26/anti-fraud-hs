package antifraud.controller;

import antifraud.domain.dto.FraudUserDTO;
import antifraud.domain.dto.FraudUserDeletionDTO;
import antifraud.domain.dto.FraudUserRoleDTO;
import antifraud.domain.dto.UserAccessDTO;
import antifraud.domain.enums.UserRoles;
import antifraud.domain.model.FraudUser;
import antifraud.domain.model.RegistrationRequest;
import antifraud.exception.ConflictException;
import antifraud.exception.NotFoundException;
import antifraud.repository.FraudUserRepository;
import antifraud.service.FraudUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
//import list
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class FraudUserController {

    private final FraudUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private FraudUserService fraudUserService;

    public FraudUserController(FraudUserRepository repository,
                          PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    boolean firstOccur = true;

    @PostMapping(path = "/user")
    public ResponseEntity<FraudUserDTO> register(@RequestBody RegistrationRequest request) {

        if (repository.existsByUsername(request.username())) {
            return ResponseEntity.status(409).build();
        }
        else if (request.name() == null || request.username() == null || request.password() == null) {
            return ResponseEntity.badRequest().build();
        }

        var user = new FraudUser();
        user.setName(request.name());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));

        boolean isFirstUser = repository.count() == 0;
        if (isFirstUser) {
            user.setRole(UserRoles.ADMINISTRATOR);
            firstOccur = false;
        } else {
            user.setRole(UserRoles.MERCHANT);
            user.setLocked(true);
        }

        repository.save(user);

        return ResponseEntity.status(201).body(new FraudUserDTO(user.getId(), user.getName(), user.getUsername(), user.getRole()));
    }

    @GetMapping(path = "/list")
    public ResponseEntity<List<FraudUserDTO>> list(){
        var list = (repository.findAllByOrderByIdAsc()).stream()
                .map(user -> new FraudUserDTO(user.getId(), user.getName(), user.getUsername(), user.getRole())).toList();

        return ResponseEntity.ok().body(list);
    }

    @DeleteMapping(path = "/user/{username}")
    public ResponseEntity<FraudUserDeletionDTO> delete(@PathVariable String username) {
        if (!repository.existsByUsername(username)) {
            return ResponseEntity.notFound().build();
        }
        var user = repository.findByUsername(username).get();
        repository.delete(user);
        return  ResponseEntity.ok().body(new FraudUserDeletionDTO(username, "Deleted successfully!"));
    }
    @PutMapping(path = "/role")
    public ResponseEntity<FraudUserDTO> changeRole(@RequestBody FraudUserDTO fraudUserDTO) {

        try {
            FraudUserDTO updatedUser = fraudUserService.changeRole(fraudUserDTO);
            return ResponseEntity.ok().body(updatedUser);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/access")
    public ResponseEntity<Map<String, String>> changeUserAccess(@RequestBody UserAccessDTO accessDTO) {
        try {
            String status = fraudUserService.changeUserAccess(accessDTO);
            return ResponseEntity.ok(Map.of("status", status));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


}
