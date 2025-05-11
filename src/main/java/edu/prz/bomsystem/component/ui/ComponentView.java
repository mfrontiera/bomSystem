package edu.prz.bomsystem.component.ui;

import com.vaadin.flow.component.grid.Grid;
import edu.prz.bomsystem.component.application.ComponentService;
import edu.prz.bomsystem.component.domain.Component;
import edu.prz.bomsystem.component.domain.ComponentRepository;
import edu.prz.bomsystem.foundation.domain.GridView;

public class ComponentView extends GridView<Component> {

  private final ComponentRepository componentRepository;
  private final ComponentService componentService;

  public ComponentView(ComponentRepository componentRepository, ComponentService componentService) {
    super(new Grid<>(Component.class, false),componentRepository.findAll());
    this.componentRepository = componentRepository;
    this.componentService = componentService;
  }


  @Override
  protected void setGridColumns() {
    grid.addColumn(Component::getCatalogId).setHeader("Catalog ID");
    grid.addColumn(Component::getName).setHeader("Name");
  }
}
