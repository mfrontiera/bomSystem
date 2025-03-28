package edu.prz.bomsystem.foundation.ui.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.List;
import java.util.function.Supplier;

public abstract class ListView<T> extends BaseView {

  protected final Grid<T> grid;
  private final Supplier<List<T>> dataProvider;

  protected ListView(Grid<T> grid, Supplier<List<T>> dataProvider) {
    this.grid = grid;
    this.dataProvider = dataProvider;

    setupLayout();
    setupGrid();
    refreshData();
  }

  protected void setupLayout(){
    VerticalLayout mainLayout = new VerticalLayout();
    mainLayout.addClassNames("list-view");
    mainLayout.add(grid);
    mainLayout.setSizeFull();
    mainLayout.setPadding(false);
    mainLayout.setSpacing(false);
    mainLayout.setAlignItems(Alignment.CENTER);
    add(mainLayout);
  }
  private void setupGrid(){
    configureGrid();
  }

  protected abstract void configureGrid();

  protected void refreshData(){
    grid.setItems(dataProvider.get());
  }
}
