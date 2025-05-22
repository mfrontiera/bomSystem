package edu.prz.bomsystem.module.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import edu.prz.bomsystem.component.domain.Component;
import edu.prz.bomsystem.component.domain.ComponentRepository;
import edu.prz.bomsystem.component.ui.ComponentBasicGrid;
import edu.prz.bomsystem.foundation.ui.view.EditableGrid;
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
    addColumn(Module::getName).setHeader("Name");

    setDetailsVisibleOnClick(false);

    addColumn(createToggleDetailsRenderer(this))
        .setWidth("80px")
        .setFlexGrow(0)
        .setFrozen(true)
        .setHeader("Details");

    // Ustawiamy renderer szczegółów — czyli komponent, który będzie pokazywany pod wierszem
    setItemDetailsRenderer(new ComponentRenderer<>(module -> {
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

      return grid;
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

  private Grid<Component> createDetailsRenderer() {
    return new Grid<Component>() {{
      addColumn(Component::getName).setHeader("Component Name");
      addColumn(Component::getCatalogId).setHeader("Catalog ID");
      addColumn(Component::getType).setHeader("Type");
    }};
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
}
