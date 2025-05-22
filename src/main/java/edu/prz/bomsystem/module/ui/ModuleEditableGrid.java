package edu.prz.bomsystem.module.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import edu.prz.bomsystem.component.domain.Component;
import edu.prz.bomsystem.component.domain.ComponentRepository;
import edu.prz.bomsystem.component.ui.ComponentBasicGrid;
import edu.prz.bomsystem.foundation.ui.view.EditableGrid;
import edu.prz.bomsystem.mbom.domain.Bon;
import edu.prz.bomsystem.module.application.ModuleService;
import edu.prz.bomsystem.module.domain.ComponentWithQuantity;
import edu.prz.bomsystem.module.domain.Module;
import edu.prz.bomsystem.module.domain.ModuleComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ModuleEditableGrid extends EditableGrid<Module> {

  private final ModuleService moduleService;
  private final ComponentRepository componentRepository;

  public ModuleEditableGrid(Binder<Module> binder,
      ListDataProvider<Module> dataProvider, ModuleService moduleService,
      ComponentRepository componentRepository) {
    super(binder, dataProvider);
    this.moduleService = moduleService;
    this.componentRepository = componentRepository;

    setupLayout();
  }

  private void setupLayout() {
    addColumn(Module::getCatalogId).setHeader("Catalog ID");
    addColumn(Module::getName).setHeader("Name");
    addColumn(Module::getDescription).setHeader("Description");
    addColumn(Module::getType).setHeader("Type");
    addColumn(Module::getUnitMeasureType).setHeader("Measure");

    addStatusColumn(Module::getStatus);

    setDetailsVisibleOnClick(false);

    addColumn(createToggleDetailsRenderer(this))
        .setWidth("80px")
        .setFlexGrow(0)
        .setFrozen(true)
        .setHeader("Details");

    setItemDetailsRenderer(new ComponentRenderer<>(module -> {
      VerticalLayout layout = new VerticalLayout();
      layout.setPadding(false);
      layout.setSpacing(false);

      // Wyświetl istniejące komponenty
      List<ComponentWithQuantity> items = new ArrayList<>();
      for (ModuleComponent mc : module.getModuleComponentList()) {
        componentRepository.findById(mc.getComponent().getId())
            .ifPresent(component -> items.add(new ComponentWithQuantity(component, mc.getComponentsQuantity())));
      }

      Grid<ComponentWithQuantity> grid = new Grid<>(ComponentWithQuantity.class, false);
      grid.setItems(items);
      grid.addColumn(ComponentWithQuantity::getName).setHeader("Name");
      grid.addColumn(ComponentWithQuantity::getCatalogId).setHeader("Catalog ID");
      grid.addColumn(ComponentWithQuantity::getType).setHeader("Type");
      grid.addColumn(ComponentWithQuantity::getQuantity).setHeader("Quantity");

      layout.add(grid);

      // Sekcja dodawania nowego komponentu
      HorizontalLayout addLayout = new HorizontalLayout();
      addLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

      ComboBox<Component> componentCombo = new ComboBox<>();
      componentCombo.setItems(componentRepository.findAll());
      componentCombo.setItemLabelGenerator(Component::getName);
      componentCombo.setWidth("300px");

      NumberField quantityField = new NumberField("Qty");
      quantityField.setStep(1);
      quantityField.setMin(1);
      quantityField.setValue(1.0);

      Button addButton = new Button("Add", event -> {
        Component selected = componentCombo.getValue();
        Integer qty = quantityField.getValue() != null ? quantityField.getValue().intValue() : 1;

        if (selected != null && qty > 0) {
          moduleService.addComponentToModule(module.getIdentity(), selected.getIdentity(), qty);
          getDataProvider().refreshAll();
          setDetailsVisible(module, false);
          setDetailsVisible(module, true);
        }
      });

      addLayout.add(componentCombo, quantityField, addButton);
      layout.add(addLayout);

      return layout;
    }));
  }

  private static LitRenderer<Module> createToggleDetailsRenderer(Grid<Module> grid) {
    return LitRenderer.<Module>of("""
      <vaadin-button
          theme="tertiary icon"
          aria-label="Toggle details"
          aria-expanded="${model.detailsOpened ? 'true' : 'false'}"
          @click="${handleClick}"
        >
          <vaadin-icon
            .icon="${model.detailsOpened ? 'lumo:angle-down' : 'lumo:angle-right'}"
          ></vaadin-icon>
      </vaadin-button>
    """).withFunction("handleClick", module -> {
      grid.setDetailsVisible(module, !grid.isDetailsVisible(module));
    });
  }


  @Override
  public void setItemDetailsRenderer(com.vaadin.flow.data.renderer.Renderer<Module> renderer) {
    super.setItemDetailsRenderer(renderer);
  }

  // Nadpisujemy getItemDetailsContent aby dynamicznie zwracać grid komponentów dla danego modułu
  @Override
  public com.vaadin.flow.component.Component getItemDetailsContent(Module module) {
    // Pobierz powiązane komponenty modułu
    List<ModuleComponent> moduleComponents = module.getModuleComponentList();

    // Przekształć na listę Component
    List<Component> componentsList = new ArrayList<>();
    for (ModuleComponent mc : moduleComponents) {
      Optional<Component> c = componentRepository.findById(mc.getComponent().getId());
      c.ifPresent(componentsList::add);
    }

    // Stwórz ComponentBasicGrid z tymi komponentami
    ListDataProvider<Component> provider = new ListDataProvider<>(componentsList);
    ComponentBasicGrid grid = new ComponentBasicGrid(new Binder<>(Component.class), provider);
    grid.addColumn(Component::getName).setHeader("name");
    return grid;
  }

  @Override
  public com.vaadin.flow.component.Component getItemDetailsContent(Bon bon) {
    return null;
  }
}
