package wasabiev.Onigo.Commands;

import org.bukkit.ChatColor;

import wasabiev.Onigo.Game;
import wasabiev.Onigo.SendMessage;

public class AdminStopCommand extends BaseCommand {

	public AdminStopCommand() {
		bePlayer = false;
		name = "stop";
		argLength = 0;
		usage = "<- stop";
	}

	@Override
	public boolean execute() {
		// メッセージ送信
		SendMessage.message(player, null, ChatColor.RED + "Onigo plugin has been forced-finish!");
		SendMessage.messageAll("鬼ごっこを初期化し強制終了しました。");
		// 強制終了
		Game.getGame().forcedFinish();
		return true;
	}

	@Override
	public boolean permission() {
		return sender.hasPermission("onigo.admin.stop");
	}
}
