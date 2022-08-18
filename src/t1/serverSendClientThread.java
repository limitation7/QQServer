package t1;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeSet;

public class serverSendClientThread extends Thread {
    private Socket socket;
    private HashMap<String,Socket> socketHashMap;
    pack p=new pack();
    public serverSendClientThread(Socket socket, HashMap<String,Socket> socketHashMap){
        this.socket=socket;
        this.socketHashMap=socketHashMap;
    }
    @Override
    public void run() {
        while(true){
            System.out.println("请输入要发送的信息");
            Scanner sc=new Scanner(System.in);
            try {
                ObjectOutputStream oos=null;
                p.setId("admin");
                p.setStr(sc.nextLine());
                System.out.println("剩余用户:"+socketHashMap.keySet());
                for (String s:socketHashMap.keySet()){
                    oos=new ObjectOutputStream(socketHashMap.get(s).getOutputStream());
                    System.out.println("To["+s+"]地址:"+socketHashMap.get(s));
                    oos.writeObject(p);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            //
        }
    }
}
