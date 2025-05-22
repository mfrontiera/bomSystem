package edu.prz.bomsystem.foundation.ui.view;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class BasicGridView<T> extends BaseView {

  protected final Grid<T> grid;
  protected final Binder<T> binder;
  private final Editor<T> editor;
  protected final TextField searchField;
  private final ListDataProvider<T> dataProvider;
  private final List<T> items;
  private final Button newEntityButton = new Button(new Icon(VaadinIcon.PLUS_CIRCLE));
  private final Button removeEntityButton = new Button(new Icon(VaadinIcon.TRASH));

  public BasicGridView(Grid<T> grid, List<T> items, Binder<T> binder) {
    this.grid = grid;
    this.items = new ArrayList<>(items);
    this.binder = binder;
    this.editor = grid.getEditor();
    this.searchField = createSearchField();
    this.dataProvider = new ListDataProvider<>(this.items);

    editor.setBinder(binder);
    editor.setBuffered(true);


    setupGridLayout();
    setupRootLayout();
  }

  public void setItems(List<T> newItems) {
    items.clear();
    items.addAll(newItems);
    dataProvider.refreshAll();
  }

  private void setupRootLayout() {
    HorizontalLayout searchAndButtonsLayout = new HorizontalLayout();
    searchAndButtonsLayout.add(searchField, newEntityButton, removeEntityButton);

    add(searchAndButtonsLayout, grid);

  }

  private void setupGridLayout() {
    grid.setDataProvider(dataProvider);
    grid.setSelectionMode(SelectionMode.MULTI);
    grid.setAllRowsVisible(true);
  }

  protected abstract void setGridColumns();

  private TextField createSearchField() {
    TextField searchField = new TextField();
    searchField.setWidth("50%");
    searchField.setPlaceholder("Search");
    searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
    searchField.setValueChangeMode(ValueChangeMode.EAGER);
    return searchField;
  }

  protected void configureSearch(Predicate<T> filterPredicate) {
    searchField.addValueChangeListener(e -> dataProvider.refreshAll());
    dataProvider.setFilter(item -> {
      String term = searchField.getValue().trim().toLowerCase();
      if (term.isEmpty()) {
        return true;
      }
      return filterPredicate.test(item);
    });
  }

  protected void addNewButton(Function<Void, T> entitySupplier) {
    newEntityButton.addClickListener(event -> {
      T newItem = entitySupplier.apply(null);
      if (newItem != null) {
        items.add(newItem);
        dataProvider.refreshAll(); // teraz zadziała
      }
    });
  }

  protected void addDeleteRecordsButton(Function<Set<T>, Void> deleteHandler) {
    removeEntityButton.addClickListener(event -> {
      confirmDeleteActionDialog(deleteHandler);
    });
  }

  protected void confirmDeleteActionDialog(Function<Set<T>, Void> deleteHandler) {
    Set<T> selectedRecords = grid.getSelectedItems();
    Dialog confirmDialog = new Dialog();
    Text text = new Text(
        i18n(BasicGridView.class, "labelText1") +
            " " + selectedRecords.size() +
            " " + i18n(BasicGridView.class, "labelText2"));

    Button confirmButton = new Button(new Icon(VaadinIcon.CHECK));
    confirmButton.addClickListener(e -> {
      if (!selectedRecords.isEmpty()) {
        deleteHandler.apply(selectedRecords);
        items.removeAll(selectedRecords);
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


  protected void addStatusColumn(Function<T, String> statusExtractor) {
    grid.addComponentColumn(item -> createStatusIcon(statusExtractor.apply(item)))
        .setTooltipGenerator(statusExtractor::apply)
        .setHeader(i18n("status"))
        .setAutoWidth(true).setFlexGrow(0);
  }


  protected Icon createStatusIcon(String status) {
    return getIcon(status);
  }


  protected static Icon getIcon(String status) {
    return switch (status) {
      case "Available" -> new Icon(VaadinIcon.CHECK);
      case "Declined" -> new Icon(VaadinIcon.CLOSE);
      case "Pending" -> new Icon(VaadinIcon.TIME_FORWARD);
      default -> new Icon(VaadinIcon.QUESTION_CIRCLE);
    };
  }

  protected void addEditActionColumn(Consumer<T> saveHandler){
    Button saveButton = new Button(new Icon(VaadinIcon.CHECK_SQUARE_O), e -> {
      editor.save();
    });

    editor.addSaveListener(event -> {
      T item = event.getItem();
      saveHandler.accept(item);
      dataProvider.refreshAll();
    });

    Button cancelButton = new Button(VaadinIcon.CLOSE.create(),
        e -> editor.cancel());
    cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON,
        ButtonVariant.LUMO_ERROR);
    HorizontalLayout actions = new HorizontalLayout(saveButton,
        cancelButton);
    actions.setPadding(false);

    grid.addComponentColumn(record -> {
      Button editButton = new Button(new Icon(VaadinIcon.EDIT));
      editButton.addClickListener(e -> {
        if (editor.isOpen())
          editor.cancel();
        grid.getEditor().editItem(record);
      });
      return editButton;
    }).setAutoWidth(true).setEditorComponent(actions).setFlexGrow(1);
  }
}
