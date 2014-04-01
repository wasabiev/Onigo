package wasabiev.Onigo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import wasabiev.Onigo.Commands.AdminCheckCommand;
import wasabiev.Onigo.Commands.AdminChooseOniCommand;
import wasabiev.Onigo.Commands.AdminKickCommand;
import wasabiev.Onigo.Commands.AdminReadyCommand;
import wasabiev.Onigo.Commands.AdminReloadCommand;
import wasabiev.Onigo.Commands.AdminSetMainTimerCommand;
import wasabiev.Onigo.Commands.AdminSetWaitLocationCommand;
import wasabiev.Onigo.Commands.AdminStartCommand;
import wasabiev.Onigo.Commands.AdminStopCommand;
import wasabiev.Onigo.Commands.AdminTpWaitLocationCommand;
import wasabiev.Onigo.Commands.AllChatCommand;
import wasabiev.Onigo.Commands.BaseCommand;
import wasabiev.Onigo.Commands.HelpCommand;
import wasabiev.Onigo.Commands.InfoCommand;
import wasabiev.Onigo.Commands.JoinCommand;
import wasabiev.Onigo.Commands.LeaveCommand;
import wasabiev.Onigo.Commands.ListCommand;
import wasabiev.Onigo.Commands.TeamChatCommand;
import wasabiev.Onigo.Commands.TimerCheckCommand;

public class Onigo extends JavaPlugin implements CommandExecutor {

	/*
	 * TODO:
	 *
	 * エリア指定
	 *
	 * configから設定を読み込む→reloadコマンド
	 *
	 * 鬼ごっこ参加者をdynmapからhideする機能
	 */

	/*
	 * DONE:
	 *
	 * 付与された経験値の量を表示させる。
	 *
	 * 捕まえた鬼役に対してランダムで100～1000の経験値付与
	 *
	 * 待機場所へのワープ
	 *
	 * コマンドによってメインタイマーの制限時間を設定する。
	 *
	 * finish時、ゲームが終了したことのお知らせ、リストからremoveしたことのお知らせの追加
	 *
	 * 参加者人数の表示（Listコマンド)
	 *
	 * 追放コマンド
	 *
	 * 全体チャット
	 *
	 * チームチャット
	 *
	 * コマンドの実装
	 *
	 * パーミッションの実装
	 *
	 * メインスレッド負荷軽減
	 *
	 * 勝利者へランダムで経験値付与
	 *
	 * 鬼役から攻撃を受けた際、攻撃を受けた逃げ役のプレイヤーを鬼に変更する
	 *
	 * Collections.synchronizedListによるArrayListの同期化及びListインターフェイスの実装
	 *
	 * ArrayListによるプレイヤーの管理
	 *
	 * タイマー（別クラスに分ける）
	 *
	 * コマンドのクラス分け
	 *
	 * ランダムで鬼を選出する
	 */

	// ** Logger **
	public final static Logger log = Logger.getLogger("Minecraft");
	public final static String logPrefix = "[Onigo] ";
	public final static String msgPrefix = ChatColor.GOLD + "[Onigo] " + ChatColor.WHITE;

	// ** Commands **
	public static List<BaseCommand> commands = new ArrayList<BaseCommand>();

	// ** Listener **
	private final EventListener eventListener = new EventListener(this);

	// ** Instance **
	public static Onigo plugin;

	/**
	 * プラグイン起動処理
	 */
	public void onEnable() {
		plugin = this;
		PluginManager pm = getServer().getPluginManager();

		// プラグインが無効の場合進まない
		if (!pm.isPluginEnabled(this)) {
			return;
		}

		// コマンド登録
		registerCommands();

		// Regist Listener
		pm.registerEvents(eventListener, this);

		// スタートログの出力
		log.info("Onigo plugin version " + plugin.getDescription().getVersion() + " has been enabled.");

		Plugin dynmap = pm.getPlugin("dynmap");
        if(dynmap == null) {
            getLogger().log(Level.SEVERE, "Cannot find dynmap!");
            return;
        }
	}

	/**
	 * プラグイン停止処理
	 */
	public void onDisable() {
		// タスクをすべて止める
		getServer().getScheduler().cancelTasks(this);

		// ストップログの出力
		log.info("Onigo plugin has been disabled.");
	}

	/**
	 * コマンドを登録
	 */
	public void registerCommands() {
		// 募集開始
		commands.add(new AdminReadyCommand());
		// 開始
		commands.add(new AdminStartCommand());
		// 強制終了
		commands.add(new AdminStopCommand());
		// 設定・フラグチェック
		commands.add(new AdminCheckCommand());
		// キック
		commands.add(new AdminKickCommand());
		// 参加
		commands.add(new JoinCommand());
		// 離脱
		commands.add(new LeaveCommand());
		// 参加者リスト表示
		commands.add(new ListCommand());
		// 参加者全体チャット
		commands.add(new AllChatCommand());
		// チームチャット
		commands.add(new TeamChatCommand());
		// 残り時間表示
		commands.add(new TimerCheckCommand());
		// メインタイマーの時間をセットする
		commands.add(new AdminSetMainTimerCommand());
		// テレポート
		commands.add(new AdminTpWaitLocationCommand());
		// テレポートセット
		commands.add(new AdminSetWaitLocationCommand());
		// 鬼役の選出
		commands.add(new AdminChooseOniCommand());
		// ヘルプ
		commands.add(new HelpCommand());
		// info
		commands.add(new InfoCommand());
		// リロード
		commands.add(new AdminReloadCommand());
	}

	/**
	 * コマンドが呼ばれた
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {
		if (cmd.getName().equalsIgnoreCase("onigo")) {
			if (args.length == 0) {
				// 引数ゼロはヘルプ表示
				args = new String[] { "help" };
			}

			outer: for (BaseCommand command : commands.toArray(new BaseCommand[0])) {
				String[] cmds = command.name.split(" ");
				for (int i = 0; i < cmds.length; i++) {
					if (i >= args.length || !cmds[i].equalsIgnoreCase(args[i])) {
						continue outer;
					}
					// 実行
					return command.run(this, sender, args, commandLabel);
				}
			}
			// 有効コマンドなし ヘルプ表示
			new HelpCommand().run(this, sender, args, commandLabel);
			return true;
		}
		return false;
	}
}
