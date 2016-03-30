import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Artur on 3/30/2016.
 */
public class ArturMain {

    public static void main(String[] args) throws Exception {

        long startTime = System.currentTimeMillis();

        HashTable hashTable = new HashTable(100002);

        hashTable.put("deed", new AtomicInteger(2));
        hashTable.put("Artur", new AtomicInteger(3));

        for (int i = 0; i < 100000; i++) {
            hashTable.put("key: " + i, new AtomicInteger((i * 100) % 10));
        }

        System.out.println("Size: " + hashTable.size());
        System.out.println("Value when key = \"Artur\": " + hashTable.get("Artur"));
        System.out.println("Is the key \"Artur\" in the table: " + hashTable.containsKey("Artur"));
        System.out.println("Key 5000 is in table? " + hashTable.containsKey("key: 5000"));

        //Iterator iterator = hashTable.iterator();

        /*
        System.out.println(iterator.hasNext());
        System.out.println(iterator.next());
        System.out.println(iterator.hasNext());
        System.out.println(iterator.next());
        */

        /*
        while (true) {
            System.out.println(iterator.hasNext());
            System.out.println(iterator.next());

            Thread.sleep(2000);
        }
        */

        /*
        ArrayList<String> arrayList = new ArrayList<>();
        Iterator iterator = arrayList.iterator();
        iterator.hasNext();
        */

        /*
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Artur", "Best Student");
        Set<String> set = hashMap.keySet();
        Iterator iterator = set.iterator();

        System.out.println(iterator.hasNext());
        System.out.println(iterator.next());

        */

        Iterator ourIterator = hashTable.keySet().iterator();

        while (ourIterator.hasNext()) {
            System.out.println(ourIterator.next());
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Time taken in milliseconds: " + (endTime - startTime));
    }
}
