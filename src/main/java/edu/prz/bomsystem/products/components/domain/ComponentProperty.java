package edu.prz.bomsystem.products.components.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.prz.bomsystem.foundation.domain.BaseEntity;
import edu.prz.bomsystem.foundation.domain.Identity;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "component_properties")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class ComponentProperty extends BaseEntity<Long> {

  @Embeddable
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ComponentPropertyId extends Identity<Long> {

    private ComponentPropertyId(Long id) {
      super(id);
    }

    public static ComponentProperty.ComponentPropertyId of(Long id) {
      return new ComponentProperty.ComponentPropertyId(id);
    }

    public static ComponentProperty.ComponentPropertyId of(String txt) {
      return new ComponentProperty.ComponentPropertyId(Long.valueOf(txt));
    }

  }

  public ComponentProperty.ComponentPropertyId getIdentity() {
    return ComponentProperty.ComponentPropertyId.of(id);
  }

  @ManyToOne
  @JoinColumn(name = "component_id")
  @ToString.Exclude
  @JsonIgnore
  Component component;

  @Lob
  String description;
  String buyLink;

  @PreRemove
  public void preRemove() {
    if (component != null) {
      component.getComponentProperties().remove(this);
    }
  }

}
