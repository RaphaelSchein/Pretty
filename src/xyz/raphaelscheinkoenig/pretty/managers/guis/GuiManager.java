package xyz.raphaelscheinkoenig.pretty.managers.guis;

import xyz.raphaelscheinkoenig.pretty.managers.Manager;
import xyz.raphaelscheinkoenig.pretty.managers.guis.listeners.GuiListener;
import org.bukkit.entity.HumanEntity;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class GuiManager extends Manager {

    private static GuiManager instance;
    private static final String NAME = "Gui";
    private List<MyGUI> guis;

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;
        guis = new CopyOnWriteArrayList<>();
        registerListener(new GuiListener());
    }

    public MyGUI getGUI(HumanEntity he){
        return guis.stream().filter(gui -> gui.isOf(he)).findFirst().orElse(null);
    }

    public void removeMyGUI(MyGUI gui){
        guis.remove(gui);
    }

    @Override
    public String getName() {
        return NAME;
    }

    public static GuiManager instance() {
        return instance;
    }

    public void registerMyGUI(MyGUI gui) {
        guis.add(gui);
    }
}
