package t1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class ClientReturnServerThread extends Thread{
    private Socket socket;
    private HashMap<String,Socket> socketHashMap;
    private String userid;
    pack p=new pack();
    public ClientReturnServerThread(String userid,Socket socket, HashMap<String,Socket> socketHashMap){
        this.userid=userid;
        this.socket=socket;
        this.socketHashMap=socketHashMap;
    }
    @Override
    public void run() {
        while (true){
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                pack o = (pack)ois.readObject();
                if(o.getStr().equals("exit")&&userid.equals(o.getId())){
                    System.out.println("==========用户["+o.getId()+"]退出系统===========");
                    socketHashMap.remove(o.getId());
                    System.out.println("剩余用户:"+socketHashMap.keySet());
                    break;
                }
                System.out.println(o.getId()+":"+o.getStr());
                //以下代码实现全体消息
                ObjectOutputStream oos=null;
                for (String s:socketHashMap.keySet()){
                    if(!o.getId().equals(s)){//判断不返回信息给发送者
                        oos=new ObjectOutputStream(socketHashMap.get(s).getOutputStream());
                        //System.out.println("To["+s+"]地址:"+socketHashMap.get(s));
                        //System.out.println("fasong"+o.getId());
                        oos.writeObject(o);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
