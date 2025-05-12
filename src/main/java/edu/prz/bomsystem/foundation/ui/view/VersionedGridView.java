package edu.prz.bomsystem.foundation.ui.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.select.SelectVariant;
import edu.prz.bomsystem.foundation.ui.i18n.I18nAware;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

public abstract class VersionedGridView<T> extends BaseView implements I18nAware {

  protected final Grid<T> grid;
  private final List<T> itemsList;
  private final Map<Object, Integer> selectedVersions = new HashMap<>();

  protected VersionedGridView(Grid<T> grid, List<T> itemsList) {
    this.grid = grid;
    this.itemsList = itemsList;

    setupGridLayout();
    setGridColumns();

    add(grid);
  }

  private void setupGridLayout() {
    grid.setItems(itemsList);
    grid.setSelectionMode(SelectionMode.MULTI);
    grid.setAllRowsVisible(true);
  }

  protected abstract void setGridColumns();

  protected void addVersionColumn(
      Function<T, Integer> versionCountProvider,
      BiFunction<T, Integer, T> historicVersionProvider
  ) {
    grid.addComponentColumn(component -> {
      Select<Integer> revisionBox = new Select<>();
      revisionBox.addThemeVariants(SelectVariant.LUMO_SMALL,SelectVariant.LUMO_ALIGN_CENTER);

      int size = versionCountProvider.apply(component);
      List<Integer> versions = IntStream.rangeClosed(0, size - 1).boxed().toList();
      revisionBox.setItems(versions);


      revisionBox.addValueChangeListener(event -> {
        Integer selectedRevision = event.getValue();
        if (selectedRevision != null) {
          T historicVersion = historicVersionProvider.apply(component, selectedRevision);
          replaceItem(component, historicVersion);
        }
      });

      return revisionBox;
    }).setHeader(i18n(VersionedGridView.class,"versionColumn"));
  }

  protected void replaceItem(T original, T replacement) {
    int index = itemsList.indexOf(original);
    if (index >= 0) {
      itemsList.set(index, replacement);
      grid.getDataProvider().refreshAll();
    }
  }
}
