package edu.prz.bomsystem.products.components.application;

import edu.prz.bomsystem.products.components.domain.Component;
import edu.prz.bomsystem.products.components.domain.ComponentProperty;
import edu.prz.bomsystem.products.components.domain.ComponentRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComponentApplicationService {

  final ComponentRepository componentRepository;

  @Transactional
  public Component createComponent(String name, List<ComponentProperty> propertyList) {
    Component component = Component.builder()
        .name(name)
        .componentsProperties(new ArrayList<>())
        .build();

    for (ComponentProperty property : propertyList) {
      property.setComponent(component);
      component.getComponentsProperties().add(property);
    }

    return componentRepository.save(component);
  }

  @Transactional
  public Optional<Component> changeComponentData(Long id, List<ComponentProperty> componentPropertyList){
    return componentRepository.findById(id)
        .map(found -> {
          found.setComponentsProperties(componentPropertyList);
          return componentRepository.save(found);
        });
  }

  @Transactional
  public Optional<Component> removeComponent(Long id){
    return componentRepository.findById(id)
        .map(found -> {
          componentRepository.delete(found);
          return found;
        });
  }
}
