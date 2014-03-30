package wasabiev.Onigo.Commands;

import org.bukkit.ChatColor;

import wasabiev.Onigo.Game;
import wasabiev.Onigo.SendMessage;

public class AllChatCommand extends BaseCommand {

	public AllChatCommand() {
		bePlayer = true;
		name = "c";
		argLength = 1;
		usage = "<message> <- send to all";
	}

	@Override
	public boolean execute() {
		if (args.get(0) == null) {
			SendMessage.message(player, null, "メッセージが入力されていません。/onigo c <message>");
		} else if (Game.getGame().started || Game.getGame().before_started || Game.getGame().ready) {
			if (Game.playersInGame.contains(playerName)) {
				String[] array = args.toArray(new String[args.size()]);
				String message = buildMessage(array, 0);
				SendMessage.messageAll(ChatColor.AQUA + "[全]" + ChatColor.WHITE + playerName + ": " + message);
				return true;
			} else {
				SendMessage.message(player, null, "鬼ごっこに参加してください。");
				return true;
			}
		} else {
			SendMessage.message(player, null, "鬼ごっこが開始されていないか、参加受付中ではありません。");
			return true;
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
		return sender.hasPermission("onigo.user.chat");
	}
}
