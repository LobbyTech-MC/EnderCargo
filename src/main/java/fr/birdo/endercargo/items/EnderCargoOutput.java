package fr.birdo.endercargo.items;

import fr.birdo.endercargo.EnderCargo;
import fr.birdo.endercargo.Utils.EnderCargoData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.block.Dispenser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class EnderCargoOutput implements Listener {

    public static final String blockName = "§3Ender Cargo Node §b(Output)";
    public static final Material blockMaterial = Material.DISPENSER;

    public EnderCargoOutput(EnderCargo enderCargo) {
    }

    @EventHandler
    public void onDispense(BlockDispenseEvent event) {
        if (event.getBlock().getType() == blockMaterial) {
            Dispenser d = (Dispenser) event.getBlock().getState();
            if (d.getCustomName() != null && d.getCustomName().equalsIgnoreCase(blockName))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == blockMaterial) {
            Dispenser d = (Dispenser) event.getBlock().getState();
            if (d.getCustomName() != null && d.getCustomName().equalsIgnoreCase(blockName)) {
                Container container = (Container) event.getBlock().getState();
                container.getInventory().clear();
                EnderCargoData.unlinkCargo(event.getBlock().getLocation());
            }
        }
    }

    @EventHandler
    public void onMove(InventoryMoveItemEvent event) {
        if (event.getSource().getType() == InventoryType.HOPPER && event.getDestination().getType() == InventoryType.DISPENSER) {
            Container destinationContainer = (Container) event.getDestination().getHolder();
            if (destinationContainer.getCustomName() != null && destinationContainer.getCustomName().equalsIgnoreCase(blockName))
                event.setCancelled(true);
        } else if (event.getDestination().getType() == InventoryType.HOPPER && event.getSource().getType() == InventoryType.DISPENSER) {
            Container sourceContainer = (Container) event.getSource().getHolder();
            if (sourceContainer.getCustomName() != null && sourceContainer.getCustomName().equalsIgnoreCase(blockName))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            if (event.getClickedBlock().getType() == Material.DISPENSER) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!event.getPlayer().isSneaking() || event.getItem() == null) {
                        Container container = (Container) event.getClickedBlock().getState();
                        if (container.getCustomName() != null && container.getCustomName().equalsIgnoreCase(blockName))
                            event.setCancelled(true);
                    } else if (event.getPlayer().isSneaking() && event.getItem() != null) {
                        if (event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasDisplayName()) {
                            String displayName = event.getItem().getItemMeta().getDisplayName();
                            Container container = (Container) event.getClickedBlock().getState();
                            if (container.getCustomName() != null && container.getCustomName().equalsIgnoreCase(blockName))
                                if (displayName.equalsIgnoreCase("§7Cargo Node §c(Output)") || displayName.equalsIgnoreCase("§6Advanced Cargo Node §c(Output)"))
                                    event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
