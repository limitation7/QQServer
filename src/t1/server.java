package t1;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class server {
    public static void main(String[] args) {

        new server();
    }
    private boolean time=true;//让给服务器发消息的线程在启动连接后只执行一次
    private static HashMap<String,String> hm = new HashMap<>();
    private static HashMap<String,Socket> socketHashMap=new HashMap<>();
    static {
        hm.put("tyx","1");
        hm.put("wp","1");
        hm.put("sxy","1");

    }
    private ServerSocket ss=null;

    public server(){
        try {
            ss= new ServerSocket(9999);
            while (true){

                Socket s = ss.accept();//阻塞
                ObjectInputStream ois=new ObjectInputStream(s.getInputStream());//获取obj输入流
                ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());//获取obj输出流
                pack o = (pack)ois.readObject();
                if(hm.get(o.getId())!=null){//判断使用者信息如果在库中就执行
                    pack p=new pack("",o.getId(),true);
                    oos.writeObject(p);
                    socketHashMap.put(p.getId(),s);//把socket存入到hashmap中统一管理
                    ClientReturnServerThread clientReturnServerThread = new ClientReturnServerThread(p.getId(),s,socketHashMap);
                    clientReturnServerThread.start();
                    if(time){
                        serverSendClientThread serverSendClientThread = new serverSendClientThread(s,socketHashMap);
                        serverSendClientThread.start();//只启动一次给所有用户发消息的程序
                        time=false;
                    }
                    System.out.println("========用户["+o.getId()+"]加入系统==========");
                }
                else {pack p=new pack("","",false);
                    oos.writeObject(p);
                    System.out.println("========用户["+o.getId()+"]不存在==========");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
