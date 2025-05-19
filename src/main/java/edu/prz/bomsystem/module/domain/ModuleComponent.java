package edu.prz.bomsystem.module.domain;

import edu.prz.bomsystem.component.domain.Component.ComponentId;
import edu.prz.bomsystem.foundation.domain.BaseEntity;
import edu.prz.bomsystem.foundation.domain.Identity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "module_components")
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModuleComponent extends BaseEntity<Long> {

  @Embeddable
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ModuleComponentId extends Identity<Long> {

    private ModuleComponentId(Long id) {
      super(id);
    }

    public static ModuleComponent.ModuleComponentId of(Long id) {
      return new ModuleComponent.ModuleComponentId(id);
    }


    public static ModuleComponent.ModuleComponentId of(String txt) {
      return new ModuleComponent.ModuleComponentId(Long.valueOf(txt));
    }

  }


  public ModuleComponent.ModuleComponentId getIdentity() {
    return ModuleComponent.ModuleComponentId.of(id);
  }

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "module_id", referencedColumnName = "id", nullable = false)
  private Module module;

  @AttributeOverride(name = "id", column = @Column(name = "component_id"))
  private ComponentId component;

  int componentsQuantity;

}
