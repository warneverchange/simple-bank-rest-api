package com.evilcorp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "bank", orphanRemoval = true)
    @JsonIgnoreProperties("deposits")
    private List<BankDeposit> deposits = new LinkedList<>();
}
