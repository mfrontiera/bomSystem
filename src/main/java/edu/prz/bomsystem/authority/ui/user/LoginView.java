package edu.prz.bomsystem.authority.ui.user;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import edu.prz.bomsystem.authority.domain.user.AuthenticatedUserService;
import edu.prz.bomsystem.foundation.ui.i18n.I18nAware;


@PageTitle("Login - BOMBER")
@Route("login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver, I18nAware {

  private final transient AuthenticatedUserService authenticatedUserService;
  private final LoginForm loginForm;

  public LoginView(AuthenticatedUserService authenticatedUserService) {
    this.authenticatedUserService = authenticatedUserService;

    setSizeFull();
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);
    getStyle().set("background-color", "#f5f7fa");

    // Tworzenie formularza logowania
    loginForm = new LoginForm();
    loginForm.setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));
    loginForm.setI18n(createCustomI18n());

    // Branding layout: logo + formularz
    HorizontalLayout content = new HorizontalLayout();
    content.setWidth("800px");
    content.setHeight("400px");
    content.getStyle().set("box-shadow", "0 4px 12px rgba(0, 0, 0, 0.1)");
    content.setAlignItems(Alignment.STRETCH);

    Image logo = new Image("images/logo2.png", "BOMBER logo");
    logo.setMaxWidth("100%");
    logo.setHeight("100%");
    logo.getStyle().set("object-fit", "cover");

    Div leftSide = new Div(logo);
    leftSide.setWidth("50%");
    leftSide.getStyle().set("background-color", "#ffffff");

    VerticalLayout formWrapper = new VerticalLayout();
    formWrapper.setWidth("50%");
    formWrapper.setPadding(true);
    formWrapper.setAlignItems(Alignment.CENTER);
    formWrapper.setJustifyContentMode(JustifyContentMode.CENTER);
    formWrapper.add(loginForm);

    content.add(leftSide, formWrapper);
    add(content);
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    if (authenticatedUserService.getUserUsingContext().isPresent()) {
      UI.getCurrent().navigate("");
    }

    // Jeśli w URL jest ?error to pokaż komunikat błędu
    loginForm.setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
  }

  private LoginI18n createCustomI18n() {
    LoginI18n i18n = LoginI18n.createDefault();
    i18n.getForm().setTitle(i18n("loginTitle"));        // "Zaloguj się"
    i18n.getForm().setUsername(i18n("username"));       // "Użytkownik"
    i18n.getForm().setPassword(i18n("password"));       // "Hasło"
    i18n.getForm().setSubmit(i18n("loginButton"));      // "Zaloguj"
    return i18n;
  }
}
