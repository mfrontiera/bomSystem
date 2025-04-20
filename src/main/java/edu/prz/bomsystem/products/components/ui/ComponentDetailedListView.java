package edu.prz.bomsystem.products.components.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import edu.prz.bomsystem.foundation.ui.view.DetailedGridView;
import edu.prz.bomsystem.products.components.domain.Component;
import edu.prz.bomsystem.products.components.domain.ComponentDialog;
import edu.prz.bomsystem.products.components.domain.ComponentProperty;
import edu.prz.bomsystem.products.components.domain.ComponentPropertyRepository;
import edu.prz.bomsystem.products.components.domain.ComponentRepository;
import jakarta.annotation.security.PermitAll;
import java.util.List;

@PermitAll
@Route("components_detail")
public class ComponentDetailedListView extends DetailedGridView<Component, ComponentProperty> {

  private final ComponentRepository componentRepository;
  private final ComponentPropertyRepository componentPropertyRepository;
  private final Binder<ComponentProperty> componentPropertyBinder = new Binder<>(
      ComponentProperty.class);

  public ComponentDetailedListView(ComponentRepository componentRepository,
      ComponentPropertyRepository componentPropertyRepository) {
    super(new Grid<>(Component.class, false), new Binder<>(
        Component.class), componentRepository::findAll);

    this.componentRepository = componentRepository;

    this.componentPropertyRepository = componentPropertyRepository;
  }

  @Override
  protected void addMoreButtonAction() {
    ComponentPropertyDialog dialog = new ComponentPropertyDialog(componentPropertyBinder,
        ComponentProperty.class, componentPropertyRepository);
    dialog.open();
  }

  @Override
  protected void setupMainGridColumns(Grid<Component> grid) {
    TextField nameField = new TextField();
    binder.forField(nameField).bind(Component::getName, Component::setName);

    grid.addColumn(Component::getComponentPicture).setHeader(new Button("ADD COMPONENT", e -> {
      ComponentDialog dialog = new ComponentDialog(componentRepository);
      dialog.addDetachListener(detachEvent -> {
        refreshData();
      });
      dialog.open();
    }));
    grid.addColumn(Component::getName).setEditorComponent(nameField).setHeader(i18n("name"));
    grid.addColumn(Component::getPartNumber).setHeader(i18n("number"));
  }

  @Override
  protected List<ComponentProperty> getDetailItems(Component mainItem) {
    return mainItem.getComponentProperties();
  }

  @Override
  protected void setupDetailGrindColumns(Grid<ComponentProperty> grid) {
    grid.addColumn(ComponentProperty::getDescription)
        .setHeader(i18n("description"));
    grid.addColumn(ComponentProperty::getBuyLink).setHeader(i18n("buyLink"));
  }

  @Override
  protected void deleteButtonAction() {
    List<Component> selectedAssemblyList = masterGrid.getSelectedItems().stream().toList();
    componentRepository.deleteAll(selectedAssemblyList);
  }

  @Override
  protected void saveButtonAction(Component item) {
    componentRepository.save(item);
  }

}
