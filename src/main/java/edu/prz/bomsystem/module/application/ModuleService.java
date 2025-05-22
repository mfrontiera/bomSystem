package edu.prz.bomsystem.module.application;

import edu.prz.bomsystem.component.domain.Component.ComponentId;
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

  @Transactional
  public Module createModule(String catalogId){
    val module = Module.builder().catalogId(catalogId).build();
    return moduleRepository.save(module);
  }

  @Transactional
  public Optional<Module> addComponentToModule(ModuleId moduleid, ComponentId componentId, int quantity){
    Optional<Module> module = moduleRepository.findById(moduleid.getId());

    module.map(found -> {
      val moduleComponent = found.getModuleComponentList().stream().filter(c -> c.getComponent() == componentId).findFirst();
      if(moduleComponent.isEmpty()){
        val moduleComponentCreated = ModuleComponent.builder().module(found).component(componentId).componentsQuantity(quantity).build();
        moduleComponentRepository.save(moduleComponentCreated);
      }
      return moduleRepository.save(found);
    });

    return module;
  }


}
