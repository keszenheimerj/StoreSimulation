/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storesimulation;

import java.util.ArrayList;

/**
 *
 * @author timmermank1, keszenheimerJ
 */
class MyHeap{
    //You do not need to resize this.
   private Event[] myHeap =  new Event[5000];  
   private int size;
    
   /**
    * 
    * @return number of events
    */
    int getSize() {
        return size;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * 
     * @return event removed
     */
    Event remove() {
        if(size == 0){//if heap is empty does nothing
            return null;
        }
        Event output = new Event(myHeap[1].getCustomer(), myHeap[1].getEventTime(), myHeap[1].getEventType());//stores event that is next to occur and to be removed
        int removeIndex = 1;//heap does not use 0 index so first event is at 1
        while(!isLeaf(removeIndex)){//stop repositioning when removing eevent is a leaf
            int swapIndex = 0;//will be set to position of next swap with removing event
            if(rightChildIndex(removeIndex) <= size){//checks if a right child exists
                if(myHeap[rightChildIndex(removeIndex)].getEventTime()< myHeap[leftChildIndex(removeIndex)].getEventTime()){//checks if left or right child is next event to take place
                    swapIndex = rightChildIndex(removeIndex);
                }else{
                    swapIndex = leftChildIndex(removeIndex);
                }
                
            }else if(leftChildIndex(removeIndex) <= size){//checks if left child exists
                swapIndex = leftChildIndex(removeIndex);
            }
            if(swapIndex != 0){//if child found swaps with the next event to take place
                swapEvents(removeIndex, swapIndex);
                removeIndex = swapIndex;
            }
        }
        swapEvents(removeIndex, getSize());
        myHeap[getSize()] = null;
        size--;
        return output;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void insert(Event item) {
        if(getSize() == 0){//if empty just add to first index
            myHeap[1] = item;
            size++;
            return;
        }
        int insertIndex = getSize()+1;
        myHeap[insertIndex] = item;// add to bottom right most position
        
        do{
            int parentIndex = getParentIndex(insertIndex);
            if(item.getEventTime()<myHeap[parentIndex].getEventTime()){//swap if parent happens at a later time
                swapEvents(insertIndex, parentIndex);
                insertIndex = parentIndex;
            }else{// if parent event happens first dont move events
                size++;
                return;
            }
        }while(insertIndex > 1);
        
        size++;
       //Event has an isFirst method to help compare event times
    }
    
    /**
     * 
     * @param index of child whose parent is being looked for
     * @return index parent of of child at index 
     */
    private int getParentIndex(int index){
        return index/2;
    }
    
    /**
     * 
     * @param index the index of parent whose child is being looked for
     * @return index of left child of parent supplied at index
     */
    private int leftChildIndex(int index){
        return 2*index;
    }
    
    /**
     * 
     * @param index the index of parent whose child is being looked for
     * @return index of right child of parent supplied at index
     */
    private int rightChildIndex(int index){
        return 2*index + 1;
    }
    
    /**
     * 
     * @param index index of event that is being tested
     * @return true if event is a leaf
     *         false if event is not a leaf
     */
    private boolean isLeaf(int index){
        if(leftChildIndex(index) <= size || rightChildIndex(index) <= size){
            return false;
        }
        return true;
    }
    
    /**
     * 
     * @param i1 first event index
     * @param i2 second event index
     */
    private void swapEvents(int i1, int i2){
        Event temp = new Event(myHeap[i1].getCustomer(), myHeap[i1].getEventTime(), myHeap[i1].getEventType());
        myHeap[i1] = myHeap[i2];
        myHeap[i2] = temp;
    }
    
    
    
}
