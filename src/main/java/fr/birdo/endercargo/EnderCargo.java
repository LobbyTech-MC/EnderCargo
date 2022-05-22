package fr.birdo.endercargo;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class EnderCargo extends JavaPlugin implements SlimefunAddon {

    public static String dataFolderPath;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EnderCargoInput(this), this);
        getServer().getPluginManager().registerEvents(new EnderCargoOutput(this), this);
        getServer().getPluginManager().registerEvents(new EnderCargoLinker(this), this);

        /*
         * 1. Creating a new Category
         * This Category will use the following ItemStack
         */
        ItemStack itemGroupItem = new CustomItemStack(Material.ENDER_EYE, "&5Ender Cargo", "", "&a> Click to open");

        // Give your Category a unique id.
        NamespacedKey itemGroupId = new NamespacedKey(this, "endercargo_category");
        ItemGroup itemGroup = new ItemGroup(itemGroupId, itemGroupItem);

        /*
         * 2. Create a new SlimefunItemStack
         * This class has many constructors, it is very important
         * that you give each item a unique id.
         */
        SlimefunItemStack ender_input = new SlimefunItemStack("ENDER_INPUT", Material.DISPENSER, EnderCargoInput.blockName, "&7Use the ender force to teleport", "&7items between worlds");
        SlimefunItemStack ender_output = new SlimefunItemStack("ENDER_OUTPUT", Material.DISPENSER, EnderCargoOutput.blockName, "&7Use the ender force to teleport", "&7items between worlds");
        SlimefunItemStack advanced_ender_output = new SlimefunItemStack("ADVANCED_ENDER_OUTPUT", Material.DISPENSER, EnderCargoAdvancedOutput.blockName, "&7Use the ender force to teleport", "&7items between worlds");
        SlimefunItemStack ender_linker = new SlimefunItemStack("ENDER_LINKER", Material.ENDER_EYE, "&3Ender Cargo Linker", "&7&eShift + Right Click&7 to link", "&7&eLeft Click&7 to reset");

        /*
         * 3. Creating a Recipe
         * The Recipe is an ItemStack Array with a length of 9.
         * It represents a Shaped Recipe in a 3x3 crafting grid.
         * The machine in which this recipe is crafted in is specified
         * further down as the RecipeType.
         */
        ItemStack[] ender_input_recipe = {null, SlimefunItems.INFUSED_HOPPER, null, SlimefunItems.ENDER_LUMP_3, SlimefunItems.CARGO_CONNECTOR_NODE, SlimefunItems.ENDER_LUMP_3, null, SlimefunItems.INFUSED_HOPPER, null};
        ItemStack[] ender_output_recipe = {null, SlimefunItems.INFUSED_HOPPER, null, SlimefunItems.MAGIC_LUMP_3, SlimefunItems.CARGO_CONNECTOR_NODE, SlimefunItems.MAGIC_LUMP_3, null, SlimefunItems.INFUSED_HOPPER, null};
        ItemStack[] advanced_ender_output_recipe = {null, SlimefunItems.CARGO_MOTOR, null, SlimefunItems.REINFORCED_PLATE, ender_output, SlimefunItems.REINFORCED_PLATE, null, SlimefunItems.CARGO_MOTOR, null};
        ItemStack[] ender_linker_recipe = {SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.HARDENED_METAL_INGOT, SlimefunItems.HARDENED_METAL_INGOT, new ItemStack(Material.STICK), SlimefunItems.HARDENED_METAL_INGOT, null, new ItemStack(Material.STICK), null};

        /*
         * 4. Registering the Item
         * Now you just have to register the item.
         * RecipeType.ENHANCED_CRAFTING_TABLE refers to the machine in
         * which this item is crafted in.
         * Recipe Types from Slimefun itself will automatically add the recipe to that machine.
         */
        SlimefunItem ender_input_item = new SlimefunItem(itemGroup, ender_input, RecipeType.ENHANCED_CRAFTING_TABLE, ender_input_recipe);
        ender_input_item.register(this);
        SlimefunItem ender_output_item = new SlimefunItem(itemGroup, ender_output, RecipeType.ENHANCED_CRAFTING_TABLE, ender_output_recipe);
        ender_output_item.register(this);
        SlimefunItem advanced_ender_output_item = new SlimefunItem(itemGroup, advanced_ender_output, RecipeType.ENHANCED_CRAFTING_TABLE, advanced_ender_output_recipe);
        advanced_ender_output_item.register(this);
        SlimefunItem ender_linker_item = new SlimefunItem(itemGroup, ender_linker, RecipeType.ENHANCED_CRAFTING_TABLE, ender_linker_recipe);
        ender_linker_item.register(this);

        dataFolderPath = getDataFolder().getAbsolutePath();
        File folder = new File(dataFolderPath);
        if (!folder.exists())
            folder.mkdir();
        File enderCargoData = new File(dataFolderPath + "/EnderCargoData.json");
        if (!enderCargoData.exists()) {
            try {
                enderCargoData.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            EnderCargoOutput.setContent();
        }, 0L, 1L);
    }

    @Override
    public void onDisable() {
        // Logic for disabling the plugin...
    }

    @Override
    public String getBugTrackerURL() {
        // You can return a link to your Bug Tracker instead of null here
        return null;
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        /*
         * You will need to return a reference to your Plugin here.
         * If you are using your main class for this, simply return "this".
         */
        return this;
    }

}
