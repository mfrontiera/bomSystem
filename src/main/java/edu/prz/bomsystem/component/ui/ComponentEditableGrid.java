package edu.prz.bomsystem.component.ui;

import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import edu.prz.bomsystem.component.domain.Component;
import edu.prz.bomsystem.foundation.ui.i18n.I18nAware;
import edu.prz.bomsystem.foundation.ui.view.EditableGrid;
import edu.prz.bomsystem.module.domain.Module;

public class ComponentEditableGrid extends EditableGrid<Component> implements I18nAware {


  public ComponentEditableGrid(Binder<Component> binder,
      ListDataProvider<Component> dataProvider) {
    super(binder, dataProvider);

    setupLayout();
  }

  @Override
  public com.vaadin.flow.component.Component getItemDetailsContent(Module module) {
    return null;
  }

  private void setupLayout(){
    addColumn(Component::getCatalogId).setHeader(i18n("catalogId"));

    TextField nameField = new TextField();
    nameField.setWidthFull();
    binder.forField(nameField).bind(Component::getName, Component::setName);
    addColumn(Component::getName).setHeader(i18n("name")).setAutoWidth(true)
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
    addColumn(Component::getDescription).setHeader(i18n("description")).setAutoWidth(true)
        .setEditorComponent(descriptionField).setSortable(true);;

    TextField typeField = new TextField();
    typeField.setWidthFull();
    binder.forField(typeField)
        .bind(Component::getType, Component::setType);
    addColumn(Component::getType)
        .setHeader(i18n("type"))
        .setAutoWidth(true)
        .setEditorComponent(typeField).setSortable(true);;

    TextField unitMeasureField = new TextField();
    unitMeasureField.setWidthFull();
    binder.forField(unitMeasureField)
        .bind(Component::getUnitMeasureType, Component::setUnitMeasureType);
    addColumn(Component::getUnitMeasureType)
        .setHeader(i18n("measure"))
        .setAutoWidth(true)
        .setEditorComponent(unitMeasureField).setSortable(true);;

    IntegerField costField = new IntegerField();
    costField.setWidthFull();
    binder.forField(costField)
        .bind(Component::getCostPerUnit, Component::setCostPerUnit);
    addColumn(Component::getCostPerUnit)
        .setHeader(i18n("cost"))
        .setAutoWidth(true)
        .setEditorComponent(costField).setSortable(true);;

    IntegerField quantityField = new IntegerField();
    quantityField.setWidthFull();
    binder.forField(quantityField)
        .bind(Component::getStockQuantity, Component::setStockQuantity);
    addColumn(Component::getStockQuantity)
        .setHeader(i18n("quantity"))
        .setAutoWidth(true)
        .setEditorComponent(quantityField).setSortable(true);;
  }
}
