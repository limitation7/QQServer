package QQ.qqservice.service;

import java.util.HashMap;
import java.util.Iterator;

/*用于管理和客户端通信的线程*/
public class ManageClientThreads {
    private static HashMap<String,ServerConnectClientThread> hm=new HashMap<>();

    //添加线程对象到hm集合
    public static void addClientThread(String userId,ServerConnectClientThread serverConnectClientThread){
        hm.put(userId,serverConnectClientThread);
    }
    public static HashMap<String,ServerConnectClientThread> getHm(){
        return hm;
    }
    public static ServerConnectClientThread getServerConnectClientThread(String userId){
        return hm.get(userId);
    }
    //可以返回在线用户列表
    public static String getOnLineUser(){
        //集合遍历
        Iterator<String> iterator = hm.keySet().iterator();
        String onLineUserList="";
        while(iterator.hasNext()){
            onLineUserList+= iterator.next().toString()+" ";
            System.out.println(onLineUserList);
        }
        return onLineUserList;
    }
    public static void removeClientThread(String userId){
        hm.remove(userId);
    }
}
