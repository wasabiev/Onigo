package wasabiev.Onigo;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class TeleportMethods {

	public static Location waitLocation = null;

	public static void init() {
		waitLocation = null;
	}

	public static boolean setWaitLocation(String playerName) {
		Player player = Bukkit.getPlayer(playerName);
		if (player != null && player.isOnline()) {
			waitLocation = player.getLocation();
			return true;
		}
		return false;
	}

	public static boolean tpWaitLocation(String playerName) {
		Player player = Bukkit.getPlayer(playerName);
		if (waitLocation != null) {
			if (player != null && player.isOnline()) {
				Entity vehicle = player.getVehicle();
				if (vehicle != null) {
					player.leaveVehicle();
				}
				player.teleport(waitLocation, TeleportCause.PLUGIN);
				return true;
			}
		}
		return false;
	}
}
