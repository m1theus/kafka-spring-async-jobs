package io.github.m1theus.job.entity.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Entity
@Table(name = "PUBLISHED_MESSAGE")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPublishedMessageEntity {

    @Id
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

}
