import java.util.concurrent.atomic.AtomicInteger;

/**
 * Course: COMP 2071
 * Assignment: Lab 5
 * Group #: 11
 * Group members: Tom Plano, Cliff Anderson, Will Lawrence, Artur Janowiec
 * Due date: 4/6/16
 */

/**
 * Custom linked list class to store pairs of AtomicInteger and String
 */
public class TeamLinkedList
{
    private Node head;
    private int size;

    /**
     * Initialize an empty linked list
     */
    public TeamLinkedList()
    {
        this.head = null;
        this.size = 0;
    }

    /**
     * Add a string and atomic integer pair
     * @param s The string
     * @param i The AtomicInteger
     */
    public void add(String s, AtomicInteger i)
    {
        Node newNode = new Node(s, i);
        newNode.setNext(this.head);
        this.head = newNode;
        this.size++;
    }

    /**
     * Get the size of the linked list
     * @return the size
     */
    public int getSize()
    {
        return this.size;
    }

    /**
     * Get an integer at an index
     * @param index The index
     * @return the AtomicInteger
     */
    public AtomicInteger getInteger(int index)
    {
        Node currentNode = this.head;
        for(int i = 0; i < index; i++)
        {
            currentNode = currentNode.getNext();
        }

        return currentNode.getInteger();
    }

    /**
     * Get a string at an index
     * @param index The index
     * @return the String
     */
    public String getString(int index)
    {
        Node currentNode = this.head;
        for(int i = 0; i < index; i++)
        {
            currentNode = currentNode.getNext();
        }

        return currentNode.getString();
    }

    /**
     * Private Node class holding a pair of String and AtomicInteger
     */
    private class Node
    {
        private String s;
        private AtomicInteger i;
        private Node next;

        /**
         * Initialize a new node
         * @param s The String
         * @param i The AtomicInteger
         */
        public Node(String s, AtomicInteger i)
        {
            this.s = s;
            this.i = i;
        }

        /**
         * Set the next node
         * @param next The next node
         */
        public void setNext(Node next)
        {
            this.next = next;
        }

        /**
         * Get the next node
         * @return The next node
         */
        public Node getNext()
        {
            return this.next;
        }

        /**
         * Get the string
         * @return The string
         */
        public String getString()
        {
            return this.s;
        }

        /**
         * Get the integer
         * @return the integer
         */
        public AtomicInteger getInteger()
        {
            return this.i;
        }
    }
}
