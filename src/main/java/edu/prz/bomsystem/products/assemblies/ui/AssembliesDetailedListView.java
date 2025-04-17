
package edu.prz.bomsystem.products.assemblies.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
      ComponentRepository componentRepository) {
    super(new Grid<>(Assembly.class), assemblyRepository.findAll(), new Button());
    this.assemblyRepository = assemblyRepository;
    this.componentRepository = componentRepository;
  }


  @Override
  protected Button createAddMoreButton() {
    Button addNewAssebly = new Button("NEW ASSEMBLY");
    addNewAssebly.addClickListener(buttonClickEvent -> {

    });
    return addNewAssebly;
  }

  @Override
  protected void setupMainGridColumns(Grid<Assembly> grid) {
    grid.removeAllColumns();
    grid.addColumn(Assembly::getName).setHeader("Assembly name");
    grid.addColumn(Assembly::getCreateDate).setHeader("Creation date");
  }

  @Override
  protected Component createDetailComponent(Assembly mainItem) {

    VerticalLayout detailLayout = new VerticalLayout();

    Button addComponentButton = new Button(i18n("addComponentButton"));
    addComponentButton.addClickListener(buttonClickEvent -> {
      Dialog dialog = new Dialog(addNewComponentMenu());
      dialog.setWidthFull();
      dialog.open();
    });

    Grid<AssemblyComponent> componentGrid = new Grid<>(AssemblyComponent.class, false);
    componentGrid.setItems(mainItem.getAssemblyComponents());

    componentGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    componentGrid.setAllRowsVisible(true);
    componentGrid.addColumn(
            component -> componentRepository.findById(component.getComponent().getId()))
        .setHeader("Componnet");
    componentGrid.addColumn(AssemblyComponent::getQuantity).setHeader("Quantity");
    componentGrid.setHeightFull();

    if (mainItem.getAssemblyComponents().isEmpty()) {
      return addComponentButton;
    }

    detailLayout.add(componentGrid, addComponentButton);
    return detailLayout;
  }

  private Component addNewComponentMenu(){
    Grid<edu.prz.bomsystem.products.components.domain.Component> allComponentGrid = new Grid<>();
    allComponentGrid.setItems(componentRepository.findAll());
    allComponentGrid.setSelectionMode(SelectionMode.MULTI);
    allComponentGrid.removeAllColumns();
    allComponentGrid.addColumn(edu.prz.bomsystem.products.components.domain.Component::getName).setHeader("NAME").setAutoWidth(true).setFlexGrow(1);

    return allComponentGrid;
  }

  @Override
  protected Component createDeleteButton() {
    deleteButton.addClickListener(buttonClickEvent -> {
      List<Assembly> selectedAssemblyList = mainGrid.getSelectedItems().stream().toList();
      assemblyRepository.deleteAll(selectedAssemblyList);
      mainGrid.setItems(assemblyRepository.findAll());
    });
    return deleteButton;
  }

}

