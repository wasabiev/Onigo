package wasabiev.Onigo;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SendMessage {

	// Logger
	private static final Logger log = Onigo.log;
	private static final String logPrefix = Onigo.logPrefix;
	private static final String msgPrefix = Onigo.msgPrefix;

	/**
	 * 全体にメッセージを送る
	 *
	 * @param message
	 */
	public static void broadCast(String message) {
		if (message.isEmpty()) {
			return;
		}
		Bukkit.getServer().broadcastMessage(msgPrefix + message);
	}

	/**
	 * 特定のプレイヤーにメッセージを送る
	 */
	public static void message(Player player, CommandSender sender, String message) {
		if (message != null) {
			if (player != null) {
				player.sendMessage(msgPrefix + message);
			} else if (sender != null) {
				sender.sendMessage(msgPrefix + message);
			}
		}
	}

	/**
	 * 参加者全員にメッセージを送る
	 *
	 * @param message
	 */
	public static void messageAll(String message) {
		// 全チームメンバーにメッセージを送る
		for (String set : Game.playersInGame) {
			if (set == null)
				continue;
			Player player = Bukkit.getPlayer(set);
			if (player != null && player.isOnline()) {
				// メッセージ送信
				message(null, player, message);
			}
		}
		// ログ出力する
		log.info(logPrefix + message);
	}

	/**
	 * 鬼役のプレイヤー全員にメッセージを送る
	 *
	 * @param message
	 */
	public static void messageOni(String message) {
		for (String set : Game.oniPlayersInGame) {
			if (set == null)
				continue;
			Player player = Bukkit.getPlayer(set);
			if (player != null && player.isOnline())
				message(null, player, message);
		}
	}

	/**
	 * 逃げ役のプレイヤー全員にメッセージを送る
	 *
	 * @param message
	 */
	public static void messageNige(String message) {
		for (String set : Game.nigePlayersInGame) {
			if (set == null)
				continue;
			Player player = Bukkit.getPlayer(set);
			if (player != null && player.isOnline())
				message(null, player, message);
		}
	}
}
