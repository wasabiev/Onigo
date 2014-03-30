package wasabiev.Onigo.Commands;

import org.bukkit.ChatColor;

import wasabiev.Onigo.Onigo;
import wasabiev.Onigo.SendMessage;

public class HelpCommand extends BaseCommand {
	public HelpCommand() {
		bePlayer = false;
		name = "help";
		argLength = 0;
		usage = "<- show command help";
	}

	@Override
	public boolean execute() {
		SendMessage.message(null, sender, ChatColor.RED + "===================================");
		// 全コマンドをループで表示
		for (BaseCommand cmd : Onigo.commands.toArray(new BaseCommand[0])) {
			cmd.sender = this.sender;
			if (cmd.permission()) {
				SendMessage.message(null, sender, ChatColor.GRAY + "/" + command + " " + ChatColor.RED + cmd.name + " " + ChatColor.GRAY + cmd.usage);
			}
		}
		SendMessage.message(null, sender, ChatColor.RED + "===================================");

		return true;
	}

	@Override
	public boolean permission() {
		return true;
	}
}
