package edu.prz.bomsystem.foundation.ui.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import java.util.List;

public abstract class DetailedGridView<T, D> extends BaseView {

  protected final Grid<T> mainGrid;

  @SuppressWarnings("unchecked")
  public DetailedGridView(List<T> items) {
    this.mainGrid = new Grid<>((Class<T>) items.getFirst().getClass(), false);
    this.mainGrid.setItems(items);

    setupMainGrid();
    add(mainGrid);
  }

  protected void setupMainGrid() {
    setupMainGridColumns(mainGrid);

    mainGrid.addColumn(createToggleDetailsRenderer(mainGrid)).setWidth("80px")
        .setFlexGrow(0).setFrozen(true).setHeader(createDeleteButton());
    mainGrid.setSelectionMode(Grid.SelectionMode.MULTI);
    mainGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    mainGrid.setHeightFull();
    mainGrid.setItemDetailsRenderer(new ComponentRenderer<>(this::createDetailComponent));
    mainGrid.setDetailsVisibleOnClick(true);
    mainGrid.setAllRowsVisible(true);
  }

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
