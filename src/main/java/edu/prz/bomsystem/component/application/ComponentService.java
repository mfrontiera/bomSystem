package edu.prz.bomsystem.component.application;

import edu.prz.bomsystem.component.domain.Component;
import edu.prz.bomsystem.component.domain.Component.ComponentId;
import edu.prz.bomsystem.component.domain.ComponentRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ComponentService {

  private final ComponentRepository componentRepository;

  @Transactional
  public Component createComponent(String catalogId) {
    val component = Component.builder().catalogId(catalogId).build();
    return componentRepository.save(component);
  }

  @Transactional
  public Optional<Component> assignParentToComponent(ComponentId parentId, ComponentId childId) {
    Component parent = componentRepository.findById(parentId.getId())
        .orElseThrow(() -> new EntityNotFoundException("Parent component not found: " + parentId));

    return componentRepository.findById(childId.getId())
            .map(found -> {
              if (parent.getIdentity().equals(found.getIdentity())) {
                throw new IllegalArgumentException("Component cannot be its own parent");
              }

              found.setParent(parent);
              return componentRepository.save(found);
            });
  }

  @Transactional
  public Optional<Component> editComponent(ComponentId componentId, String name){
    return componentRepository.findById(componentId.getId())
        .map(found -> {
          found.setName(name);
          return componentRepository.save(found);
        });
  }
  @Transactional
  public Optional<Component> deleteComponent(ComponentId componentId){
    return componentRepository.findById(componentId.getId())
        .map(found -> {
          componentRepository.delete(found);
          return componentRepository.save(found);
        });
  }

}
