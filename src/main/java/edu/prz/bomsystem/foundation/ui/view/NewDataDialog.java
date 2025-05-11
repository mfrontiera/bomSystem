package edu.prz.bomsystem.foundation.ui.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import edu.prz.bomsystem.foundation.ui.i18n.I18nAware;
import java.util.function.Consumer;
import lombok.Getter;


public abstract class NewDataDialog<T> extends Dialog implements I18nAware {

  private final Class<T> clazz;

  @Getter
  private final Consumer<T> saveAction;

  @Getter
  private final Binder<T> binder;

  public NewDataDialog(Binder<T> binder, Class<T> clazz, Consumer<T> saveAction) {
    this.binder = binder;
    this.clazz = clazz;
    this.saveAction = saveAction;
    dialogLayout();
  }

  private void dialogLayout() {
    add(createDialogFields());
    addComponentsToHeader();
    addComponentsToFooter();
  }
  private void addComponentsToHeader(){
    Div layout = new Div();
    layout.addClassNames(Display.FLEX, AlignItems.CENTER, Padding.Horizontal.MEDIUM, JustifyContent.BETWEEN);
    layout.setWidthFull();

    H3 header = new H3(i18n("header"));
    header.addClassNames(Margin.Vertical.MEDIUM, Margin.End.AUTO);

   layout.add(header,createCloseButton());

    getHeader().add(layout);
  }

  private void addComponentsToFooter(){
    getFooter().add(createSaveButton(),createCancelButton());
  }

  private Component createCloseButton(){
    Button closeButton = new Button(new Icon("lumo", "cross"),e -> {
      close();
      binder.removeBean();
    });
    closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    return closeButton;
  }
  private Component createSaveButton() {
    Button saveButton = new Button(i18n(NewDataDialog.class,"saveButton"));
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
        saveAction.accept(currentBean);
        close();
      }
    });

    return saveButton;
  }

  private Component createCancelButton(){
    Button cancelButton = new Button(i18n(NewDataDialog.class,"cancelButton"), e -> {
      close();
      binder.removeBean();
    });

    return cancelButton;
  }

  protected abstract Component createDialogFields();
}
