package edu.ib.object;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="login_logs")
public class LoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pesel;

    private String ip;

    @Column(name="date_of_action")
    private LocalDateTime timestamp;

    private boolean success;

    public LoginLog() {
    }

    public LoginLog(Long pesel, String ip, LocalDateTime timestamp, boolean success) {
        this.pesel = pesel;
        this.ip = ip;
        this.timestamp = timestamp;
        this.success = success;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPesel() {
        return pesel;
    }

    public void setPesel(Long pesel) {
        this.pesel = pesel;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
