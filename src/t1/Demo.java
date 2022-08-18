package t1;

import java.util.ArrayList;

public class Demo {
    public static void main(String[] args) {
        ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add("String");

        Thr t=new Thr(arrayList);
        t.start();
        arrayList.set(0,"bbbbb");
        System.out.println("========================");
    }
}
