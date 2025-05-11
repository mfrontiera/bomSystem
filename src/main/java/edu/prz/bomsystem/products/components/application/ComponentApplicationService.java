package edu.prz.bomsystem.products.components.application;

import edu.prz.bomsystem.products.components.domain.Component;
import edu.prz.bomsystem.products.components.domain.Component.ComponentId;
import edu.prz.bomsystem.products.components.domain.ComponentProperty;
import edu.prz.bomsystem.products.components.domain.ComponentPropertyRepository;
import edu.prz.bomsystem.products.components.domain.ComponentRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComponentApplicationService {

  final ComponentRepository componentRepository;
  private final ComponentPropertyRepository componentPropertyRepository;

  @Transactional
  public Component createComponent(String name, List<ComponentProperty> componentPropertyList) {
    Component component = Component.builder()
        .name(name)
        .componentProperties(componentPropertyList)
        .build();
    return componentRepository.save(component);
  }

  @Transactional
  public Optional<Component> changeComponentData(ComponentId id, List<ComponentProperty> componentPropertyList){
    return componentRepository.findById(id.getId())
        .map(found -> {
          found.setComponentProperties(componentPropertyList);
          return componentRepository.save(found);
        });
  }

  @Transactional
  public Optional<Component> addPropertyToComponent(ComponentId id, ComponentProperty componentProperty){
    return componentRepository.findById(id.getId())
        .map(found -> {
          found.addProperty(componentProperty);
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

  public Optional<List<ComponentProperty>> getComponentProperties(ComponentId id){
    return componentRepository.findById(id.getId())
        .map(Component::getComponentProperties);
  }
}
