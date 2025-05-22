package edu.prz.bomsystem.mbom.application;

import edu.prz.bomsystem.mbom.domain.Bon;
import edu.prz.bomsystem.mbom.domain.Bon.BonId;
import edu.prz.bomsystem.mbom.domain.BonModule;
import edu.prz.bomsystem.mbom.domain.BonModuleRepository;
import edu.prz.bomsystem.mbom.domain.BonRepository;
import edu.prz.bomsystem.module.domain.Module;
import edu.prz.bomsystem.module.domain.Module.ModuleId;
import edu.prz.bomsystem.module.domain.ModuleRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BonService {

  private final BonRepository bonRepository;
  private final BonModuleRepository bonModuleRepository;
  private final ModuleRepository moduleRepository;

  @Transactional
  public Bon createBon(String catalogId) {
    val bon = Bon.builder().catalogId(catalogId).build();
    return bonRepository.save(bon);
  }

  @Transactional
  public Optional<Bon> addModuleToBon(BonId bonId, ModuleId moduleId, int quantity) {
    return bonRepository.findById(bonId.getId()).map(bon -> {
      boolean alreadyExists = bon.getBonModuleList().stream()
          .anyMatch(bm -> bm.getModuleId().getId().equals(moduleId.getId()));

      if (!alreadyExists) {
        Module module = moduleRepository.findById(moduleId.getId())
            .orElseThrow(() -> new IllegalArgumentException("Module not found"));

        BonModule bonModule = BonModule.builder()
            .bon(bon)
            .moduleId(module.getIdentity())
            .modulesQuantity(quantity)
            .build();

        bonModuleRepository.save(bonModule);
        bon.getBonModuleList().add(bonModule);
      }

      return bonRepository.save(bon);
    });
  }
}
