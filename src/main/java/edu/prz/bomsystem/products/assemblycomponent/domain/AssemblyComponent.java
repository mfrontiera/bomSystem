package edu.prz.bomsystem.products.assemblycomponent.domain;

import edu.prz.bomsystem.foundation.domain.BaseEntity;
import edu.prz.bomsystem.foundation.domain.Identity;
import edu.prz.bomsystem.products.components.domain.Component.ComponentId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assembly_components")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class AssemblyComponent extends BaseEntity<Long> {


  @Embeddable
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class AssemblyComponentId extends Identity<Long> {

    private AssemblyComponentId(Long id) {
      super(id);
    }

    public static AssemblyComponent.AssemblyComponentId of(Long id) {
      return new AssemblyComponent.AssemblyComponentId(id);
    }

    public static AssemblyComponent.AssemblyComponentId of(String txt) {
      return new AssemblyComponent.AssemblyComponentId(Long.valueOf(txt));
    }

  }

  public AssemblyComponent.AssemblyComponentId getIdentity() {
    return AssemblyComponent.AssemblyComponentId.of(id);
  }

  @AttributeOverride(name = "id", column = @Column(name = "component_id"))
  private ComponentId component;

  int quantity;

  public void addToQuantity(int amount){
    this.quantity = quantity + amount;
  }
}
