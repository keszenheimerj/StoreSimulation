/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storesimulation;

/**
 *
 * @author timmermank1
 */

enum EventType {ARRIVAL, END_SHOPPING, END_CHECKOUT}

class Event{

    private Customer customer;
    private double eventTime;
    private EventType eventType;

    Event(Customer customer, double eventTime, EventType eventType){
        setCustomer(customer);
        setEventTime(eventTime);
        setEventType(eventType);
    }
    
    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the eventTime
     */
    public double getEventTime() {
        return eventTime;
    }

    /**
     * @param eventTime the eventTime to set
     */
    public void setEventTime(double eventTime) {
        this.eventTime = eventTime;
    }

    /**
     * @return the eventType
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * @param eventType the eventType to set
     */
    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

   /**
    * Returns true if this event happens before or at the same time as the
    * passed other event.
    * @param other
    * @return 
    */
    public boolean isFirst(Event other) {
         return this.eventTime <= other.eventTime;
    }

   
}
            

