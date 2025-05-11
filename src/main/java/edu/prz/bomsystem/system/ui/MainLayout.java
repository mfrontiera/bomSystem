package edu.prz.bomsystem.system.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.BoxSizing;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Height;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;
import com.vaadin.flow.theme.lumo.LumoUtility.Width;
import edu.prz.bomsystem.authority.domain.user.AuthenticatedUserService;
import edu.prz.bomsystem.authority.domain.user.User;
import edu.prz.bomsystem.foundation.ui.i18n.I18nAware;
import java.io.ByteArrayInputStream;
import java.util.Optional;
import lombok.val;
import org.vaadin.lineawesome.LineAwesomeIcon;

@Layout
@AnonymousAllowed
public class MainLayout extends AppLayout implements I18nAware {

  private final transient AuthenticatedUserService authenticatedUserService;

  public MainLayout(AuthenticatedUserService authenticatedUserService){
    this.authenticatedUserService = authenticatedUserService;

    addToNavbar(createHeaderContent());
  }

  private Component createHeaderContent(){
    Header header = new Header();
    header.addClassNames(BoxSizing.BORDER, Display.FLEX, FlexDirection.COLUMN, Width.FULL);
    header.add(createStatusBar());
    return header;
  }

  private Component createStatusBar() {
    Div layout = new Div();
    layout.addClassNames(Display.FLEX, AlignItems.CENTER, Padding.Horizontal.MEDIUM);

    Image logoImage = new Image("images/unabomber.svg","");
    logoImage.setWidth("50px");

    H1 appName = new H1("BOMBER");
    H3 appDescription = new H3("Bill Of Materials Edit and Review");
    VerticalLayout appNameAndDescription = new VerticalLayout(appName,appDescription);
    appNameAndDescription.setSpacing(false);
    appNameAndDescription.addClassNames(Margin.Vertical.MEDIUM, Margin.End.AUTO, "BOMBER");
    layout.add(logoImage,appNameAndDescription);

    Optional<User> maybeUser = authenticatedUserService.getUserUsingContext();
    if (maybeUser.isPresent()) {
      User user = maybeUser.get();

      Avatar avatar = new Avatar(user.getName());
      if (user.getProfilePicture() != null) {
        StreamResource resource = new StreamResource("profile-pic",
            () -> new ByteArrayInputStream(user.getProfilePicture()));
        avatar.setImageResource(resource);
      }
      avatar.setThemeName("xsmall");
      avatar.getElement().setAttribute("tabindex", "-1");

      MenuBar userMenu = new MenuBar();
      userMenu.setThemeName("tertiary-inline contrast");

      MenuItem userName = userMenu.addItem("");
      Div div = new Div();
      div.add(avatar);
      div.add(user.getName());
      div.add(new Icon("lumo", "dropdown"));
      div.getElement().getStyle().set("display", "flex");
      div.getElement().getStyle().set("align-items", "center");
      div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
      userName.add(div);
      userName.getSubMenu().addItem(i18n("signOut"), e -> authenticatedUserService.logout());

      layout.add(userMenu);
    } else {
      Anchor loginLink = new Anchor("login", i18n("signIn"));
      layout.add(loginLink);
    }

    return layout;
  }

  private Component createSystemMenu(String... localizations) {
    MenuBar menu = new MenuBar();
    menu.setThemeName("tertiary");

    for (String localization : localizations) {
      MenuItem menuItem = menu.addItem(createMenuIcon());
      menuItem.add(i18n(localization));
      menuItem.addClassNames(Display.FLEX, Gap.XSMALL, Height.MEDIUM,
          AlignItems.CENTER, Padding.Horizontal.SMALL, TextColor.BODY);

      menuItem.addClickListener(click -> UI.getCurrent().navigate(localization));
    }

    return menu;
  }

  private Component createMenuIcon() {
    val icon = LineAwesomeIcon.ADDRESS_CARD.create();
    icon.getStyle().set("marginRight", "var(--lumo-space-s)");
    return icon;
  }



}
