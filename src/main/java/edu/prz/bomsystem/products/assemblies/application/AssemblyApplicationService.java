package edu.prz.bomsystem.products.assemblies.application;

import edu.prz.bomsystem.products.assemblies.domain.Assembly;
import edu.prz.bomsystem.products.assemblies.domain.Assembly.AssemblyId;
import edu.prz.bomsystem.products.assemblies.domain.AssemblyRepository;
import edu.prz.bomsystem.products.components.domain.ComponentRepository;
import jakarta.transaction.Transactional;
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
  public Assembly createAssembly(String name) {
    val assembly = Assembly.builder()
        .name(name)
        .build();
    return assemblyRepository.save(assembly);
  }

  @Transactional
  public Optional<Assembly> removeAssembly(AssemblyId id) {
    return assemblyRepository.findById(id.getId())
        .map(found -> {
          assemblyRepository.delete(found);
          return found;
        });
  }

}
