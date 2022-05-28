package fr.birdo.endercargo.items;

import fr.birdo.endercargo.EnderCargo;
import fr.birdo.endercargo.Utils.EnderCargoData;
import org.bukkit.Bukkit;
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

public class EnderCargoAdvancedOutput implements Listener {

    public static final String blockName = "§3高级末影接口 §b(输出)";
    public static final Material blockMaterial = Material.END_STONE_BRICK_WALL;

    public EnderCargoAdvancedOutput(EnderCargo enderCargo) {
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
            if (d.getCustomName().equalsIgnoreCase(blockName)) {
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
            if (destinationContainer.getCustomName().equalsIgnoreCase(blockName))
                event.setCancelled(true);
        } else if (event.getDestination().getType() == InventoryType.HOPPER && event.getSource().getType() == InventoryType.DISPENSER) {
            Container sourceContainer = (Container) event.getSource().getHolder();
            if (sourceContainer.getCustomName().equalsIgnoreCase(blockName))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClickInv(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase("§3Ender Cargo Node view (Output)"))
            event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            if (event.getClickedBlock().getType() == blockMaterial) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!event.getPlayer().isSneaking() || event.getItem() == null) {
                        Container container = (Container) event.getClickedBlock().getState();
                        if (container.getCustomName().equalsIgnoreCase(blockName)) {
                            event.setCancelled(true);
                            Inventory inventory = Bukkit.createInventory(null, InventoryType.DISPENSER, "§3末影节点视图 (输出)");
                            inventory.setContents(container.getInventory().getContents());
                            event.getPlayer().openInventory(inventory);
                        }
                    } else if (event.getPlayer().isSneaking() && event.getItem() != null) {
                        if (event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasDisplayName()) {
                            String displayName = event.getItem().getItemMeta().getDisplayName();
                            Container container = (Container) event.getClickedBlock().getState();
                            if (container.getCustomName().equalsIgnoreCase(blockName))
                                if (displayName.equalsIgnoreCase("§7节点模式 §c(输出)") || displayName.equalsIgnoreCase("§6高级节点模式 §c(输出)"))
                                    event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
