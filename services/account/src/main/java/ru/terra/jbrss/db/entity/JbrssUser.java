package ru.terra.jbrss.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "users")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class JbrssUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", nullable = false, length = 35)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    @Column(name = "level")
    private Integer level;
    @Basic(optional = false)
    @Column(name = "login", nullable = false, length = 128)
    private String login;
    @Basic(optional = false)
    @Column(name = "password", nullable = false, length = 128)
    private String password;
}