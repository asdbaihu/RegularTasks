package hello.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
public class Sendemail {

    @Id
    private Long id;

    private String script;

    private Timestamp timedate;

    private String frequency;

    private Timestamp nextexecution;

    public Timestamp getNextexecution() {
        return nextexecution;
    }

    public void setNextexecution(Timestamp nextexecution) {
        this.nextexecution = nextexecution;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Timestamp getTimedate() {
        return timedate;
    }

    public void setTimedate(Timestamp timedate) {
        this.timedate = timedate;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}

