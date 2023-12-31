package com.evilcorp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "client")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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
    @JoinColumn(name = "legal_type_id", nullable = false, referencedColumnName = "id")
    private LegalType legalType;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "client", orphanRemoval = true)
    @JsonIgnoreProperties("deposits")
    private List<BankDeposit> deposits = new LinkedList<>();
}
