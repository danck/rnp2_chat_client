/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author m215025
 */
public class tUpdater extends Thread {
    private BufferedReader reader;
    private BufferedWriter writer;
    private ChatClientGUI gui;
    private final int UPDATE_INTERVAL = 5000;
    
    public tUpdater(Socket ssocket, ChatClientGUI gui){
        try {
            reader = new BufferedReader(new InputStreamReader(
                    ssocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(
                    ssocket.getOutputStream()));
        } catch (IOException ex) {
            Logger.getLogger(tUpdater.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.gui = gui;
    }
    
    public void run() {
        // Establish application-level connection
        try {
            write("NEW " + ChatClientGUI.nickname);
            String input = read();
            assert(input.equals("OK"));
        } catch (IOException ex) {
            Logger.getLogger(ChatClientGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Enter infinite update loop
        while (true) {
            try {
                write("INFO");
                String input = read();
                Scanner scan = new Scanner(input);
                assert(scan.hasNext());
                input = scan.next();
                assert(input.equals("LIST"));
                assert(scan.hasNext());
                int numberParticipants = scan.nextInt();
                List<String> participantNicks = new ArrayList<String>(); 
                List<String> participantIPs = new ArrayList<String>();

                // Retrieve currentn participant lists
                for (int i=0; i<numberParticipants; ++i) {
                    assert(scan.hasNext());
                    participantIPs.add(scan.next());
                    assert(scan.hasNext());
                    participantNicks.add(scan.next());                    
                }
                // Refresh backend
                Backend.setParticipantIPs(participantIPs);
                Backend.setParticipantNicks(participantNicks);

                // Refresh display of participant lists
                StringBuilder sb = new StringBuilder();
                for (int i=0; i<participantNicks.size(); ++i) {
                    sb.append(participantNicks.get(i));
                    sb.append(" [" + participantIPs.get(i) + "]");
                    sb.append(System.lineSeparator());
                }
                gui.updateParticipantsDisplay(sb.toString());
                
            } catch (IOException ex) {
                Logger.getLogger(ChatClientGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                sleep(UPDATE_INTERVAL);
            } catch (InterruptedException ex) {
                Logger.getLogger(tUpdater.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void write(String msg) throws IOException {
        System.out.println("[Updater] Sending: " + msg);
        writer.write(msg);
        writer.newLine();
        writer.flush();    
    }
    
    private String read() throws IOException {
        String received = reader.readLine();
        System.out.println("[Updater] Received: " + received);
        return received;
    }
}