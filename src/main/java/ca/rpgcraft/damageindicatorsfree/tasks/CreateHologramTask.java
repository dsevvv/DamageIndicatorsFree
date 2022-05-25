package ca.rpgcraft.damageindicatorsfree.tasks;

import ca.rpgcraft.damageindicatorsfree.DamageIndicatorsFree;
import ca.rpgcraft.damageindicatorsfree.HologramManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;


public class CreateHologramTask extends BukkitRunnable {

    private final DamageIndicatorsFree plugin;
    private final EntityDamageEvent e;
    private final GenerateVectorTask generateVectorTask;
    private final HologramManager hologramManager;

    private Player playerDamager = null;

    private final ChatColor RED  = ChatColor.RED;

    public CreateHologramTask(DamageIndicatorsFree plugin, GenerateVectorTask generateVectorTask, EntityDamageEvent e, HologramManager hologramManager)
    {
        this.plugin = plugin;
        this.e = e;
        this.generateVectorTask = generateVectorTask;
        this.hologramManager = hologramManager;
    }

    public CreateHologramTask(DamageIndicatorsFree plugin, GenerateVectorTask generateVectorTask, EntityDamageEvent e, Player playerDamager, HologramManager hologramManager)
    {
        this.plugin = plugin;
        this.e = e;
        this.playerDamager = playerDamager;
        this.generateVectorTask = generateVectorTask;
        this.hologramManager = hologramManager;
    }

    @Override
    public void run()
    {
        double dmgFinal = e.getFinalDamage();
        Entity victim = e.getEntity();

        ArmorStand hologram = (ArmorStand) victim.getWorld().spawnEntity(victim.getLocation(), EntityType.ARMOR_STAND);
        hologram.setVisible(false);
        hologram.getPersistentDataContainer().set(new NamespacedKey(plugin, "hologram"), PersistentDataType.STRING, "true");
        hologram.setBasePlate(false);
        hologram.setCollidable(false);
        hologram.setArms(false);
        hologram.setSmall(true);
        hologram.setSilent(true);
        hologram.setCanPickupItems(false);
        hologram.setGliding(true);
        hologram.setLeftLegPose(EulerAngle.ZERO.add(180, 0, 0));
        hologram.setRightLegPose(EulerAngle.ZERO.add(180, 0, 0));
        hologram.setInvulnerable(true);
        hologram.setVelocity(plugin.getRingBuffer().take());
        plugin.getRingBuffer().put(generateVectorTask.getVector());
        hologramManager.addHologram(hologram);

        String customName = String.format(RED + "-%.1f", dmgFinal);

        //checking if a critical hit
        if(playerDamager != null)
        {
            if(playerDamager.getFallDistance() > 0
            && !(((Entity) playerDamager).isOnGround())
            && !(playerDamager.getLocation().getBlock().getType().equals(Material.VINE))
            && !(playerDamager.getLocation().getBlock().getType().equals(Material.LADDER))
            && !(playerDamager.getLocation().getBlock().getType().equals(Material.WATER))
            && !(playerDamager.getLocation().getBlock().getType().equals(Material.LAVA))
            && !(playerDamager.hasPotionEffect(PotionEffectType.BLINDNESS))
            && playerDamager.getVehicle() == null)
            {
                customName = customName + ChatColor.DARK_RED + "" + ChatColor.ITALIC + ChatColor.BOLD + " Crit!";
            }
        }

        hologram.setCustomNameVisible(true);
        hologram.setCustomName(customName);

        victim.getWorld().spawnParticle(Particle.HEART, victim.getLocation().add(0, 2.5,0), 1);

        CleanupHologramTask cleanupTask = new CleanupHologramTask(hologram, hologramManager);
        cleanupTask.runTaskLater(plugin, 20);
    }
}
