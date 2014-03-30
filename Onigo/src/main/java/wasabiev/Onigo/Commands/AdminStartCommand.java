package wasabiev.Onigo.Commands;

import wasabiev.Onigo.Game;
import wasabiev.Onigo.SendMessage;

public class AdminStartCommand extends BaseCommand {

	public AdminStartCommand() {
		bePlayer = true;
		name = "start";
		argLength = 0;
		usage = "<- start";
	}

	@Override
	public boolean execute() {
		// 開始準備状態確認
		if (!Game.getGame().ready) {
			SendMessage.message(player, null, "開始準備フラグがFalseです。");
			return true;
		}
		if(Game.playersInGame.size()==0){
			SendMessage.message(player, null, "参加者が一人も居ないためスタート出来ません。");
			return true;
		}
		Game.getGame().randOni();
		return true;
	}

	@Override
	public boolean permission() {
		return sender.hasPermission("onigo.admin.start");
	}
}
