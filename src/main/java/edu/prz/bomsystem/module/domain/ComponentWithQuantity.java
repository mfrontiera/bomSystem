package edu.prz.bomsystem.module.domain;

import edu.prz.bomsystem.component.domain.Component;

public class ComponentWithQuantity {
  private final Component component;
  private final int quantity;

  public ComponentWithQuantity(Component component, int quantity) {
    this.component = component;
    this.quantity = quantity;
  }

  public Component getComponent() {
    return component;
  }

  public int getQuantity() {
    return quantity;
  }

  public String getName() {
    return component.getName();
  }

  public String getCatalogId() {
    return component.getCatalogId();
  }

  public String getType() {
    return component.getType();
  }
}
