package edu.prz.bomsystem.foundation.ui.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.data.binder.Binder;
import edu.prz.bomsystem.foundation.ui.i18n.I18nAware;
import lombok.Getter;


public abstract class NewDataDialog<T> extends Dialog implements I18nAware {

  private final Class<T> clazz;

  @Getter
  private final Binder<T> binder;

  public NewDataDialog(Binder<T> binder, Class<T> clazz) {
    this.binder = binder;
    this.clazz = clazz;
    dialogLayout();
  }

  private void dialogLayout() {
    add(createDialogFields());

    Button cancelButton = new Button(i18n("cancelButton"), e -> {
      close();
      binder.removeBean();
    });
    getFooter().add(createSaveButton(), cancelButton);

    Button closeButton = new Button(new Icon("lumo", "cross"),e -> {
      close();
      binder.removeBean();
    });
    closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    getHeader().add(closeButton, createSaveButton());
  }

  private Component createSaveButton() {
    Button saveButton = new Button(i18n("saveButton"));
    saveButton.addClickListener(event -> {
      T currentBean = binder.getBean();

      if (currentBean == null) {
        try {
          currentBean = clazz.getDeclaredConstructor().newInstance();
          binder.setBean(currentBean);
        } catch (Exception e) {
          throw new RuntimeException("Failed to create a new instance of " + clazz.getName(), e);
        }
      }

      if (binder.writeBeanIfValid(currentBean)) {
        createSaveAction(currentBean);
        close();
      }
    });

    return saveButton;
  }

  protected abstract void createSaveAction(T item);

  protected abstract Component createDialogFields();
}
