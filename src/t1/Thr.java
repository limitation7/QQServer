package t1;

import java.util.ArrayList;

public class Thr extends Thread{
    private ArrayList<String> arrayList;
    public Thr(ArrayList<String> arrayList){
        this.arrayList=arrayList;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(arrayList.get(0));
        }
    }
}
