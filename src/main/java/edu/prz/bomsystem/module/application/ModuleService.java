package edu.prz.bomsystem.module.application;

import edu.prz.bomsystem.component.domain.Component;
import edu.prz.bomsystem.component.domain.Component.ComponentId;
import edu.prz.bomsystem.component.domain.ComponentRepository;
import edu.prz.bomsystem.module.domain.Module;
import edu.prz.bomsystem.module.domain.Module.ModuleId;
import edu.prz.bomsystem.module.domain.ModuleComponent;
import edu.prz.bomsystem.module.domain.ModuleComponentRepository;
import edu.prz.bomsystem.module.domain.ModuleRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ModuleService {

  private final ModuleRepository moduleRepository;
  private final ModuleComponentRepository moduleComponentRepository;
  private final ComponentRepository componentRepository;

  @Transactional
  public Module createModule(String catalogId){
    val module = Module.builder().catalogId(catalogId).build();
    return moduleRepository.save(module);
  }

  @Transactional
  public Optional<Module> addComponentToModule(ModuleId moduleId, ComponentId componentId, int quantity) {
    return moduleRepository.findById(moduleId.getId()).map(module -> {
      boolean alreadyExists = module.getModuleComponentList().stream()
          .anyMatch(mc -> mc.getComponent().getId().equals(componentId.getId()));

      if (!alreadyExists) {
        Component component = componentRepository.findById(componentId.getId())
            .orElseThrow(() -> new IllegalArgumentException("Component not found"));

        ModuleComponent moduleComponent = ModuleComponent.builder()
            .module(module)
            .component(component.getIdentity())
            .componentsQuantity(quantity)
            .build();

        moduleComponentRepository.save(moduleComponent);
        module.getModuleComponentList().add(moduleComponent);
      }

      return moduleRepository.save(module);
    });
  }

  }

