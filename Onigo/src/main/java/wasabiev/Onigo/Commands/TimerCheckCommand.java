package wasabiev.Onigo.Commands;

import wasabiev.Onigo.Game;
import wasabiev.Onigo.SendMessage;

public class TimerCheckCommand extends BaseCommand {

	public TimerCheckCommand() {
		bePlayer = true;
		name = "timer";
		argLength = 0;
		usage = "<- show remain timer";
	}

	@Override
	public boolean execute() {
		if (Game.getGame().started) {
			int remainMin = Game.getGame().remainTimer() / 60;
			int remainSec = Game.getGame().remainTimer() % 60;
			if (remainMin == 0) {
				SendMessage.message(player, null, "残り時間は" + remainSec + "です。");

			} else {
				SendMessage.message(player, null, "残り時間は" + remainMin + "分" + remainSec + "秒です。");

			}
			return true;
		} else {
			SendMessage.message(player, null, "メインタイマーがスタートしていません。");
			return true;
		}
	}

	@Override
	public boolean permission() {
		return sender.hasPermission("onigo.user.timer");
	}
}
