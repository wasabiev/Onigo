package wasabiev.Onigo;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TheMethods {

	private static boolean canceledrandOni;

	public static int randAmount = 0;
	public static int onirandAmount = 0;

	/**
	 * ランダムで逃げ役のプレイヤーの中から鬼役のプレイヤーを選出する
	 */
	public static boolean randOni() {
		// ランダム選出が失敗したかどうかのフラグ初期化
		canceledrandOni = false;
		int random = new Random().nextInt(Game.playersInGame.size());
		// Listからランダムでプレイヤーを抽出
		String randPlayer = Game.playersInGame.get(random);
		Player player = Bukkit.getPlayer(randPlayer);
		// 抽出されたプレイヤーが存在し、オンラインであり、playersInGameに含まれている場合
		if (randPlayer != null && player.isOnline() && Game.playersInGame.contains(randPlayer)) {
			// 逃げ役からプレイヤーを削除
			Game.nigePlayersInGame.remove(randPlayer);
			// 鬼役にプレイヤーの追加
			Game.oniPlayersInGame.add(randPlayer);
			// 金のヘルメットを装備させる
			putHelmet(randPlayer);
			// 参加者へメッセージ送信
			SendMessage.messageAll("鬼役のプレイヤーは" + ChatColor.RED + randPlayer + ChatColor.WHITE + "です。");

			return true;
		}
		// そうでない場合
		else {
			// ランダム選出が失敗したフラグセット
			canceledrandOni = true;
			// 参加者へメッセージ送信
			SendMessage.messageAll("自動で鬼役のプレイヤーを選出できませんでした。");
			SendMessage.messageAll("手動で鬼役のプレイヤーを選出してください。");
			return false;
		}
	}

	/**
	 * 手動で逃げプレイヤーの中から鬼役のプレイヤーを選出する
	 */
	public static boolean manualOni(CommandSender sender) {
		if (canceledrandOni) {
			// String型にプレイヤー名を変換
			String manualOni = sender.getName();
			// 鬼役のプレイヤーが存在しない場合
			if (Game.oniPlayersInGame.size() == 0) {
				// 逃げ役からプレイヤーを削除
				Game.nigePlayersInGame.remove(manualOni);
				// 鬼役にプレイヤーの追加
				Game.oniPlayersInGame.add(manualOni);
				// 金のヘルメットを装備させる
				putHelmet(manualOni);
				// 参加者へメッセージ送信
				SendMessage.messageAll("鬼役のプレイヤーは" + ChatColor.RED + manualOni + ChatColor.WHITE + "です。");
				return true;
			}
			// 鬼役のプレイヤーに既にsenderが登録されている場合
			else if (Game.oniPlayersInGame.contains(sender)) {
				SendMessage.message(null, sender, "あなたは既に鬼役のプレイヤーに登録されています！");
				return false;
			}

			// 鬼役のプレイヤーが他に登録されている場合
			else if (Game.oniPlayersInGame.size() != 0 && !Game.oniPlayersInGame.contains(sender)) {
				SendMessage.message(null, sender, "既に鬼役のプレイヤーが登録されています。");
				SendMessage.message(null, sender, "これ以上マニュアルで鬼プレイヤーを登録することが出来ません。");
				return false;
			}
		} else {
			SendMessage.message(null, sender, "ランダムで鬼を選出するフラグがFalseです。");
			return false;
		}
		return false;
	}

	/**
	 * 頭に金のヘルメットを装備させる。
	 *
	 * @param player
	 * @return 失敗ならfalse
	 */
	public static boolean putHelmet(String playerName) {
		Player player = Bukkit.getPlayer(playerName);
		if (player != null && player.isOnline()) {
			ItemStack goldHelmet = new ItemStack(Material.GOLD_HELMET, 1);
			// ItemStack headItem = player.getInventory().getHelmet();

			player.getInventory().setHelmet(goldHelmet);
			return true;
		}
		return false;
	}

	/**
	 * ランダムで勝利プレイヤーに経験値を付与
	 *
	 * @param player
	 */
	public static void randExperience(Player player) {
		int[] experienceArray = new int[20];
		experienceArray[0] = 1;
		experienceArray[1] = 1;
		experienceArray[2] = 1000;
		experienceArray[3] = 1000;
		experienceArray[4] = 1000;
		experienceArray[5] = 1000;
		experienceArray[6] = 1000;
		experienceArray[7] = 1000;
		experienceArray[8] = 1000;
		experienceArray[9] = 1000;
		experienceArray[10] = 1000;
		experienceArray[11] = 1000;
		experienceArray[12] = 1000;
		experienceArray[13] = 1000;
		experienceArray[14] = 1000;
		experienceArray[15] = 5000;
		experienceArray[16] = 5000;
		experienceArray[17] = 10000;
		experienceArray[18] = 50000;
		experienceArray[19] = 100000;
		int random = new Random().nextInt(20);
		randAmount = experienceArray[random];
		player.giveExp(randAmount);
	}

	/**
	 * ランダムで逃げ役を捕まえた鬼に経験値を付与
	 */
	public static void randOniExperience(Player player) {
		int[] oniexperienceArray = new int[10];
		oniexperienceArray[0] = 100;
		oniexperienceArray[1] = 200;
		oniexperienceArray[2] = 300;
		oniexperienceArray[3] = 400;
		oniexperienceArray[4] = 500;
		oniexperienceArray[5] = 600;
		oniexperienceArray[6] = 700;
		oniexperienceArray[7] = 800;
		oniexperienceArray[8] = 900;
		oniexperienceArray[9] = 1000;
		int onirandom = new Random().nextInt(10);
		onirandAmount = oniexperienceArray[onirandom];
		player.giveExp(onirandAmount);
	}

	/**
	 * ３０秒間プレイヤーの視界を奪うポーションの効果の付与
	 */
	public static void blind(Player player) {
		if (player != null && player.isOnline()) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 600, 3));
		}
	}

	/**
	 * スピードポーションの効果を付与
	 */
	public static void speedUpOni(Player player) {
		if (player != null && player.isOnline()) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2000, 1));
		}
	}
}
