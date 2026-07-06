package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class ServerSender extends Thread {
    private DataOutputStream output;
    private Scanner scanner;

    public ServerSender(DataOutputStream output){
        this.output=output;
        this.scanner=new Scanner(System.in);
    }

    @Override
    public void run(){
        try {
            while (true) {
                String message=scanner.nextLine();
                output.writeUTF(message);
                output.flush();
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Unable to send message.");
        }
    }
}
