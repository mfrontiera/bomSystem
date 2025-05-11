package edu.prz.bomsystem.products.components.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponentPropertyRepository extends JpaRepository<ComponentProperty,Long> {
  List<ComponentProperty> findComponentPropertiesByComponent(Component component);

}
