package fr.birdo.endercargo;

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
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class EnderCargoOutput implements Listener {

    public static final String blockName = "§3Ender Cargo Node §b(Output)";

    private static EnderCargo instance;

    public EnderCargoOutput(EnderCargo pluginInstance) {
        instance = pluginInstance;
    }

    @EventHandler
    public void onDispense(BlockDispenseEvent event) {
        if (event.getBlock().getType() == Material.DISPENSER) {
            Dispenser d = (Dispenser) event.getBlock().getState();
            if (d.getCustomName().equalsIgnoreCase(blockName) || d.getCustomName().equalsIgnoreCase(EnderCargoAdvancedOutput.blockName)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.DISPENSER) {
            Dispenser d = (Dispenser) event.getBlock().getState();
            if (d.getCustomName().equalsIgnoreCase(blockName) || d.getCustomName().equalsIgnoreCase(EnderCargoAdvancedOutput.blockName)) {
                Container container = (Container) event.getBlock().getState();
                container.getInventory().clear();
                EnderCargoData.unlinkCargo(event.getBlock().getLocation());
            } else if (d.getCustomName().equalsIgnoreCase(EnderCargoInput.blockName)) {
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
    public void move(InventoryMoveItemEvent event) {
        Container sourceContainer = (Container) event.getSource().getHolder();
        Container destinationContainer = (Container) event.getDestination().getHolder();

        if (event.getSource().getType() == InventoryType.HOPPER) {
            if (destinationContainer.getCustomName().equalsIgnoreCase(EnderCargoAdvancedOutput.blockName) || destinationContainer.getCustomName().equalsIgnoreCase(blockName) || destinationContainer.getCustomName().equalsIgnoreCase(EnderCargoInput.blockName)) {
                event.setCancelled(true);
            }
        } else if (event.getDestination().getType() == InventoryType.HOPPER) {
            if (sourceContainer.getCustomName().equalsIgnoreCase(EnderCargoAdvancedOutput.blockName) || sourceContainer.getCustomName().equalsIgnoreCase(blockName) || sourceContainer.getCustomName().equalsIgnoreCase(EnderCargoInput.blockName)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            if (event.getClickedBlock().getType() == Material.DISPENSER) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (!event.getPlayer().isSneaking() || event.getItem() == null) {
                        Container container = (Container) event.getClickedBlock().getState();
                        if (container.getCustomName().equalsIgnoreCase(EnderCargoAdvancedOutput.blockName)) {
                            event.setCancelled(true);
                            Inventory inventory = Bukkit.createInventory(null, InventoryType.DISPENSER, "§3Ender Cargo Node view (Output)");
                            inventory.setContents(container.getInventory().getContents());
                            event.getPlayer().openInventory(inventory);
                        } else if (container.getCustomName().equalsIgnoreCase(EnderCargoInput.blockName)) {
                            event.setCancelled(true);
                            Inventory inventory = Bukkit.createInventory(null, InventoryType.DISPENSER, "§3Ender Cargo Node view (Input)");
                            inventory.setContents(container.getInventory().getContents());
                            event.getPlayer().openInventory(inventory);
                        } else if (container.getCustomName().equalsIgnoreCase(blockName)) {
                            event.setCancelled(true);
                        }
                    } else if (event.getPlayer().isSneaking() && event.getItem() != null) {
                        if (event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasDisplayName()) {
                            String displayName = event.getItem().getItemMeta().getDisplayName();
                            Container container = (Container) event.getClickedBlock().getState();
                            if (container.getCustomName().equalsIgnoreCase(EnderCargoAdvancedOutput.blockName) || container.getCustomName().equalsIgnoreCase(blockName)) {
                                if (displayName.equalsIgnoreCase("§7Cargo Node §c(Output)") || displayName.equalsIgnoreCase("§6Advanced Cargo Node §c(Output)"))
                                    event.setCancelled(true);
                            } else if (container.getCustomName().equalsIgnoreCase(EnderCargoInput.blockName)) {
                                if (displayName.equalsIgnoreCase("§7Cargo Node §c(Input)"))
                                    event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInv(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase("§3Ender Cargo Node view (Input)") || event.getView().getTitle().equalsIgnoreCase("§3Ender Cargo Node view (Output)")) {
            event.setCancelled(true);
        }
    }

    public static void setContent() {
        if (EnderCargoData.getLinkedCargo() != null) {
            for (String string : EnderCargoData.getLinkedCargo()) {

                String[] str = string.split(" ");
                Location location = new Location(Bukkit.getWorld(str[0]), Integer.parseInt(str[1]), Integer.parseInt(str[2]), Integer.parseInt(str[3]));

                Container outputContainer = (Container) location.getWorld().getBlockAt(location).getState();
                ItemStack[] outputContent = outputContainer.getInventory().getStorageContents();

                outputContainer.update();

                BukkitRunnable task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (location.getBlock().getType() == Material.DISPENSER && EnderCargoData.getInputCargo(location) != null && EnderCargoData.getInputCargo(location).getBlock().getType() == Material.DISPENSER) {

                            Container outputContainerTick = (Container) location.getWorld().getBlockAt(location).getState();
                            ItemStack[] outputContentTick = outputContainerTick.getInventory().getStorageContents();

                            Container inputContainer = (Container) location.getWorld().getBlockAt(EnderCargoData.getInputCargo(location)).getState();
                            ItemStack[] inputContent = inputContainer.getInventory().getStorageContents();

                            for (int i = 0; i < 9; i++) {
                                int nb;
                                if (outputContent[i] != null) {
                                    if (outputContentTick[i] == null) {
                                        nb = outputContent[i].getAmount();
                                    } else {
                                        nb = outputContent[i].getAmount() - outputContentTick[i].getAmount();
                                    }
                                    ItemStack item = inputContent[i];
                                    item.setAmount(inputContent[i].getAmount() - nb);
                                    inputContainer.getInventory().setItem(i, item);
                                }
                            }
                            outputContainerTick.getInventory().setContents(inputContainer.getInventory().getContents());
                        }
                    }
                };
                task.runTaskLater(instance, 1);
            }
        }
    }
}
