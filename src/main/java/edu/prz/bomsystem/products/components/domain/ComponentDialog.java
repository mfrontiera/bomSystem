package edu.prz.bomsystem.products.components.domain;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import edu.prz.bomsystem.foundation.ui.view.NewDataDialog;

public class ComponentDialog extends NewDataDialog<Component> {

  private final ComponentRepository componentRepository;

  public ComponentDialog(ComponentRepository componentRepository) {
    super(new Binder<>(Component.class), Component.class);
    this.componentRepository = componentRepository;
  }

  @Override
  protected void createSaveAction(Component item) {
    componentRepository.save(item);

  }

  @Override
  protected com.vaadin.flow.component.Component createDialogFields() {
    TextField nameField = new TextField(i18n("nameField"));
    getBinder()
        .forField(nameField)
        .asRequired(i18n("nameReq"))
        .bind(Component::getName, Component::setName);

    TextField idField = new TextField(i18n("idField"));
    getBinder()
        .forField(idField)
        .bind(Component::getPartNumber, Component::setPartNumber);

    HorizontalLayout dialogLayout = new HorizontalLayout();
    dialogLayout.add(nameField, idField);

    return dialogLayout;
  }
}
