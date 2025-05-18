package edu.prz.bomsystem.component.ui;

import static java.lang.Math.abs;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.router.Route;
import edu.prz.bomsystem.component.application.ComponentAuditService;
import edu.prz.bomsystem.component.application.ComponentService;
import edu.prz.bomsystem.component.domain.Component;
import edu.prz.bomsystem.component.domain.ComponentRepository;
import edu.prz.bomsystem.foundation.ui.view.VersionedGridView;
import jakarta.annotation.security.PermitAll;
import java.util.Random;
import org.springframework.data.domain.PageRequest;

@PermitAll
@Route("components")
public class ComponentView extends VersionedGridView<Component> {

  private final ComponentRepository componentRepository;
  private final ComponentService componentService;
  private final ComponentAuditService componentAuditService;


  public ComponentView(ComponentRepository componentRepository, ComponentService componentService,
      ComponentAuditService componentAuditService) {
    super(new Grid<>(Component.class, false),
        new CallbackDataProvider<Component, Void>(
            query -> {
              int offset = query.getOffset();
              int limit = query.getLimit();
              return componentRepository.findAll(PageRequest.of(offset / limit, limit)).stream();
            },
            query -> (int) componentRepository.count()
        ));

    this.componentRepository = componentRepository;
    this.componentService = componentService;
    this.componentAuditService = componentAuditService;

    setupLayout();
  }

  private void setupLayout(){
    addNewButton(
        unused -> componentService.createComponent("DD" + abs(new Random().nextLong()))
    );
  }

  @Override
  protected void setGridColumns() {
    addVersionColumn(
        component -> componentAuditService.getComponentVersions(component.getIdentity()).size(),
        (component, revision) -> componentAuditService.getComponentFromVersion(
            component.getIdentity(), revision),
        component -> component.getIdentity().getId()
    );

    grid.addColumn(Component::getCatalogId).setHeader(i18n("catalogId"));
    grid.addColumn(Component::getName).setHeader(i18n("name")).setAutoWidth(true);
    grid.addColumn(Component::getDescription).setHeader(i18n("description")).setAutoWidth(true);
    grid.addColumn(Component::getType).setHeader(i18n("type")).setAutoWidth(true);
    grid.addColumn(Component::getUnitMeasureType).setHeader(i18n("measure")).setAutoWidth(true);
    grid.addColumn(Component::getCostPerUnit).setHeader(i18n("cost")).setAutoWidth(true);
    grid.addComponentColumn(component -> createStatusIcon(component.getStatus()))
        .setTooltipGenerator(
            Component::getStatus).setHeader(i18n("status")).setAutoWidth(false);
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
