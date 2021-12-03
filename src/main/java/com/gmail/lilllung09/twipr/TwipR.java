package com.gmail.lilllung09.twipr;

import com.gmail.lilllung09.twipr.command.CommandRunner;
import com.gmail.lilllung09.twipr.command.CommandTabComplete;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class TwipR extends JavaPlugin {

    public static Plugin plugin = null;
    public static TwipConnection twipConnection = null;

    @Override
    public void onEnable() {
        TwipRMessage.sendMsgConsol("[TwipR 플러그인 활성화 중 입니다]");

        this.plugin = this;

        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        getServer().getPluginCommand("twipr").setExecutor(new CommandRunner(this));
        getCommand("twipr").setTabCompleter(new CommandTabComplete(this));


        this.twipConnection = new TwipConnection();
    }
    @Override
    public void onDisable() {
        TwipRMessage.sendMsgConsol("[TwipR 플러그인 비활성화 중 입니다]");
        if (this.twipConnection != null) {
            this.twipConnection.disconnectAllStremaer();
        }
    }

    public static void reload() {
        if (twipConnection != null) {
            twipConnection.disconnectAllStremaer();
            twipConnection = new TwipConnection();
        }
    }



}
