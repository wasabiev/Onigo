package wasabiev.Onigo.Commands;

import org.bukkit.ChatColor;

import wasabiev.Onigo.SendMessage;

public class InfoCommand extends BaseCommand {

	public InfoCommand() {
		bePlayer = false;
		name = "info";
		argLength = 0;
		usage = "<- plugin info";
	}

	@Override
	public boolean execute() {
		SendMessage.message(player, null, ChatColor.AQUA + "Onigo Plugin version " + plugin.getDescription().getVersion() + " by wasabiev");
		return true;
	}

	@Override
	public boolean permission() {

		return true;
	}

}
