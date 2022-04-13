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

package com.xcodiq.jqcksonllama.module;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.annotation.*;
import com.xcodiq.jqcksonllama.LlamaPlugin;
import com.xcodiq.jqcksonllama.handler.LlamaTargetHandler;
import com.xcodiq.jqcksonllama.util.HexUtil;
import org.bukkit.entity.Player;

@CommandAlias("jqcksonllama|llama")
public final class LlamaCommand extends BaseCommand {

	private static final String PREFIX = "<#00416A>[&lLlama<#00416A>] <g:#00416A:#E4E5E6>";

	private final LlamaPlugin plugin;
	private final LlamaTargetHandler targetHandler;

	public LlamaCommand(LlamaPlugin plugin, LlamaTargetHandler targetHandler) {
		this.plugin = plugin;
		this.targetHandler = targetHandler;
	}

	@Default
	public void onDefault(CommandIssuer commandIssuer) {
		commandIssuer.sendMessage(HexUtil.colorify(PREFIX + "This server is running JqcksonLlama v1.0 by Cody Lynn (xCodiq)"));
	}

	@Subcommand("reset")
	@CommandPermission("jqcksonllama.command.reset")
	public void onReset(Player player) {
		// Reset the player, with resetLlamas set to true
		targetHandler.resetPlayer(player, true);

		player.sendMessage(HexUtil.colorify(PREFIX + "You will no longer be attacked by llamas (for now ;d)"));
	}

	@Subcommand("setdamagemulti")
	@Syntax("<multiplier>")
	@CommandCompletion("@range:1-20")
	@CommandPermission("jqcksonllama.command.setdamagemulti")
	public void onSetDamageMulti(Player player, float multiplier) {
		// Set the new damage multiplier and save the config
		plugin.getConfig().set("spit-damage-multiplier", multiplier);
		plugin.saveConfig();

		player.sendMessage(HexUtil.colorify(PREFIX + "Damage multiplier has been set to &l" + multiplier + "x"));
	}

	@Subcommand("settargetradius")
	@Syntax("<radius>")
	@CommandCompletion("@range:10-100")
	@CommandPermission("jqcksonllama.command.settargetradius")
	public void onSetTargetRadius(Player player, int radius) {
		// Set the new target radius and save the config
		plugin.getConfig().set("target-radius", radius);
		plugin.saveConfig();

		player.sendMessage(HexUtil.colorify(PREFIX + "Target radius has been set to &l" + radius + "x" + radius));
	}
}
