package wasabiev.Onigo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

//import org.dynmap.DynmapAPI;

;

public class Game {

	// Logger
	public static final Logger log = Onigo.log;
	private static final String logPrefix = Onigo.logPrefix;
	private static final String msgPrefix = Onigo.msgPrefix;

	private static final Game game = new Game();

	public static Game getGame() {
		return game;
	}

	Game() {
	};

	// ゲーム設定
	public boolean ready = false;// 参加待ちしているかどうか
	public boolean started = false;// 開始しているかどうか
	public boolean before_started = false;//
	public Player freezedOni = null;// 初めの行動制限を受ける鬼役のプレイヤー

	// 参加プレイヤー管理
	// 10/31同期化→Listインターフェイスの実装
	public static List<String> playersInGame = Collections.synchronizedList(new ArrayList<String>());
	public static List<String> oniPlayersInGame = Collections.synchronizedList(new ArrayList<String>());
	public static List<String> nigePlayersInGame = Collections.synchronizedList(new ArrayList<String>());

	// スタート前タイマー

	private int starttimerThreadID = -1; // スタート前タイマーのID
	private int starttimerInSec = 60;// スタート前の時間
	private int remainstarttimerInSec = starttimerInSec;// 残り時間

	// メインタイマー
	private int timerThreadID = -1; // タイマータスクのID
	public int gameTimeInSec = 600; // 1ゲームの制限時間
	public int remainSec = gameTimeInSec; // 1ゲームの残り時間

	// TODO:dynmap連携
	// private DynmapAPI dAPI;

	/**
	 * ゲームデータの初期化
	 */
	public void init() {
		/*
		 * Dynmap-Hide初期化 for (String set : playersInGame) { Player player =
		 * Bukkit.getPlayer(set); if (player != null) {
		 * dAPI.setPlayerVisiblity(player, true); } }
		 */

		// プレイヤーリスト初期化
		playersInGame.clear();
		oniPlayersInGame.clear();
		nigePlayersInGame.clear();

		// フラグ初期化
		started = false;
		ready = false;
		before_started = false;

		// 行動制限の鬼のリセット
		freezedOni = null;

		// タイマータスク終了
		if (starttimerThreadID != -1) {
			Onigo.plugin.getServer().getScheduler().cancelTask(starttimerThreadID);// タイマー停止
		}
		if (timerThreadID != -1) {
			Onigo.plugin.getServer().getScheduler().cancelTask(timerThreadID);// タイマー停止
		}

		// スタート前タイマー初期化
		starttimerThreadID = -1;
		remainstarttimerInSec = starttimerInSec = 60;

		// メインタイマー初期化
		// メインタイマーの設定は維持(11/07)
		timerThreadID = -1;
		remainSec = gameTimeInSec;

	}

	/**********
	 * ゲーム *
	 **********/

	/**
	 * 参加待ち状態にする
	 */
	public void ready(CommandSender sender) {
		if (started) {
			SendMessage.message(null, sender, "鬼ごっこが既に開始されています。");
			return;
		}
		if (ready) {
			SendMessage.message(null, sender, "鬼ごっこの参加受付中です。");
			return;
		}

		// プレイヤーリスト初期化
		playersInGame.clear();
		oniPlayersInGame.clear();
		nigePlayersInGame.clear();

		// 参加待ち状態にする
		ready = true;

		// ブロードキャスト
		SendMessage.broadCast("鬼ごっこがまもなく開始されます。");
		SendMessage.broadCast("参加される方は/onigo joinコマンドで参加してください。");

	}

	/**
	 * ランダムで鬼役のプレイヤーを選出するメソッドを呼び出すと同時に受付の締め切り
	 */
	public void randOni() {
		ready = false;
		started = false;
		before_started = true;
		SendMessage.broadCast("鬼ごっこの参加受付を締め切りました");

		// プレイヤーの頭の装備を消去する。
		for (String set : playersInGame) {
			Player player = Bukkit.getPlayer(set);
			if (player != null && player.isOnline()) {
				player.getInventory().setHelmet(null);
			}
		}

		// 参加者を待機場所までワープさせる
		for (String set : playersInGame) {
			if (set == null)
				continue;
			TeleportMethods.tpWaitLocation(set);
		}

		// 一度全ての参加者を逃げ役のプレイヤーに追加する
		if (playersInGame.size() != 0) {
			nigePlayersInGame.addAll(playersInGame);
		}

		// ランダムで鬼役のプレイヤーを選出するメソッドの呼び出し
		if (TheMethods.randOni()) {
			before_start();
		}
	}

	/**
	 * 開始までの猶予
	 */
	public void before_start() {

		ready = false;
		started = false;
		before_started = true;

		// 参加者へメッセージ送信
		SendMessage.messageAll("鬼ごっこが開始されました。");

		// 参加者を回す
		for (String set : playersInGame) {
			Player player = Bukkit.getPlayer(set);
			if (set == null)
				continue;
			if (player == null || !player.isOnline())
				continue;

			// TODO:dynmap-hide
			// dAPI.setPlayerVisiblity(player, false);

			// ゲームモード強制変更
			player.setGameMode(GameMode.SURVIVAL);

			// 体力回復
			player.setHealth(20);

			// 効果のあるポーション無効化
			if (player.hasPotionEffect(PotionEffectType.JUMP))
				player.removePotionEffect(PotionEffectType.JUMP);
			if (player.hasPotionEffect(PotionEffectType.SPEED))
				player.removePotionEffect(PotionEffectType.SPEED);

			// 逃げ役のプレイヤーに通知
			if (nigePlayersInGame.contains(set)) {
				SendMessage.message(player, null, ChatColor.WHITE + "あなたは" + ChatColor.BLUE + "逃げ役" + ChatColor.WHITE
						+ "です。");
			}
		}

		String firstOni = oniPlayersInGame.get(0);
		Player player = Bukkit.getPlayer(firstOni);

		// 鬼の行動制限を有効
		freezedOni = player;

		SendMessage.message(player, null, ChatColor.WHITE + "あなたは" + ChatColor.RED + "鬼役" + ChatColor.WHITE + "です。");
		SendMessage.message(player, null, "60秒間移動することが出来ません。");

		// スタートタイマーの呼び出し
		starttimerThreadID = Onigo.plugin.getServer().getScheduler()
				.scheduleSyncRepeatingTask(Onigo.plugin, new Runnable() {

					public void run() {
						if (remainstarttimerInSec == 0) {
							freezedOni = null;// 鬼役のプレイヤーの行動制限を解除
							start();// ゲーム開始
							if (starttimerThreadID != -1) {
								Onigo.plugin.getServer().getScheduler().cancelTask(starttimerThreadID);// タイマー停止
							}
							return;
						}
						// 5秒前
						else if (remainstarttimerInSec <= 5) {
							SendMessage.messageAll(ChatColor.GREEN + "残り" + remainstarttimerInSec + "秒で鬼が放出されます");
						}
						// 10秒前
						else if (remainstarttimerInSec == 10) {
							SendMessage.messageAll(ChatColor.GREEN + "残り" + remainstarttimerInSec + "秒で鬼が放出されます");
						}
						// 30秒前
						else if (remainstarttimerInSec == 30) {
							SendMessage.messageAll(ChatColor.GREEN + "残り" + remainstarttimerInSec + "秒で鬼が放出されます");
							SendMessage.message(freezedOni, null, ChatColor.RED + "視界を遮断します。データが破損したわけではありません。");
							TheMethods.blind(freezedOni);
						}
						remainstarttimerInSec--;
					}
				}, 0L, 20L);
	}

	/**
	 * ゲームを開始する
	 */
	public void start() {

		// ゲーム開始
		ready = false;
		started = true;
		before_started = false;

		// 鬼役のプレイヤーの行動制限を解除
		freezedOni = null;

		// 参加者へメッセージを送信
		SendMessage.messageAll("鬼役のプレイヤーが放出されました");

		// メインタイマーの呼び出し
		timerThreadID = Onigo.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(Onigo.plugin, new Runnable() {
			@Override
			public void run() {
				if (remainSec == 0) {
					if (timerThreadID != -1) {
						Onigo.plugin.getServer().getScheduler().cancelTask(timerThreadID);// タイマー停止
					}
					finish();// ゲーム終了
					return;
				}

				// 1分毎
				else if ((remainSec % 60) == 0) {
					int remainMin = remainSec / 60;
					SendMessage.messageAll(ChatColor.GREEN + "鬼ごっこ終了まであと" + remainMin + "分です");
					if (remainMin == 1) {
						for (String set : oniPlayersInGame) {
							Player player = Bukkit.getPlayer(set);
							TheMethods.speedUpOni(player);
						}
						SendMessage.messageAll(ChatColor.RED + "鬼の移動速度がアップしました。");
					}
				}
				// 30秒前
				else if (remainSec == 30) {
					SendMessage.messageAll(ChatColor.GREEN + "鬼ごっこ終了まであと" + remainSec + "秒です");
				}
				// 10秒前
				else if (remainSec == 10) {
					SendMessage.messageAll(ChatColor.GREEN + "鬼ごっこ終了まであと" + remainSec + "秒です");
				}
				// 5秒以下
				else if (remainSec <= 5) {
					SendMessage.messageAll(ChatColor.GREEN + "鬼ごっこ終了まであと" + remainSec + "秒です");
				}
				remainSec--;
			}
		}, 0L, 20L);
	}

	/**
	 * ゲーム終了。結果判定
	 */
	public void finish() {

		// 逃げ役が0だったら鬼の勝利
		if (nigePlayersInGame.size() == 0) {
			String firstOni = oniPlayersInGame.get(0);
			Player player = Bukkit.getPlayer(firstOni);
			SendMessage.messageAll(ChatColor.YELLOW + "全ての参加者が鬼役となったため、はじめの鬼役(" + ChatColor.RED + firstOni
					+ ChatColor.YELLOW + ")の勝利です！");
			if (player != null && player.isOnline()) {
				TheMethods.randExperience(player);
				SendMessage.message(player, null, ChatColor.AQUA + "見事全員を捕まえられましたのでボーナスとして" + TheMethods.randAmount
						+ "経験値を獲得しました。");
			}
		}

		// 逃げ役が残っていた場合、逃げ役の勝利とし、そのメンバーを表示する
		else if (nigePlayersInGame.size() != 0) {
			String nigeplayerList = "";
			for (String nName : nigePlayersInGame) {
				Player p = Onigo.plugin.getServer().getPlayer(nName);
				if (p != null) {
					nName += ", ";
					nigeplayerList += nName;
				}
			}
			SendMessage.messageAll(ChatColor.YELLOW + "見事" + nigePlayersInGame.size() + "人が逃げ切りました！");
			SendMessage.messageAll(ChatColor.WHITE + "逃げ切れたプレイヤーは" + ChatColor.BLUE + nigeplayerList + ChatColor.WHITE
					+ "です");
			for (String set : nigePlayersInGame) {
				Player player = Bukkit.getPlayer(set);
				if (player.isOnline()) {
					TheMethods.randExperience(player);
					SendMessage.message(player, null, ChatColor.AQUA + "見事逃げ切りましたのでボーナスとして" + TheMethods.randAmount
							+ "経験値を獲得しました。");
				}
			}
		}

		SendMessage.messageAll(ChatColor.GREEN + "以上で鬼ごっこを終了します。");
		SendMessage.messageAll(ChatColor.GREEN + "次のゲームにも参加する場合は再度Joinし直してください。");

		// 参加者を回す
		for (String set : playersInGame) {
			if (set == null)
				continue;
			Player player = Bukkit.getPlayer(set);
			// スピードポーションの効果を消す
			if (player.hasPotionEffect(PotionEffectType.SPEED)) {
				player.removePotionEffect(PotionEffectType.SPEED);
			}
			// 待機場所に移動させる
			TeleportMethods.tpWaitLocation(set);
		}

		// 初期化
		init();
	}

	/**
	 * ゲームの強制終了
	 */
	public void forcedFinish() {

		// 待機場所に移動させる
		for (String set : playersInGame) {
			if (set == null)
				continue;
			TeleportMethods.tpWaitLocation(set);
		}

		// 初期化
		init();
	}

	/******************
	 * ユーティリティ *
	 ******************/

	/**
	 * プレイヤーリストにプレイヤーを追加する
	 *
	 * @param player
	 *            or playerName
	 * @return 失敗ならfalse 成功ならtrue
	 */

	public static boolean addPlayer(String playerName) {
		if (playerName == null || playersInGame.contains(playerName)) {
			return false;
		}
		playersInGame.add(playerName);
		return true;
	}

	public static boolean addOniPlayer(String playerName) {
		if (playerName == null || oniPlayersInGame.contains(playerName)) {
			return false;
		}
		oniPlayersInGame.add(playerName);
		return true;
	}

	public static boolean addNigePlayer(String playerName) {
		if (playerName == null || nigePlayersInGame.contains(playerName)) {
			return false;
		}
		nigePlayersInGame.add(playerName);
		return true;
	}

	/**
	 * プレイヤーリストからプレイヤーを削除する
	 *
	 * @param player
	 *            削除するプレイヤー
	 * @return 失敗ならfalse 成功ならtrue
	 */

	public static boolean remPlayer(String playerName) {
		if (playerName != null) {
			if (nigePlayersInGame.contains(playerName)) {
				nigePlayersInGame.remove(playerName);
				return true;
			} else if (oniPlayersInGame.contains(playerName)) {
				oniPlayersInGame.remove(playerName);
				return true;
			}
		}
		return false;
	}

	public int remainTimer() {
		return remainSec;
	}

	/**
	 * キャンセルタイマータスク
	 */
	public void cancelTimerTask() {
		Onigo.plugin.getServer().getScheduler().cancelTask(timerThreadID);
	}
}
