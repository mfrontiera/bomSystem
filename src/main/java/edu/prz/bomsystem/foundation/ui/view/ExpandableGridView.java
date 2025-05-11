package edu.prz.bomsystem.foundation.ui.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import java.util.List;
import java.util.function.Supplier;

public abstract class ExpandableGridView<T> extends BaseView {

  protected final Grid<T> grid;
  private final Supplier<List<T>> dataProvider;

  public ExpandableGridView(Grid<T> grid, Supplier<List<T>> dataProvider) {
    this.grid = grid;
    this.dataProvider = dataProvider;

    setupGridLayout();
    add(grid);
  }

  private void setupGridLayout(){
    grid.setItems(dataProvider.get());
    grid.setSelectionMode(SelectionMode.MULTI);
    grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    grid.setAllRowsVisible(true);
    grid.setDetailsVisibleOnClick(true);
    grid.setHeightFull();
    grid.addColumn(createToggleDetailsRenderer(grid)).setFlexGrow(0).setFrozen(true);
    setGridColumns();

  }

  protected abstract void setGridColumns();

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
