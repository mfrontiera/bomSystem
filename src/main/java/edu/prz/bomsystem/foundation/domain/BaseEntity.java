package edu.prz.bomsystem.foundation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

  @CreationTimestamp
  @Column(updatable = false, nullable = false)
  protected LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  protected LocalDateTime updatedAt;
}
