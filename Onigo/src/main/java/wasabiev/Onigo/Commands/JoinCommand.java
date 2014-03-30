package wasabiev.Onigo.Commands;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import wasabiev.Onigo.Game;
import wasabiev.Onigo.SendMessage;

public class JoinCommand extends BaseCommand {

	public JoinCommand() {
		bePlayer = true;
		name = "join";
		argLength = 0;
		usage = "<- join the game";
	}

	@Override
	public boolean execute() {
		// ゲームの開始有無チェック
		if (Game.getGame().started || Game.getGame().before_started) {
			SendMessage.message(player, null, "既にゲームが開始されています。途中参加は出来ません。");
			return true;
		}
		// ゲームの参加受付チェック
		if (!Game.getGame().ready) {
			SendMessage.message(player, null, "現在参加者を受付中ではありません。");
			return true;
		}
		// 重複参加チェック
		if (Game.playersInGame.contains(null)) {
			SendMessage.message(player, null, "既にあなたは参加しています。");
			return true;
		}
		// 参加
		if (!Game.playersInGame.contains(playerName)) {
			Game.playersInGame.add(playerName);
			// 参加者にメッセージ送信
			SendMessage.messageAll(ChatColor.YELLOW + playerName + ChatColor.WHITE + "が鬼ごっこに参加しました。");
			// 頭に装備がある場合警告文送信
			ItemStack headItem = player.getInventory().getHelmet();
			if (headItem != null) {
				SendMessage.message(player, null, ChatColor.RED + "ゲーム開始前に頭の装備を外してください。外していない場合、強制的に頭の装備を消去します。");
			}
		} else {
			SendMessage.message(player, null, "あなたは既に鬼ごっこに参加しています。");

		}

		return true;
	}

	@Override
	public boolean permission() {
		return sender.hasPermission("onigo.user.join");
	}

}
