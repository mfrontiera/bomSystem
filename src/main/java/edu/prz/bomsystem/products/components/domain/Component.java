package edu.prz.bomsystem.products.components.domain;

import edu.prz.bomsystem.foundation.domain.BaseEntity;
import edu.prz.bomsystem.foundation.domain.Identity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
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

  @OneToMany(mappedBy = "component", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  List<ComponentProperty> componentProperties = new ArrayList<>();

  public void addProperty(ComponentProperty componentProperty){
    this.componentProperties.add(componentProperty);
  }

}
