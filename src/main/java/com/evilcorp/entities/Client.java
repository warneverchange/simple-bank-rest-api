package com.evilcorp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(schema = "bank", name = "client")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "short_name", nullable = false)
    private String shortName;

    @Basic
    @Column(name = "address", nullable = false)
    private String address;

    @ManyToOne
    @JoinColumn(name = "legal_type_id", nullable = false)
    private LegalType legalType;

    @OneToMany
    @JoinColumn(name = "client_id", nullable = false)
    private List<BankDeposit> deposits;
}
