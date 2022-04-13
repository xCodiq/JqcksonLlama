/*
 * THE MINECART - Elmar (Cody) B.
 * elmarblume91@gmail.com
 *
 * [2021] - [2022] The Minecart
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of The Minecart and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to The Minecart
 * and its suppliers and may be covered by NL and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from The Minecart.
 */

package com.xcodiq.jqcksonllama;

import co.aikar.commands.PaperCommandManager;
import com.xcodiq.jqcksonllama.handler.LlamaTargetHandler;
import com.xcodiq.jqcksonllama.module.LlamaCommand;
import com.xcodiq.jqcksonllama.module.LlamaListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class LlamaPlugin extends JavaPlugin {

	private static LlamaPlugin instance = null;

	private LlamaTargetHandler targetHandler;
	private PaperCommandManager commandManager;

	public LlamaPlugin() {
		if (instance != null) throw new IllegalStateException("Only one instance can run at the time");
		instance = this;
	}

	public static LlamaPlugin getInstance() {
		if (instance == null) throw new IllegalStateException("Cannot access instance; instance might be null");
		return instance;
	}

	@Override
	public void onEnable() {
		// Save the default config
		this.saveDefaultConfig();

		// Initialize the llama target handler
		this.targetHandler = new LlamaTargetHandler(this);

		// Initialize the command manager and register the llama command
		this.commandManager = new PaperCommandManager(this);
		this.commandManager.registerCommand(new LlamaCommand(this, this.targetHandler));

		// Register the llama listener
		this.getServer().getPluginManager().registerEvents(new LlamaListener(this, this.targetHandler), this);
	}

	@Override
	public void onDisable() {
		// Unregister all the commands
		this.commandManager.unregisterCommands();

		// Reset all the players that were online
		Bukkit.getOnlinePlayers().forEach(onlinePlayer -> this.targetHandler.resetPlayer(onlinePlayer, false));
	}
}
