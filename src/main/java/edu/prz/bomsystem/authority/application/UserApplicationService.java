package edu.prz.bomsystem.authority.application;

import edu.prz.bomsystem.authority.domain.user.Role;
import edu.prz.bomsystem.authority.domain.user.User;
import edu.prz.bomsystem.authority.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

  final UserRepository repository;

  @Transactional
  public User createUser(String username, String password, String name, String email, Set<Role> roles) {
    val user = User.builder()
        .username(username)
        .passwordSignature(createPasswordSignature(password))
        .name(name)
        .email(email)
        .roles(roles)
        .build();
    return repository.save(user);
  }

  @Transactional
  public Optional<User> changePersonalData(Long id, String name, String email) {
    return repository.findById(id)
        .map(found -> {
          found.setPersonalData(name, email);
          return repository.save(found);
        });
  }

  @Transactional
  public Optional<User> removeUser(Long id) {
    return repository.findById(id)
        .map(found -> {
          repository.delete(found);
          return found;
        });
  }

  public Optional<User> grantRolesToUser(Long id, Set<Role> roles) {
    return repository.findById(id)
        .map(found -> {
          found.addRoles(roles);
          return repository.save(found);
        });
  }

  private String createPasswordSignature(String password) {
    return null; // TODO
  }
}
