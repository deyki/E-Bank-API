package com.EBank.EBankApplication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bank_account")
public class BankAccount {

    @Id
    @SequenceGenerator(name = "bank_account_sequence", sequenceName = "bank_account_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_account_sequence")
    private Long bankAccountID;

    @Column(name = "iban", nullable = false, unique = true)
    private String iban;

    @Column(name = "balance", nullable = false)
    private Float balance;

    @Column(name = "banned")
    private Boolean banned;

    @OneToOne(mappedBy = "bankAccount")
    @JsonIgnore
    private User user;
}
