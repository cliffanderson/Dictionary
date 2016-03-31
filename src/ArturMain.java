import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Artur on 3/30/2016.
 */
public class ArturMain {

    public static void main(String[] args) throws Exception {

        HashMapCustom hashMap = new HashMapCustom(10002);

        hashMap.put("deed", new AtomicInteger(2));
        hashMap.put("Artur", new AtomicInteger(3));

        //Place data into hashMap.
        for (int i = 0; i < 10000; i++) {
            hashMap.put("key: " + i, new AtomicInteger((i * 100) % 7));
        }

        System.out.println("Size: " + hashMap.size());
        System.out.println("Value when key = \"Artur\": " + hashMap.get("Artur"));
        System.out.println("Is the key \"Artur\" in the table: " + hashMap.containsKey("Artur"));
        System.out.println("Key 5000 is in table? " + hashMap.containsKey("key: 5000"));

        //Start log time.
        long startTime = System.currentTimeMillis();

        //The iterators
        Iterator keyIterator = hashMap.keySet().iterator();
        Iterator valueIterator = hashMap.values().iterator();

        /*
        while (keyIterator.hasNext() && valueIterator.hasNext()) {
            System.out.println(keyIterator.next() + ", value: " + valueIterator.next());
        }
        */

        //End log time.
        long endTime = System.currentTimeMillis();

        //Testing clear
        hashMap.clear();

        System.out.println("Is Artur key in hashMap now: " + hashMap.containsKey("Artur"));

        System.out.println(keyIterator.next());
        //So the iterator doesn't update if the hashMap is cleared.
        //Why does this happen?

        System.out.println("Time taken in milliseconds: " + (endTime - startTime));
    }
}
