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

public class EnderCargoInput implements Listener {

    public static final String blockName = "§3末影接口 §b(输入)";
    public static final Material blockMaterial = Material.WARPED_FENCE;

    public EnderCargoInput(EnderCargo enderCargo) {
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
                for (String string : EnderCargoData.getLinkedCargo()) {
                    String[] str = string.split(" ");
                    Location location = new Location(Bukkit.getWorld(str[0]), Integer.parseInt(str[1]), Integer.parseInt(str[2]), Integer.parseInt(str[3]));
                    Location loc = event.getBlock().getLocation();
                    Location loc2 = EnderCargoData.getInputCargo(location);
                    if (loc.getWorld() == loc2.getWorld() && loc.getX() == loc2.getX() && loc.getY() == loc2.getY() && loc.getZ() == loc2.getZ()) {
                        EnderCargoData.unlinkCargo(location);
                    }
                }
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
    public void onClickInv(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase("§3末影节点视图 (输入)"))
            event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            if (event.getClickedBlock().getType() == Material.DISPENSER) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!event.getPlayer().isSneaking() || event.getItem() == null) {
                        Container container = (Container) event.getClickedBlock().getState();
                        if (container.getCustomName() != null && container.getCustomName().equalsIgnoreCase(blockName)) {
                            event.setCancelled(true);
                            Inventory inventory = Bukkit.createInventory(null, InventoryType.DISPENSER, "§3末影节点视图 (输入)");
                            inventory.setContents(container.getInventory().getContents());
                            event.getPlayer().openInventory(inventory);
                        }
                    } else if (event.getPlayer().isSneaking() && event.getItem() != null) {
                        if (event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasDisplayName()) {
                            String displayName = event.getItem().getItemMeta().getDisplayName();
                            Container container = (Container) event.getClickedBlock().getState();
                            if (container.getCustomName() != null && container.getCustomName().equalsIgnoreCase(blockName))
                                if (displayName.equalsIgnoreCase("§7节点模式 §c(输入)"))
                                    event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
