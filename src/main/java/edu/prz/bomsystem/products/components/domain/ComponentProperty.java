package edu.prz.bomsystem.products.components.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="component_properties")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComponentProperty {

  @Getter
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name="component_id",  nullable = false)
  @JsonBackReference
  private Component component;

  private String vendor;
  private String vendor_id;
  private String size;
  private String certificate;
  private String material;
  private String params;
  private String additional_info;
  private String buy_link;
}
