package edu.prz.bomsystem.products.components.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.Route;
import edu.prz.bomsystem.foundation.ui.view.DetailedGridView;
import edu.prz.bomsystem.products.components.domain.Component;
import edu.prz.bomsystem.products.components.domain.ComponentProperty;
import edu.prz.bomsystem.products.components.domain.ComponentRepository;
import jakarta.annotation.security.PermitAll;
import java.util.List;

@PermitAll
@Route("components_detail")
public class ComponentDetailedListView extends DetailedGridView<Component, ComponentProperty> {

  private final ComponentRepository componentRepository;

  public ComponentDetailedListView(ComponentRepository componentRepository) {
    super(new Grid<>(Component.class,false),componentRepository.findAll(),new Button());
    this.componentRepository = componentRepository;
  }

  @Override
  protected Button createAddMoreButton() {
    return new Button();
  }

  @Override
  protected void setupMainGridColumns(Grid<Component> grid) {
    grid.addColumn(Component::getComponentPicture);
    grid.addColumn(Component::getName).setHeader("name");
    grid.addColumn(Component::getPartNumber).setHeader(i18n("number"));
  }

  @Override
  protected com.vaadin.flow.component.Component createDetailComponent(Component mainItem) {
    Grid<ComponentProperty> componentPropertyGrid = new Grid<>(ComponentProperty.class,false);
    componentPropertyGrid.addColumn(ComponentProperty::getDescription).setHeader(i18n("description"));
    componentPropertyGrid.addColumn(ComponentProperty::getBuyLink).setHeader(i18n("buyLink"));
    return componentPropertyGrid;
  }

  @Override
  protected com.vaadin.flow.component.Component createDeleteButton() {
    deleteButton.addClickListener(buttonClickEvent -> {
      List<Component> selectedAssemblyList = mainGrid.getSelectedItems().stream().toList();
      componentRepository.deleteAll(selectedAssemblyList);
      mainGrid.setItems(componentRepository.findAll());
    });
    return deleteButton;}
}
