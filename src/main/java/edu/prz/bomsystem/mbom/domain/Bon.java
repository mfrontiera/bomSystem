package edu.prz.bomsystem.mbom.domain;

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
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "bons")
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bon extends BaseEntity<Long> {

  @Embeddable
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  public static class BonId extends Identity<Long> {

    private BonId(Long id) {
      super(id);
    }

    public static Bon.BonId of(Long id) {
      return new Bon.BonId(id);
    }

    public static Bon.BonId of(String txt) {
      return new Bon.BonId(Long.valueOf(txt));
    }
  }

  public Bon.BonId getIdentity() {
    return Bon.BonId.of(id);
  }

  @Builder.Default
  @ColumnDefault("'NewBon'")
  String name = "NewBon";

  @Column(unique = true)
  @NotNull
  String catalogId;

  @Lob
  String description;

  @Builder.Default
  @ColumnDefault("'Pending'")
  String status = "Pending";

  int stockQuantity;

  @DateTimeFormat
  LocalDateTime date;

  @OneToMany(mappedBy = "bon", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  List<BonModule> bonModuleList;
}
