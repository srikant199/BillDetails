package com.evolveback.srikant.domain;



import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A LineItoms.
 */
@Entity
@Table(name = "line_itoms")
@Document(indexName = "lineitoms")
public class LineItoms implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "serial_number")
    private Long serialNumber;

    @Column(name = "particular")
    private String particular;

    @Column(name = "amounts")
    private Integer amounts;

    @ManyToOne
    private BillDetails billDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSerialNumber() {
        return serialNumber;
    }

    public LineItoms serialNumber(Long serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

    public void setSerialNumber(Long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getParticular() {
        return particular;
    }

    public LineItoms particular(String particular) {
        this.particular = particular;
        return this;
    }

    public void setParticular(String particular) {
        this.particular = particular;
    }

    public Integer getAmounts() {
        return amounts;
    }

    public LineItoms amounts(Integer amounts) {
        this.amounts = amounts;
        return this;
    }

    public void setAmounts(Integer amounts) {
        this.amounts = amounts;
    }

    public BillDetails getBillDetails() {
        return billDetails;
    }

    public LineItoms billDetails(BillDetails billDetails) {
        this.billDetails = billDetails;
        return this;
    }

    public void setBillDetails(BillDetails billDetails) {
        this.billDetails = billDetails;
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
        LineItoms lineItoms = (LineItoms) o;
        if (lineItoms.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), lineItoms.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LineItoms{" +
            "id=" + getId() +
            ", serialNumber=" + getSerialNumber() +
            ", particular='" + getParticular() + "'" +
            ", amounts=" + getAmounts() +
            "}";
    }
}
