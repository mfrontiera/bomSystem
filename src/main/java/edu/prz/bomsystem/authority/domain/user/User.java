package edu.prz.bomsystem.authority.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "users")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "passwordSignature")
@EqualsAndHashCode(of = "id")
public class User {

  @Getter
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  String username;
  String name;
  @Email
  String email;

  @Lob
  @Column(length = 1_000_000)
  byte[] profilePicture;

  @Enumerated(EnumType.STRING)
  @ElementCollection(fetch = FetchType.EAGER)
  Set<Role> roles;

  @JsonIgnore
  String passwordSignature;

  public void setPersonalData(String name, String email) {
    this.name = name;
    this.email = email;
  }

  public void addRoles(Set<Role> roles) {
    this.roles.addAll(roles);
  }
}
