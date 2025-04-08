package edu.prz.bomsystem.foundation.domain;

import jakarta.persistence.MappedSuperclass;
import lombok.NoArgsConstructor;

@MappedSuperclass
@NoArgsConstructor
public class Identity<T> {

  protected T id;

  public Identity(T id) {
    this.id = id;
  }

  public T getId() {
    return id;
  }

  public String toURLSegment() {
    return id.toString();
  }
}
