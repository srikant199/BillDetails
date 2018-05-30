package com.evolveback.srikant.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A BillDetails.
 */
@Entity
@Table(name = "bill_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "billdetails")
public class BillDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_date")
    private ZonedDateTime date;

    @Column(name = "name")
    private String name;

    @Column(name = "bill_number")
    private Long billNumber;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "billDetails")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LineItoms> lineItoms = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public BillDetails date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public BillDetails name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBillNumber() {
        return billNumber;
    }

    public BillDetails billNumber(Long billNumber) {
        this.billNumber = billNumber;
        return this;
    }

    public void setBillNumber(Long billNumber) {
        this.billNumber = billNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public BillDetails phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<LineItoms> getLineItoms() {
        return lineItoms;
    }

    public BillDetails lineItoms(Set<LineItoms> lineItoms) {
        this.lineItoms = lineItoms;
        return this;
    }

    public BillDetails addLineItoms(LineItoms lineItoms) {
        this.lineItoms.add(lineItoms);
        lineItoms.setBillDetails(this);
        return this;
    }

    public BillDetails removeLineItoms(LineItoms lineItoms) {
        this.lineItoms.remove(lineItoms);
        lineItoms.setBillDetails(null);
        return this;
    }

    public void setLineItoms(Set<LineItoms> lineItoms) {
        this.lineItoms = lineItoms;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BillDetails billDetails = (BillDetails) o;
        if (billDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), billDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BillDetails{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", name='" + getName() + "'" +
            ", billNumber=" + getBillNumber() +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            "}";
    }
}
