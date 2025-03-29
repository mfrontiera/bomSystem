package edu.prz.bomsystem.products.assemblies.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import edu.prz.bomsystem.foundation.ui.view.ListView;
import edu.prz.bomsystem.products.assemblies.domain.Assembly;
import edu.prz.bomsystem.products.assemblies.domain.AssemblyRepository;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route("assemblies")
public class AssembliesListView extends ListView<Assembly> {

  protected AssembliesListView(AssemblyRepository assemblyRepository) {
    super(new Grid<>(Assembly.class),new Binder<>(Assembly.class),assemblyRepository::findAll, assemblyRepository::save);
  }

  @Override
  protected void configureGrid() {
    grid.removeAllColumns();
    Grid.Column<Assembly> nameColumn =
    grid.addColumn(Assembly::getName)
        .setHeader(i18n("assemblyName"))
        .setSortable(true);

    TextField nameField = new TextField();
    binder.forField(nameField).bind(Assembly::getName,Assembly::setName);

    nameColumn.setEditorComponent(nameField);

    grid.addColumn(Assembly::getCreateDate).setHeader(i18n("assemblyCreateDate")).setSortable(true);



  }
}
