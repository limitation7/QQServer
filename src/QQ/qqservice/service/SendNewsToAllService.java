package QQ.qqservice.service;

import QQ.qqcommon.Message;
import QQ.qqcommon.MessageType;
import QQ.utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class SendNewsToAllService implements Runnable{
    private Scanner sc=new Scanner(System.in);

    @Override
    public void run() {
        while (true){
            System.out.println("请输入服务器要推送的新闻,输入exit退出推送");
            String news = Utility.readString(100);
            if("exit".equals(news)){
                break;
            }
            Message message = new Message();
            message.setSender("服务器");
            message.setContent(news);
            message.setSendTime(new Date().toString());
            message.setMesType(MessageType.MESSAGE_TO_ALL_MES);
            System.out.println("服务器给所有人推送消息给所有人说："+news);
            //遍历当前所有的童心线程，得到socket发送message
            HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
            for(String use:hm.keySet()){
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(hm.get(use).getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
