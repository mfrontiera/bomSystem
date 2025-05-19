package edu.prz.bomsystem.foundation.ui.view;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class BasicGridView2<T> extends BaseView {
  protected final Grid<T> grid = new Grid<>();
  protected final List<T> items;
  protected final ListDataProvider<T> dataProvider;
  protected final TextField searchField;

  protected BasicGridView2(List<T> items) {
    this.items = new ArrayList<>(items);
    this.dataProvider = new ListDataProvider<>(this.items);
    this.grid.setDataProvider(dataProvider);
    this.searchField = createSearchField();

    setupLayout();
    setupGrid();
  }

  private void setupLayout() {
    HorizontalLayout topBar = new HorizontalLayout(searchField);
    add(topBar, grid);
  }

  private void setupGrid() {
    grid.setSelectionMode(Grid.SelectionMode.MULTI);
    grid.setAllRowsVisible(true);
  }

  private TextField createSearchField() {
    TextField field = new TextField();
    field.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
    field.setValueChangeMode(ValueChangeMode.EAGER);
    return field;
  }

  protected void configureSearch(Predicate<T> filterPredicate) {
    searchField.addValueChangeListener(e -> dataProvider.refreshAll());
    dataProvider.setFilter(item -> {
      String term = searchField.getValue().trim().toLowerCase();
      if (term.isEmpty()) {
        return true;
      }
      return filterPredicate.test(item);
    });
  }

  protected void refresh() {
    dataProvider.refreshAll();
  }

  protected abstract void setGridColumns();
}
