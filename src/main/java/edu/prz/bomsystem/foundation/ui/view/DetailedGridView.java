package edu.prz.bomsystem.foundation.ui.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import java.util.List;

public abstract class DetailedGridView<T, D> extends BaseView {

  protected final Grid<T> mainGrid;
  protected final List<T> mainItemList;
  protected final Button deleteButton;

  public DetailedGridView(Grid<T> mainGrid, List<T> mainItemList, Button deleteButton) {
    this.mainGrid = mainGrid;
    this.mainItemList = mainItemList;
    this.deleteButton = deleteButton;

    deleteButton.setEnabled(false);
    deleteButton.setDisableOnClick(true);
    deleteButton.setText(i18n("deleteButton"));
    setupMainGrid();
    add(mainGrid,createAddMoreButton());
  }

  protected void setupMainGrid() {
    mainGrid.setItems(mainItemList);

    setupMainGridColumns(mainGrid);

    mainGrid.addColumn(createToggleDetailsRenderer(mainGrid)).setAutoWidth(true)
        .setFlexGrow(0).setFrozen(true).setHeader(createDeleteButton());
    mainGrid.setSelectionMode(Grid.SelectionMode.MULTI);
    mainGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    mainGrid.setHeightFull();
    mainGrid.setItemDetailsRenderer(new ComponentRenderer<>(this::createDetailComponent));
    mainGrid.setDetailsVisibleOnClick(true);
    mainGrid.setAllRowsVisible(true);

    mainGrid.addSelectionListener(selectionEvent -> {
      if(!mainGrid.getSelectedItems().isEmpty()){
        deleteButton.setEnabled(true);
      }
    });
  }
  protected abstract Button createAddMoreButton();

  protected abstract void setupMainGridColumns(Grid<T> grid);

  protected abstract Component createDetailComponent(T mainItem);

  protected abstract Component createDeleteButton();

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
