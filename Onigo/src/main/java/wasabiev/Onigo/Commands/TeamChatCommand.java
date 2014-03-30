package wasabiev.Onigo.Commands;

import org.bukkit.ChatColor;

import wasabiev.Onigo.Game;
import wasabiev.Onigo.SendMessage;

public class TeamChatCommand extends BaseCommand {

	public TeamChatCommand() {
		bePlayer = true;
		name = "tc";
		argLength = 1;
		usage = "<message> <- send to your team";
	}

	@Override
	public boolean execute() {
		if (args.get(0) == null) {
			SendMessage.message(player, null, "メッセージが入力されていません。/onigo tc <message>");
		} else if ((Game.getGame().started || Game.getGame().before_started) && Game.playersInGame.contains(playerName)) {
			String[] array = args.toArray(new String[args.size()]);
			String message = buildMessage(array, 0);
			if (Game.nigePlayersInGame.contains(playerName)) {
				SendMessage.messageNige(ChatColor.BLUE + "[逃]" + ChatColor.WHITE + playerName + ": " + message);
			} else if (Game.oniPlayersInGame.contains(playerName)) {
				SendMessage.messageOni(ChatColor.RED + "[鬼]" + ChatColor.WHITE + playerName + ": " + message);
			}
		} else {
			SendMessage.message(player, null, "チームが存在しないか、鬼ごっこに参加していないため、チームメッセージを送信できません。");
		}
		return true;
	}

	public String buildMessage(String[] input, int startArg) {
		StringBuilder sb = new StringBuilder(input[startArg]);
		for (int i = ++startArg; i < input.length; i++) {
			sb.append(' ').append(input[i]);
		}
		return sb.toString();
	}

	@Override
	public boolean permission() {
		return sender.hasPermission("onigo.user.teamchat");
	}

}
