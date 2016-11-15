
import java.io.Serializable;
//import net.i2p.client.streaming.I2PSocketManager;


public class User_Profile implements Serializable {
    
    private String nickname;
    private String ip;
    private int option;
    private String what_are_you;
    
    
    User_Profile(String p_nick,String p_ip,int p_option){
        nickname=p_nick;
        ip=p_ip;
        option=p_option;
    }
    
    
    String get_nickname(){
        return nickname;
    }
    
    String getip(){
        return ip;
    }
    
    int getoption(){
        return option;
    }
    
    void setwhat(String l){
        what_are_you=l;
    }
    
    String getwhat(){
        return what_are_you;
    }
}
