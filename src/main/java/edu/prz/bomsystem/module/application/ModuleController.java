package edu.prz.bomsystem.module.application;

import edu.prz.bomsystem.component.domain.Component.ComponentId;
import edu.prz.bomsystem.module.domain.Module;
import edu.prz.bomsystem.module.domain.Module.ModuleId;
import edu.prz.bomsystem.module.domain.ModuleRepository;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class ModuleController {

  public static final String ID = "/{id}";
  private final ModuleRepository moduleRepository;
  private final ModuleService moduleService;

  @GetMapping
  public ResponseEntity<Page<Module>> findAll(Pageable pageable) {
    return ResponseEntity.ok(moduleRepository.findAll(pageable));
  }

  @GetMapping(ID)
  public ResponseEntity<Module> getOne(@PathVariable ModuleId id) {
    return ResponseEntity.of(moduleRepository.findById(id.getId()));
  }

  @PostMapping
  public ResponseEntity<Module> createModule(@RequestBody createModuleReq request) {
    return ResponseEntity
        .created(URI.create(""))
        .body(moduleService.createModule(request.catalogId));
  }

  @PutMapping(ID + "/component")
  public ResponseEntity<Module> addComponentToModule(@PathVariable ModuleId id, @RequestBody addComponentReq request){
    return ResponseEntity.of(moduleService.addComponentToModule(id,request.componentId,request.quantity));
  }

  public record createModuleReq(String catalogId) {
  }

  public record addComponentReq(ComponentId componentId, int quantity){}
}
