
package edu.prz.bomsystem.products.assemblies.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.router.Route;
import edu.prz.bomsystem.foundation.ui.view.DetailedGridView;
import edu.prz.bomsystem.products.assemblies.domain.Assembly;
import edu.prz.bomsystem.products.assemblies.domain.AssemblyRepository;
import edu.prz.bomsystem.products.assemblycomponent.domain.AssemblyComponent;
import edu.prz.bomsystem.products.components.domain.ComponentRepository;
import jakarta.annotation.security.PermitAll;
import java.util.List;

@PermitAll
@Route("assemblies_detail")
public class AssembliesDetailedListView extends DetailedGridView<Assembly, AssemblyComponent> {

  private final AssemblyRepository assemblyRepository;
  private final ComponentRepository componentRepository;

  public AssembliesDetailedListView(AssemblyRepository assemblyRepository,
      ComponentRepository componentRepository){
    super(assemblyRepository.findAll());
    this.assemblyRepository = assemblyRepository;
    this.componentRepository = componentRepository;
  }


  @Override
  protected void setupMainGridColumns(Grid<Assembly> grid) {
    grid.addColumn(Assembly::getName).setHeader("Assembly name");
    grid.addColumn(Assembly::getCreateDate).setHeader("Creation date");
  }

  @Override
  protected Component createDetailComponent(Assembly mainItem) {
    Grid<AssemblyComponent> componentGrid = new Grid<>(AssemblyComponent.class,false);
    componentGrid.setItems(mainItem.getAssemblyComponents());

    componentGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    componentGrid.setAllRowsVisible(true);
    componentGrid.addColumn(component -> componentRepository.findById(component.getComponent().getId())).setHeader("Componnet");
    componentGrid.addColumn(AssemblyComponent::getQuantity).setHeader("Quantity");
    componentGrid.setHeightFull();

    return componentGrid;
  }

  @Override
  protected Component createDeleteButton() {
    Button deleteButton = new Button("DELETE");
    deleteButton.addClickListener(buttonClickEvent -> {
      List<Assembly> selectedAssemblyList = mainGrid.getSelectedItems().stream().toList();
      Assembly first = selectedAssemblyList.getFirst();
      first.getAssemblyComponents().clear();
      assemblyRepository.delete(first);
    });
    return deleteButton;
  }
}

