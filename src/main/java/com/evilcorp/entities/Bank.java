package com.evilcorp.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(schema = "bank", name = "bank")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "bin", nullable = false, unique = true, length = 9)
    private String bin;
}
