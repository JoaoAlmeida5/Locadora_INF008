package br.edu.ifba.inf008.shell;

import br.edu.ifba.inf008.interfaces.IUIController;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class UIController implements IUIController {

    private static UIController instance;
    private final TabPane tabPane;
    private final MenuBar menuBar;

    public UIController(TabPane tabPane, MenuBar menuBar) {
        this.tabPane = tabPane;
        this.menuBar = menuBar;
        instance = this;
    }

    public static UIController getInstance() {
        return instance;
    }

    @Override
    public boolean addTab(Tab tab) {
        if (tabPane != null) {
            tabPane.getTabs().add(tab);
            return true;
        }
        return false;
    }

    @Override
    public boolean addMenuItem(String menuName, MenuItem menuItem) {
        if (menuBar == null) return false;

        Menu targetMenu = null;
        for (Menu menu : menuBar.getMenus()) {
            if (menu.getText().equals(menuName)) {
                targetMenu = menu;
                break;
            }
        }

        if (targetMenu == null) {
            targetMenu = new Menu(menuName);
            menuBar.getMenus().add(targetMenu);
        }

        targetMenu.getItems().add(menuItem);
        return true;
    }

    public boolean createTab(String tabText, Node contents) {
        Tab tab = new Tab(tabText);
        tab.setContent(contents);
        return addTab(tab);
    }
}