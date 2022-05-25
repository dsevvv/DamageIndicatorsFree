package ca.rpgcraft.damageindicatorsfree.util;

import org.bukkit.util.Vector;

public class VectorRingBuffer {
    public Vector[] vectors = null;

    private int capacity = 0;
    private int writePos = 0;
    private int available = 0;

    public VectorRingBuffer(int capacity){
        this.capacity = capacity;
        this.vectors = new Vector[capacity];
    }

    public void reset(){
        writePos = 0;
        available = 0;
    }

    public int getAvailable() {
        return available;
    }

    public int getCapacity() {
        return capacity;
    }

    public int remainingCapacity(){
        return capacity - available;
    }

    public boolean put(Vector element){

        if(available < capacity){
            if(writePos >= capacity){
                writePos = 0;
            }
            vectors[writePos] = element;
            writePos++;
            available++;
            return true;
        }

        return false;
    }

    public Vector take(){
        if(available == 0){
            return null;
        }
        int nextSlot = writePos - available;
        if(nextSlot < 0){
            nextSlot += capacity;
        }
        Vector nextElement = vectors[nextSlot];
        available--;
        return nextElement;
    }
}
