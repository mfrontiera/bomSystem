package edu.prz.bomsystem.foundation.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.select.SelectVariant;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import edu.prz.bomsystem.foundation.ui.i18n.I18nAware;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

public abstract class VersionedGridView<T> extends BaseView implements I18nAware {

  protected final Grid<T> grid;
  private final Map<Object, Integer> selectedRevisions = new HashMap<>();
  private final DataProvider<T, ?> dataProvider;
  protected VersionedGridView(Grid<T> grid, DataProvider<T, ?> dataProvider) {
    this.grid = grid;
    this.dataProvider = dataProvider;

    setupGridLayout();
    setGridColumns();

    add(grid);
  }

  private void setupGridLayout() {
    grid.setDataProvider(dataProvider);
    grid.setSelectionMode(SelectionMode.MULTI);
    grid.setAllRowsVisible(true);
  }

  protected abstract void setGridColumns();

  protected void addVersionColumn(
      Function<T, Integer> versionCountProvider,
      BiFunction<T, Integer, T> historicVersionProvider,
      Function<T, Object> identityProvider
  ) {
    grid.addComponentColumn(component -> {
      Select<Integer> revisionBox = new Select<>();
      revisionBox.addThemeVariants(SelectVariant.LUMO_SMALL,SelectVariant.LUMO_ALIGN_CENTER);
      revisionBox.setWidth("80px");
      int size = versionCountProvider.apply(component);
      List<Integer> versions = IntStream.rangeClosed(0, size - 1).boxed().toList();
      revisionBox.setItems(versions);

      Object identity = identityProvider.apply(component);
      revisionBox.setValue(selectedRevisions.getOrDefault(identity, size-1));

      revisionBox.addValueChangeListener(event -> {
        Integer selectedRevision = event.getValue();
        if (selectedRevision != null) {
          selectedRevisions.put(identity,selectedRevision);
          T historicVersion = historicVersionProvider.apply(component, selectedRevision);
          replaceItem(component, historicVersion);
        }
      });

      return revisionBox;
    }).setHeader(i18n(VersionedGridView.class,"versionColumn")).setFlexGrow(0).setAutoWidth(false);
  }

  protected void addNewButton(Function<Void, T> entitySupplier) {
    Button addNewButton = new Button(new Icon(VaadinIcon.PLUS_CIRCLE));
    addNewButton.getElement().setProperty("title", "Dodaj nowy");

    addNewButton.addClickListener(event -> {
      entitySupplier.apply(null);
      grid.getDataProvider().refreshAll();
    });

    add(addNewButton);
  }

  @SuppressWarnings("unchecked")
  protected void replaceItem(T original, T replacement) {
    if (dataProvider instanceof ListDataProvider<?> listProvider) {

      List<T> items = (List<T>) listProvider.getItems();
      int index = items.indexOf(original);
      if (index >= 0) {
        items.set(index, replacement);
        listProvider.refreshAll();
      }
    } else {
      throw new UnsupportedOperationException("replaceItem is only supported for ListDataProvider");
    }
  }
}
