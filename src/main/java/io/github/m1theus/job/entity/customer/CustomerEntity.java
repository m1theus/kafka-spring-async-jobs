package io.github.m1theus.job.entity.customer;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import static java.lang.String.format;

@Builder
@Entity
@Table(name = "CUSTOMER")
@Getter
@NoArgsConstructor
public class CustomerEntity {

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

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "NATIONALITY", nullable = false)
    private String nationality;

    @Column(name = "COUNTRY", nullable = false)
    private String country;

    @Column(name = "FULL_ADDRESS", nullable = false)
    private String fullAddress;

    public CustomerEntity(Long id, String firstName, String lastName, String email, String nationality, String country,
                          String fullAddress) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.nationality = nationality;
        this.country = country;
        this.fullAddress = fullAddress;
    }

    public String getFullName() {
        return format("%s %s", firstName, lastName);
    }

}
