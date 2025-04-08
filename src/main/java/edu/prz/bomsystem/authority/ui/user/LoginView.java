package edu.prz.bomsystem.authority.ui.user;

import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import edu.prz.bomsystem.authority.domain.user.AuthenticatedUserService;
import edu.prz.bomsystem.foundation.ui.i18n.I18nAware;


@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay implements BeforeEnterObserver, I18nAware {

  private final transient AuthenticatedUserService authenticatedUserService;

  public LoginView(AuthenticatedUserService authenticatedUserService) {

    this.authenticatedUserService = authenticatedUserService;
    setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));

    LoginI18n i18n = LoginI18n.createDefault();
    i18n.setHeader(new LoginI18n.Header());
    i18n.getHeader().setTitle("BOMBER");
    i18n.getHeader().setDescription(i18n("loginDescription"));
    i18n.getForm().setTitle(i18n("loginTitle"));
    i18n.getForm().setUsername(i18n("username"));
    i18n.getForm().setPassword(i18n("password"));
    i18n.getForm().setSubmit(i18n("loginButton"));
    i18n.setAdditionalInformation(null);
    setI18n(i18n);

    setForgotPasswordButtonVisible(false);
    setOpened(true);
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    if (authenticatedUserService.getUserUsingContext().isPresent()) {
      // Already logged in
      setOpened(false);
      event.forwardTo("");
    }

    setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
  }
}
