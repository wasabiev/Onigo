package wasabiev.Onigo.Commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import wasabiev.Onigo.Game;
import wasabiev.Onigo.SendMessage;

public class ListCommand extends BaseCommand {

	private List<String> players = Game.playersInGame;
	private List<String> onis = Game.oniPlayersInGame;
	private List<String> niges = Game.nigePlayersInGame;

	public ListCommand() {
		bePlayer = false;
		name = "list";
		argLength = 0;
		usage = "<- player list";
	}

	@Override
	public boolean execute() {
		if (Game.playersInGame.size() != 0) {
			if (Game.getGame().started || Game.getGame().before_started) {
				SendMessage.message(player, null, oniplayerlist());
				SendMessage.message(player, null, nigeplayerlist());
				return true;
			} else {
				SendMessage.message(player, null, playerlist());
				return true;
			}
		} else {
			SendMessage.message(player, null, "現在参加者は存在しません。");
			return true;
		}
	}

	// 全体のプレイヤーリスト取得
	public String playerlist() {
		String playerList = ChatColor.YELLOW + "参加者(" + Game.playersInGame.size() + "名)" + ChatColor.WHITE + "： ";
		for (String pName : players) {
			Player p = plugin.getServer().getPlayer(pName);
			if (p != null) {
				pName += ", ";
				playerList += pName;
			}
		}
		return playerList;
	}

	// 鬼役のプレイヤーリスト取得
	public String oniplayerlist() {
		String oniplayerList = ChatColor.RED + "鬼役" + ChatColor.WHITE + "： ";
		for (String oName : onis) {
			Player p = plugin.getServer().getPlayer(oName);
			if (p != null) {
				oName += ", ";
				oniplayerList += oName;
			}
		}
		return oniplayerList;
	}

	// 逃げ役のプレイヤーリスト取得
	public String nigeplayerlist() {
		String nigeplayerList = ChatColor.BLUE + "逃げ役" + ChatColor.WHITE + "： ";
		for (String nName : niges) {
			Player p = plugin.getServer().getPlayer(nName);
			if (p != null) {
				nName += ", ";
				nigeplayerList += nName;
			}
		}
		return nigeplayerList;
	}

	@Override
	public boolean permission() {
		return sender.hasPermission("onigo.user.list");
	}
}
