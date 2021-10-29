/**
 * Store Simulation Project
 * This file controls the flow of the store simulation.
 */
package storesimulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Katie Timmerman 
 */
public class StoreSimulation {

    private static final int NUMBER_STANDARD_CHECKOUT = 20; // number of cashier registers
    private static final int NUMBER_SELF_CHECKOUTS = 4; // number of self-scan registers
    private static double simClock = 0; // elapsed time (minutes)
    private static MyHeap events = new MyHeap(); // events that occur in the store
    private static ArrayList<Register> registers = new ArrayList(); // registers used in the store

    public static void main(String[] args) {
        //testHeap();
        //testRegister();
        startSimulation();
    }
    
    public static void testHeap(){
        MyHeap h = new MyHeap();
        System.out.println("Initial Size :" + h.getSize() + " ; should be 0");
        System.out.print("Remove should return null; returned: ");
        if(h.remove() == null){
            System.out.println("null");
        }else{
            System.out.println("value");
        }
        
        Customer first = new Customer(4, 6, .5);
        Customer second = new Customer(5, 10, .25);
        Customer third = new Customer(6, 5, 1);
        
        h.insert(new Event(first, 4, EventType.ARRIVAL));
        Event check = new Event(second, 5, EventType.ARRIVAL);
        h.insert(check);
        Event check2 = new Event(third, 6, EventType.ARRIVAL);
        h.insert(check2);
        h.insert(new Event(first, 7, EventType.END_SHOPPING));
        h.insert(new Event(second, 9, EventType.END_SHOPPING));
        h.insert(new Event(third, 11, EventType.END_SHOPPING));
        
        System.out.println("Size after adding six events :" + h.getSize() + " ; should be 6");
        System.out.print("First removed shouldnt be null; it is ");
        if(h.remove() == null){
            System.out.println("null");
        }else{
            System.out.println("not null");
        }
        System.out.print("Second removed event should be first " + h.remove().isFirst(check2));
        
    }
    
    public static void testRegister(){
        Register r = new Register(0.015, 1.5);
        Register p = new Register(0.04, 3);
        Customer first = new Customer(4, 6, .5);
        Customer second = new Customer(5, 10, .25);
        Customer third = new Customer(6, 5, 1);
        r.enqueue(first);
        r.enqueue(second);
        
        //test enqueue
        System.out.println("Line Length should be 2; it is: " + r.getLineLength());
        System.out.println("Line Length should be 0; it is: " + p.getLineLength());
        
        //test dequeue
        r.dequeue();
        System.out.println("Line Length should be 1; it is: " + r.getLineLength());
        
        //peak
        System.out.print("Peek should yield the second customer; it yielded : ");
        if(r.peek().equals(second)){
            System.out.println("the second customer");
        }else{
            System.out.println("not the second cutomer");
        }
        
        System.out.print("Peek shouldn't yield the first customer; it yielded : ");
        if(r.peek().equals(first)){
            System.out.println("the first customer");
        }else{
            System.out.println("not the first cutomer");
        }
        
        r.setRegisterIndex(1);
        p.setRegisterIndex(4);
        
        System.out.println("R register index should be 1; it is: " + r.getRegisterIndex());
        System.out.println("P register index should be 4; it is: " + p.getRegisterIndex());
    }
    
    public static void startSimulation(){
        
        loadRegisters();
        loadCustomerData();

        // Events are stored in a priority queue, so they will always be returned in order.
        while (events.getSize() > 0) {
            Event e = events.remove();
            simClock = e.getEventTime(); // Always set the clock to the time of the new event.
            if (e.getEventType() == EventType.ARRIVAL) {
                handleArrival(e);
            } else if (e.getEventType() == EventType.END_SHOPPING) {
                handleEndShopping(e);
            } else {
                handleEndCheckout(e);
            }
        }
        printCollectedStatistics();
       
    }
    
    /**
     * The arguments for the Register class are (scanTime, payTime)
     * They may need to be updated.
     */
    private static void loadRegisters() {
        for (int i = 0; i < NUMBER_STANDARD_CHECKOUT; i++) {
            Register r = new Register(0.015, 1.5);
            registers.add(r);
        }
        for (int i = 0; i < NUMBER_SELF_CHECKOUTS; i++) {
            Register r = new Register(0.04, 3);
            registers.add(r);
        }
    }

    private static void loadCustomerData() {
        double arriveTime, avgSelectionTime;
        int items;

        try {
            File myFile = new File("arrival.txt");
            Scanner inputFile = new Scanner(myFile);
            while (inputFile.hasNext()) {
                arriveTime = inputFile.nextDouble();
                items = inputFile.nextInt();
                avgSelectionTime = inputFile.nextDouble();
                Customer customer = new Customer(arriveTime, items, avgSelectionTime);
                Event event = new Event(customer, arriveTime, EventType.ARRIVAL);
                events.insert(event);
            }//end while
            inputFile.close();
        } catch (FileNotFoundException e) {
            System.err.println("File was not found");
            System.exit(0);
        }
    }

    private static void handleArrival(Event e) {
        Customer c = e.getCustomer();
        double endShoppingTime = c.getArriveTime() + c.getNumItems() * c.getAvgSelectionTime();
        Event endShopping = new Event(c, endShoppingTime, EventType.END_SHOPPING);
        events.insert(endShopping);
    }

    private static void handleEndShopping(Event e) {
        int shortest = getShortestLine();
        Customer customer = e.getCustomer();
        customer.setCheckoutLine(shortest); // Customer will always enter shortest checkout line.
        registers.get(shortest).enqueue(customer); // Even if line is empty, customer must be enqueued and dequeued so that the customer gets included in the stats for the register
        if (registers.get(shortest).getLineLength() == 1) { // If new customer is the only one in line, begin checkout.
            startCheckout(customer);
        }
    }

    private static void handleEndCheckout(Event e) {
        int line = e.getCustomer().getCheckoutLine();
        Customer c = registers.get(line).dequeue();
        if (registers.get(line).isEmpty()) {
            return;
        } else {
            Customer customer = registers.get(line).peek();
            startCheckout(customer);
        }
    }

    /**
     * 
     * @param customer that is starting checkout
     */
    private static void startCheckout(Customer customer) {
        int line = customer.getCheckoutLine();
        double checkoutLength = customer.getNumItems() * registers.get(line).getScanTime() + registers.get(line).getPayTime();
        Event endCheckout = new Event(customer, checkoutLength + simClock, EventType.END_CHECKOUT);
        events.insert(endCheckout);
    }

    /**
     * runs all stats and prints
     */
    private static void printCollectedStatistics() {
        printAllStat();
    }
    
    //moved stats incase desired to add more, preventing future clutter
    private static void printAllStat(){
        double totalWaitTimes = 0;
        double totalCustomers = 0;
        
        double standardWaitTimes = 0;
        double standardCustomers = 0;
        
        double selfWaitTimes = 0;
        double selfCustomers = 0;
        
        int waitedOver2 = 0;
        int waitedOver3 = 0;
        int waitedOver5 = 0;
        int waitedOver10 = 0;
        
        
        System.out.println("Collected average wait times for registers:");
        for (int i = 0; i < registers.size(); i++) {//cycle through registers
            totalWaitTimes = registers.get(i).getAverageWaitTime();
            totalCustomers += registers.get(i).getTotalCustomers();////+
            System.out.println("Register " + i + " had an average wait time of " + registers.get(i).getAverageWaitTime()/registers.get(i).getTotalCustomers());
            System.out.println(registers.get(i).getTotalCustomers() + " customers passed through register " + i);
            System.out.println(registers.get(i).getMaxLineLength() + " customers was the maximum line length");
            if(registers.get(i).getPayTime() == 1.5){//standard chekout
                standardWaitTimes = registers.get(i).getAverageWaitTime();
                standardCustomers = registers.get(i).getTotalCustomers();
                System.out.println("^^ Standard Checkout Register");
            }else{//self checkout
                selfWaitTimes = registers.get(i).getAverageWaitTime();
                selfCustomers = registers.get(i).getTotalCustomers();
                System.out.println("^^ Self Checkout Register");
            }
            System.out.println("Waited over 2 minutes: " + registers.get(i).getWaitedOver2());
            waitedOver2 += registers.get(i).getWaitedOver2();
            waitedOver3 += registers.get(i).getWaitedOver3();
            waitedOver5 += registers.get(i).getWaitedOver5();
            waitedOver10 += registers.get(i).getWaitedOver10();
            System.out.println("");//provide space
        }
        System.out.println("");//provide space
        System.out.println("The average wait time across registers was " + (totalWaitTimes/totalCustomers) + " when the number of registers was " + registers.size());
        System.out.println("The average wait time for standard checkout registers is " + (standardWaitTimes/standardCustomers));
        System.out.println("The average wait time for self checkout registers is " + (selfWaitTimes/selfCustomers));
        System.out.println("With " + NUMBER_STANDARD_CHECKOUT + " standard checkout registers and " + NUMBER_SELF_CHECKOUTS + " self checkout registers");
        System.out.println("");//provide space
        System.out.println("The percentage that waited over two minutes was " + (waitedOver2/totalCustomers)*100 + "%");
        System.out.println("The percentage that waited over three minutes was " + (waitedOver3/totalCustomers)*100 + "%");
        System.out.println("The percentage that waited over five minutes was " + (waitedOver5/totalCustomers)*100 + "%");
        System.out.println("The percentage that waited over ten minutes was " + (waitedOver10/totalCustomers)*100 + "%");
        System.out.println("Total Customers " + totalCustomers);
    }

    /**
     * 
     * @return shortest register line number
     * if no registers exist returns -1
     */
    private static int getShortestLine() {
        
        if(registers.size()<1)
        {
            return -1;
        }
        int shortestLineIndex = 0;
        for (int i = 0; i < registers.size(); i++) {//cycle through registers to update lowest line size
            if(registers.get(i).getLineLength() < registers.get(shortestLineIndex).getLineLength()){
                shortestLineIndex = i;
            }
        }
        //System.out.println("NOT IMPLEMENTED YET");
        return shortestLineIndex;
    }

}
