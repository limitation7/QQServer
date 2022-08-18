package QQ.qqservice.service;

import QQ.qqcommon.Message;
import QQ.qqcommon.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ServerConnectClientThread extends Thread{
    private Socket socket;
    private String userId;//链接到服务端的用户id

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }
    public Socket getSocket(){
        return socket;
    }

    @Override
    public void run() {//线程处于run状态，可以发送/接受消息
        while (true){
            System.out.println("服务端和客户端"+userId+"保持通讯，读取数据");
            try {

                Message message=new Message();
                //System.out.println(QQServer.getOffLineDB().get(userId));
                    if(QQServer.getOffLineDB().get(userId)!=null){

                        //System.out.println(QQServer.getOffLineDB().get(userId));
                        ArrayList<Message> arrayMessage=QQServer.getOffLineDB().get(userId);
                        //System.out.println("赋值message执行了");
                        if(!arrayMessage.isEmpty()){
                            for(Message m:arrayMessage){

                                message =m;
                                arrayMessage.remove(m);
                                break;
                            }
                        }
                        else {
                            System.out.println("删除");
                            QQServer.getOffLineDB().remove(userId);
                            continue;
                        }
                    }
                    else {
                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        message=(Message)ois.readObject();
                    }

            //    System.out.println(QQServer.getOffLineDB().get("200"));


                //后面会使用message,根据Message类型做相应的业务处理
                if(message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){
                    //客户端要在线用户列表
                    /*100 200 xxxx*/
                    System.out.println(message.getSender()+"要在线用户列表");
                    String onLineUser=ManageClientThreads.getOnLineUser();
                    //返回message
                    // 构建message对象返回给客户端
                    Message message2=new Message();
                    message2.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message2.setContent(onLineUser);
                    message2.setGetter(message.getSender());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);

                }else if(message.getMesType().equals(MessageType.MESSAGE_COMM_MES)){

                    if(ManageClientThreads.getHm().get(message.getGetter())==null){
                        //判断如果用户不在线
                        if(QQServer.getOffLineDB().get(message.getGetter())==null){//如果不在线的人不在列表中就新建一个
                            ArrayList<Message> arrayMessage=new ArrayList<>();
                            arrayMessage.add(message);
                            QQServer.getOffLineDB().put(message.getGetter(),arrayMessage );
                        }
                        else {//如果不在线人的人在offLine中
                            QQServer.getOffLineDB().get(message.getGetter()).add(message);
                        }
//                        System.out.println(QQServer.getOffLineDB());
                    }else {
                        //根据message获取getterId再得到对应的线程
                        ServerConnectClientThread serverConnectClientThread =
                                ManageClientThreads.getServerConnectClientThread(message.getGetter());
                        //得到对应的socket的对象输出流。将message对象转发给指定的客户端
                        ObjectOutputStream oos =
                                new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                        oos.writeObject(message);//如果客户不在线，可以保存到数据库，这样就可以实现离线留言
                    }

                }else if(message.getMesType().equals(MessageType.MESSAGE_TO_ALL_MES)){
                    //需要遍历管理线程的集合，把所有线程的socket都得到，然后把message转发
                    HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
                    Iterator<String> iterator = hm.keySet().iterator();
                    while (iterator.hasNext()){
                        String onLineUserId = iterator.next();
                        if(!onLineUserId.equals(message.getSender())){

                            ObjectOutputStream oos =
                                    new ObjectOutputStream(hm.get(onLineUserId).getSocket().getOutputStream());
                            oos.writeObject(message);
                        }
                    }
                }else if(message.getMesType().equals(MessageType.MESSAGE_FILE_MES)){
                    //根据getterId获取到对应线程将message对象转发
                    ServerConnectClientThread serverConnectClientThread = ManageClientThreads.getServerConnectClientThread(message.getGetter());
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    oos.writeObject(message);
                }

                else if(message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)){
                    //关闭线程
                    System.out.println(message.getSender()+"退出客户端");
                    //将客户端从集合中删除
                    ManageClientThreads.removeClientThread(message.getSender());
                    socket.close();
                    //退出线程
                    break;
                }

                else {
                    System.out.println("其他类型的message,暂时不处理");
                }
                //System.out.println(QQServer.getOffLineDB().get("200"));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
