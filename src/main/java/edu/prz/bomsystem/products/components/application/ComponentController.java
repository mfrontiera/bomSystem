package edu.prz.bomsystem.products.components.application;

import edu.prz.bomsystem.products.components.domain.Component;
import edu.prz.bomsystem.products.components.domain.ComponentProperty;
import edu.prz.bomsystem.products.components.domain.ComponentRepository;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/components")
public class ComponentController {

  public static final String ID = "/{id}";

  final ComponentRepository componentRepository;
  final ComponentApplicationService componentApplicationService;

  public ComponentController(ComponentRepository componentRepository,
      ComponentApplicationService componentApplicationService) {
    this.componentRepository = componentRepository;
    this.componentApplicationService = componentApplicationService;
  }

  @GetMapping
  public ResponseEntity<Page<Component>> findAll(Pageable pageable){
    return ResponseEntity.ok(componentRepository.findAll(pageable));
  }

  @GetMapping(ID)
  public ResponseEntity<Component> getOne(@PathVariable Long id){
    return ResponseEntity.of(componentRepository.findById(id));
  }

  @PostMapping
  public ResponseEntity<Component> createComponent(@RequestBody CreateComponentReq request){
    return ResponseEntity
        .created(URI.create(""))
        .body(componentApplicationService.createComponent(request.name(), request.componentPropertyList()));
  }

  @PutMapping
  public ResponseEntity<Component> changeComponentData(@PathVariable Long id, @RequestBody ChangeComponentDataReq request ){
    return ResponseEntity.of(componentApplicationService.changeComponentData(id,request.componentPropertyList));
  }

  @DeleteMapping(ID)
  public ResponseEntity<Component> removeComponent(@PathVariable Long id){
    return ResponseEntity.of(componentApplicationService.removeComponent(id));
  }
  public record CreateComponentReq(String name, List<ComponentProperty> componentPropertyList){
  }

  public record ChangeComponentDataReq(List<ComponentProperty> componentPropertyList){

  }
}


