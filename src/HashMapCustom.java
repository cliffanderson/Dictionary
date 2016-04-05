import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Course: COMP 2071
 * Assignment: Lab 5
 * Group #: 11
 * Group members: Tom Plano, Cliff Anderson, Will Lawrence, Artur Janowiec
 * Due date: 4/6/16
 */
public class HashMapCustom
{
    //amount of items in the HashMapCustom. Keep in mind that a single array spot can hold many items.
    private int currentSize = 0;

    //Used array space
    private int usedArraySpace = 0;

    //internal array to hold Nodes
    private Node[] nodes;

    public HashMapCustom(int initialSize) throws Exception
    {
        if(initialSize <= 0)
        {
            throw new Exception("Initial size must be greater than 0");
        }

        //make initialSize prime
        if(!MathUtils.isPrime(initialSize))
            initialSize = MathUtils.getNextPrime(initialSize);

        this.nodes = new Node[initialSize];
    }

    /**
     * Add a new entry to the HashTable. If the entry already exists, replace the value
     * @param s the String
     * @param i the value
     */
    public void put(String s, AtomicInteger i)
    {
        /*
        if s exists in any node replace the value
        else add new string/ int pair
         */
        int index = this.getStringHashCode(s)%this.nodes.length;
        Node node = this.nodes[index];

        if (node == null){
            //Make a new node
            this.nodes[index]=new Node(s,i);
            this.usedArraySpace++;
            this.currentSize++;
        }
        else
        {
             while (node.getNext() != null){
                 node = node.getNext();
             }

            //when next=null
            Node newNode =new Node(s,i);
            node.setNext(newNode);
            this.currentSize++;
        }

        //Check to see if resizing is needed
        //Used array space must not exceed 75% of the array length
        if(usedArraySpace >= (int) (this.nodes.length * 0.75))
        {
            //Must resize array
            //Move array to a temp variable
            Node[] temp = this.nodes;

            //Setting the usedArraySpace to 0 because it needs to recalculated since the indexes are going to be different.
            usedArraySpace = 0;

            //Get the new size, the next prime greater than the current length * 2
            int newSize = MathUtils.getNextPrime(this.nodes.length * 2);
            this.nodes = new Node[newSize];

            //Fill new array with old elements
            for(int c = 0; c < temp.length; c++)
            {
                Node n = temp[c];
                while(n != null)
                {
                    //Add every node in chain
                    this.put(n.getString(), n.getInteger());
                    n = n.getNext();
                }
            }

            //Done
        }
    }

    /**
     * Check whether the table contains a key
     * @param s The key to check for
     * @return True if exists, False is not
     */
    public boolean containsKey(String s)
    {
        try
        {
            return this.get(s) != null;
        }
        catch (NoSuchElementException e)
        {
            return false;
        }
    }

    /**
     * Get the value given a key
     * @param s The key
     * @return The value
     */
    public AtomicInteger get(String s) throws NoSuchElementException
    {
        //First check and see if there is a node at index hashcode(s)
        int index = this.getStringHashCode(s) % this.nodes.length;
        if(this.nodes[index] == null)
        {
            return null;
        }

        //Search through this chain of nodes looking for the string s
        Node node = this.nodes[index];
        while(node != null)
        {
            if(node.getString().equals(s))
            {
                //Found it
                return node.getInteger();
            }
            else
            {
                //Go to next node
                node = node.getNext();
            }
        }

        //We never found it
        throw new NoSuchElementException("Could not find integer for string: " + s);
    }

    /**
     * Get the current (filled) size of the table
     * @return The current filled size
     */
    public int size()
    {
        return this.currentSize;
    }

    /**
     * keySet() returns a HashSet of all the keys in the HashMapCustom.
     * The HashSet can be used to get get an iterator to work with the keys in whatever way is needed.
     * @return a HashSet of all the keys.
     * */
    public HashSet<String> keySet() {
        HashSet<String> arrayList = new HashSet<String>();

        //go through every node in this.nodes and then go through the chain
        for(int i = 0; i < this.nodes.length; i++)
        {
            Node n = this.nodes[i];

            while (n != null) {
                arrayList.add(n.getString());
                n = n.getNext();
            }
        }

        return arrayList;
    }

    /**
     * values() returns a linked list of all the values in the HashMapCustom.
     * The linked list can be used to get an iterator to work with the values in whatever way is needed.
     * @return a linked list of all the values.
     */
    public LinkedList<AtomicInteger> values() {

        LinkedList<AtomicInteger> linkedList = new LinkedList<AtomicInteger>();

        int numLeft = nodes.length;

        for (int i = 0; i < numLeft; numLeft--, i++) {
            if (nodes[i] != null) {
                linkedList.add(nodes[i].getInteger());
                numLeft--;

                Node nextNode = nodes[i].getNext();
                while (nextNode != null) {
                    linkedList.add(nextNode.getInteger());
                    nextNode = nextNode.getNext();
                    numLeft--;
                }
            }
        }

        return linkedList;
    }

    public void clear() {
        nodes = new Node[10];
        currentSize = 0;
        usedArraySpace = 0;
    }

    /**
     * Get the hashcode of a string
     * @param s The string
     * @return The hashcode
     */
    private int getStringHashCode(String s)
    {
        int hash = 0;

        for(Character c : s.toCharArray())
        {
            hash += Character.getNumericValue(c);
        }

        return hash;
    }

    private class Node
    {
        private String string;
        private AtomicInteger integer;
        private Node next;

        public Node() {
            next = null;
            string = null;
            integer = null;
        }

        public Node(String string, AtomicInteger integer)
        {
            this.string = string;
            this.integer = integer;
        }

        public String getString()
        {
            return this.string;
        }

        public AtomicInteger getInteger()
        {
            return this.integer;
        }

        public Node getNext()
        {
            return this.next;
        }

        public void setNext(Node next)
        {
            this.next = next;
        }
    }
}
