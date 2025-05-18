package edu.prz.bomsystem.module.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.router.Route;
import edu.prz.bomsystem.component.domain.ComponentRepository;
import edu.prz.bomsystem.foundation.ui.view.VersionedGridView;
import edu.prz.bomsystem.module.application.ModuleAuditService;
import edu.prz.bomsystem.module.application.ModuleService;
import edu.prz.bomsystem.module.domain.Module;
import edu.prz.bomsystem.module.domain.ModuleComponentRepository;
import edu.prz.bomsystem.module.domain.ModuleRepository;
import jakarta.annotation.security.PermitAll;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.val;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.springframework.data.domain.PageRequest;

@PermitAll
@Route("module")
public class ModuleView extends VersionedGridView<Module> {

  private final ModuleRepository moduleRepository;
  private final ModuleService moduleService;
  private final ModuleComponentRepository moduleComponentRepository;
  private final ModuleAuditService moduleAuditService;
  private final ComponentRepository componentRepository;

  public ModuleView(ModuleRepository moduleRepository, ModuleService moduleService,
      ModuleComponentRepository moduleComponentRepository, ModuleAuditService moduleAuditService,
      ComponentRepository componentRepository) {
    super(new Grid<>(edu.prz.bomsystem.module.domain.Module.class, false),
        new CallbackDataProvider<Module, Void>(
            query -> {
              int offset = query.getOffset();
              int limit = query.getLimit();
              return moduleRepository.findAll(PageRequest.of(offset / limit, limit)).stream();
            },
            query -> (int) moduleRepository.count()
        ));

    this.moduleRepository = moduleRepository;
    this.moduleService = moduleService;
    this.moduleComponentRepository = moduleComponentRepository;
    this.componentRepository = componentRepository;

    setupLayout();
    this.moduleAuditService = moduleAuditService;
  }

  private void setupLayout(){
    addNewButton(
        unused -> moduleService.createModule("DD" + new RandomDataGenerator().nextLong(0, 10000))
    );
  }

  @Override
  protected void setGridColumns() {

    addVersionColumn(
        module -> moduleAuditService.getModuleVersions(module.getIdentity()).size(),
        (module, revision) -> moduleAuditService.getModuleFromVersion(
            module.getIdentity(), revision),
        module -> module.getIdentity().getId()
    );


    grid.addColumn(Module::getCatalogId).setHeader("CatalogId").setAutoWidth(true);
    grid.addColumn(Module::getName).setHeader("Name").setAutoWidth(true);
    grid.addColumn(Module::getDescription).setHeader("Description").setAutoWidth(true);
    grid.addColumn(Module::getType).setHeader("Type").setAutoWidth(true);
    grid.addComponentColumn(module -> {
      IntegerField priceField = new IntegerField();
      priceField.setReadOnly(true);
      AtomicInteger totalPrice = new AtomicInteger();
      totalPrice.set(0);
      module.getModuleComponentList().forEach(list -> {
        val component = componentRepository.findById(list.getComponent().getId());
        totalPrice.addAndGet(component.get().getCostPerUnit() * list.getComponentsQuantity());
      });

      priceField.setValue(totalPrice.intValue());
      return priceField;
    });
  }

  private Icon createStatusIcon(String status) {
    return switch (status) {
      case "Available" -> new Icon(VaadinIcon.CHECK);
      case "Declined" -> new Icon(VaadinIcon.CLOSE);
      case "Pending" -> new Icon(VaadinIcon.TIME_FORWARD);
      default -> new Icon(VaadinIcon.QUESTION_CIRCLE);
    };
  }
}
