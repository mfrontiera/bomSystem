package edu.prz.bomsystem.products.components.application;

import edu.prz.bomsystem.products.components.domain.Component;
import edu.prz.bomsystem.products.components.domain.Component.ComponentId;
import edu.prz.bomsystem.products.components.domain.ComponentProperty.ComponentPropertyId;
import edu.prz.bomsystem.products.components.domain.ComponentRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComponentApplicationService {

  final ComponentRepository componentRepository;

  @Transactional
  public Component createComponent(String name, ComponentPropertyId componentPropertyId) {
    Component component = Component.builder()
        .name(name)
        .componentProperty(componentPropertyId)
        .build();
    return componentRepository.save(component);
  }

  @Transactional
  public Optional<Component> changeComponentData(ComponentId id, ComponentPropertyId componentPropertyId){
    return componentRepository.findById(id.getId())
        .map(found -> {
          found.setComponentProperty(componentPropertyId);
          return componentRepository.save(found);
        });
  }

  @Transactional
  public Optional<Component> removeComponent(ComponentId id){
    return componentRepository.findById(id.getId())
        .map(found -> {
          componentRepository.delete(found);
          return found;
        });
  }
}
