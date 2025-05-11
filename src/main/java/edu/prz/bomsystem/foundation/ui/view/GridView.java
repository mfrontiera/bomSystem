package edu.prz.bomsystem.foundation.ui.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.grid.editor.EditorSaveEvent;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import java.util.List;
import java.util.Set;

public abstract class GridView<T> extends BaseView {

  protected final Grid<T> grid;
  private Editor<T> editor;
  protected final Binder<T> binder;
  protected ListDataProvider<T> dataProvider;

  protected GridView(Grid<T> grid, Binder<T> binder,
      ListDataProvider<T> dataProvider) {
    this(grid, binder, dataProvider, false);
  }

  protected GridView(Grid<T> grid,
      Binder<T> binder, ListDataProvider<T> dataProvider, boolean createDetails) {
    this.grid = grid;
    this.binder = binder;
    this.dataProvider = dataProvider;

    setupEditor();
    setupGridLayout();

    if (createDetails) {
      setupDetailsRenderer();
    }

    setGridColumns();
    setupEditColumn();

    add(buttonLayout(), grid);
  }

  private void setupGridLayout() {
    grid.setDataProvider(dataProvider);
    grid.setItems(dataProvider.getItems());
    grid.setSelectionMode(SelectionMode.MULTI);
    grid.setAllRowsVisible(true);

  }

  private void setupDetailsRenderer() {
    grid.setDetailsVisibleOnClick(true);
    grid.setHeightFull();
    grid.addColumn(createToggleDetailsRenderer(grid)).setFlexGrow(0).setFrozen(true);
  }

  private void setupEditColumn() {
    Grid.Column<T> editColumn =
        grid.addComponentColumn(item -> {
              Button editButton = new Button(i18n(GridView.class, "editButton"));
              editButton.addClickListener(e -> {
                if (editor.isOpen()) {
                  editor.cancel();
                }
                grid.getEditor().editItem(item);
              });
              return editButton;
            })
            .setWidth("150px")
            .setFlexGrow(0);

    Button saveButton = new Button(i18n(GridView.class, "saveButton"), e -> editor.save());
    Button cancelButton = new Button(VaadinIcon.CLOSE.create(),
        e -> editor.cancel());
    cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON,
        ButtonVariant.LUMO_ERROR);
    HorizontalLayout actions = new HorizontalLayout(saveButton,
        cancelButton);
    actions.setPadding(false);
    editColumn.setEditorComponent(actions);
  }

  private Component buttonLayout() {
    HorizontalLayout buttonLayout = new HorizontalLayout();

    Button deleteButton = new Button(i18n(GridView.class, "deleteButton"),
        new Icon(VaadinIcon.CLOSE_SMALL));
    deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
        ButtonVariant.LUMO_ERROR);
    deleteButton.addClickListener(c -> {
      createDeleteButtonAction(grid.getSelectedItems());
      refreshData();
    });

    Button addNewButton = new Button(i18n(GridView.class, "addNewButton"),
        new Icon(VaadinIcon.PLUS));
    addNewButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
        ButtonVariant.LUMO_SUCCESS);
    addNewButton.addClickListener(c -> {
      createAddNewButtonAction();
    });
    buttonLayout.add(addNewButton, deleteButton);

    return buttonLayout;
  }

  private void setupEditor() {
    this.editor = grid.getEditor();
    editor.setBinder(binder);
    editor.setBuffered(true);
    editor.addSaveListener(event -> {
      createSaveAction(event);
      refreshData();
    });
  }


  protected void refreshData() {
    List<T> updatedItems = getUpdatedItems();
    dataProvider.getItems().clear();
    dataProvider.getItems().addAll(updatedItems);
    dataProvider.refreshAll();

    grid.setItems(dataProvider.getItems());
  }

  protected abstract List<T> getUpdatedItems();

  protected abstract void createSaveAction(EditorSaveEvent<T> event);

  protected abstract void setGridColumns();

  protected abstract void createDeleteButtonAction(Set<T> selectedItems);

  protected abstract void createAddNewButtonAction();

  private Renderer<T> createToggleDetailsRenderer(
      Grid<T> grid) {

    return LitRenderer
        .<T>of("""
                <vaadin-button
                    theme="tertiary icon"
                    aria-label="Toggle details"
                    aria-expanded="${model.detailsOpened ? 'true' : 'false'}"
                    @click="${handleClick}"
                >
                    <vaadin-icon
                    .icon="${model.detailsOpened ? 'lumo:angle-down' : 'lumo:angle-right'}"
                    ></vaadin-icon>
                </vaadin-button>
            """)
        .withFunction("handleClick",
            details -> grid.setDetailsVisible(details,
                !grid.isDetailsVisible(details)));
  }


}
