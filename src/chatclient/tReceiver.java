/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author m215025
 */
public class tReceiver extends Thread {
    private ChatClientGUI gui;
    private DatagramSocket receiver;

    public tReceiver(DatagramSocket receiver, ChatClientGUI gui){
            this.gui = gui;
            this.receiver = receiver;
    }
    
    public void run() {
        while (true) {
            byte[] input = new byte[400];
            DatagramPacket inputPack = new DatagramPacket(input, input.length);
            try {
                receiver.receive(inputPack);
            } catch (IOException ex) {
                Logger.getLogger(tReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }
            String msg;
            try {
                msg = new String(inputPack.getData(), "UTF-8");
                System.out.println("[Receiver] " + msg);
                gui.updateMessageDisplay(msg);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(tReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
