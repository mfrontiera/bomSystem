package edu.prz.bomsystem.component.application;

import edu.prz.bomsystem.component.domain.Component;
import edu.prz.bomsystem.component.domain.Component.ComponentId;
import edu.prz.bomsystem.component.domain.ComponentRepository;
import jakarta.persistence.EntityNotFoundException;
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
  public void assignParentToComponent(ComponentId parentId, ComponentId childId) {
    Component parent = componentRepository.findById(parentId.getId())
        .orElseThrow(() -> new EntityNotFoundException("Parent component not found: " + parentId));

    Component child = componentRepository.findById(childId.getId())
        .orElseThrow(() -> new EntityNotFoundException("Child component not found: " + childId));

    if (parent.getIdentity().equals(child.getIdentity())) {
      throw new IllegalArgumentException("Component cannot be its own parent");
    }

    child.setParent(parent);
    componentRepository.save(child);
  }

  @Transactional
  public void deleteComponent(ComponentId componentId){
    componentRepository.deleteById(componentId.getId());
  }

}
