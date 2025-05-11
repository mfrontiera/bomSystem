package edu.prz.bomsystem.products.components.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.EditorSaveEvent;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import edu.prz.bomsystem.foundation.ui.view.GridView;
import edu.prz.bomsystem.products.components.application.ComponentApplicationService;
import edu.prz.bomsystem.products.components.domain.Component;
import edu.prz.bomsystem.products.components.domain.ComponentPropertyRepository;
import edu.prz.bomsystem.products.components.domain.ComponentRepository;
import jakarta.annotation.security.PermitAll;
import java.util.List;
import java.util.Set;

@PermitAll
@Route("components")
public class ComponentExpandableGrid extends GridView<Component> {

  private final ComponentPropertyRepository componentPropertyRepository;
  private final ComponentApplicationService componentApplicationService;
  private final ComponentRepository componentRepository;

  public ComponentExpandableGrid(ComponentRepository componentRepository,
      ComponentPropertyRepository componentPropertyRepository,
      ComponentApplicationService componentApplicationService) {

    super(new Grid<>(Component.class, false), new Binder<>(Component.class),
        new ListDataProvider<>(componentRepository.findAll()), true);

    this.componentPropertyRepository = componentPropertyRepository;
    this.componentApplicationService = componentApplicationService;
    this.componentRepository = componentRepository;
  }

  @Override
  protected List<Component> getUpdatedItems() {
    return componentRepository.findAll();
  }

  @Override
  protected void createSaveAction(EditorSaveEvent<Component> event) {
    componentRepository.save(event.getItem());
  }

  @Override
  protected void setGridColumns() {
    Grid.Column<Component> nameColumn = grid.addColumn(Component::getName).setHeader(i18n("name"));
    TextField nameField = new TextField();
    binder.forField(nameField).bind(Component::getName, Component::setName);
    nameColumn.setEditorComponent(nameField);

    grid.addColumn(Component::getPartNumber).setHeader(i18n("partNumber"));
    grid.setItemDetailsRenderer(createDetailsRenderer());
  }


  @Override
  protected void createDeleteButtonAction(Set<Component> selectedItems) {
    selectedItems.forEach(
        component -> componentApplicationService.removeComponent(component.getIdentity()));
  }

  @Override
  protected void createAddNewButtonAction() {
    ComponentDialog componentDialog = new ComponentDialog(componentRepository);
    componentDialog.addDetachListener(detachEvent -> {
      dataProvider = new ListDataProvider<>(componentRepository.findAll());
      refreshData();
    });
    componentDialog.open();
  }

  private ComponentRenderer<com.vaadin.flow.component.Component, Component> createDetailsRenderer() {
    return new ComponentRenderer<>(component -> new ComponentPropertyGrid(
        new ListDataProvider<>(componentPropertyRepository.findComponentPropertiesByComponent(
            component)), componentPropertyRepository, component));
  }


}
