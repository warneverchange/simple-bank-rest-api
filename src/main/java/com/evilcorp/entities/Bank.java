package com.evilcorp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bank")
    private List<BankDeposit> deposits;
}
