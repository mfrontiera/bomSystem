package edu.prz.bomsystem.mbom.domain;


import edu.prz.bomsystem.foundation.domain.BaseEntity;
import edu.prz.bomsystem.foundation.domain.Identity;
import edu.prz.bomsystem.module.domain.Module.ModuleId;
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
@Table(name = "bon_modules")
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BonModule extends BaseEntity<Long> {

  @Embeddable
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class BonModuleId extends Identity<Long> {

    private BonModuleId(Long id) {
      super(id);
    }

    public static BonModule.BonModuleId of(Long id) {
      return new BonModule.BonModuleId(id);
    }

    public static BonModule.BonModuleId of(String txt) {
      return new BonModule.BonModuleId(Long.valueOf(txt));
    }
  }

  public BonModule.BonModuleId getIdentity() {
    return BonModule.BonModuleId.of(id);
  }

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "bon_id", referencedColumnName = "id", nullable = false)
  private Bon bon;

  @AttributeOverride(name = "id", column = @Column(name = "module_id"))
  private ModuleId moduleId;

  int modulesQuantity;
}
