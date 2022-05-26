package ca.rpgcraft.damageindicatorsfree.listeners;

import ca.rpgcraft.damageindicatorsfree.DamageIndicatorsFree;
import ca.rpgcraft.damageindicatorsfree.HologramManager;
import ca.rpgcraft.damageindicatorsfree.tasks.CreateHologramTask;
import ca.rpgcraft.damageindicatorsfree.util.VectorGenerator;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageEvent implements Listener {

    private final DamageIndicatorsFree plugin;
    private final VectorGenerator vectorGenerator;
    private final HologramManager hologramManager;

    public PlayerDamageEvent(DamageIndicatorsFree plugin, VectorGenerator vectorGenerator, HologramManager hologramManager)
    {
        this.plugin = plugin;
        this.vectorGenerator = vectorGenerator;
        this.hologramManager = hologramManager;
    }

    @EventHandler
    void onPlayerDamageEvent(EntityDamageEvent e)
    {
        if(!(e.getEntity() instanceof Player)) return;
        if(e.getEntity() instanceof ArmorStand) return;


        if(e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)
        || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
        || e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)
        || e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) return;

        if(e.isCancelled()) return;

        if(!plugin.getConfig().getBoolean("sweeping-edge") && e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)) return;

        CreateHologramTask createHologramTask = new CreateHologramTask(plugin, vectorGenerator, e, hologramManager);
        createHologramTask.run();
    }

}
