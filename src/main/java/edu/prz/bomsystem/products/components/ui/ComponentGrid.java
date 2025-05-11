package edu.prz.bomsystem.products.components.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.EditorSaveEvent;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import edu.prz.bomsystem.foundation.ui.view.GridView;
import edu.prz.bomsystem.products.assemblies.domain.Assembly;
import edu.prz.bomsystem.products.components.domain.Component;
import edu.prz.bomsystem.products.components.domain.ComponentRepository;
import java.util.List;
import java.util.Set;

public class ComponentGrid extends GridView<edu.prz.bomsystem.products.components.domain.Component> {

  private final ComponentRepository componentRepository;;
  private final Assembly parentAssembly;
  public ComponentGrid(ComponentRepository componentRepository,Assembly parentAssembly) {
    super(new Grid<>(edu.prz.bomsystem.products.components.domain.Component.class), new Binder<>(edu.prz.bomsystem.products.components.domain.Component.class), new ListDataProvider<>(componentRepository.findAll()));
    this.componentRepository = componentRepository;
    this.parentAssembly = parentAssembly;
  }

  @Override
  protected List<edu.prz.bomsystem.products.components.domain.Component> getUpdatedItems() {
    return List.of();
  }

  @Override
  protected void createSaveAction(
      EditorSaveEvent<edu.prz.bomsystem.products.components.domain.Component> event) {

  }

  @Override
  protected void setGridColumns() {

  }

  @Override
  protected void createDeleteButtonAction(Set<Component> selectedItems) {

  }

  @Override
  protected void createAddNewButtonAction() {

  }
}
