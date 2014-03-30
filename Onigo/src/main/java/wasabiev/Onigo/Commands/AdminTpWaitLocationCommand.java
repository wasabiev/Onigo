package wasabiev.Onigo.Commands;

import wasabiev.Onigo.SendMessage;
import wasabiev.Onigo.TeleportMethods;

public class AdminTpWaitLocationCommand extends BaseCommand {

	public AdminTpWaitLocationCommand() {
		bePlayer = true;
		name = "wait";
		argLength = 0;
		usage = "<- teleport waitlocation";
	}

	@Override
	public boolean execute() {
		if (TeleportMethods.tpWaitLocation(playerName)) {
			SendMessage.message(player, null, "待機場所にワープしました。");
		} else {
			SendMessage.message(player, null, "待機場所が設定されていない等の原因でワープ出来ません。");
		}

		return true;
	}

	@Override
	public boolean permission() {
		return sender.hasPermission("onigo.admin.tp.wait");
	}
}
