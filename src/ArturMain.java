import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Artur on 3/30/2016.
 */
public class ArturMain {

    public static void main(String[] args) throws Exception {

        HashMap hashMap = new HashMap(100002);

        hashMap.put("deed", new AtomicInteger(2));
        hashMap.put("Artur", new AtomicInteger(3));

        for (int i = 0; i < 100000; i++) {
            hashMap.put("key: " + i, new AtomicInteger((i * 100) % 7));
        }

        System.out.println("Size: " + hashMap.size());
        System.out.println("Value when key = \"Artur\": " + hashMap.get("Artur"));
        System.out.println("Is the key \"Artur\" in the table: " + hashMap.containsKey("Artur"));
        System.out.println("Key 5000 is in table? " + hashMap.containsKey("key: 5000"));

        //Iterator iterator = hashMap.iterator();

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

        long startTime = System.currentTimeMillis();

        Iterator valueIterator = hashMap.values().iterator();
        Iterator keyIterator = hashMap.keySet().iterator();

        while (valueIterator.hasNext() && keyIterator.hasNext()) {
            System.out.println((keyIterator.next()) + ", value: " + (valueIterator.next()));
        }


        long endTime = System.currentTimeMillis();

        System.out.println("Time taken in milliseconds: " + (endTime - startTime));
    }
}
