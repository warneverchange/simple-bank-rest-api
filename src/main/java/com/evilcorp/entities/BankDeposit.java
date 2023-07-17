package com.evilcorp.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "bank_deposit")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BankDeposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false, referencedColumnName = "id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_id", nullable = false, referencedColumnName = "id")
    private Bank bank;

    @Basic
    @Column(name = "opening_date", nullable = false)
    private Timestamp openingDate;

    @Basic
    @Column(name = "annual_rate", nullable = false)
    private Double annualRate;

    @Basic
    @Column(name = "period", nullable = false)
    private Integer period;
}
