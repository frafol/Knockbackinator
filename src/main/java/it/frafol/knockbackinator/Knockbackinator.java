package it.frafol.knockbackinator;

import com.tchristofferson.configupdater.ConfigUpdater;
import it.frafol.knockbackinator.commands.MainCommand;
import it.frafol.knockbackinator.enums.SpigotConfig;
import it.frafol.knockbackinator.enums.SpigotVersion;
import it.frafol.knockbackinator.listeners.*;
import it.frafol.knockbackinator.objects.PlayerCache;
import it.frafol.knockbackinator.objects.StickItem;
import it.frafol.knockbackinator.objects.TextFile;
import it.frafol.knockbackinator.tasks.GeneralTask;
import lombok.Getter;
import lombok.SneakyThrows;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;

public class Knockbackinator extends JavaPlugin {

	private TextFile configTextFile;
	private TextFile messagesTextFile;
	private TextFile versionTextFile;

	private boolean updated = false;

	@Getter
	public static Knockbackinator instance;

	@Getter
	private final ItemStack stick = StickItem.getStick();

	@Override
	public void onEnable() {

		instance = this;

		loadDependencies();
		checkSupportedVersion();

		loadConfigurations();
		updateConfiguration();

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

		if (SpigotConfig.UPDATE_CHECK.get(Boolean.class)) {
			UpdateChecker();
		}

		StickItem.loadItemStick();

		if (!getServer().getOnlinePlayers().isEmpty()) {
			for (Player players : getServer().getOnlinePlayers()) {
				startupPlayer(players);
			}
		}

		getLogger().info("Plugin successfully loaded!");
	}

	private void loadDependencies() {
		BukkitLibraryManager bukkitLibraryManager = new BukkitLibraryManager(this);

		Library yaml = Library.builder()
				.groupId("me{}carleslc{}Simple-YAML")
				.artifactId("Simple-Yaml")
				.version("1.8.4")
				.build();

		bukkitLibraryManager.addJitPack();

		try {
			bukkitLibraryManager.loadLibrary(yaml);
		} catch (RuntimeException ignored) {
			getLogger().severe("Failed to load Simple-YAML library. Trying to download it from GitHub...");
			yaml = Library.builder()
					.groupId("me{}carleslc{}Simple-YAML")
					.artifactId("Simple-Yaml")
					.version("1.8.4")
					.url("https://github.com/Carleslc/Simple-YAML/releases/download/1.8.4/Simple-Yaml-1.8.4.jar")
					.build();
		}

		bukkitLibraryManager.loadLibrary(yaml);
	}

	private void checkSupportedVersion() {
		getLogger().info("Server version: " + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + ".");
		if (getServer().getClass().getPackage().getName().split("\\.")[3].contains("1_6_R")
				|| getServer().getClass().getPackage().getName().split("\\.")[3].contains("1_5_R")
				|| getServer().getClass().getPackage().getName().split("\\.")[3].contains("1_4_R")
				|| getServer().getClass().getPackage().getName().split("\\.")[3].contains("1_3_R")
				|| getServer().getClass().getPackage().getName().split("\\.")[3].contains("1_2_R")
				|| getServer().getClass().getPackage().getName().split("\\.")[3].contains("1_1_R")
				|| getServer().getClass().getPackage().getName().split("\\.")[3].contains("1_0_R")) {
			getLogger().severe("Support for your version was declined.");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		if (!getServer().getClass().getPackage().getName().split("\\.")[3].contains("1_7_R")
				&& !getServer().getClass().getPackage().getName().split("\\.")[3].contains("1_8_R")
				&& !getServer().getPluginManager().isPluginEnabled("OldCombatMechanics")) {
			getLogger().warning("Your server version may not support the pvp maccanics of 1.8. " +
					"To solve this, install a plugin to fix the pvp cooldown like OldCombatMechanics.");
		}

		if (isFolia()) {
			getLogger().warning("Support for Folia has not been tested and is only for experimental purposes.");
		}
	}

	private void loadConfigurations() {
		getLogger().info("Loading configuration...");
		configTextFile = new TextFile(getDataFolder().toPath(), "config.yml");
		messagesTextFile = new TextFile(getDataFolder().toPath(), "messages.yml");
		versionTextFile = new TextFile(getDataFolder().toPath(), "version.yml");
	}

	@SneakyThrows
	private void updateConfiguration() {
		File configFile = new File(getDataFolder(), "config.yml");
		File messagesFile = new File(getDataFolder(), "messages.yml");
		if (!getDescription().getVersion().equals(SpigotVersion.VERSION.get(String.class))) {
			getLogger().info("Creating new configurations...");
			try {
				ConfigUpdater.update(this, "config.yml", configFile, Collections.emptyList());
				ConfigUpdater.update(this, "messages.yml", messagesFile, Collections.emptyList());
			} catch (IOException ignored) {
				getLogger().severe("Unable to update configuration files.");
			}

			versionTextFile.getConfig().set("version", getDescription().getVersion());
			versionTextFile.getConfig().save();
			configTextFile = new TextFile(getDataFolder().toPath(), "config.yml");
			messagesTextFile = new TextFile(getDataFolder().toPath(), "messages.yml");
			versionTextFile = new TextFile(getDataFolder().toPath(), "version.yml");
		}
	}

	public void startupPlayer(Player player) {

		PlayerCache.getDelays().put(player, player.getMaximumNoDamageTicks());
		if (SpigotConfig.PERMISSION.get(String.class) != null && !player.hasPermission(SpigotConfig.PERMISSION.get(String.class))) {
			return;
		}

		for (ItemStack items : player.getInventory().getContents()) {
			if (items != null && items.equals(stick)) {
				items.setAmount(0);
			}
		}

		instance.getServer().getScheduler().runTaskLater(instance, () -> player.getInventory().setItem(SpigotConfig.SLOT.get(Integer.class), Knockbackinator.getInstance().getStick()), (long) SpigotConfig.DELAY.get(Integer.class));
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

	public YamlFile getVersionTextFile() {
		return getInstance().versionTextFile.getConfig();
	}

	@Override
	public void onDisable() {
		getLogger().info("Clearing instances...");
		instance = null;
		getLogger().info("Plugin successfully disabled!");
	}

	public void UpdateChecker(Player player) {

		if (!SpigotConfig.UPDATE_CHECK.get(Boolean.class)) {
			return;
		}

		new UpdateCheck(this).getVersion(version -> {
			if (Integer.parseInt(getDescription().getVersion().replace(".", "")) < Integer.parseInt(version.replace(".", ""))) {

				if (SpigotConfig.AUTO_UPDATE.get(Boolean.class) && !updated) {
					autoUpdate();
					return;
				}

				if (!updated) {
					player.sendMessage("§e[Knockbackinator] There is a new update available, download it on SpigotMC!");
				}
			}
		});
	}

	private void UpdateChecker() {

		if (!SpigotConfig.UPDATE_CHECK.get(Boolean.class)) {
			return;
		}

		new UpdateCheck(this).getVersion(version -> {

			if (Integer.parseInt(getDescription().getVersion().replace(".", "")) < Integer.parseInt(version.replace(".", ""))) {

				if (SpigotConfig.AUTO_UPDATE.get(Boolean.class) && !updated) {
					autoUpdate();
					return;
				}

				if (!updated) {
					getLogger().warning("§eThere is a new update available, download it on SpigotMC!");
				}
			}

			if (Integer.parseInt(getDescription().getVersion().replace(".", "")) > Integer.parseInt(version.replace(".", ""))) {
				getLogger().warning("§eYou are using a development version, please report any bugs!");
			}

		});
	}

	public void autoUpdate() {
		try {
			String fileUrl = "https://github.com/frafol/Knockbackinator/releases/download/release/Knockbackinator.jar";
			String destination = "./plugins/";

			String fileName = getFileNameFromUrl(fileUrl);
			File outputFile = new File(destination, fileName);

			downloadFile(fileUrl, outputFile);
			updated = true;
			getLogger().warning("Knockbackinator successfully updated, a restart is required.");

		} catch (IOException ignored) {
			getLogger().severe("Unable to update the Knockbackinator plugin, update it manually.");
		}
	}

	private String getFileNameFromUrl(String fileUrl) {
		int slashIndex = fileUrl.lastIndexOf('/');
		if (slashIndex != -1 && slashIndex < fileUrl.length() - 1) {
			return fileUrl.substring(slashIndex + 1);
		}
		throw new IllegalArgumentException("Invalid file URL");
	}

	private void downloadFile(String fileUrl, File outputFile) throws IOException {
		URL url = new URL(fileUrl);
		try (InputStream inputStream = url.openStream()) {
			Files.copy(inputStream, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
	}

}