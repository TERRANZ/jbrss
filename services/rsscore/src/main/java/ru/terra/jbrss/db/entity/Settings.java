package ru.terra.jbrss.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "settings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Settings implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "skey", nullable = false, length = 128)
    private String key;
    @Basic(optional = false)
    @Column(name = "svalue", nullable = false, length = 512)
    private String value;
}