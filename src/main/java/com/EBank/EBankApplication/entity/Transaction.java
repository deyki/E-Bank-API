package com.EBank.EBankApplication.entity;

import com.EBank.EBankApplication.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @SequenceGenerator(name = "transaction_sequence", sequenceName = "transaction_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_sequence")
    private Long transactionID;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "sender", nullable = false)
    private String sender;

    @Column(name = "receiver", nullable = false)
    private String receiver;

    @Column(name = "amount", nullable = false)
    private Float amount;

    @Column(name = "date", nullable = false)
    private Date date;
}
