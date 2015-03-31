package com.demigodsrpg.wefixcombat.listener;

import com.demigodsrpg.wefixcombat.WeFixCombat;
import com.demigodsrpg.wefixcombat.model.PlayerModel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DamageListener implements Listener {
    // Handle all incoming damage
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerModel model = WeFixCombat.getPlayerRegistry().fromPlayer(player);

            double damage = event.getDamage();
            double armor = event.getDamage(EntityDamageEvent.DamageModifier.ARMOR);
            double enchant = event.getDamage(EntityDamageEvent.DamageModifier.MAGIC);
            double block = event.getDamage(EntityDamageEvent.DamageModifier.BLOCKING);

            double finalDamage = model.damage(player, damage, armor, enchant, block);
            WeFixCombat.getPlayerRegistry().register(model);

            event.setDamage(finalDamage);

            player.sendMessage(ChatColor.YELLOW + "DAMAGE: " + ChatColor.RED + finalDamage); // FIXME Debug
        }
    }

    // Handle outgoing damage
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            PlayerModel model = WeFixCombat.getPlayerRegistry().fromPlayer((Player) event.getDamager());

            // TODO Handle weapons
        }
    }

    // Handle join
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerModel model = WeFixCombat.getPlayerRegistry().fromPlayer(event.getPlayer());
        model.resetToCurrent(event.getPlayer());
        WeFixCombat.getPlayerRegistry().register(model);
    }

    // Handle respawn
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        PlayerModel model = WeFixCombat.getPlayerRegistry().fromPlayer(event.getPlayer());
        model.resetMaxHealth(event.getPlayer());
        model.resetHealth(event.getPlayer());
        model.resetToCurrent(event.getPlayer());
        WeFixCombat.getPlayerRegistry().register(model);
    }

    // Handle death
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        // TODO
    }
}
