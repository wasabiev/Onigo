package wasabiev.Onigo.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import wasabiev.Onigo.Onigo;

public abstract class BaseCommand {

	// Logger
	protected static final Logger log = Onigo.log;
	protected static final String logPrefix = Onigo.logPrefix;
	protected static final String msgPrefix = Onigo.msgPrefix;

	/* コマンド関係 */
	public CommandSender sender;
	public List<String> args = new ArrayList<String>();
	public String name;
	public int argLength = 0;
	public String usage;
	public boolean bePlayer = true;
	public String command;
	public Onigo plugin;
	public Player player;
	public String playerName;

	public boolean run(Onigo plugin, CommandSender sender, String[] preargs, String cmd) {
		this.plugin = plugin;
		this.sender = sender;
		this.command = cmd;

		// 引数をソート
		args.clear();
		for (String arg : preargs)
			args.add(arg);

		// 引数からコマンドの部分を取り除く
		// (コマンド名に含まれる半角スペースをカウント、リストの先頭から順にループで取り除く)
		for (int i = 0; i < name.split(" ").length && i < args.size(); i++)
			args.remove(0);

		// 引数の長さチェック
		if (argLength > args.size()) {
			sendUsage();
			return true;
		}

		// 実行にプレイヤーであることが必要かチェックする
		if (bePlayer == true && !(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command cannot run from Console!");
			return true;
		}
		if (sender instanceof Player) {
			player = (Player) sender;
			playerName = player.getName();
		}

		// 権限チェック
		if (!permission()) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to use this!");
			return true;
		}

		// 実行
		return execute();
	}

	/**
	 * コマンドを実際に実行する
	 *
	 * @return 成功すればtrue それ以外はfalse
	 */
	public abstract boolean execute();

	/**
	 * コマンド実行に必要な権限を持っているか検証する
	 *
	 * @return trueなら権限あり、falseなら権限なし
	 */
	public abstract boolean permission();

	/**
	 * コマンドの使い方を送信する
	 */
	private void sendUsage() {
		sender.sendMessage(ChatColor.RED + "/" + this.command + " " + name + " " + usage);
	}
}
