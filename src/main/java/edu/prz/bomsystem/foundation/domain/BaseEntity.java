package edu.prz.bomsystem.foundation.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Getter(AccessLevel.PROTECTED)
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public abstract class BaseEntity<ID> implements Identifiable<ID> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected ID id;

  @Version
  protected int version;
}
