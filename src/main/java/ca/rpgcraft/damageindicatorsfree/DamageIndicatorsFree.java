package ca.rpgcraft.damageindicatorsfree;

import ca.rpgcraft.damageindicatorsfree.listeners.*;
import ca.rpgcraft.damageindicatorsfree.util.VectorGenerator;
import ca.rpgcraft.damageindicatorsfree.util.VectorRingBuffer;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.MultiLineChart;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DamageIndicatorsFree extends JavaPlugin {

    private HologramManager hologramManager;
    private Logger logger;
    private VectorRingBuffer ringBuffer;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        logger = this.getLogger();

        logger.log(Level.INFO, "Initializing hologram manager...");
        hologramManager = new HologramManager();

        logger.log(Level.INFO, "Initializing vector generation...");
        VectorGenerator vectorGenerator = new VectorGenerator();
        ringBuffer = new VectorRingBuffer(50, vectorGenerator);

        logger.log(Level.INFO, "Registering listeners...");
        if(getConfig().getBoolean("players")){
            Bukkit.getPluginManager().registerEvents(new PlayerDamageByEntityEvent(this, vectorGenerator, hologramManager), this);
            Bukkit.getPluginManager().registerEvents(new PlayerDamageByPlayerEvent(this, vectorGenerator, hologramManager), this);
            Bukkit.getPluginManager().registerEvents(new PlayerDamageEvent(this, vectorGenerator, hologramManager), this);
        }
        if(getConfig().getBoolean("mobs")){
            Bukkit.getPluginManager().registerEvents(new EntityDamageByEntityEvent(this, vectorGenerator, hologramManager), this);
            Bukkit.getPluginManager().registerEvents(new EntityDamageByPlayerEvent(this, vectorGenerator, hologramManager), this);
        }
        Bukkit.getPluginManager().registerEvents(new HologramBurnListener(this), this);

        logger.log(Level.INFO, "Listening for hologram spawns...");

        Metrics metrics = new Metrics(this, 14826);
        metrics.addCustomChart(new MultiLineChart("players_and_servers", new Callable<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> call() throws Exception {
                Map<String, Integer> valueMap = new HashMap<>();
                valueMap.put("servers", 1);
                valueMap.put("players", Bukkit.getOnlinePlayers().size());
                return valueMap;


            }
        }));
    }

    @Override
    public void onDisable()
    {
        logger.log(Level.INFO, "Removing all holograms...");
        for(ArmorStand hologram : hologramManager.getHologramList().values())
        {
            hologram.remove();
        }
        logger = null;
        hologramManager = null;
    }

    public VectorRingBuffer getRingBuffer() {
        return ringBuffer;
    }
}
