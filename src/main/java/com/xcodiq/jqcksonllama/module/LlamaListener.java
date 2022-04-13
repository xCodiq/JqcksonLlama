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

import com.xcodiq.jqcksonllama.LlamaPlugin;
import com.xcodiq.jqcksonllama.handler.LlamaTargetHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public record LlamaListener(LlamaPlugin plugin, LlamaTargetHandler targetHandler) implements Listener {

	@EventHandler
	public void onPlayerAttackLlama(EntityDamageByEntityEvent event) {
		final Entity damager = event.getDamager(); // Player
		final Entity entity = event.getEntity(); // Llama

		// Check if the damager is a player and the defender a llama
		if (damager instanceof Player player && entity instanceof Llama llama) {
			// Set the target f the llama to the player
			llama.setTarget(player);

			// Start the target task of the player for other llamas to attack the same player
			targetHandler.startTargetTask(player);
		}
	}

	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent event) {
		// Check if the spawned entity is indeed a llama
		if (this.targetHandler.getTargetedPlayer() != null && event.getEntity() instanceof Llama llama) {
			final Player player = Bukkit.getPlayer(this.targetHandler.getTargetedPlayer());

			// Set the llamas target to the player if they are online
			if (player != null && player.isOnline()) llama.setTarget(player);
		}
	}

	@EventHandler
	public void onLlamaSpit(EntityDamageByEntityEvent event) {
		final Entity damager = event.getDamager(); // LlamaSpit
		final Entity entity = event.getEntity(); // Player

		// Check if the damager is a llama spit and the defender a playerew
		if (damager instanceof LlamaSpit spit && entity instanceof Player player) {

			// Multiply the damage by the spit damage multiplier
			event.setDamage(event.getDamage() * plugin.getConfig().getDouble("spit-damage-multiplier", 1.0d));

			// Check if the damager is a llama spit and the defender a llama
		} else if (damager instanceof LlamaSpit spit && entity instanceof Llama llama) {

			// Cancel the event, to make sure Llamas don't take damage from other llamas
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		// Reset the targeted player on death
		this.targetHandler.resetPlayer(event.getPlayer(), true);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		// Reset the targeted player on quit
		this.targetHandler.resetPlayer(event.getPlayer(), true);
	}
}
