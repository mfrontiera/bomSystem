package edu.prz.bomsystem.component.ui;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import edu.prz.bomsystem.component.domain.Component;
import edu.prz.bomsystem.foundation.ui.view.BasicGrid;

public class ComponentBasicGrid extends BasicGrid<Component> {


  public ComponentBasicGrid(Binder<Component> gridBinder,
      ListDataProvider<Component> dataProvider) {
    super(gridBinder, dataProvider);
  }

  protected void setupGridColumns(){
    addColumn(Component::getName).setHeader("name");
  }
}
