package edu.prz.bomsystem.products.components.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import edu.prz.bomsystem.foundation.ui.view.ListView;
import edu.prz.bomsystem.products.components.domain.Component;
import edu.prz.bomsystem.products.components.domain.ComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;

@AnonymousAllowed
@Route("components")
public class ComponentsListView extends ListView<Component> {

  @Autowired
  public ComponentsListView(ComponentRepository componentRepository){
    super(new Grid<>(Component.class),componentRepository::findAll);
  }

  @Override
  protected void configureGrid() {
  }
}
