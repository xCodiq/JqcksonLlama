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

package com.xcodiq.jqcksonllama.handler;

import com.xcodiq.jqcksonllama.LlamaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Iterator;
import java.util.UUID;

public final class LlamaTargetHandler {

	private final LlamaPlugin plugin;
	private final int targetRadius;

	private UUID targetedPlayer = null;
	private BukkitTask targetTask = null;

	public LlamaTargetHandler(LlamaPlugin plugin) {
		this.plugin = plugin;
		this.targetRadius = plugin.getConfig().getInt("target-radius", 20);
	}

	public void startTargetTask(Player player) {
		this.targetedPlayer = player.getUniqueId();

		// Check if there is already an existing task, if so, cancel it
		if (this.targetTask != null) targetTask.cancel();

		// Create a new task and run it every 1/4 of a second
		this.targetTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
			if (this.targetedPlayer == null) return;
			this.setLlamaTargetOfPlayer(player, true);
		}, 0, 5L);
	}

	public void resetPlayer(Player player, boolean resetLlamas) {
		if (this.targetedPlayer != player.getUniqueId()) return;

		// Check if there is already an existing task, if so, cancel it
		if (this.targetTask != null) targetTask.cancel();
		this.targetedPlayer = null;

		// Reset the llamas if set to true
		if (resetLlamas) this.setLlamaTargetOfPlayer(player, false);
	}

	public UUID getTargetedPlayer() {
		return targetedPlayer;
	}

	public Iterator<Entity> getNearbyEntities(Location location) {
		return location.getNearbyEntities(targetRadius * 2, targetRadius * 2, targetRadius * 2).iterator();
	}

	public void setTargetLlamas(Iterator<Entity> iterator, Player player, boolean target) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
			// Loop through all the entities in the iteration
			while (iterator.hasNext()) {
				final Entity next = iterator.next();

				// Set the target either to player or to null if the next entity is an instnace of a Llama
				if (next instanceof Llama nearbyLlama) nearbyLlama.setTarget(target ? player : null);
			}
		});
	}

	private void setLlamaTargetOfPlayer(Player player, boolean target) {
		// Get all the nearby entities at the player's location
		final Iterator<Entity> iterator = this.getNearbyEntities(player.getLocation());

		// Adjust the target of the llamas within the entity iterator
		this.setTargetLlamas(iterator, player, target);
	}
}
