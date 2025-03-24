package edu.prz.bomsystem.products.assemblies.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.prz.bomsystem.products.components.domain.Component;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assemblies")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Assembly {

  @Getter
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Builder.Default
  private LocalDate createDate = LocalDate.now();

  @JsonManagedReference
  @ManyToMany
  @JoinTable(
      name = "assembly_component",
      joinColumns = @JoinColumn(name = "assembly_id"),
      inverseJoinColumns = @JoinColumn(name = "component_id")
  )
  private List<Component> components;
}
