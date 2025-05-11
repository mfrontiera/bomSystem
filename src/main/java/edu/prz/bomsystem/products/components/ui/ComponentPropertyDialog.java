package edu.prz.bomsystem.products.components.ui;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import edu.prz.bomsystem.foundation.ui.view.NewDataDialog;
import edu.prz.bomsystem.products.components.domain.Component;
import edu.prz.bomsystem.products.components.domain.ComponentProperty;
import edu.prz.bomsystem.products.components.domain.ComponentPropertyRepository;

public class ComponentPropertyDialog extends NewDataDialog<ComponentProperty> {

  private final ComponentPropertyRepository componentPropertyRepository;
  private final Component parentComponent;

  public ComponentPropertyDialog(ComponentPropertyRepository componentPropertyRepository,
      Component parentComponent) {
    super(new Binder<>(ComponentProperty.class), ComponentProperty.class, componentPropertyRepository::save);
    this.componentPropertyRepository = componentPropertyRepository;
    this.parentComponent = parentComponent;
  }

  @Override
  protected com.vaadin.flow.component.Component createDialogFields() {
    TextField nameField = new TextField(i18n("nameField"));
    getBinder()
        .forField(nameField)
        .asRequired(i18n("nameReq"))
        .bind(ComponentProperty::getDescription, ComponentProperty::setDescription);

    // Przed zapisem właściwości, przypisz ID komponentu
    nameField.addValueChangeListener(event -> {
      ComponentProperty componentProperty = getBinder().getBean();
      if (componentProperty != null) {
        componentProperty.setComponent(parentComponent);  // Przypisanie komponentu
      }
    });

    HorizontalLayout dialogLayout = new HorizontalLayout();
    dialogLayout.add(nameField);

    return dialogLayout;
  }
}
