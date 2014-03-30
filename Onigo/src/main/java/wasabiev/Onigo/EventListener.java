package wasabiev.Onigo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {

	public Onigo plugin;

	public EventListener(Onigo Instance) {
		plugin = Instance;
	}

	/**
	 * 鬼役のプレイヤーが逃げ役のプレイヤーにダメージを与えた時の処理
	 *
	 * @param event
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onOni(EntityDamageByEntityEvent event) {
		if (Game.getGame().started) {
			if ((event.getEntity() instanceof Player && event.getDamager() instanceof Player)) {
				String damager = ((Player) event.getDamager()).getName();
				String entity = ((Player) event.getEntity()).getName();
				Player Dplayer = Bukkit.getPlayer(damager);
				Player Eplayer = Bukkit.getPlayer(entity);
				// ダメージを与えたプレイヤーが鬼役かどうかの判定
				if (Game.oniPlayersInGame.contains(damager) && Game.nigePlayersInGame.contains(entity)) {

					// プレイヤーリスト変更
					Game.remPlayer(entity);
					Game.addOniPlayer(entity);

					// 金のヘルメットを装備させる
					TheMethods.putHelmet(entity);

					// 捕まえた鬼役に経験値付与
					TheMethods.randOniExperience(Dplayer);

					// 参加者へメッセージ送信
					SendMessage.messageAll(ChatColor.BLUE + entity + ChatColor.WHITE + "が" + ChatColor.RED + damager + ChatColor.WHITE + "によって鬼役のプレイヤーに追加されました。");

					// 捕まえた鬼役にメッセージ送信
					SendMessage.message(Dplayer, null, ChatColor.AQUA + "ボーナスとして" + TheMethods.onirandAmount + "経験値を獲得しました。");

					// メインタイマーが１分未満ならば鬼にスピードポーションの効果付与
					if (Game.getGame().remainSec < 60) {
						TheMethods.speedUpOni(Eplayer);
					}

					// 逃げ役のプレイヤー数によりゲームの終了を判定
					if (Game.nigePlayersInGame.size() != 0) {
						// 参加者へメッセージ送信
						SendMessage.messageAll("鬼役： " + Game.oniPlayersInGame.size() + "名");
						SendMessage.messageAll("逃げ役： " + Game.nigePlayersInGame.size() + "名");
					} else if (Game.nigePlayersInGame.size() == 0) {
						Game.getGame().finish();
						Game.getGame().cancelTimerTask();
					}
				}
			}
		}
	}

	/**
	 * プレイヤーが動こうとした時の処理
	 */
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player freezedOni = Game.getGame().freezedOni;
		if (freezedOni != null) {
			freezedOni.teleport(freezedOni);
		}
	}

	/**
	 * ゲーム中にログアウトした場合、該当するプレイヤーをLeaveさせる処理
	 */
	@EventHandler
	public void onLogoutPlayer(PlayerQuitEvent event) {
		String playerName = ((Player) event.getPlayer()).getName();
		// ゲーム開始中
		if ((Game.getGame().started || Game.getGame().before_started) && Game.playersInGame.contains(playerName)) {
			// プレイヤーをリストから削除
			if (Game.nigePlayersInGame.contains(playerName)) {
				Game.nigePlayersInGame.remove(playerName);
			}
			if (Game.oniPlayersInGame.contains(playerName)) {
				Game.oniPlayersInGame.remove(playerName);
			}
			Game.playersInGame.remove(playerName);
			SendMessage.messageAll(ChatColor.WHITE + "プレイヤー" + ChatColor.YELLOW + playerName + ChatColor.WHITE + "がゲームから抜けました。");
			if (Game.playersInGame.size() == 0) {
				SendMessage.messageAll(ChatColor.RED + "全ての参加プレイヤー居なくなったためゲームを終了します。");
				Game.getGame().forcedFinish();
				return;
			} else if (Game.nigePlayersInGame.size() == 0) {
				SendMessage.messageAll(ChatColor.RED + "全ての逃げ役のプレイヤーが居なくなったためゲームを終了します。");
				Game.getGame().finish();
				return;
			} else if (Game.oniPlayersInGame.size() == 0) {
				SendMessage.messageAll(ChatColor.RED + "全ての鬼役のプレイヤーが居なくなったためゲームを終了します。");
				Game.getGame().forcedFinish();
				return;
			}
		}
		// 参加受付中
		else if (Game.getGame().ready && Game.playersInGame.contains(playerName)) {
			Game.playersInGame.remove(playerName);
			SendMessage.messageAll(ChatColor.WHITE + "プレイヤー" + ChatColor.YELLOW + playerName + ChatColor.WHITE + "がゲームから抜けました。");
			return;
		}
	}

	/**
	 * ゲーム中に死んだ場合、該当するプレイヤーをLeaveさせる処理
	 */
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		String playerName = ((Player) event.getEntity()).getName();

		// ゲーム開始中
		if ((Game.getGame().started || Game.getGame().before_started) && Game.playersInGame.contains(playerName)) {
			// 死亡者を含む全体へメッセージ送信
			SendMessage.messageAll(ChatColor.WHITE + "ゲーム中にプレイヤーが死亡したため、プレイヤー" + ChatColor.YELLOW + playerName + ChatColor.WHITE + "がゲームから抜けました。");
			// プレイヤーリストから削除
			Game.playersInGame.remove(playerName);
			if (Game.playersInGame.size() == 0) {
				SendMessage.messageAll(ChatColor.RED + "全ての参加プレイヤー居なくなったためゲームを終了します。");
				Game.getGame().forcedFinish();
				return;
			}
			// 逃げ役のプレイヤーリストから削除
			if (Game.nigePlayersInGame.contains(playerName)) {
				Game.nigePlayersInGame.remove(playerName);
				if (Game.nigePlayersInGame.size() == 0) {
					SendMessage.messageAll(ChatColor.RED + "全ての逃げ役のプレイヤーが居なくなったためゲームを終了します。");
					Game.getGame().finish();
					return;
				}
			}
			// 鬼役のプレイヤーリストから削除
			if (Game.oniPlayersInGame.contains(playerName)) {
				Game.oniPlayersInGame.remove(playerName);
				if (Game.oniPlayersInGame.size() == 0) {
					SendMessage.messageAll(ChatColor.RED + "全ての鬼役のプレイヤーが居なくなったためゲームを終了します。");
					Game.getGame().forcedFinish();
					return;
				}
			}
		}
	}
}
