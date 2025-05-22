package edu.prz.bomsystem.component.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import edu.prz.bomsystem.authority.domain.user.AuthenticatedUserService;
import edu.prz.bomsystem.authority.domain.user.Role;
import edu.prz.bomsystem.authority.domain.user.User;
import edu.prz.bomsystem.component.application.ComponentService;
import edu.prz.bomsystem.component.domain.Component;
import edu.prz.bomsystem.component.domain.ComponentRepository;
import edu.prz.bomsystem.foundation.ui.view.BaseView;
import jakarta.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.math3.random.RandomDataGenerator;

@PermitAll
@Route("components")
public class ComponentEditableGridView extends BaseView {

  private final ComponentRepository componentRepository;
  private final ComponentEditableGrid editableGrid;
  private final ComponentService componentService;
  private final AuthenticatedUserService authenticatedUserService;

  public ComponentEditableGridView(ComponentRepository componentRepository,
      ComponentService componentService, AuthenticatedUserService authenticatedUserService) {
    this.componentRepository = componentRepository;
    this.componentService = componentService;
    this.authenticatedUserService = authenticatedUserService;

    List<Component> components = new ArrayList<>(componentRepository.findAll());
    ListDataProvider<Component> dataProvider = new ListDataProvider<>(components);

    editableGrid = new ComponentEditableGrid(new Binder<>(Component.class), dataProvider);

    setupLayout();
  }


  private void setupLayout() {
    TextField searchField = editableGrid.createSearchField(List.of(
        Component::getName,
        Component::getCatalogId,
        Component::getType
    ));

    Button newEntityButton = editableGrid.addNewButton(
        unused -> componentService.createComponent(
            "DD" + new RandomDataGenerator().nextLong(0, 10000))
    );

    Button removeEntityButton = editableGrid.addDeleteButton(
        delete -> {
          componentRepository.deleteAll();
          return null;
        }
    );

    Optional<User> user = authenticatedUserService.getUserUsingContext();
    user.map(loged -> {
      Set<Role> roles = loged.getRoles();

      if(roles.contains(Role.MODERATOR) || roles.contains(Role.ADMIN)){
        editableGrid.setStatusUpdater((component, newStatus) -> {
          component.setStatus(newStatus);
          componentRepository.save(component);
        });

        editableGrid.addStatusColumn(Component::getStatus);
      } else {
        editableGrid.addStatusColumnWorker(Component::getStatus);
      }
      return true;
    });

    editableGrid.addEditActionColumn(componentRepository::save);
    editableGrid.addClassName("grid-styling");

    editableGrid.setPartNameGenerator(component -> {
      if(component.getStockQuantity() >= 5 && 20 >= component.getStockQuantity()){
        return "high-rating";
      }

      if (component.getStockQuantity() <= 0){
        return "low-rating";
      }

      return null;
    });

    HorizontalLayout actionsLayout = new HorizontalLayout(searchField, newEntityButton, removeEntityButton);
    add(actionsLayout,editableGrid);
  }


}
