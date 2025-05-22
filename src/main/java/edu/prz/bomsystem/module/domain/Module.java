package edu.prz.bomsystem.module.domain;

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
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "modules")
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Module extends BaseEntity<Long> {


  @Embeddable
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class ModuleId extends Identity<Long> {

    private ModuleId(Long id) {
      super(id);
    }

    public static Module.ModuleId of(Long id) {
      return new Module.ModuleId(id);
    }


    public static Module.ModuleId of(String txt) {
      return new Module.ModuleId(Long.valueOf(txt));
    }

  }


  public Module.ModuleId getIdentity() {
    return Module.ModuleId.of(id);
  }

  @Builder.Default
  @ColumnDefault("'NewModule'")
  String name = "NewModule";

  @Column(unique = true)
  @NotNull
  String catalogId;

  @Lob
  String description;

  String type;
  String unitMeasureType;

  @Builder.Default
  @ColumnDefault("'Pending'")
  String status = "Pending";

  int stockQuantity;
  
  @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  List<ModuleComponent> moduleComponentList;
}
