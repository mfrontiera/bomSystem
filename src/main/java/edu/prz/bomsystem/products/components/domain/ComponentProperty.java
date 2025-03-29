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
  private String vendorsId;
  private String size;
  private String params;
  private String additionalInfo;
  private String buyLink;
}
