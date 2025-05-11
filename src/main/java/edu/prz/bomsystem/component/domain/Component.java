package edu.prz.bomsystem.component.domain;

import edu.prz.bomsystem.foundation.domain.BaseEntity;
import edu.prz.bomsystem.foundation.domain.Identity;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

  @NotNull
  String catalogId;
  String name;

  @Lob
  String description;

  String type;
  String unitMeasureType;
  String status;

  int costPerUnit;
  int stockQuantity;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "parent_id")
  private Component parent;
}
