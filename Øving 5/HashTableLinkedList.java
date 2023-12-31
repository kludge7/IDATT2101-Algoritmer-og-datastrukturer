import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HashTableLinkedList {
    // Size of HashTable
    private static final int size = 169;
    // Array that contains Entry (The actual HashTable)
    private final Entry[] table = new Entry[size];

    // Class that represents data stored in HashTable
    class Entry {
        String key;
        String value;
        Entry next;
        Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
    // Method for transforming a String (name) to int
    int stringToKey(String value) {
        int key = 0;
        for(int i = 0; i < value.length(); i++) {
            key += (i+1) * (value.charAt(i));
        }
        return key;
    }
    // HashFunction
    int hashFunction(long key) {
        return (int) (key % size);
    }
    // Add an Entry to HashTable
    int addEntry(String key, String value, int collisions) {
        int hash = hashFunction(stringToKey(key));
        Entry entry = table[hash];
        // If there already is an Entry at this "Bucket" (collision)
        if(entry != null) {
            collisions+=1;
            Entry current = entry;
            System.out.println("Collision: " + value + " & " + current.value);
            while(current.next != null) {
                current = current.next;
            }
            current.next = new Entry(key, value);
        } else {
            table[hash] = new Entry(key, value);
        }
        return collisions;
    }

    // Get an Entry from HashTable
    Entry getEntry(String value) {
        int hash = hashFunction(stringToKey(value));
        Entry current = table[hash];
        if(current == null) {
            throw new IllegalArgumentException("Name not in HashTable");
        } else {
            while(current != null) {
                if(current.value.equals(value)) {
                    break;
                }
                current = current.next;
            }
        }
        if(current != null) {
            return current;
        } else {
            throw new IllegalArgumentException("Name not in HashTable");
        }
    }


    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        HashTableLinkedList hashTableLinkedList = new HashTableLinkedList();
        try {
            File file = new File("navn.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String name = scanner.nextLine();
                names.add(name);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        int collisions = 0;
        for (String name : names) {
            collisions = hashTableLinkedList.addEntry(name, name, collisions);
        }
        System.out.println("\nLINKED LIST");
        System.out.println("Amount of collisions were: " + collisions);
        System.out.println("Avarage amount of collisions per person was: " + (double) collisions / names.size());
        System.out.println("Lastefaktor: " + (double) names.size() / size);
        System.out.println("Andreas Kluge Svendsrud gives me back: " + hashTableLinkedList.getEntry("Andreas Kluge Svendsrud").value);
    }
}