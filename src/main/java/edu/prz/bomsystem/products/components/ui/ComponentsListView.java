package edu.prz.bomsystem.products.components.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import edu.prz.bomsystem.foundation.ui.view.ListView;
import edu.prz.bomsystem.products.components.domain.Component;
import edu.prz.bomsystem.products.components.domain.ComponentRepository;
import jakarta.annotation.security.PermitAll;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@PermitAll
@Route("components")
public class ComponentsListView extends ListView<Component> {

  @Autowired
  public ComponentsListView(ComponentRepository componentRepository){
    super(new Grid<>(Component.class),new Binder<>(Component.class), componentRepository::findAll, componentRepository::save);
  }

  @Override
  protected void configureGrid() {
    grid.removeAllColumns();
    grid.addColumn(Component::getName).setHeader(i18n("componentName")).setSortable(true);
    grid.addColumn(component -> Optional.ofNullable(component.getComponentsProperty().getVendor()).orElse("")).setHeader(i18n("componentVendorName")).setSortable(true);
    grid.addColumn(component -> Optional.ofNullable(component.getComponentsProperty().getVendorsId()).orElse("")).setHeader(i18n("componentVendorsId")).setSortable(true);
    grid.addColumn(component -> Optional.ofNullable(component.getComponentsProperty().getParams()).orElse("")).setHeader(i18n("componentParams")).setSortable(true);
    grid.addColumn(component -> Optional.ofNullable(component.getComponentsProperty().getSize()).orElse("")).setHeader(i18n("componentSize")).setSortable(true);
    grid.addColumn(component -> Optional.ofNullable(component.getComponentsProperty().getAdditionalInfo()).orElse("")).setHeader(i18n("componentAdditionalInfo")).setSortable(true);
    grid.addColumn(component -> Optional.ofNullable(component.getComponentsProperty().getBuyLink()).orElse("")).setHeader(i18n("componentBuyLink")).setSortable(true);
  }
}
