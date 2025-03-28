package edu.prz.bomsystem.products.components.application;

import edu.prz.bomsystem.products.components.domain.Component;
import edu.prz.bomsystem.products.components.domain.ComponentProperty;
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
  public Component createComponent(String name, ComponentProperty property) {
    Component component = Component.builder()
        .name(name)
        .componentsProperty(property)
        .build();
    return componentRepository.save(component);
  }

  @Transactional
  public Optional<Component> changeComponentData(Long id, ComponentProperty componentProperty){
    return componentRepository.findById(id)
        .map(found -> {
          found.setComponentsProperty(componentProperty);
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
