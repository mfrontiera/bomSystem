package edu.prz.bomsystem.products.assemblies.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.EditorSaveEvent;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import edu.prz.bomsystem.foundation.ui.view.GridView;
import edu.prz.bomsystem.products.assemblies.domain.Assembly;
import edu.prz.bomsystem.products.assemblies.domain.AssemblyRepository;
import edu.prz.bomsystem.products.components.domain.ComponentRepository;
import edu.prz.bomsystem.products.components.ui.ComponentGrid;
import java.util.List;
import java.util.Set;

public class AssemblyExpandableGrid extends GridView<Assembly> {

  private final AssemblyRepository assemblyRepository;
  private final ComponentRepository componentRepository;

  public AssemblyExpandableGrid(AssemblyRepository assemblyRepository,
      ComponentRepository componentRepository) {
    super(new Grid<>(Assembly.class), new Binder<>(Assembly.class),
        new ListDataProvider<>(assemblyRepository.findAll()), true);
    this.assemblyRepository = assemblyRepository;
    this.componentRepository = componentRepository;
  }


  @Override
  protected List<Assembly> getUpdatedItems() {
    return assemblyRepository.findAll();
  }

  @Override
  protected void createSaveAction(EditorSaveEvent<Assembly> event) {
    assemblyRepository.save(event.getItem());
  }

  @Override
  protected void setGridColumns() {
    grid.addColumn(Assembly::getName).setHeader("name");
  }

  @Override
  protected void createDeleteButtonAction(Set<Assembly> selectedItems) {
    assemblyRepository.deleteAll(selectedItems);
  }

  @Override
  protected void createAddNewButtonAction() {

  }

  private ComponentRenderer<com.vaadin.flow.component.Component, Assembly> createDetailsRenderer() {
    return new ComponentRenderer<>(assembly -> new ComponentGrid(componentRepository,assembly));
  }
}
