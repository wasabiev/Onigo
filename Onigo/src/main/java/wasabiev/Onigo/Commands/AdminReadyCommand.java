package wasabiev.Onigo.Commands;

import org.bukkit.ChatColor;

import wasabiev.Onigo.Game;
import wasabiev.Onigo.SendMessage;

public class AdminReadyCommand extends BaseCommand {

	public AdminReadyCommand() {
		bePlayer = false;
		name = "ready";
		argLength = 0;
		usage = "<- ready";
	}

	@Override
	public boolean execute() {
		// 参加受付開始
		Game.getGame().ready(sender);
		// 待機場所の設定の勧告
		SendMessage.message(player, null, ChatColor.RED + "待機場所の設定を忘れずにしてください！");
		// ゲームタイマー設定変更の勧告
		SendMessage.message(player, null, ChatColor.RED +"ゲームタイマーは待機受付中に変更してください！");
		return true;
	}

	@Override
	public boolean permission() {
		return sender.hasPermission("onigo.admin.ready");
	}

}
