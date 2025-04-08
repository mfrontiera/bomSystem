package edu.prz.bomsystem.foundation.ui.view;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class AccordionView<T> extends BaseView {

  protected final Accordion accordion;
  private final Grid<T> grid;
  protected final String name;

  protected AccordionView(Accordion accordion, Grid<T> grid,
      String name) {
    this.accordion = accordion;
    this.grid = grid;
    this.name = name;
  }

  protected void setupLayout() {
    VerticalLayout mainLayout = new VerticalLayout();
    mainLayout.addClassNames("accordion-view");
    mainLayout.add(accordion);
    mainLayout.setAlignItems(Alignment.CENTER);
    add(mainLayout);
  }

  private void SetupAccordion() {
    configureAccordion();
  }

  protected abstract void configureAccordion();

  protected void refreshData(){
    accordion.add("",grid);
  }
}
