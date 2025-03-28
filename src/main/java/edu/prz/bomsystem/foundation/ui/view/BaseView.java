package edu.prz.bomsystem.foundation.ui.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveListener;
import com.vaadin.flow.router.HasDynamicTitle;
import edu.prz.bomsystem.foundation.ui.i18n.I18nAware;

public abstract class BaseView extends VerticalLayout implements I18nAware, BeforeEnterObserver,
    BeforeLeaveListener, HasDynamicTitle {


  public BaseView() {
  }

  public String getTitle() {
    return i18n(getClass(), "pageTitle");
  }

  @Override
  public String getPageTitle() {
    return getTitle();
  }

  @Override
  public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {

  }

  @Override
  public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {

  }

}
