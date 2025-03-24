package edu.prz.bomsystem.products.assemblies.application;

import edu.prz.bomsystem.products.assemblies.domain.Assembly;
import edu.prz.bomsystem.products.assemblies.domain.AssemblyRepository;
import edu.prz.bomsystem.products.components.domain.Component;
import edu.prz.bomsystem.products.components.domain.ComponentRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssemblyApplicationService {

  final AssemblyRepository assemblyRepository;
  final ComponentRepository componentRepository;

  @Transactional
  public Assembly createAssembly(String name){
    val assembly = Assembly.builder()
        .name(name)
        .build();
    return assemblyRepository.save(assembly);
  }

  @Transactional
  public Optional<Assembly> addComponent(Long id, List<Long> componentIdList){
    return assemblyRepository.findById(id)
        .map(found -> {
          List<Component> components = componentRepository.findAllById(componentIdList);
          found.setComponents(components);
          return assemblyRepository.save(found);
        });
  }

  @Transactional
  public Optional<Assembly> removeAssembly(Long id){
    return assemblyRepository.findById(id)
        .map(found -> {
          assemblyRepository.delete(found);
          return found;
        });
  }

}
