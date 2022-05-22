package fr.birdo.endercargo;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EnderCargoData {

    public static void linkCargo(Location input, Location output) {
        FileConfiguration cfg = getConfigFile();
        List outputList = new ArrayList<String>();
        if (getLinkedCargo() != null) {
            for (String str : getLinkedCargo()) {
                outputList.add(str);
            }
        }
        String out = output.getWorld().getName() + " " + (int) output.getX() + " " + (int) output.getY() + " " + (int) output.getZ();
        outputList.add(out);
        cfg.set("output", outputList);
        cfg.set(out, input.getWorld().getName() + " " + (int) input.getX() + " " + (int) input.getY() + " " + (int) input.getZ());
        saveFile(cfg);
    }

    public static void unlinkCargo(Location output) {
        FileConfiguration cfg = getConfigFile();
        List outputList = new ArrayList<String>();
        if (getLinkedCargo() != null) {
            for (String str : getLinkedCargo()) {
                outputList.add(str);
            }
        }
        String out = output.getWorld().getName() + " " + (int) output.getX() + " " + (int) output.getY() + " " + (int) output.getZ();
        outputList.remove(out);
        cfg.set("output", outputList);
        cfg.set(out, null);
        saveFile(cfg);
    }

    public static Location getInputCargo(Location output) {
        if (getConfigFile().getString(output.getWorld().getName() + " " + (int) output.getX() + " " + (int) output.getY() + " " + (int) output.getZ()) != null) {
            String[] str = getConfigFile().getString(output.getWorld().getName() + " " + (int) output.getX() + " " + (int) output.getY() + " " + (int) output.getZ()).split(" ");
            return new Location(Bukkit.getWorld(str[0]), Integer.parseInt(str[1]), Integer.parseInt(str[2]), Integer.parseInt(str[3]));
        }
        return null;
    }

    public static List<String> getLinkedCargo() {
        return (List<String>) getConfigFile().getList("output");
    }

    private static FileConfiguration getConfigFile() {
        return YamlConfiguration.loadConfiguration(getFile());
    }

    private static File getFile() {
        return new File(EnderCargo.dataFolderPath + "/EnderCargoData.json");
    }

    private static void saveFile(FileConfiguration fileConfiguration) {
        try {
            fileConfiguration.save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
