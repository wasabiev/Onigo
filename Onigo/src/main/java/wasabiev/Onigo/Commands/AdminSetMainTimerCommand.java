package wasabiev.Onigo.Commands;

import org.bukkit.ChatColor;

import wasabiev.Onigo.Game;
import wasabiev.Onigo.SendMessage;

public class AdminSetMainTimerCommand extends BaseCommand {

	public AdminSetMainTimerCommand() {
		bePlayer = false;
		name = "settimer";
		argLength = 1;
		usage = "<minites> <- set main game time in minites";
	}

	@Override
	public boolean execute() {
		if (args.get(0) != null) {
			if (Game.getGame().ready) {
				int setMin = Integer.parseInt(args.get(0));
				int setSec = setMin * 60;
				Game.getGame().remainSec = Game.getGame().gameTimeInSec = setSec;
				SendMessage.messageAll(ChatColor.GREEN + "ゲームタイマーを" + Game.getGame().remainSec / 60 + "分に設定しました。");
			} else {
				SendMessage.message(player, null, ChatColor.GREEN + "参加受付中でなければタイマーを変更できません。");
			}
		} else {
			SendMessage.message(player, null, "引数が足りません。/onigo settimer <(int)minites>");
		}
		return true;
	}

	@Override
	public boolean permission() {
		return sender.hasPermission("onigo.admin.settimer");
	}

}
