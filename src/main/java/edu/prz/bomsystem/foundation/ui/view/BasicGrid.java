package edu.prz.bomsystem.foundation.ui.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;

public class BasicGrid<T> extends Grid<T> {

  protected final Binder<T> gridBinder;
  protected final Editor<T> editor;
  protected final ListDataProvider<T> dataProvider;

  public BasicGrid(Binder<T> gridBinder, ListDataProvider<T> dataProvider){

    this.gridBinder = gridBinder;
    this.dataProvider = dataProvider;
    this.editor = getEditor();

    editor.setBinder(gridBinder);
    editor.setBuffered(true);

    setGridLayout();
  }

  private void setGridLayout(){
    setDataProvider(dataProvider);
    setSelectionMode(SelectionMode.MULTI);
    setAllRowsVisible(true);
  }

  protected void refreshItems(){
    dataProvider.refreshAll();
  }

}
