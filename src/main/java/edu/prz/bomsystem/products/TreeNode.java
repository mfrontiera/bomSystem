package edu.prz.bomsystem.products;

import edu.prz.bomsystem.products.assemblies.domain.Assembly;
import edu.prz.bomsystem.products.components.domain.Component;

public interface TreeNode {

  class AssemblyNode implements TreeNode{
    Assembly assembly;
  }

  class ComponentNode implements TreeNode{
    Component component;
  }

}
