package edu.prz.bomsystem.module.ui;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import edu.prz.bomsystem.foundation.ui.view.BasicGrid;
import edu.prz.bomsystem.module.domain.Module;

public class ModuleBasicGrid extends BasicGrid<edu.prz.bomsystem.module.domain.Module> {

  public ModuleBasicGrid(Binder<edu.prz.bomsystem.module.domain.Module> gridBinder,
      ListDataProvider<edu.prz.bomsystem.module.domain.Module> dataProvider) {
    super(gridBinder, dataProvider);
  }

  protected void setupGridColumns(){
    addColumn(Module::getName).setHeader("name");
  }
}
