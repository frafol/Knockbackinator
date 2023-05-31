package it.frafol.knockbackinator;

import it.frafol.knockbackinator.commands.MainCommand;
import it.frafol.knockbackinator.enums.SpigotConfig;
import it.frafol.knockbackinator.listeners.*;
import it.frafol.knockbackinator.objects.PlayerCache;
import it.frafol.knockbackinator.objects.TextFile;
import it.frafol.knockbackinator.tasks.GeneralTask;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.simpleyaml.configuration.file.YamlFile;

import java.util.Objects;

public class Knockbackinator extends JavaPlugin {

	private TextFile configTextFile;
	private TextFile messagesTextFile;
	public static Knockbackinator instance;
	private final ItemStack stick = new ItemStack(Material.STICK);

	public static Knockbackinator getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {

		instance = this;

		BukkitLibraryManager bukkitLibraryManager = new BukkitLibraryManager(this);

		Library yaml = Library.builder()
				.groupId("me{}carleslc{}Simple-YAML")
				.artifactId("Simple-Yaml")
				.version("1.8.4")
				.build();

		bukkitLibraryManager.addJitPack();
		bukkitLibraryManager.loadLibrary(yaml);

		getLogger().info("Server version: " + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + ".");

		if (Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].contains("1_6_R")
				|| Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].contains("1_5_R")
				|| Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].contains("1_4_R")
				|| Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].contains("1_3_R")
				|| Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].contains("1_2_R")
				|| Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].contains("1_1_R")
				|| Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].contains("1_0_R")) {
			getLogger().severe("Support for your version was declined.");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		if (!Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].contains("1_7_R")
				&& !Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].contains("1_8_R")) {
			getLogger().warning("Your server version may not support the pvp maccanics of 1.8. " +
					"To solve this, install a plugin to fix the pvp cooldown like OldCombatMechanics.");
		}

		if (isFolia()) {
			getLogger().warning("Support for Folia has not been tested and is only for experimental purposes.");
		}

		getLogger().info("Loading configuration...");
		configTextFile = new TextFile(getDataFolder().toPath(), "config.yml");
		messagesTextFile = new TextFile(getDataFolder().toPath(), "messages.yml");

		getLogger().info("Loading commands...");
		getServer().getPluginManager().registerEvents(new MainCommand(), this);

		getLogger().info("Loading events...");
		getServer().getPluginManager().registerEvents(new JoinListener(), this);
		getServer().getPluginManager().registerEvents(new DropListener(), this);
		getServer().getPluginManager().registerEvents(new HitListener(), this);
		getServer().getPluginManager().registerEvents(new ItemListener(), this);
		getServer().getPluginManager().registerEvents(new FallListener(), this);
		getServer().getPluginManager().registerEvents(new LeaveListener(), this);

		getLogger().info("Loading tasks...");
		getServer().getScheduler().runTaskTimer(this, new GeneralTask(), 20L, 20L);

		if (SpigotConfig.STATS.get(Boolean.class)) {

			new Metrics(this, 18641);

			getLogger().info("Metrics loaded successfully!");

		}

		if (!isFolia()) {
			if (SpigotConfig.UPDATE_CHECK.get(Boolean.class)) {
				new UpdateCheck(this).getVersion(version -> {
					if (!getDescription().getVersion().equals(version)) {
						getLogger().warning("There is a new update available, download it on SpigotMC!");
					}
				});
			}
		} else {
			getLogger().severe("Folia does not support the update checker.");
		}

		loadItemStick();

		if (getServer().getOnlinePlayers().size() > 0) {

			for (Player players : getServer().getOnlinePlayers()) {
				startupPlayer(players);
			}

		}

		getLogger().info("Plugin successfully loaded!");
	}

	private void loadItemStick() {

		ItemMeta stickMeta = stick.getItemMeta();
		Objects.requireNonNull(stickMeta).setDisplayName(SpigotConfig.ITEM_NAME.color());

		if (SpigotConfig.BREAK.get(Boolean.class)) {
			stickMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
			stickMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			stickMeta.addEnchant(Enchantment.DURABILITY, 32767, true);
		}

		stick.setItemMeta(stickMeta);

	}

	public void startupPlayer(Player player) {

		PlayerCache.getDelays().put(player, player.getMaximumNoDamageTicks());

		if (SpigotConfig.PERMISSION.get(String.class) != null && !player.hasPermission(SpigotConfig.PERMISSION.get(String.class))) {
			return;
		}

		player.getInventory().setItem(SpigotConfig.SLOT.get(Integer.class), Knockbackinator.getInstance().getStick());

	}

	public ItemStack getStick() {
		return stick;
	}

	public static boolean isFolia() {
		try {
			Class.forName("io.papermc.paper.threadedregions.RegionizedServerInitEvent");
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}

	public YamlFile getConfigTextFile() {
		return getInstance().configTextFile.getConfig();
	}

	public YamlFile getMessagesTextFile() {
		return getInstance().messagesTextFile.getConfig();
	}

	@Override
	public void onDisable() {

		getLogger().info("Clearing instances...");
		instance = null;

		getLogger().info("Plugin successfully disabled!");
	}

}