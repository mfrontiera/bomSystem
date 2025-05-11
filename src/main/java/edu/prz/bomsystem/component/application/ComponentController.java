package edu.prz.bomsystem.component.application;

import edu.prz.bomsystem.component.domain.Component;
import edu.prz.bomsystem.component.domain.Component.ComponentId;
import edu.prz.bomsystem.component.domain.ComponentRepository;
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
@RequestMapping("/api/components")
@RequiredArgsConstructor
public class ComponentController {

  public static final String ID = "/{id}";
  private final ComponentService componentService;
  private final ComponentRepository componentRepository;

  @GetMapping
  public ResponseEntity<Page<Component>> findAll(Pageable pageable) {
    return ResponseEntity.ok(componentRepository.findAll(pageable));
  }

  @GetMapping(ID)
  public ResponseEntity<Component> getOne(@PathVariable ComponentId id) {
    return ResponseEntity.of(componentRepository.findById(id.getId()));
  }

  @PostMapping
  public ResponseEntity<Component> createComponent(@RequestBody createComponentReq request) {
    return ResponseEntity
        .created(URI.create(""))
        .body(componentService.createComponent(request.catalogId));
  }

  @PutMapping(ID + "/parent")
  public ResponseEntity<Component> setComponentParent(@PathVariable ComponentId id, @RequestBody assignParentReq request){
    return ResponseEntity.of(componentService.assignParentToComponent(request.parentId,id));
  }

  public record createComponentReq(String catalogId) {
  }

  public record assignParentReq(ComponentId parentId){}
}
