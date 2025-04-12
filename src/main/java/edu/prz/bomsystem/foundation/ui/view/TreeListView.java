/*
package edu.prz.bomsystem.foundation.ui.view;

import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;

public abstract class TreeListView<T> extends BaseView {

  protected final TreeGrid<T> treeGrid;
  protected final Editor<T> editor;
  protected final Binder<T> binder;
  protected final TreeData<T> treeData;

  protected TreeListView(TreeGrid<T> treeGrid, Editor<T> editor, Binder<T> binder,
      TreeData<T> treeData, Binder<T>){

    this.treeGrid = treeGrid;
    this.editor = editor;
    this.binder = binder;
    this.treeData = treeData;
  }

  protected void setupLayout(){}

  private void treeGridSetup() {
    treeGrid.addHierarchyColumn(this::getName)
        .setHeader("Nazwa");

    populateTreeData(treeData);

    TreeDataProvider<T> dataProvider = new TreeDataProvider<>(treeData);
    treeGrid.setDataProvider(dataProvider);

    add(treeGrid);
    setSizeFull();
  }

  // 🔹 Metoda do nadpisania: jak uzyskać nazwę elementu
  protected abstract String getName(T item);

  // 🔹 Metoda do nadpisania: jak zbudować strukturę drzewa
  protected abstract void populateTreeData(TreeData<T> treeData);

  protected void refreshData(){}

  protected void addEditColumn(){}

  protected HorizontalLayout createEditActions(){


  }
}
*/
