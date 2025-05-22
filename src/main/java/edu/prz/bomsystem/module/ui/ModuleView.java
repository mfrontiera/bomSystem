package edu.prz.bomsystem.module.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.LitRenderer;
import edu.prz.bomsystem.component.domain.ComponentRepository;
import edu.prz.bomsystem.foundation.ui.view.BasicGridView;
import edu.prz.bomsystem.module.application.ModuleService;
import edu.prz.bomsystem.module.domain.Module;
import edu.prz.bomsystem.module.domain.ModuleComponent;
import edu.prz.bomsystem.module.domain.ModuleRepository;
import org.apache.commons.math3.random.RandomDataGenerator;

public class ModuleView extends BasicGridView<Module> {

  private final ModuleRepository moduleRepository;
  private final ModuleService moduleService;
  private final ComponentRepository componentRepository;

  public ModuleView(ModuleRepository moduleRepository, ModuleService moduleService,
      ComponentRepository componentRepository) {
    super(new Grid<>(Module.class, false), moduleRepository.findAll(),
        new Binder<>(Module.class));
    this.moduleRepository = moduleRepository;
    this.moduleService = moduleService;
    this.componentRepository = componentRepository;

    setupLayout();
    setGridColumns();
  }

  @Override
  protected void setGridColumns() {
    grid.addColumn(Module::getName).setHeader("Name");
  }

  private void setupLayout() {
    addNewButton(
        unused -> moduleService.createModule(
            "DD" + new RandomDataGenerator().nextLong(0, 10000))
    );

    addDeleteRecordsButton(recordsToDelete -> {
      moduleRepository.deleteAll();
      return null;
    });

    configureSearch(component -> {
      String term = searchField.getValue().trim().toLowerCase();
      return matches(component.getName(), term) || matches(component.getCatalogId(), term)
          || matches(component.getType(), term);
    });
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
            """)
        .withFunction("handleClick", module -> grid.setDetailsVisible(module, !grid.isDetailsVisible(module)));
  }

  private static Grid<ModuleComponent> createComponentsGrid(Module module) {
    Grid<ModuleComponent> componentsGrid = new Grid<>(ModuleComponent.class, false);

    componentsGrid.addColumn(mc -> mc.getComponent().getId())
        .setHeader("Component Name").setAutoWidth(true);

    componentsGrid.addColumn(mc -> mc.getComponentsQuantity())
        .setHeader("Quantity").setAutoWidth(true);

    componentsGrid.setItems(module.getModuleComponentList());

    return componentsGrid;
  }


  private boolean matches(String field, String term) {
    return field != null && field.toLowerCase().contains(term);
  }
}
