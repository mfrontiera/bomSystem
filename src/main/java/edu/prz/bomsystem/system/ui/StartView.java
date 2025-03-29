package edu.prz.bomsystem.system.ui;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import edu.prz.bomsystem.foundation.ui.view.BaseView;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route("")
public class StartView extends BaseView {

  public StartView() {
    setSizeFull();
    VerticalLayout layout = new VerticalLayout();
    layout.setSizeFull();
    layout.setPadding(false);
    layout.setSpacing(false);
    add(layout);
  }

}
