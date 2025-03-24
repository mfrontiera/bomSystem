package edu.prz.bomsystem.authority.domain.user;

import com.vaadin.flow.spring.security.AuthenticationContext;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticatedUserService {

  final UserRepository userRepository;
  final AuthenticationContext authenticationContext;

  // TODO ID użytkownika zapamiętać w sesji
  public Long getUserIdFromContext() {
    return authenticationContext.getAuthenticatedUser(UserDetails.class)
        .map(userDetails -> userRepository.findByUsername(userDetails.getUsername()))
        .map(User::getId)
        .orElseThrow(() -> new SessionAuthenticationException("No user"));
  }

  public Optional<User> getUserUsingContext() {
    return authenticationContext.getAuthenticatedUser(UserDetails.class)
        .map(userDetails -> userRepository.findByUsername(userDetails.getUsername()));
  }

  public void logout() {
    authenticationContext.logout();
  }

}
