package wasabiev.Onigo.Commands;

import org.bukkit.ChatColor;

import wasabiev.Onigo.Game;
import wasabiev.Onigo.SendMessage;

public class AdminKickCommand extends BaseCommand {

	public AdminKickCommand() {
		bePlayer = false;
		name = "kick";
		argLength = 1;
		usage = "<player> <- kick player from onigo";
	}

	@Override
	public boolean execute() {
		if (args.get(0) != null) {
			String kickedPlayer = args.get(0);
			if (Game.playersInGame.contains(kickedPlayer)) {
				SendMessage.messageAll(ChatColor.RED + kickedPlayer + ChatColor.WHITE + "は" + ChatColor.BLUE + playerName + ChatColor.WHITE + "により鬼ごっこから追放されました。");
				log.info(kickedPlayer + "は" + playerName + "により鬼ごっこから追放されました。");
				Game.playersInGame.remove(kickedPlayer);
				if (Game.oniPlayersInGame.contains(kickedPlayer)) {
					Game.oniPlayersInGame.remove(kickedPlayer);
					if (Game.oniPlayersInGame.size() == 0) {
						SendMessage.messageAll("全ての鬼役のプレイヤーが居なくなりました。");
						SendMessage.messageAll("ゲームを強制終了します。");
						Game.getGame().forcedFinish();
						Game.getGame().cancelTimerTask();
					}
				} else if (Game.nigePlayersInGame.contains(kickedPlayer)) {
					Game.nigePlayersInGame.remove(kickedPlayer);
					if (Game.nigePlayersInGame.size() == 0) {
						SendMessage.messageAll("全ての逃げ役のプレイヤーが居なくなりました。");
						SendMessage.messageAll("ゲームを終了します。");
						Game.getGame().finish();
						Game.getGame().cancelTimerTask();
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean permission() {
		return sender.hasPermission("onigo.admin.kick");
	}
}
