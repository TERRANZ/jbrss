package ru.terra.jbrss.db.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class JbrssUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "level")
    private Integer level;
    @Basic(optional = false)
    @Column(name = "login", nullable = false, length = 128)
    private String login;
    @Basic(optional = false)
    @Column(name = "password", nullable = false, length = 128)
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}