package fr.birdo.endercargo.items;

import fr.birdo.endercargo.EnderCargo;
import fr.birdo.endercargo.Utils.EnderCargoData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;

public class EnderCargoLinker implements Listener {

    public static final String itemName = "§3Ender Cargo Linker";
    public static final Material itemMaterial = Material.ENDER_EYE;
    private static Map<Player, Location> input = new HashMap<>();

    public EnderCargoLinker(EnderCargo enderCargo) {
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getItem() != null) {
            if (event.getItem().getType() == itemMaterial) {
                if (event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasDisplayName() && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(itemName)) {
                    event.setCancelled(true);
                    if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().isSneaking()) {
                        if (event.getClickedBlock().getType() == Material.DISPENSER) {
                            Dispenser d = (Dispenser) event.getClickedBlock().getState();
                            if (d.getCustomName().equalsIgnoreCase(EnderCargoInput.blockName)) {//Ender Input
                                boolean linked = false;
                                for (String string : EnderCargoData.getLinkedCargo()) {
                                    String[] str = string.split(" ");
                                    Location location = new Location(Bukkit.getWorld(str[0]), Integer.parseInt(str[1]), Integer.parseInt(str[2]), Integer.parseInt(str[3]));
                                    Location loc = event.getClickedBlock().getLocation();
                                    Location loc2 = EnderCargoData.getInputCargo(location);
                                    if (loc.getWorld() == loc2.getWorld() && loc.getX() == loc2.getX() && loc.getY() == loc2.getY() && loc.getZ() == loc2.getZ()) {
                                        event.getPlayer().sendMessage(ChatColor.RED + "Ender Cargo already linked !");
                                        linked = true;
                                    }
                                }
                                if (!linked) {
                                    input.put(event.getPlayer(), event.getClickedBlock().getLocation());
                                    event.getPlayer().sendMessage(ChatColor.GREEN + "Input Ender Cargo selected !");
                                }
                            }
                            if (d.getCustomName().equalsIgnoreCase(EnderCargoOutput.blockName) || d.getCustomName().equalsIgnoreCase(EnderCargoAdvancedOutput.blockName)) {//Output
                                boolean linked = false;
                                if (input.get(event.getPlayer()) == null) {
                                    event.getPlayer().sendMessage(ChatColor.RED + "Please select Input Ender Cargo Node !");
                                } else {
                                    for (String string : EnderCargoData.getLinkedCargo()) {
                                        String[] str = string.split(" ");
                                        Location location = new Location(Bukkit.getWorld(str[0]), Integer.parseInt(str[1]), Integer.parseInt(str[2]), Integer.parseInt(str[3]));
                                        Location loc = event.getClickedBlock().getLocation();
                                        if (loc.getWorld() == location.getWorld() && loc.getX() == location.getX() && loc.getY() == location.getY() && loc.getZ() == location.getZ()) {
                                            event.getPlayer().sendMessage(ChatColor.RED + "Ender Cargo already linked !");
                                            linked = true;
                                        }
                                    }
                                    if (!linked) {
                                        if (input.get(event.getPlayer()).getWorld().getName().equalsIgnoreCase(event.getClickedBlock().getLocation().getWorld().getName())) {
                                            event.getPlayer().sendMessage(ChatColor.RED + "You can't link Ender Cargo in a same world !");
                                        } else {
                                            EnderCargoData.linkCargo(input.get(event.getPlayer()), event.getClickedBlock().getLocation());
                                            input.remove(event.getPlayer());
                                            event.getPlayer().sendMessage(ChatColor.GREEN + "Ender Cargo Linked !");
                                        }
                                    }
                                }
                            }
                        }
                    } else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                        input.remove(event.getPlayer());
                        event.getPlayer().sendMessage(ChatColor.GREEN + "Ender Cargo Linker has been reset !");
                    }
                }
            }
        }
    }
}
