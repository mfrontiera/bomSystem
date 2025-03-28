package edu.prz.bomsystem.products.components.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class ComponentProperty {
  private String vendor;
  private String vendor_id;
  private String size;
  private String certificate;
  private String material;
  private String params;
  private String additional_info;
  private String buy_link;
}
