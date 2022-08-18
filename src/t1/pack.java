package t1;

import java.io.Serializable;

public class pack implements Serializable {
    private String str;
    private String id;
    private boolean serverUser;
    public pack(String str, String id,boolean serverUser) {
        this.str = str;
        this.id = id;
        this.serverUser=serverUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public pack() {
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public boolean isServerUser() {
        return serverUser;
    }

    public void setServerUser(boolean serverUser) {
        this.serverUser = serverUser;
    }
}
