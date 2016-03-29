import java.util.concurrent.atomic.AtomicInteger;

/**
 * Course: COMP 2071
 * Assignment: Lab 5
 * Group #: 11
 * Group members: Tom Plano, Cliff Anderson, Will Lawrence, Artur Janowiec
 * Due date: 4/6/16
 */
public class HashTable
{

    public HashTable(int initialSize)
    {
        /*
           string s
           if map contain s
                get(s).increment()
           else
                map.put(s, new AtomicInteger(1)
            */
    }

    /**
     * Add a new entry to the HashTable. If the entry already exists, replace the value
     * @param s the String
     * @param i the value
     */
    public void put(String s, AtomicInteger i)
    {

    }

    /**
     * Check whether the table contains a key
     * @param s The key to check for
     * @return True if exists, False is not
     */
    public boolean containsKey(String s)
    {
        return false;
    }

    /**
     * Get the value given a key
     * @param s The key
     * @return The value
     */
    public AtomicInteger get(String s)
    {
        return null;
    }

    /**
     * Get the current (filled) size of the table
     * @return The current filled size
     */
    public int size()
    {
        return 0;
    }

    /**
     * Get the hashcode of a string
     * @param s The string
     * @return The hashcode
     */
    int getStringHashCode(String s)
    {
        int hash = 0;

        for(Character c : s.toCharArray())
        {
            hash += Character.getNumericValue(c);
        }

        return hash;
    }
}
