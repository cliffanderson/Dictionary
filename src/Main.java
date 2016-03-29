import java.util.HashMap;

/**
 * Course: COMP 2071
 * Assignment: Lab 5
 * Group #: 11
 * Group members: Tom Plano, Cliff Anderson, Will Lawrence, Artur Janowiec
 * Due date: 4/6/16
 */
public class Main {

    /** Testing the java HashMap. */
    public static void main(String[] args) {

        HashMap<String, String> map = new HashMap<>();
        map.put("Zetzra", "MODEL X101F563H1G");
        System.out.println(map.get("Zetzra"));

        System.out.println("Value: " + map.containsValue("MODEL X101F563H1G"));

        System.out.println("Key: " + map.containsKey("Zetzra"));

        System.out.println("Entry Set: " + map.entrySet());
    }
}
