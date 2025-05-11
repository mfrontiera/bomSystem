package edu.prz.bomsystem.foundation.ui.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import java.util.List;
import java.util.function.Supplier;
import lombok.Getter;

public abstract class BasicGridView<T> extends BaseView {

  @Getter
  protected final Grid<T> grid;
  private final Supplier<List<T>> dataProvider;

  protected BasicGridView(Grid<T> grid, Supplier<List<T>> dataProvider) {
    this.grid = grid;
    this.dataProvider = dataProvider;

    setupGridLayout();
    add(grid);
  }

  private void setupGridLayout(){
    grid.setItems(dataProvider.get());
    grid.setSelectionMode(SelectionMode.MULTI);
    grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    grid.setHeightFull();
    grid.setAllRowsVisible(true);
  }


}
