package wasabiev.Onigo.Commands;

import org.bukkit.command.CommandException;

import wasabiev.Onigo.Game;
import wasabiev.Onigo.SendMessage;

public class LeaveCommand extends BaseCommand {

	public LeaveCommand() {
		bePlayer = true;
		name = "leave";
		argLength = 0;
		usage = "<- leave the game";
	}

	@Override
	public boolean execute() throws CommandException {
		// ゲームに参加していない場合
		if (!Game.playersInGame.contains(playerName)) {
			SendMessage.message(player, null, "あなたは参加していません。");
			return true;
		}
		// ゲームに参加している場合
		else {
			if (Game.getGame().started || Game.getGame().before_started) {
				if (!player.hasPermission("onigo.user,leave.game")) {
					SendMessage.message(player, null, "あなたは途中離脱する権限を持っていません。");
					return true;
				} else {
					SendMessage.messageAll(playerName + "は鬼ごっこから離脱しました");
					Game.playersInGame.remove(playerName);
					// 鬼役で参加していた場合
					if (Game.oniPlayersInGame.contains(playerName)) {
						Game.oniPlayersInGame.remove(playerName);
						// 鬼役の人数チェック
						if (Game.oniPlayersInGame.size() == 0) {
							SendMessage.messageAll("全ての鬼役のプレイヤーが居なくなりました");
							SendMessage.messageAll("ゲームを強制終了します");
							Game.getGame().forcedFinish();
							Game.getGame().cancelTimerTask();
						}
					}
					// 逃げ役で参加していた場合
					if (Game.nigePlayersInGame.contains(playerName)) {
						Game.nigePlayersInGame.remove(playerName);
						// 逃げ役の人数チェック
						if (Game.nigePlayersInGame.size() == 0) {
							SendMessage.messageAll("全ての逃げ役のプレイヤーが居なくなりました");
							SendMessage.messageAll("ゲームを終了します");
							Game.getGame().finish();
							Game.getGame().cancelTimerTask();
						}
					}
				}
			} else if (Game.getGame().ready) {
				if (!player.hasPermission("onigo.user.leave.ready")) {
					SendMessage.message(player, null, "あなたは参加をキャンセルする権限を持っていません。");
				} else {
					SendMessage.messageAll(playerName + "は参加をキャンセルしました。");
					Game.playersInGame.remove(playerName);
				}
			} else {
				SendMessage.message(player, null, "内部エラー：LeaveCommand.class");
				log.warning(logPrefix + "Internal Exception on LeaveCommand.class");
			}
		}
		return true;
	}

	@Override
	public boolean permission() {
		if (sender.hasPermission("onigo.user.leave.game") || (sender.hasPermission("onigo.user.leave.ready"))) {
			return true;
		} else {
			return false;
		}
	}
}
