package edu.prz.bomsystem.component.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import edu.prz.bomsystem.component.application.ComponentService;
import edu.prz.bomsystem.component.domain.Component;
import edu.prz.bomsystem.component.domain.ComponentRepository;
import edu.prz.bomsystem.foundation.ui.view.BasicGridView;
import jakarta.annotation.security.PermitAll;
import org.apache.commons.math3.random.RandomDataGenerator;

@PermitAll
@Route("components")
public class ComponentView extends BasicGridView<Component> {

  private final ComponentRepository componentRepository;
  private final ComponentService componentService;


  public ComponentView(ComponentRepository componentRepository, ComponentService componentService) {
    super(new Grid<>(Component.class, false), componentRepository.findAll(),
        new Binder<>(Component.class));

    this.componentRepository = componentRepository;
    this.componentService = componentService;

    setupLayout();
    setGridColumns();
    addEditActionColumn(componentRepository::save);
  }

  private void setupLayout() {
    addNewButton(
        unused -> componentService.createComponent(
            "DD" + new RandomDataGenerator().nextLong(0, 10000))
    );

    addDeleteRecordsButton(recordsToDelete -> {
      componentRepository.deleteAll();
      return null;
    });

    configureSearch(component -> {
      String term = searchField.getValue().trim().toLowerCase();
      return matches(component.getName(), term) || matches(component.getCatalogId(), term)
          || matches(component.getType(), term);
    });
  }

  @Override
  protected void setGridColumns() {
    grid.addColumn(Component::getCatalogId).setHeader(i18n("catalogId"));

    TextField nameField = new TextField();
    nameField.setWidthFull();
    binder.forField(nameField).bind(Component::getName, Component::setName);
    grid.addColumn(Component::getName).setHeader(i18n("name")).setAutoWidth(true)
        .setEditorComponent(nameField).setSortable(true);;

    TextArea descriptionField = new TextArea();
    descriptionField.setWidthFull();
    descriptionField.setMaxLength(150);
    descriptionField.setValueChangeMode(ValueChangeMode.EAGER);
    descriptionField.addValueChangeListener(e -> {
      e.getSource()
          .setHelperText(e.getValue().length() + "/" + 150);
    });
    binder.forField(descriptionField).bind(Component::getDescription, Component::setDescription);
    grid.addColumn(Component::getDescription).setHeader(i18n("description")).setAutoWidth(true)
        .setEditorComponent(descriptionField).setSortable(true);;

    TextField typeField = new TextField();
    typeField.setWidthFull();
    binder.forField(typeField)
        .bind(Component::getType, Component::setType);
    grid.addColumn(Component::getType)
        .setHeader(i18n("type"))
        .setAutoWidth(true)
        .setEditorComponent(typeField).setSortable(true);;

    TextField unitMeasureField = new TextField();
    unitMeasureField.setWidthFull();
    binder.forField(unitMeasureField)
        .bind(Component::getUnitMeasureType, Component::setUnitMeasureType);
    grid.addColumn(Component::getUnitMeasureType)
        .setHeader(i18n("measure"))
        .setAutoWidth(true)
        .setEditorComponent(unitMeasureField).setSortable(true);;

    IntegerField costField = new IntegerField();
    costField.setWidthFull();
    binder.forField(costField)
        .bind(Component::getCostPerUnit, Component::setCostPerUnit);
    grid.addColumn(Component::getCostPerUnit)
        .setHeader(i18n("cost"))
        .setAutoWidth(true)
        .setEditorComponent(costField).setSortable(true);;

    IntegerField quantityField = new IntegerField();
    quantityField.setWidthFull();
    binder.forField(quantityField)
        .bind(Component::getStockQuantity, Component::setStockQuantity);
    grid.addColumn(Component::getStockQuantity)
        .setHeader(i18n("quantity"))
        .setAutoWidth(true)
        .setEditorComponent(quantityField).setSortable(true);;
  }


  private boolean matches(String field, String term) {
    return field != null && field.toLowerCase().contains(term);
  }
}

