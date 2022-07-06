package bearmaps.utils.pq;


import java.util.*;


/* A MinHeap class of Comparable elements backed by an ArrayList. */

public class MinHeap<E extends Comparable<E>> {


    /* An ArrayList that stores the elements in this MinHeap. */

    private ArrayList<E> contents;
    private int size;
    

    // TODO: YOUR CODE HERE (no code should be needed here if not

    // implementing the more optimized version)

    private Map<Integer, E> optimize;


    /* Initializes an empty MinHeap. */

    public MinHeap() {

        contents = new ArrayList<>();

        contents.add(null);

        optimize = new HashMap<>();

    }


    /* Returns the element at index INDEX, and null if it is out of bounds. */

    private E getElement(int index) {

        if (index >= contents.size()) {

            return null;

        } else {

            return contents.get(index);

        }

    }


    /* Sets the element at index INDEX to ELEMENT. If the ArrayList is not big

       enough, add elements until it is the right size. */

    private void setElement(int index, E element) {

        while (index >= contents.size()) {

            contents.add(null);

        }


        contents.set(index, element);

    }


    /* Swaps the elements at the two indices. */

    private void swap(int index1, int index2) {

        E element1 = getElement(index1);

        E element2 = getElement(index2);

        setElement(index2, element1);

        setElement(index1, element2);

    }


    /* Prints out the underlying heap sideways. Use for debugging. */

    @Override

    public String toString() {

        return toStringHelper(1, "");

    }


    /* Recursive helper method for toString. */

    private String toStringHelper(int index, String soFar) {

        if (getElement(index) == null) {

            return "";

        } else {

            String toReturn = "";

            int rightChild = getRightOf(index);

            toReturn += toStringHelper(rightChild, "        " + soFar);

            if (getElement(rightChild) != null) {

                toReturn += soFar + "    /";

            }

            toReturn += "\n" + soFar + getElement(index) + "\n";

            int leftChild = getLeftOf(index);

            if (getElement(leftChild) != null) {

                toReturn += soFar + "    \\";

            }

            toReturn += toStringHelper(leftChild, "        " + soFar);

            return toReturn;

        }

    }


    /* Returns the index of the left child of the element at index INDEX. */

    private int getLeftOf(int index) {

        // TODO: YOUR CODE HERE

        return index * 2;

    }


    /* Returns the index of the right child of the element at index INDEX. */

    private int getRightOf(int index) {

        // TODO: YOUR CODE HERE

        return (index * 2) + 1;

    }


    /* Returns the index of the parent of the element at index INDEX. */

    private int getParentOf(int index) {

        // TODO: YOUR CODE HERE

        return index / 2;

    }


    /* Returns the index of the smaller element. At least one index has a

       non-null element. If the elements are equal, return either index. */

    private int min(int index1, int index2) {

        // TODO: YOUR CODE HERE

        if (getElement(index1) == null) {

            return index2;

        }

        else if (getElement(index2) == null) {

            return index1;

        }

        else if (getElement(index1).equals(getElement(index2))) {

            return index1;

        }

        else if (getElement(index1).compareTo(getElement(index2)) < 0) {

            return index1;

        }

        else {

            return index2;

        }

    }


    /* Returns but does not remove the smallest element in the MinHeap. */

    public E findMin() {

        // TODO: YOUR CODE HERE

        return contents.get(1);

    }


    /* Bubbles up the element currently at index INDEX. */

    private void bubbleUp(int index) {

        // TODO: YOUR CODE HERE

        int pIndex = getParentOf(index);

        if (pIndex != 0) {

            if (getElement(index).compareTo(getElement(pIndex)) < 0) {

                swap(index, pIndex);

                bubbleUp(pIndex);

            }

        }

    }


    /* Bubbles down the element currently at index INDEX. */

    private void bubbleDown(int index) {

        // TODO: YOUR CODE HERE

        int lIndex = getLeftOf(index);

        int rIndex = getRightOf(index);

        E curr = getElement(index);


        if (lIndex >= contents.size()) {

            return;

        }

        else {

            int min = min(lIndex, rIndex);


            if (curr.compareTo(getElement(min)) > 0) {

                swap(index, min);

                bubbleDown(min);

            }

        }

    }


    /* Returns the number of elements in the MinHeap. */

    public int size() {

        // TODO: YOUR CODE HERE

        return contents.size() - 1;

    }


    /* Inserts ELEMENT into the MinHeap. If ELEMENT is already in the MinHeap,

       throw an IllegalArgumentException.*/

    public void insert(E element) {

        // TODO: YOUR CODE HERE

        if (optimize.keySet().contains(element.hashCode())) {

            throw new IllegalArgumentException();

        }

        else {

            contents.add(element);

            bubbleUp(contents.size() - 1);

            //optimized

            optimize.put(element.hashCode(), element);

        }

    }


    /* Returns and removes the smallest element in the MinHeap. */

    public E removeMin() {

        // TODO: YOUR CODE HERE

        E min = contents.get(1);

        swap(1, contents.size() - 1);

        contents.remove(contents.size() - 1);

        //optimized

        optimize.remove(min.hashCode());

        //

        if (contents.size() > 1) {

            bubbleDown(1);

        }

        return min;

    }


    /* Replaces and updates the position of ELEMENT inside the MinHeap, which

       may have been mutated since the initial insert. If a copy of ELEMENT does

       not exist in the MinHeap, throw a NoSuchElementException. Item equality

       should be checked using .equals(), not ==. */

    public void update(E element) {

        // TODO: YOUR CODE HERE

        if (contains(element)) {

            int index = 1;

            int low = 1;

            int high = contents.size() - 1;

            while (low <= high) {

                int mid = (int) Math.round(((double) low + high) / 2);

                /*

                System.out.println("low " + low + " mid " + mid + " high " + high);

                System.out.println("index of element " + contents.indexOf(element));

                System.out.println(optimize.get(element.hashCode()).toString());

                */

                int compareResult = contents.get(mid).compareTo(optimize.get(element.hashCode()));

                if (contents.get(mid).equals(element)) {

                    index = mid;

                    break;

                }

                else if (compareResult > 0) {

                    high = mid - 1;

                }

                else {

                    low = mid + 1;

                }

            }

            //optimized must implement balanced binary search tree

            //minheapPQ is a tree based off priorities lowest -> highest (lowest has most priority)

            if (index != 0) {

                setElement(index, element);

                int pIndex = getParentOf(index);

                if (pIndex == 0) {

                    bubbleDown(index);

                }

                else if (getElement(index).compareTo(getElement(pIndex)) < 0) {

                    bubbleUp(index);

                } else {

                    bubbleDown(index);

                }

            }

        }

        else {

            throw new NoSuchElementException();

        }

    }

    /* Returns true if ELEMENT is contained in the MinHeap. Item equality should

       be checked using .equals(), not ==. */

    public boolean contains(E element) {

        // TODO: YOUR CODE HERE

        return optimize.containsKey(element.hashCode());

    }

}
