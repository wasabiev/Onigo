package wasabiev.Onigo.Commands;

import wasabiev.Onigo.SendMessage;
import wasabiev.Onigo.TeleportMethods;

public class AdminSetWaitLocationCommand extends BaseCommand {

	public AdminSetWaitLocationCommand() {
		bePlayer = true;
		name = "setwait";
		argLength = 0;
		usage = "<- set teleport waitlocation";
	}

	@Override
	public boolean execute() {
		TeleportMethods.setWaitLocation(playerName);
		SendMessage.message(player, null, "待機場所を設定しました。");
		return true;
	}

	@Override
	public boolean permission() {
		return sender.hasPermission("onigo.admin.tp.setwait");
	}

}
