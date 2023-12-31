import java.util.*;
import static java.lang.Math.abs;
import java.util.Random;

/**
 * Abstract class to represent a HashTable
 */
abstract class HashTable {
    /**
     * Class that represents data stored in HashTable
     */
    static class Entry{
        int key;
        int value;
        Entry(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * HashFunction returns a hash (int) based on key
     * @param key will determine what has we get back
     * @return integer
     */
    abstract int hashFunction(long key);
    // Method for adding an Entry to table
    abstract long addEntry(long key, long value, long collisions);

    // Main function where test code for task 2 is run
    public static void main(String[] args) {
        HashTableLinearProbing hashTableLinearProbing = new HashTableLinearProbing();
        HashTableDoubleHashing hashTableDoubleHashing = new HashTableDoubleHashing();
        Random random = new Random();
        List<Long> numbers = new ArrayList<>();

        System.out.println("80%");
        int amountNumbers = ((HashTableLinearProbing.tableSize)/100) * 80;
        numbers.add(1L);
        for(int i = 1; i <= amountNumbers; i++) {
            numbers.add(numbers.get(i-1) + random.nextInt(1, 1000));
        }
        Collections.shuffle(numbers);

        // LINEAR PROBING
        long collisions = 0;
        Date start = new Date();

        for (Long number : numbers) {
            collisions = hashTableLinearProbing.addEntry(number, number, collisions);
        }
        Date end = new Date();
        System.out.println("LINEAR PROBING");
        System.out.println("Amount of collisions were: " + collisions);
        System.out.println("Average amount of collisions per number was: " + (double) collisions / numbers.size());
        System.out.println("Lastefaktor: " + (double) numbers.size() / HashTableLinearProbing.tableSize);
        System.out.println("Time it took in milliseconds " + (double) (end.getTime()-start.getTime()));


        // DOUBLE HASHING
        collisions = 0;
        start = new Date();
        for (Long number : numbers) {
            collisions = hashTableDoubleHashing.addEntry(number, number, collisions);
        }
        end = new Date();
        System.out.println("\nDOUBLE HASHING");
        System.out.println("Amount of collisions were: " + collisions);
        System.out.println("Average amount of collisions per number was: " + (double) collisions / numbers.size());
        System.out.println("Lastefaktor: " + (double) numbers.size() / HashTableDoubleHashing.tableSize);
        System.out.println("Time it took in milliseconds " + (double) (end.getTime()-start.getTime()));
    }
}
class HashTableLinearProbing extends HashTable {
    // Size of HashTable
    static int tableSize = 16777216; //2^24;
    // Array that contains Entry (The actual HashTable)
    Entry[] table = new Entry[tableSize];
    // HashFunction
    @Override
    int hashFunction(long key) {
        return (int) (key % tableSize);
    }
    // Add an Entry to table
    @Override
    long addEntry(long key, long value, long collisions) {
        int hash = hashFunction(key);
        HashTable.Entry entry = table[hash];
        while(entry != null) {
            collisions += 1;
            hash = (hash + 1) % tableSize;
            entry = table[hash];
        }
        table[hash] = new HashTable.Entry((int) key, (int) value);
        return collisions;
    }
}
class HashTableDoubleHashing extends HashTable {
    // Size of HashTable
    static int tableSize = 16777216; //2^24
    // Array that contains Entry (The actual HashTable)
    Entry[] table = new Entry[tableSize];
    // HashFunction #1
    @Override
    int hashFunction(long key) {
        return ((int) (key % tableSize));
    }
    // HashFunction #2
    int hashFunction2(long key) {
        return abs((int) (2*key + 1) % tableSize);
    }
    // Add Entry to HashTable
    @Override
    long addEntry(long key, long value, long collisions) {
        int hash = hashFunction(key);
        HashTable.Entry entry = table[hash];
        if(entry != null) {
            int hash2 = hashFunction2(key);
            while(entry != null) {
                collisions += 1;
                hash = (hash + hash2) % tableSize;
                entry = table[(hash)];
            }
        }
        table[(hash)] = new HashTable.Entry((int) key, (int) value);
        return collisions;
    }
}



