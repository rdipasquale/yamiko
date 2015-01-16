package ar.edu.ungs.yamiko.ga.toolkit;

/**
 * VEBHeap.java
 *
 * Java implementation of a van Emde Boas max-heap, capable of
 * O(lglg n) insert/delete times.
 * 
 * Mark F. Rogers
 * Ph.D., Computational Biology
 * Post-doctoral research assistant at the University of Bristol
 */
public class VEBHeap
{
    private static final int kMinusInfinity = -Integer.MAX_VALUE;
    private static final int kMaxLeaves     = 2;

    /** Exception strings **/
    private static final String kEmptyHeap  = "Heap is empty";
    private static final String kRangeError = "Value out of range";

    /** Child heaps **/
    private VEBHeap fChildren[];

    /** Side heap, tracks indices of maximum-value subheaps **/
    private VEBHeap fSideHeap;

    /** Values stored in leaf nodes only **/
    private int fLeafValues[];

    /** Bookkeeping: **/
    private int fCounter;
    private int fLength;
    private int fLowest;
    private int fRootCeiling;

    /** Values stored for fast retrieval: **/
    private int fMaxTreeValue = kMinusInfinity;


    /**
     * Constructs a new VEB-heap object to hold 'length' distinct
     * values beginning with 'start'.  For example, the following
     * call would create a (tiny) heap for the values 5 through 14:
     *
     *     VEBHeap heap = new VEBHeap(5, 10)
     *
     * @param start beginning of value range for the heap
     * @param length length of the heap
     */
    public VEBHeap(int start, int length)
    {
        assert (length > 0);

        fLowest  = start;
        fLength  = length;

        if (length > kMaxLeaves)
        {
            double range = length;
            fRootCeiling = (int) Math.ceil(Math.sqrt(range));

            int highest  = (start + length) - 1;
            int low      = fLowest;
            int k        = 0;
            fChildren    = new VEBHeap[fRootCeiling];

            /** Generate child heaps: **/
            while (low <= highest)
            {
                int high     = Math.min(highest, low + fRootCeiling - 1);
                int num      = high - low + 1;
                fChildren[k] = new VEBHeap(low, num);

                low += fRootCeiling;
                k++;
            }

            /** Create side heap **/
            fSideHeap = new VEBHeap(0, fRootCeiling);
        }
        else
        {
            fLeafValues = new int[length];
        }
    }


    /**
     * @return true if the node is pristine; false otherwise
     */
    public boolean isPristine()
    {
        return (fCounter == 0);
    }

    /**
     * @return true if the node is a singleton; false otherwise
     */
    public boolean isSingleton()
    {
        return (fCounter == 1);
    }

    /**
     * Removes the maximum element from the heap.
     *
     * @return maximum element in the heap
     * @throws Exception if the heap is empty. 
     */
    public int extractMax() throws Exception
    {
        if (fCounter == 0)
        {
            throw new Exception(kEmptyHeap);
        }

        fCounter--;

        int result = fMaxTreeValue;

        if (isLeaf())
        {
            int i = fMaxTreeValue - fLowest;
            fLeafValues[i]--;

            /**
             * If the value counter drops to 0, but the overall count is
             * nonzero, the current counter must have been higher.
             * If the overall count is 0, the leaf is empty in either case.
             */
            if (fLeafValues[i] == 0)
            {
                fMaxTreeValue = (fCounter > 0) ? fLowest : kMinusInfinity;
            }
        }
        else if (fCounter > 0)
        {
            int i  = fSideHeap.findMax();
            result = fChildren[i].extractMax();

            if (fChildren[i].isPristine())
            {
                fSideHeap.extractMax();
            }

            /**
             * Update the current max value.  Note that this causes
             * the last singleton child to become pristine.
             */
            fMaxTreeValue = (isSingleton())
                ? fChildren[fSideHeap.extractMax()].extractMax()
                : fChildren[fSideHeap.findMax()].findMax();
        }

        return result;
    }

    /**
     * Inserts a value into the heap.
     *
     * If the node is a leaf, it simply updates the counter for the value.
     *
     * If the node is not a leaf, then:
     *   1. If it is pristine, store the value as the max and return
     *   2. If it is a singleton:
     *       a. for the same child, update the child
     *          twice and notify the side heap
     *       b. for different children, update each child once
     *          and notify the side heap twice
     *       c. either way it is two O(1) calls and one O(lglgn) call
     *          so the total is O(lglgn)
     *   3. If the heap has more than two children:
     *       a. update the child
     *       b. if the child is now a singleton, update the side heap
     *       c. either way it is O(lglgn)
     *
     * @param  val value to insert into the heap
     * @throws Exception if the value is out of range
     */
    public void insert(int val) throws Exception
    {
        if ((val < fLowest) || (val >= fLowest + fLength))
        {
            throw new Exception(kRangeError);
        }

        if (isLeaf())
        {
            fLeafValues[val - fLowest]++;
        }
        else if (isPristine())
        {
            // no-op
        }
        else if (isSingleton())
        {
            int i = childIndex(val);
            int j = childIndex(fMaxTreeValue);
            if (i != j)
            {
                fSideHeap.insert(i);
                fSideHeap.insert(j);
            }
            else
            {
                fSideHeap.insert(i);
            }
            fChildren[i].insert(val);
            fChildren[j].insert(fMaxTreeValue);
        }
        else
        {
            int i = childIndex(val);
            fChildren[i].insert(val);

            if (fChildren[i].isSingleton())
            {
                fSideHeap.insert(i);
            }
        }

        fCounter++;
        fMaxTreeValue = Math.max(val, fMaxTreeValue);
    }

    /**
     * @return the maximum value in the heap
     */
    public int findMax()
    {
        return fMaxTreeValue;
    }

    /**
     * Convenience method that displays the entire heap.
     */
    public void showHeap(String prefix)
    {
        System.out.print(prefix + toString() + ": ");
        if (isLeaf()) System.out.print("(leaf)");

        if (isPristine())
        {
            System.out.println("pristine");
        }
        else if (isSingleton())
        {
            System.out.println("singleton-" + fMaxTreeValue);
        }
        else
        {
            System.out.println("count = " + fCounter + "; max = " + fMaxTreeValue);
        }

        if (! isLeaf())
        {
            String nextLevel = prefix + "  ";
            for (int i=0; i<fChildren.length; i++)
            {
                if (fChildren[i] != null) fChildren[i].showHeap(nextLevel);
            }
            nextLevel = prefix + toString()+"/side";
            fSideHeap.showHeap(nextLevel);
        }
    }

    /**
     * @return simple string representation of heap information
     */
    public String toString()
    {
        return " (" + fLowest + "-" + ((fLowest + fLength) - 1) + ")";
    }


    private boolean isLeaf()
    {
        return (fLength <= kMaxLeaves);
    }

    private int childIndex(int val)
    {
        return (val - fLowest) / fRootCeiling;
    }
}