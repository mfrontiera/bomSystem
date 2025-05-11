package edu.prz.bomsystem.products.components.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.EditorSaveEvent;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import edu.prz.bomsystem.foundation.ui.view.GridView;
import edu.prz.bomsystem.products.components.domain.Component;
import edu.prz.bomsystem.products.components.domain.ComponentProperty;
import edu.prz.bomsystem.products.components.domain.ComponentPropertyRepository;
import java.util.List;
import java.util.Set;

public class ComponentPropertyGrid extends GridView<ComponentProperty> {

  private final ComponentPropertyRepository componentPropertyRepository;
  private final Component parentComponent;

  protected ComponentPropertyGrid(ListDataProvider<ComponentProperty> dataProvider,
      ComponentPropertyRepository componentPropertyRepository, Component parentComponent) {

    super(new Grid<>(ComponentProperty.class, false), new Binder<>(ComponentProperty.class),
        dataProvider);

    editGridTheme();

    this.componentPropertyRepository = componentPropertyRepository;
    this.parentComponent = parentComponent;
  }

  @Override
  protected List<ComponentProperty> getUpdatedItems() {
    return componentPropertyRepository.findComponentPropertiesByComponent(parentComponent);
  }

  @Override
  protected void createSaveAction(EditorSaveEvent<ComponentProperty> event) {
    componentPropertyRepository.save(event.getItem());
  }

  @Override
  protected void setGridColumns() {
    grid.addColumn(ComponentProperty::getBuyLink).setHeader("buy link");
    grid.addColumn(ComponentProperty::getDescription).setHeader("description");
  }

  @Override
  protected void createDeleteButtonAction(Set<ComponentProperty> selectedItems) {
    componentPropertyRepository.deleteAll(selectedItems);
    refreshData();
  }

  @Override
  protected void createAddNewButtonAction() {
    ComponentPropertyDialog componentDialog = new ComponentPropertyDialog(componentPropertyRepository, parentComponent);
    componentDialog.addDetachListener(detachEvent -> refreshData());
    componentDialog.open();
  }

  private void editGridTheme(){
    grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT,GridVariant.LUMO_NO_BORDER);
  }

}
