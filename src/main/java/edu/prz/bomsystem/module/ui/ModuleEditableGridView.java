package edu.prz.bomsystem.module.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import edu.prz.bomsystem.component.domain.ComponentRepository;
import edu.prz.bomsystem.foundation.ui.view.BaseView;
import edu.prz.bomsystem.module.application.ModuleService;
import edu.prz.bomsystem.module.domain.Module;
import edu.prz.bomsystem.module.domain.ModuleComponentRepository;
import edu.prz.bomsystem.module.domain.ModuleRepository;
import jakarta.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.random.RandomDataGenerator;

@PermitAll
@Route("modules")
public class ModuleEditableGridView extends BaseView {

  private final ModuleRepository moduleRepository;
  private final ModuleComponentRepository moduleComponentRepository;
  private final ComponentRepository componentRepository;
  private final ModuleService moduleService;
  private final ModuleEditableGrid moduleEditableGrid;

  public ModuleEditableGridView(ModuleRepository moduleRepository, ModuleComponentRepository moduleComponentRepository,
      ComponentRepository componentRepository, ModuleService moduleService) {
    this.moduleRepository = moduleRepository;
    this.moduleComponentRepository = moduleComponentRepository;
    this.componentRepository = componentRepository;
    this.moduleService = moduleService;


    List<Module> modules = new ArrayList<>(moduleRepository.findAll());
    ListDataProvider<Module> dataProvider = new ListDataProvider<>(modules);

    moduleEditableGrid = new ModuleEditableGrid(new Binder<>(Module.class),dataProvider,moduleService,componentRepository);
    setupLayout();
  }

  private void setupLayout(){
    TextField searchField = moduleEditableGrid.createSearchField(List.of(
        Module::getName,
        Module::getCatalogId,
        Module::getType
    ));

    Button newEntityButton = moduleEditableGrid.addNewButton(
        unused -> moduleService.createModule(
            "DD" + new RandomDataGenerator().nextLong(0, 10000))
    );

    Button removeEntityButton = moduleEditableGrid.addDeleteButton(
        delete -> {
          moduleRepository.deleteAll();
          return null;
        }
    );
    add(searchField,newEntityButton,removeEntityButton,moduleEditableGrid);
  }
}
