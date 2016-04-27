/**
 * Created by hannahzhang on 16/4/24.
 */
public class User {
    private String userName;
    private String passwd;
    private StratergyEnum stratergyEnum;

    public User(String userName, String passwd, StratergyEnum stratergyEnum) {
        this.userName = userName;
        this.passwd = passwd;
        this.stratergyEnum = stratergyEnum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public StratergyEnum getStratergyEnum() {
        return stratergyEnum;
    }

    public void setStratergyEnum(StratergyEnum stratergyEnum) {
        this.stratergyEnum = stratergyEnum;
    }
}
