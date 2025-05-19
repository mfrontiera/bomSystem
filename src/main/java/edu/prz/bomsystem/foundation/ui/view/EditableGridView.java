package edu.prz.bomsystem.foundation.ui.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import java.util.List;
import java.util.function.Consumer;

public abstract class EditableGridView<T> extends BasicGridView2<T> {
  protected final Binder<T> binder;
  protected final Editor<T> editor;

  public EditableGridView(List<T> items, Binder<T> binder) {
    super(items);
    this.binder = binder;
    this.editor = grid.getEditor();
    this.editor.setBinder(binder);
    this.editor.setBuffered(true);
  }

  protected void addEditActionColumn(Consumer<T> saveHandler) {
    Button saveButton = new Button(new Icon(VaadinIcon.CHECK_SQUARE_O), e -> editor.save());
    Button cancelButton = new Button(new Icon(VaadinIcon.CLOSE), e -> editor.cancel());
    cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);

    HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton);

    editor.addSaveListener(event -> {
      T item = event.getItem();
      saveHandler.accept(item);
      dataProvider.refreshItem(item);
    });

    grid.addComponentColumn(item -> {
      Button editButton = new Button(new Icon(VaadinIcon.EDIT));
      editButton.addClickListener(e -> {
        if (editor.isOpen()) editor.cancel();
        editor.editItem(item);
      });
      return editButton;
    }).setEditorComponent(actions).setAutoWidth(true).setFlexGrow(0);
  }
}