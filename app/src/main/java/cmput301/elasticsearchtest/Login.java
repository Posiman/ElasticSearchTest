package cmput301.elasticsearchtest;

/**
 * Created by Stu on 2016-03-12.
 */
public class Login {
    private String uname;
    private String pword;

    public Login(String uname, String pword){
        this.uname = uname;
        this.pword = pword;
    }

    public String getUsername() {
        return uname;
    }

    public void setUsername(String username) {
        this.uname = username;
    }

    public String getPassword() {
        return pword;
    }

    public void setPassword(String password) {
        this.pword = password;
    }

    public String toString(){
        return "{" + this.uname + ": " + this.pword + "}";
    }
}
