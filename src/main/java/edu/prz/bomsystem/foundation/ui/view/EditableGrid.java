package edu.prz.bomsystem.foundation.ui.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import edu.prz.bomsystem.foundation.ui.i18n.I18nAware;
import edu.prz.bomsystem.mbom.domain.Bon;
import edu.prz.bomsystem.module.domain.Module;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.Setter;

public abstract class EditableGrid<T> extends BasicGrid<T> implements I18nAware {

  protected final ListDataProvider<T> dataProvider;
  protected final Binder<T> binder;

  public EditableGrid(Binder<T> binder, ListDataProvider<T> dataProvider){
    super(binder,dataProvider);
    this.dataProvider = dataProvider;
    this.binder = binder;
  }

  public TextField createSearchField(List<Function<T, String>> fieldExtractors) {
    TextField searchField = new TextField();
    searchField.setWidth("50%");
    searchField.setPlaceholder("Search");
    searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
    searchField.setValueChangeMode(ValueChangeMode.EAGER);

    searchField.addValueChangeListener(e -> dataProvider.refreshAll());
    dataProvider.setFilter(item -> {
      String term = searchField.getValue().trim().toLowerCase();
      if (term.isEmpty()) return true;
      return fieldExtractors.stream()
          .map(f -> f.apply(item))
          .filter(Objects::nonNull)
          .anyMatch(s -> s.toLowerCase().contains(term));
    });

    return searchField;
  }

  public Button addNewButton(Function<Void, T> entitySupplier) {
    Button newEntityButton = new Button(new Icon(VaadinIcon.PLUS_CIRCLE));
    newEntityButton.addClickListener(event -> {
      T newItem = entitySupplier.apply(null);
      if (newItem != null) {
        dataProvider.getItems().add(newItem);
        dataProvider.refreshAll();
      }
    });
    return newEntityButton;
  }

  public Button addDeleteButton(Function<Set<T>, Void> deleteHandler) {
    Button removeEntityButton = new Button(new Icon(VaadinIcon.TRASH));
    removeEntityButton.addClickListener(event -> {
      confirmDeleteActionDialog(deleteHandler);
    });

    return removeEntityButton;
  }

  private void confirmDeleteActionDialog(Function<Set<T>, Void> deleteHandler) {
    Set<T> selectedRecords = getSelectedItems();
    Dialog confirmDialog = new Dialog();
    Text text = new Text(
        i18n(BasicGridView.class, "labelText1") +
            " " + selectedRecords.size() +
            " " + i18n(BasicGridView.class, "labelText2"));

    Button confirmButton = new Button(new Icon(VaadinIcon.CHECK));
    confirmButton.addClickListener(e -> {
      if (!selectedRecords.isEmpty()) {
        deleteHandler.apply(selectedRecords);
        dataProvider.getItems().removeAll(selectedRecords);
        dataProvider.refreshAll();
      }

      confirmDialog.close();
    });

    Button cancelButton = new Button(new Icon(VaadinIcon.CLOSE));
    cancelButton.addClickListener(e -> confirmDialog.close());

    confirmDialog.setHeaderTitle(i18n(BasicGridView.class, "dialogHeader"));
    confirmDialog.add(text);
    confirmDialog.getFooter().add(confirmButton, cancelButton);
    confirmDialog.open();
  }

  public void addEditActionColumn(Consumer<T> saveHandler) {
    Button saveButton = createSaveButton();
    Button cancelButton = createCancelButton(saveButton);
    HorizontalLayout actions = createActionsLayout(saveButton, cancelButton);

    registerBinderListener(saveButton);
    registerSaveListener(saveHandler, saveButton);
    addEditButtonColumn(saveButton, actions);
  }

// ---------------------- HELPER METHODS ----------------------

  private Button createSaveButton() {
    Button saveButton = new Button(new Icon(VaadinIcon.CHECK_SQUARE_O), e -> editor.save());
    saveButton.setEnabled(false);
    saveButton.setDisableOnClick(true);
    return saveButton;
  }

  private Button createCancelButton(Button saveButton) {
    Button cancelButton = new Button(new Icon(VaadinIcon.CLOSE), e -> {
      editor.cancel();
      saveButton.setEnabled(false);
    });
    cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
    return cancelButton;
  }

  private HorizontalLayout createActionsLayout(Button saveButton, Button cancelButton) {
    HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton);
    actions.setPadding(false);
    return actions;
  }

  private void registerBinderListener(Button saveButton) {
    binder.addValueChangeListener(ev -> saveButton.setEnabled(true));
  }

  private void registerSaveListener(Consumer<T> saveHandler, Button saveButton) {
    editor.addSaveListener(event -> {
      T item = event.getItem();
      saveHandler.accept(item);
      dataProvider.refreshAll();
      saveButton.setEnabled(false);
    });
  }

  private void addEditButtonColumn(Button saveButton, HorizontalLayout actions) {
    this.addComponentColumn(record -> {
      Button editButton = new Button(new Icon(VaadinIcon.EDIT));
      editButton.addClickListener(e -> {
        if (editor.isOpen()) editor.cancel();

        saveButton.setEnabled(false); // reset stanu
        editor.editItem(record);
        binder.readBean(record); // wczytaj dane do edytora
      });
      return editButton;
    }).setAutoWidth(true).setEditorComponent(actions).setFlexGrow(1);
  }

  public void addStatusColumnWorker(Function<T, String> statusExtractor) {
    addComponentColumn(item -> createStatusIcon(statusExtractor.apply(item)))
        .setTooltipGenerator(statusExtractor::apply)
        .setHeader(i18n("status"))
        .setAutoWidth(true).setFlexGrow(0);
  }

  public void addStatusColumn(Function<T, String> statusExtractor) {
    addComponentColumn(item -> {

      String currentStatus = statusExtractor.apply(item);

      Select<String> statusSelect = getStringSelect(currentStatus);

      statusSelect.addValueChangeListener(event -> {
        String newStatus = event.getValue();
        if (newStatus != null && statusUpdater != null) {
          statusUpdater.accept(item, newStatus);
          getDataProvider().refreshItem(item);
        }
      });

      return statusSelect;

    })
        .setTooltipGenerator(statusExtractor::apply)
        .setHeader(i18n("status"))
        .setAutoWidth(true)
        .setFlexGrow(1);
  }

  private Select<String> getStringSelect(String currentStatus) {
    Select<String> statusSelect = new Select<>();
    statusSelect.setItems("Available", "Pending", "Declined");

    statusSelect.setValue(currentStatus);
    statusSelect.setRenderer(new ComponentRenderer<>(status -> {
      Icon icon = createStatusIcon(status);
      Span label = new Span(status);
      label.getStyle().set("margin-left", "0.5em");

      HorizontalLayout wrapper = new HorizontalLayout(icon, label);
      wrapper.setAlignItems(FlexComponent.Alignment.CENTER);
      return wrapper;
    }));
    return statusSelect;
  }


  @Setter
  private BiConsumer<T, String> statusUpdater;

  private Icon createStatusIcon(String status) {
    Icon icon = switch (status) {
      case "Available" -> new Icon(VaadinIcon.CHECK);
      case "Declined" -> new Icon(VaadinIcon.CLOSE);
      case "Pending" -> new Icon(VaadinIcon.TIME_FORWARD);
      default -> new Icon(VaadinIcon.QUESTION_CIRCLE);
    };
    icon.setSize("1em");
    return icon;
  }


  public abstract com.vaadin.flow.component.Component getItemDetailsContent(Module module);

  public abstract com.vaadin.flow.component.Component getItemDetailsContent(Bon bon);
}
