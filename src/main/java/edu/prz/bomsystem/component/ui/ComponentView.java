package edu.prz.bomsystem.component.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.Route;
import edu.prz.bomsystem.component.application.ComponentAuditService;
import edu.prz.bomsystem.component.application.ComponentService;
import edu.prz.bomsystem.component.domain.Component;
import edu.prz.bomsystem.component.domain.ComponentRepository;
import edu.prz.bomsystem.foundation.ui.view.VersionedGridView;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route("components")
public class ComponentView extends VersionedGridView<Component> {

  private final ComponentRepository componentRepository;
  private final ComponentService componentService;
  private final ComponentAuditService componentAuditService;

  public ComponentView(ComponentRepository componentRepository, ComponentService componentService,
      ComponentAuditService componentAuditService) {
    super(new Grid<>(Component.class, false),componentRepository.findAll());
    this.componentRepository = componentRepository;
    this.componentService = componentService;
    this.componentAuditService = componentAuditService;

  }


  @Override
  protected void setGridColumns() {
    addVersionColumn(
        component -> componentAuditService.getComponentVersions(component.getIdentity()).size(),
        (component, revision) -> componentAuditService.getComponentFromVersion(component.getIdentity(), revision)
    );

    grid.addColumn(Component::getCatalogId).setHeader("Catalog ID");
    grid.addColumn(Component::getName).setHeader("Name");
    grid.addColumn(Component::getParent).setHeader("Parent");
  }

}
