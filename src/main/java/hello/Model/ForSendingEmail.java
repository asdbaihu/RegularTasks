package hello.Model;

import org.springframework.stereotype.Repository;

@Repository
public class ForSendingEmail {
    private String email = "germashevanton@gmail.com";
    private String password = "ar0513vo";
    private String text = "Hello,\n\nIt is my task for Infocom\n\nYour faithfully,\nAnton";

    public ForSendingEmail(){};

    public ForSendingEmail(String email, String password, String text) {
        this.email = email;
        this.password = password;
        this.text = text;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
