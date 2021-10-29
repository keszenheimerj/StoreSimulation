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
 * 
 */
class Register {
    private ArrayList<Customer> queue;
    private double scanTime;
    private double payTime;
    private int registerIndex;
    private double averageWaitTime;
    private int totalCustomers;
    private double totalWaitTime;
    private int maxLineLength = 0;
    private int waitedOver2 = 0;
    private int waitedOver3 = 0;
    private int waitedOver5 = 0;
    private int waitedOver10 = 0;
    
    /**
     * 
     * @param d double scanTime
     * @param d0 double payTime
     */
    Register(double d, double d0) {
        queue = new ArrayList<>();
        this.scanTime = d;
        this.payTime = d0;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * 
     * @return current size of Line
     */
    int getLineLength() {
        return queue.size();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * 
     * @param customer takes in customer that will be added to line
     */
    void enqueue(Customer customer) {
        double customerWaitTime = 0;
        queue.add(customer); //adds customer to back of line
        totalCustomers++;
        
        if(getLineLength()>1){//if line is longer than 1 after customer added then a wait exists
            for (int i = 1; i < queue.size()-1; i++) {//cycle through line to see wait time
                totalWaitTime += (queue.get(i).getNumItems()*scanTime + payTime);
                customerWaitTime += (queue.get(i).getNumItems()*scanTime + payTime);
            }
        }
        
        averageWaitTime = totalWaitTime/totalCustomers;//calculate average wait time
        
        if(getLineLength() > maxLineLength)//updates max line length
        {
            maxLineLength = queue.size();
        }
        
        //update customer waited over times
        if(customerWaitTime > 2){
            waitedOver2++;
        }
        if(customerWaitTime > 3){
            waitedOver3++;
        }
        if(customerWaitTime > 5){
            waitedOver5++;
        }
        if(customerWaitTime > 10){
            waitedOver10++;
        }
        if(maxLineLength>10){
            //System.out.println("");
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * 
     * @return customer who is being removed from line
     */
    Customer dequeue() {
        if(isEmpty()){//if empty line do not continue
            return null;
        }
        Customer output = new Customer(queue.get(0).getArriveTime(), queue.get(0).getNumItems(), queue.get(0).getArriveTime());//store customer who will be removed
        output.setCheckoutLine(registerIndex);
        queue.remove(0);
        return output;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @return true if line is empty
     *         false if line has customer(s)
     */
    boolean isEmpty() {
        return queue.size() == 0;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @return customer who is at front of line
     */
    Customer peek() {
        if(isEmpty()){
            return null;
        }
        return queue.get(0);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @return scan time at register
     */
    double getScanTime() {
        return scanTime;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 
     * @return pay time at register
     */
    double getPayTime() {
        return payTime;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * 
     * @param rIndex the register index number
     */
    void setRegisterIndex(int rIndex){
        registerIndex = rIndex;
    }
    
    /**
     * 
     * @return the register index number
     */
    int getRegisterIndex(){
        return registerIndex;
    }
    
    /**
     * 
     * @return average wait time
     */
    double getAverageWaitTime(){
        return averageWaitTime;
    }
    
    /**
     * 
     * @return total customers register has had
     */
    double getTotalCustomers(){
        return totalCustomers;
    }
    
    /**
     * 
     * @return accumulative wait time at register
     */
    double getTotalWaitTime(){
        return totalWaitTime;
    }
    
    /**
     * 
     * @return maximum length register has had
     */
    int getMaxLineLength(){
        return maxLineLength;
    }
    
    /**
     * 
     * @return number of customers who waited over 2 minutes
     */
    int getWaitedOver2(){
        return waitedOver2;
    }
    
    /**
     * 
     * @return number of customers who waited over 3 minutes
     */
    int getWaitedOver3(){
        return waitedOver3;
    }
    
    /**
     * 
     * @return number of customers who waited over 5 minutes
     */
    int getWaitedOver5(){
        return waitedOver5;
    }
    
    /**
     * 
     * @return number of customers who waited over 10 minutes
     */
    int getWaitedOver10(){
        return waitedOver10;
    }
}
