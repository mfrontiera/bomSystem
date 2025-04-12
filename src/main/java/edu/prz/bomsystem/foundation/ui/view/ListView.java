package edu.prz.bomsystem.foundation.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import java.util.List;
import java.util.function.Supplier;

public abstract class ListView<T> extends BaseView {

  protected final Grid<T> grid;
  protected final Editor<T> editor;
  protected final Binder<T> binder;
  private final Supplier<List<T>> dataProvider;


  protected ListView(Grid<T> grid, Binder<T> binder, Supplier<List<T>> dataProvider) {
    this.grid = grid;
    this.binder = binder;
    this.dataProvider = dataProvider;

    editor = grid.getEditor();
    editor.setBinder(binder);
    editor.setBuffered(true);

    setupLayout();
    setupGrid();
    refreshData();
  }

  protected void setupLayout() {
    VerticalLayout mainLayout = new VerticalLayout();
    mainLayout.addClassNames("list-view");
    mainLayout.add(grid);
    mainLayout.setSizeFull();
    mainLayout.setPadding(false);
    mainLayout.setSpacing(false);
    mainLayout.setAlignItems(Alignment.CENTER);
    add(mainLayout);
  }

  private void setupGrid() {
    configureGrid();
    addEditColumn();
  }
  
  protected abstract void configureGrid();

  protected void refreshData() {
    grid.setItems(dataProvider.get());

  }

  protected void addEditColumn() {
    grid.addComponentColumn(item -> {
      Button editButton = new Button(i18n("editButton"));

      editButton.addClickListener(click -> {
        if (editor.isOpen()) {
          editor.cancel();
        }
        editor.editItem(item);
      });

      return editButton;
    }).setEditorComponent(createEditActions());
  }

  protected HorizontalLayout createEditActions() {
    Button saveButton = new Button(i18n("saveButton"));
    Button cancelButton = new Button(i18n("cancelButton"));
    cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON,
        ButtonVariant.LUMO_ERROR);

    saveButton.addClickListener(click -> {
      T updatedItem = editor.getItem();
      try {
        saveToDatabase(updatedItem);
      } catch (ValidationException e) {
        throw new RuntimeException(e);
      }
      refreshData();

    });

    cancelButton.addClickListener(click -> {
      editor.cancel();
    });

    return new HorizontalLayout(saveButton, cancelButton);
  }

  protected void saveToDatabase(T item) throws ValidationException {
    binder.writeBean(item);
    editor.save();
    editor.closeEditor();
  }


}
