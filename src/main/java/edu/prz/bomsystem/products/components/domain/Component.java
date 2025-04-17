package edu.prz.bomsystem.products.components.domain;

import edu.prz.bomsystem.foundation.domain.BaseEntity;
import edu.prz.bomsystem.foundation.domain.Identity;
import edu.prz.bomsystem.products.components.domain.ComponentProperty.ComponentPropertyId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "components")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Component extends BaseEntity<Long> {

  @Embeddable
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ComponentId extends Identity<Long> {

    private ComponentId(Long id) {
      super(id);
    }

    public static ComponentId of(Long id) {
      return new ComponentId(id);
    }

    public static ComponentId of(String txt) {
      return new ComponentId(Long.valueOf(txt));
    }

  }

  public ComponentId getIdentity() {
    return ComponentId.of(id);
  }

  @Lob
  @Column(length = 1_000_000)
  byte[] componentPicture;

  @NotNull
  String name;
  String partNumber;

  @AttributeOverride(name = "id", column = @Column(name = "components_property_id"))
  ComponentPropertyId componentProperty;

}
