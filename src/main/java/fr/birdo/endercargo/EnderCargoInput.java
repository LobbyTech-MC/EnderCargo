package fr.birdo.endercargo;

import org.bukkit.Material;
import org.bukkit.block.Dispenser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

public class EnderCargoInput implements Listener {

    public static final String blockName = "§3Ender Cargo Node §b(Input)";

    public EnderCargoInput(EnderCargo enderCargo) {
    }

    @EventHandler
    public static void onDispense(BlockDispenseEvent event) {
        if (event.getBlock().getType() == Material.DISPENSER) {
            Dispenser d = (Dispenser) event.getBlock().getState();
            if (d.getCustomName().equalsIgnoreCase(blockName)) {
                event.setCancelled(true);
            }
        }
    }
}