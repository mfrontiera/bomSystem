package edu.prz.bomsystem.foundation.domain;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import edu.prz.bomsystem.foundation.ui.i18n.I18nAware;
import edu.prz.bomsystem.foundation.ui.view.BaseView;
import java.util.List;

public abstract class GridView<T> extends BaseView implements I18nAware {

  protected final Grid<T> grid;
  private final List<T> itemsList;

  protected GridView(Grid<T> grid, List<T> itemsList) {
    this.grid = grid;
    this.itemsList = itemsList;

    setupGridLayout();
    setGridColumns();
  }

  private void setupGridLayout() {
    grid.setItems(itemsList);
    grid.setSelectionMode(SelectionMode.MULTI);
    grid.setAllRowsVisible(true);
  }

  protected abstract void setGridColumns();

}
