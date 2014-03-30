package wasabiev.Onigo.Commands;

import wasabiev.Onigo.Game;
import wasabiev.Onigo.TheMethods;

public class AdminChooseOniCommand extends BaseCommand {

	public AdminChooseOniCommand() {
		bePlayer = false;
		name = "chooseoni";
		argLength = 0;
		usage = "<- chooseoni";
	}

	@Override
	public boolean execute() {
		if (TheMethods.manualOni(sender)) {
			Game.getGame().before_start();
			return true;
		}
		return true;
	}

	@Override
	public boolean permission() {
		return sender.hasPermission("onigo.admin.chooseoni");
	}
}
