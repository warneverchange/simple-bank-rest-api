package com.evilcorp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(schema = "bank", name = "bank_deposit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BankDeposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;

    @Basic
    @Column(name = "opening_date", nullable = false)
    private Timestamp openingDate;

    @Basic
    @Column(name = "annual_rate", nullable = false)
    private Double annual_rate;

    @Basic
    @Column(name = "period", nullable = false)
    private Integer period;
}
