/**
 * Course: COMP 2071
 * Assignment: Lab 5
 * Group #: 11
 * Group members: Tom Plano, Cliff Anderson, Will Lawrence, Artur Janowiec
 * Due date: 4/6/16
 */
public class MathUtils
{
    //Get the next prime that is equal to or greater than num
    public static int getNextPrime(int num)
    {
        while(!isPrime(num))
        {
            num++;
        }

        return num;
    }

    /**
     * Check if a number is prime or not
     * @param num
     * @return True if prime, False is not
     */
    public static boolean isPrime(int num)
    {
        //Check through all numbers less than half of num
        for(int i = 2; i < num/2; i++)
        {
            if(num % i  == 0)
            {
                //It is divisible
                return false;
            }
        }

        //Was not divisible by any numbers
        return true;
    }
}
