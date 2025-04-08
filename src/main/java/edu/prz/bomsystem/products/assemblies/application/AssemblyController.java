package edu.prz.bomsystem.products.assemblies.application;

import edu.prz.bomsystem.products.assemblies.domain.Assembly;
import edu.prz.bomsystem.products.assemblies.domain.Assembly.AssemblyId;
import edu.prz.bomsystem.products.assemblies.domain.AssemblyRepository;
import edu.prz.bomsystem.products.components.domain.Component.ComponentId;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/assembly")
public class AssemblyController {

  public static final String ID = "/{id}";

  final AssemblyRepository assemblyRepository;
  final AssemblyApplicationService assemblyApplicationService;

  public AssemblyController(AssemblyRepository assemblyRepository,
      AssemblyApplicationService assemblyApplicationService) {
    this.assemblyRepository = assemblyRepository;
    this.assemblyApplicationService = assemblyApplicationService;
  }

  @GetMapping
  public ResponseEntity<Page<Assembly>> findAll(Pageable pageable) {
    return ResponseEntity.ok(assemblyRepository.findAll(pageable));
  }

  @GetMapping(ID)
  public ResponseEntity<Assembly> getOne(@PathVariable Long id) {
    return ResponseEntity.of(assemblyRepository.findById(id));
  }

  @PostMapping
  public ResponseEntity<Assembly> createAssembly(@RequestBody CreateAssemblyReq request) {
    return ResponseEntity
        .created(URI.create(""))
        .body(assemblyApplicationService.createAssembly(request.name));
  }

  @PostMapping(ID + "/component")
  public ResponseEntity<Assembly> addComponentToAssembly(@PathVariable Long id, @RequestBody addComponentReq request){
    return ResponseEntity.of(assemblyApplicationService.addComponent(AssemblyId.of(id),request.componentsId));
  }

  public record CreateAssemblyReq(String name) {
  }

  public record addComponentReq(ComponentId componentsId){

  }
}
