package xyz.wagyourtail.jsmacros.config;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfigOptions {
    public boolean allowPrivateAccess;
    public String defaultProfile;
    public HashMap<String, ArrayList<RawMacro>> profiles;
    
    public ConfigOptions(boolean allowPrivateAccess, String defaultProfile, HashMap<String, ArrayList<RawMacro>> profiles) {
        this.allowPrivateAccess = allowPrivateAccess;
        this.defaultProfile = defaultProfile;
        this.profiles = profiles;
    }
}