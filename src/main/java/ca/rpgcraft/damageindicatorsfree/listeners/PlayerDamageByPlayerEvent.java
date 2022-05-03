package ca.rpgcraft.damageindicatorsfree.listeners;

import ca.rpgcraft.damageindicatorsfree.DamageIndicatorsFree;
import ca.rpgcraft.damageindicatorsfree.HologramManager;
import ca.rpgcraft.damageindicatorsfree.tasks.CreateHologramTask;
import ca.rpgcraft.damageindicatorsfree.tasks.GenerateVectorTask;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;


public class PlayerDamageByPlayerEvent implements Listener {

    private final DamageIndicatorsFree plugin;
    private final GenerateVectorTask generateVectorTask;
    private final HologramManager hologramManager;

    public PlayerDamageByPlayerEvent(DamageIndicatorsFree plugin, GenerateVectorTask generateVectorTask, HologramManager hologramManager)
    {
        this.plugin = plugin;
        this.generateVectorTask = generateVectorTask;
        this.hologramManager = hologramManager;
    }

    @EventHandler
    void onPlayerDamageByPlayerEvent(EntityDamageByEntityEvent e)
    {
        if(!(e.getEntity() instanceof Player)) return;
        if(e.getEntity() instanceof ArmorStand) return;
        if(!(e.getDamager() instanceof Player)) return;

        if(e.isCancelled()) return;

        if(!plugin.getConfig().getBoolean("sweeping-edge") && e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) return;

        CreateHologramTask createHologramTask = new CreateHologramTask(plugin, generateVectorTask, e, (Player) e.getDamager(), hologramManager);
        createHologramTask.run();
    }
}
