package edu.prz.bomsystem.products.assemblies.application;

import edu.prz.bomsystem.products.assemblies.domain.Assembly;
import edu.prz.bomsystem.products.components.domain.Component;

public class AssemblyTreeNode {
  private Assembly assembly;
  private Component component;

  public AssemblyTreeNode(Assembly assembly) {
    this.assembly = assembly;
  }

  public AssemblyTreeNode(Component component) {
    this.component = component;
  }

  public boolean isAssembly() {
    return assembly != null;
  }

  public String getName() {
    return isAssembly() ? assembly.getName() : component.getName();
  }

  public Assembly getAssembly() {
    return assembly;
  }

  public Component getComponent() {
    return component;
  }
}
