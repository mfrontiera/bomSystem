package edu.prz.bomsystem.products.assemblies.domain;

import edu.prz.bomsystem.foundation.domain.BaseEntity;
import edu.prz.bomsystem.foundation.domain.Identity;
import edu.prz.bomsystem.products.assemblycomponent.domain.AssemblyComponent;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assemblies")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
public class Assembly extends BaseEntity<Long> {

  public Assembly() {
  }

  @Embeddable
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class AssemblyId extends Identity<Long> {

    private AssemblyId(Long id) {
      super(id);
    }

    public static AssemblyId of(Long id) {
      return new AssemblyId(id);
    }

    public static AssemblyId of(String txt) {
      return new AssemblyId(Long.valueOf(txt));
    }

  }

  public AssemblyId getIdentity() {
    return AssemblyId.of(id);
  }

  private String name;

  @Builder.Default
  private LocalDate createDate = LocalDate.now();

  @OneToMany(mappedBy = "assembly", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  List<AssemblyComponent> assemblyComponents;


}
