package xyz.wagyourtail.jsmacros.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.loader.api.FabricLoader;
import xyz.wagyourtail.jsmacros.api.sharedinterfaces.IRawMacro;
import xyz.wagyourtail.jsmacros.JsMacros;
import xyz.wagyourtail.jsmacros.api.sharedinterfaces.IConfig;

public class ConfigManager implements IConfig {
    public ConfigOptions options;
    public final File configFolder = new File(FabricLoader.getInstance().getConfigDir().toFile(), "jsMacros");
    public final File macroFolder = new File(configFolder, "Macros");
    public final File configFile = new File(configFolder, "options.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ConfigManager() {
        options = new ConfigOptions(true, "default", RawMacro.SortMethod.Enabled, new HashMap<>(), new LinkedHashMap<>());
        options.profiles.put("default", new ArrayList<>());
        options.profiles.get("default").add(new RawMacro(IRawMacro.MacroType.KEY_RISING, "key.keyboard.j", "test.js", true));
        if (!macroFolder.exists()) {
            macroFolder.mkdirs();
        }
        final File tf = new File(macroFolder, "test.js");
        if (!tf.exists()) try {
            tf.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        try {
            options = gson.fromJson(new FileReader(configFile), ConfigOptions.class);
        } catch (Exception e) {
            System.out.println("Config Failed To Load.");
            e.printStackTrace();
            if (configFile.exists()) {
                final File back = new File(configFolder, "options.json.bak");
                if (back.exists()) back.delete();
                configFile.renameTo(back);
            }
            saveConfig();
        }
        System.out.println("Loaded Profiles:");
        for (String key : JsMacros.config.options.profiles.keySet()) {
            System.out.println("    " + key);
        }

    }

    public void saveConfig() {
        try {
            final FileWriter fw = new FileWriter(configFile);
            fw.write(gson.toJson(options));
            fw.close();
        } catch (Exception e) {
            System.out.println("Config Failed To Save.");
            e.printStackTrace();
        }
    }
    
    public Comparator<RawMacro> getSortComparator() {
        if (options.sortMethod == null) options.sortMethod = RawMacro.SortMethod.Enabled;
        switch(options.sortMethod) {
            default:
            case Enabled:
                return new RawMacro.SortByEnabled();
            case FileName:
                return new RawMacro.SortByFileName();
            case TriggerName:
                return new RawMacro.SortByTriggerName();
        }
    }
    
    public void setSortComparator(RawMacro.SortMethod method) {
        options.sortMethod = method;
    }
}