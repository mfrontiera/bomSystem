package edu.prz.bomsystem.system.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PageTitle("Start")
@Route("")
@PermitAll
public class StartView extends Div {

  public StartView() {
    setSizeFull();
    VerticalLayout layout = new VerticalLayout();
    layout.setSizeFull();
    layout.setPadding(false);
    layout.setSpacing(false);
    add(layout);
  }

}
