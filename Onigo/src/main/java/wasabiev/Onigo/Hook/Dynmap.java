package wasabiev.Onigo.Hook;

import org.dynmap.DynmapCommonAPI;

public class Dynmap {

	private DynmapCommonAPI dynmap;

	public void hidePlayer(String playerName) {
		if (dynmap.getPlayerVisbility(playerName)) {
			dynmap.setPlayerVisiblity(playerName, false);
		} else {
			return;
		}
	}

	public void showPlayer(String playerName) {
		if (!dynmap.getPlayerVisbility(playerName)) {
			dynmap.setPlayerVisiblity(playerName, true);
		} else {
			return;
		}
	}
}
