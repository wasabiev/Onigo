package wasabiev.Onigo.Hook;

import org.bukkit.plugin.Plugin;
import org.dynmap.DynmapCommonAPI;

import wasabiev.Onigo.Onigo;

public class Dynmap {

	private static DynmapCommonAPI api;
	private static Onigo plugin;

	public Dynmap(Onigo plugin){
		this.plugin = plugin;
	}

	public static void hidePlayer(String playerName) {
		Plugin dynmap = Onigo.plugin.getServer().getPluginManager().getPlugin("dynmap");
		api = (DynmapCommonAPI)dynmap;
		if (api.getPlayerVisbility(playerName)) {
			api.setPlayerVisiblity(playerName, false);
		} else {
			return;
		}
	}

	public static void showPlayer(String playerName) {
		if (!api.getPlayerVisbility(playerName)) {
			api.setPlayerVisiblity(playerName, true);
		} else {
			return;
		}
	}
}
