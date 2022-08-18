package t1;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerThread extends Thread{
    private Socket socket;
    public ServerThread(Socket s){
        this.socket=s;
    }
    @Override
    public void run() {
        while (true) {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                pack o = (pack) ois.readObject();
                System.out.println(currentThread().getName()+":"+o.getStr());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
