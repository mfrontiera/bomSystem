package edu.prz.bomsystem.mbom.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import edu.prz.bomsystem.foundation.ui.view.BaseView;
import edu.prz.bomsystem.mbom.application.BonService;
import edu.prz.bomsystem.mbom.domain.Bon;
import edu.prz.bomsystem.mbom.domain.BonModuleRepository;
import edu.prz.bomsystem.mbom.domain.BonRepository;
import edu.prz.bomsystem.module.domain.ModuleRepository;
import jakarta.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.random.RandomDataGenerator;

@PermitAll
@Route("boms")
public class BonEditableGridView extends BaseView {

  private final BonRepository bonRepository;
  private final BonModuleRepository bonModuleRepository;
  private final ModuleRepository moduleRepository;
  private final BonService bonService;
  private final BonEditableGrid bonEditableGrid;

  public BonEditableGridView(BonRepository bonRepository, BonModuleRepository bonModuleRepository,
      ModuleRepository moduleRepository, BonService bonService) {
    this.bonRepository = bonRepository;
    this.bonModuleRepository = bonModuleRepository;
    this.moduleRepository = moduleRepository;
    this.bonService = bonService;

    List<Bon> bons = new ArrayList<>(bonRepository.findAll());
    ListDataProvider<Bon> dataProvider = new ListDataProvider<>(bons);

    bonEditableGrid = new BonEditableGrid(new Binder<>(Bon.class),dataProvider,bonService,moduleRepository);

    setupLayout();
  }

  private void setupLayout(){
    TextField searchField = bonEditableGrid.createSearchField(List.of(
        Bon::getName,
        Bon::getCatalogId
    ));

    Button newEntityButton = bonEditableGrid.addNewButton(
        unused -> bonService.createBon(
            "BB" + new RandomDataGenerator().nextLong(0, 10000))
    );

    Button removeEntityButton = bonEditableGrid.addDeleteButton(
        delete -> {
          bonRepository.deleteAll();
          return null;
        }
    );

    bonEditableGrid.addEditActionColumn(bonRepository::save);

    HorizontalLayout buttonLayout = new HorizontalLayout(searchField,newEntityButton,removeEntityButton);
    add(buttonLayout,bonEditableGrid);
  }

}
