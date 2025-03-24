package edu.prz.bomsystem.authority.application;

import edu.prz.bomsystem.authority.domain.user.Role;
import edu.prz.bomsystem.authority.domain.user.User;
import edu.prz.bomsystem.authority.domain.user.UserRepository;
import java.net.URI;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authority/users")
public class UserController {

  public static final String ID = "/{id}";

  final UserRepository repository;
  final UserApplicationService service;

  public UserController(UserRepository repository, UserApplicationService service) {
    this.repository = repository;
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<Page<User>> findAll(Pageable pageable) {
    return ResponseEntity.ok(repository.findAll(pageable));
  }

  @GetMapping(ID)
  public ResponseEntity<User> getOne(@PathVariable Long id) {
    return ResponseEntity.of(repository.findById(id));
  }

  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody CreateUserReq request) {
    return ResponseEntity
        .created(URI.create(""))
        .body(service.createUser(request.username, request.password, request.name, request.email,request.roles));
  }

  @PostMapping(ID + "/roles")
  public ResponseEntity<User> grantRolesToUser(@PathVariable Long id, @RequestBody GrantRolesReq request) {
    return ResponseEntity.of(service.grantRolesToUser(id, request.roles));
  }

  @PutMapping(ID + "/personal-data")
  public ResponseEntity<User> changePersonalData(@PathVariable Long id,
      @RequestBody ChangeUserPersonalDataReq request) {
    return ResponseEntity.of(service.changePersonalData(id, request.name, request.email));
  }

  @DeleteMapping(ID)
  public ResponseEntity<User> removeUser(@PathVariable Long id) {
    return ResponseEntity.of(service.removeUser(id));
  }

  public record CreateUserReq(String username, String password, String name, String email, Set<Role> roles) {

  }

  public record ChangeUserPersonalDataReq(String name, String email) {

  }

  public record GrantRolesReq(Set<Role> roles) {

  }

}
