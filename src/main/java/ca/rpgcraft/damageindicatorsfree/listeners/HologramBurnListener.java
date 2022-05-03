package ca.rpgcraft.damageindicatorsfree.listeners;

import ca.rpgcraft.damageindicatorsfree.DamageIndicatorsFree;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataType;

public class HologramBurnListener implements Listener {

    private final DamageIndicatorsFree plugin;

    public HologramBurnListener(DamageIndicatorsFree plugin){
        this.plugin = plugin;
    }

    @EventHandler
    void onHologramBurn(EntityDamageEvent e)
    {
        if(!(e.getEntity() instanceof ArmorStand)) return;

        ArmorStand hologram = (ArmorStand) e.getEntity();

        if(!hologram.getPersistentDataContainer()
                .has(new NamespacedKey(plugin, "hologram"), PersistentDataType.STRING)) return;

        e.getEntity().setFireTicks(0);
    }
}

