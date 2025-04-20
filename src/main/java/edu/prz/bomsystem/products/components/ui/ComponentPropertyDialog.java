package edu.prz.bomsystem.products.components.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import edu.prz.bomsystem.foundation.ui.view.NewDataDialog;
import edu.prz.bomsystem.products.components.domain.ComponentProperty;
import edu.prz.bomsystem.products.components.domain.ComponentPropertyRepository;

public class ComponentPropertyDialog extends NewDataDialog<ComponentProperty> {

  private final ComponentPropertyRepository componentPropertyRepository;
  private final Binder<ComponentProperty> binder;
  public ComponentPropertyDialog(Binder<ComponentProperty> binder,
      Class<ComponentProperty> clazz, ComponentPropertyRepository componentPropertyRepository) {
    super(binder, clazz);

    this.binder = binder;
    this.componentPropertyRepository = componentPropertyRepository;
  }

  @Override
  protected void createSaveAction(ComponentProperty item) {
    componentPropertyRepository.save(item);
  }

  @Override
  protected com.vaadin.flow.component.Component createDialogFields() {
    VerticalLayout dialogLayout = new VerticalLayout();

    TextField buyLink = new TextField(i18n("buyLink"));
    binder.forField(buyLink).bind(ComponentProperty::getBuyLink,ComponentProperty::setBuyLink);

    dialogLayout.add(buyLink);
    return dialogLayout;
  }
}
