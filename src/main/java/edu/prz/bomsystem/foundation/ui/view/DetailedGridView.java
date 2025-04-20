package edu.prz.bomsystem.foundation.ui.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import java.util.List;
import java.util.function.Supplier;

public abstract class DetailedGridView<T, D> extends BaseView {

  protected final Grid<T> masterGrid;
  protected final Editor<T> editor;
  protected final Binder<T> binder;
  protected final Supplier<List<T>> masterDataProvider;

  public DetailedGridView(Grid<T> masterGrid,
      Binder<T> binder, Supplier<List<T>> masterDataProvider) {

    this.masterGrid = masterGrid;
    this.binder = binder;
    this.masterDataProvider = masterDataProvider;

    editor = masterGrid.getEditor();
    editor.setBinder(binder);
    editor.setBuffered(true);

    setupMasterGrid();
    add(masterGrid, createAddMoreButton());
  }

  protected void setupMasterGrid() {
    masterGrid.setItems(masterDataProvider.get());

    masterGrid.addColumn(createToggleDetailsRenderer(masterGrid)).setAutoWidth(false)
        .setFlexGrow(0);

    setupMainGridColumns(masterGrid);

    createAndAddActionsColumn();

    masterGrid.setSelectionMode(Grid.SelectionMode.MULTI);
    masterGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    masterGrid.setHeightFull();
    masterGrid.setItemDetailsRenderer(new ComponentRenderer<>(this::createDetailComponent));
    masterGrid.setDetailsVisibleOnClick(true);
    masterGrid.setAllRowsVisible(true);

  }

  protected Component createDetailComponent(T mainItem) {
    Grid<D> detailGrid = new Grid<>();
    List<D> detailItems = getDetailItems(mainItem);
    detailGrid.setItems(detailItems);
    setupDetailGrindColumns(detailGrid);
    return detailGrid;
  }

  protected abstract void setupDetailGrindColumns(Grid<D> grid);


  protected void createAndAddActionsColumn() {
    HorizontalLayout buttonColumnLayout = new HorizontalLayout();
    buttonColumnLayout.add(createSaveButton(), createCancelButton());

    masterGrid.addComponentColumn(this::createEditButton)
        .setEditorComponent(buttonColumnLayout)
        .setHeader(createDeleteButton());
  }

  protected Component createSaveButton() {
    Button saveButton = new Button(i18n("saveButton"));

    saveButton.addClickListener(buttonClickEvent -> {
      T updatedItem = editor.getItem();
      try {
        saveToDatabase(updatedItem);
        refreshData();
      } catch (ValidationException e) {
        throw new RuntimeException(e);
      }
    });

    return saveButton;
  }

  protected Component createCancelButton() {
    Button cancelButton = new Button(i18n("cancelButton"));

    cancelButton.addClickListener(buttonClickEvent -> {
      editor.cancel();
    });

    return cancelButton;
  }

  protected Component createEditButton(T item) {
    Button editButton = new Button(i18n("editButton"));
    editButton.addClickListener(click -> {
      if (editor.isOpen()) {
        editor.cancel();
      }
      editor.editItem(item);
    });
    return editButton;
  }

  protected Component createDeleteButton() {
    Button deleteButton = new Button(i18n("deleteButton"));
    deleteButton.setEnabled(false);
    deleteButton.setDisableOnClick(true);

    deleteButton.addClickListener(buttonClickEvent -> {
      deleteButtonAction();
      refreshData();
    });

    masterGrid.addSelectionListener(selectionEvent -> {
      if (!masterGrid.getSelectedItems().isEmpty()) {
        deleteButton.setEnabled(true);
      }
    });

    return deleteButton;
  }

  protected Component createAddMoreButton() {

    Button addMore = new Button(i18n("addMoreButton"));
    addMore.addClickListener(buttonClickEvent -> {
      addMoreButtonAction();
    });

    return addMore;
  }

  protected void saveToDatabase(T item) throws ValidationException {
    binder.writeBean(item);
    editor.save();
    editor.closeEditor();
    saveButtonAction(item);
  }

  protected void refreshData() {
    masterGrid.setItems(masterDataProvider.get());
  }

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

  protected abstract void saveButtonAction(T item);

  protected abstract void addMoreButtonAction();

  protected abstract void setupMainGridColumns(Grid<T> grid);

  protected abstract List<D> getDetailItems(T mainItem);

  protected abstract void deleteButtonAction();

}
