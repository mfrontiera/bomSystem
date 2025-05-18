package edu.prz.bomsystem.component.domain;

import edu.prz.bomsystem.foundation.domain.BaseEntity;
import edu.prz.bomsystem.foundation.domain.Identity;
import jakarta.persistence.Column;
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
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Table(name = "components")
@Audited
@AuditTable(value = "components_audit")
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

  @Column(unique = true)
  @NotNull
  String catalogId;

  @Builder.Default
  @ColumnDefault("'NewComponent'")
  String name = "NewComponent";

  @Lob
  String description;

  String type;
  String unitMeasureType;

  @Builder.Default
  @ColumnDefault("'Pending'")
  String status = "Pending";

  int costPerUnit;
  int stockQuantity;

  @NotAudited
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "parent_id")
  private Component parent;
}
