package io.github.m1theus.job.entity.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Entity
@Table(name = "PENDING_MESSAGE")
@Getter
@AllArgsConstructor
public class CustomerPendingMessageEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "CUSTOMER_ID", nullable = false)
    private Long customerId;

    @Column(name = "FULL_NAME", nullable = false)
    private String fullName;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "NATIONALITY", nullable = false)
    private String nationality;

    @Column(name = "COUNTRY", nullable = false)
    private String country;

    @Column(name = "FULL_ADDRESS", nullable = false)
    private String fullAddress;

    public CustomerPendingMessageEntity() {

    }

}
