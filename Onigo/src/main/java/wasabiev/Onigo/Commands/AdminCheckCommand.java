package wasabiev.Onigo.Commands;

import wasabiev.Onigo.Game;
import wasabiev.Onigo.SendMessage;
import wasabiev.Onigo.TeleportMethods;

public class AdminCheckCommand extends BaseCommand {

	public AdminCheckCommand() {
		bePlayer = false;
		name = "check";
		argLength = 0;
		usage = "<- check";
	}

	@Override
	public boolean execute() {
		// 各種フラグ確認
		if (Game.getGame().ready) {
			SendMessage.message(player, null, "ready：true");
		} else {
			SendMessage.message(player, null, "ready：false");
		}
		if (Game.getGame().started) {
			SendMessage.message(player, null, "started：true");
		} else {
			SendMessage.message(player, null, "started：false");
		}
		if (Game.getGame().before_started) {
			SendMessage.message(player, null, "before_started：true");
		} else {
			SendMessage.message(player, null, "before_started：false");
		}
		// タイマー確認
		SendMessage.message(player, null, "ゲームタイマー：" + Game.getGame().gameTimeInSec / 60 + "分");
		// 待機場所設定確認
		if (TeleportMethods.waitLocation != null) {
			SendMessage.message(player, null, "待機場所：設定済み");
		} else {
			SendMessage.message(player, null, "待機場所：未設定");
		}

		return true;
	}

	@Override
	public boolean permission() {
		return sender.hasPermission("onigo.admin.check");
	}

}
