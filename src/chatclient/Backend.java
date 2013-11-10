/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author m215025
 */

/**
 * Thread-safe backend API
 */
public class Backend {
    private static Lock ipLock      = new ReentrantLock();
    private static Lock nickLock    = new ReentrantLock();
    private static List<String> participantNicks;
    private static List<String> participantIPs;
    
    static List<String> getParticipantNicks() {
        nickLock.lock();
        List<String> rv = participantNicks;
        nickLock.unlock();
        return rv;
    }
    
    static List<String> getParticipantIPs() {
        ipLock.lock();
        List<String> rv = participantIPs;
        ipLock.unlock();
        return rv;
    }
    
    static void setParticipantNicks(List<String> pn) {
        nickLock.lock();
        participantNicks = pn;
        nickLock.unlock();
    }
    
    static void setParticipantIPs(List<String> pi) {
        ipLock.lock();
        participantIPs = pi;
        ipLock.unlock();  
    }
}
