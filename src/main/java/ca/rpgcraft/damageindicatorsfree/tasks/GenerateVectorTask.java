package ca.rpgcraft.damageindicatorsfree.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class GenerateVectorTask extends BukkitRunnable {

    private Random rand;
    private Vector vector;
    private double x;
    private double y;
    private double z;


    public GenerateVectorTask()
    {
        rand = new Random();
    }

    @Override
    public void run() {

        double horiRange = .25;
        double vertRange = .15;

        if(rand.nextInt(2) == 0)
        {
            x = rand.nextDouble(horiRange);
        }
        else
        {
            x = -(rand.nextDouble(horiRange));
        }

        if(rand.nextInt(2) == 0)
        {
            z = rand.nextDouble(horiRange);
        }
        else
        {
            z = -(rand.nextDouble(horiRange));
        }

        y = rand.nextDouble(vertRange)+.1;

        vector = new Vector(x, y, z);
    }

    public Vector getVector()
    {
        return vector;
    }

    public double getX(){return x;}

    public double getY(){return y;}

    public double getZ(){return z;}
}
