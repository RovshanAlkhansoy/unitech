package com.app.unitech.entity;

import com.app.unitech.enums.TransactionStatuses;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Table(name = "TRANSACTIONS", schema = "db")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue
    @Column(name = "ID", unique = true, nullable = false)
    private UUID id;

    @Column(name = "FROM_ACCOUNT", nullable = false)
    private String fromAccount;

    @Column(name = "TO_ACCOUNT", nullable = false)
    private String toAccount;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "STATUS")
    private TransactionStatuses status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    @ToString.Exclude
    private User userId;

}
