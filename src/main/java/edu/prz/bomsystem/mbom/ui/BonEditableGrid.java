package edu.prz.bomsystem.mbom.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import edu.prz.bomsystem.foundation.ui.view.EditableGrid;
import edu.prz.bomsystem.mbom.application.BonService;
import edu.prz.bomsystem.mbom.domain.Bon;
import edu.prz.bomsystem.mbom.domain.BonModule;
import edu.prz.bomsystem.module.domain.Module;
import edu.prz.bomsystem.module.domain.ModuleRepository;
import edu.prz.bomsystem.module.ui.ModuleBasicGrid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BonEditableGrid extends EditableGrid<Bon> {

  private final BonService bonService;
  private final ModuleRepository moduleRepository;

  public BonEditableGrid(Binder<Bon> binder, ListDataProvider<Bon> dataProvider, BonService bonService, ModuleRepository moduleRepository){
    super(binder,dataProvider);
    this.bonService = bonService;
    this.moduleRepository = moduleRepository;

    setupLayout();
  }
  @Override
  public Component getItemDetailsContent(Module module) {
    return null;
  }

  private void setupLayout(){
    // Pole katalogowe (bez edytora – tylko odczyt)
    addColumn(Bon::getCatalogId)
        .setHeader(i18n("catalogId"))
        .setAutoWidth(true)
        .setSortable(true);

// Pole nazwy (edytowalne)
    TextField nameField = new TextField();
    nameField.setWidthFull();
    binder.forField(nameField).bind(Bon::getName, Bon::setName);
    addColumn(Bon::getName)
        .setHeader(i18n("name"))
        .setAutoWidth(true)
        .setEditorComponent(nameField)
        .setSortable(true);

// Pole opisu (edytowalne, z ograniczeniem długości)
    TextArea descriptionField = new TextArea();
    descriptionField.setWidthFull();
    descriptionField.setMaxLength(150);
    descriptionField.setValueChangeMode(ValueChangeMode.EAGER);
    descriptionField.addValueChangeListener(e ->
        e.getSource().setHelperText(e.getValue().length() + "/" + 150)
    );
    binder.forField(descriptionField).bind(Bon::getDescription, Bon::setDescription);
    addColumn(Bon::getDescription)
        .setHeader(i18n("description"))
        .setAutoWidth(true)
        .setEditorComponent(descriptionField)
        .setSortable(true);

// Pole statusu (edytowalne)
    TextField statusField = new TextField();
    statusField.setWidthFull();
    binder.forField(statusField).bind(Bon::getStatus, Bon::setStatus);
    addColumn(Bon::getStatus)
        .setHeader(i18n("status"))
        .setAutoWidth(true)
        .setEditorComponent(statusField)
        .setSortable(true);

// Pole ilości na stanie (edytowalne)
    IntegerField quantityField = new IntegerField();
    quantityField.setWidthFull();
    binder.forField(quantityField).bind(Bon::getStockQuantity, Bon::setStockQuantity);
    addColumn(Bon::getStockQuantity)
        .setHeader(i18n("quantity"))
        .setAutoWidth(true)
        .setEditorComponent(quantityField)
        .setSortable(true);

// Pole daty (jeśli chcesz edytować)
    DateTimePicker dateField = new DateTimePicker();
    dateField.setWidthFull();
    binder.forField(dateField).bind(Bon::getDate, Bon::setDate);
    addColumn(Bon::getDate)
        .setHeader(i18n("date"))
        .setAutoWidth(true)
        .setEditorComponent(dateField)
        .setSortable(true);
  }

  private static LitRenderer<Bon> createToggleDetailsRenderer(Grid<Bon> grid) {
    return LitRenderer.<Bon>of("""
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
    """).withFunction("handleClick", bon -> {
      grid.setDetailsVisible(bon, !grid.isDetailsVisible(bon));
    });
  }

  @Override
  public void setItemDetailsRenderer(com.vaadin.flow.data.renderer.Renderer<Bon> renderer) {
    super.setItemDetailsRenderer(renderer);
  }

  @Override
  public com.vaadin.flow.component.Component getItemDetailsContent(Bon bon) {
    // Pobierz powiązane komponenty modułu
    List<BonModule> bonModules = bon.getBonModuleList();

    // Przekształć na listę Component
    List<Module> moduleList = new ArrayList<>();
    for (BonModule mc : bonModules) {
      Optional<Module> c = moduleRepository.findById(mc.getModuleId().getId());
      c.ifPresent(moduleList::add);
    }

    // Stwórz ComponentBasicGrid z tymi komponentami
    ListDataProvider<Module> provider = new ListDataProvider<>(moduleList);
    ModuleBasicGrid grid = new ModuleBasicGrid(new Binder<>(
        Module.class), provider);
    grid.addColumn(Module::getName).setHeader("name");
    return grid;
  }
}
