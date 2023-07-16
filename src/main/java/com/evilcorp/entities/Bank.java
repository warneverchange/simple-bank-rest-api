package com.evilcorp.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenerationTime;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "bank")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "bin", nullable = false, unique = true, length = 9)
    private String bin;

    @Basic
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bank", orphanRemoval = true)
    private List<BankDeposit> deposits = new LinkedList<>();
}
