package com.evolveback.srikant.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the LineItoms entity.
 */
public class LineItomsDTO implements Serializable {

    private Long id;

    private Long serialNumber;

    private String particular;

    private Integer amounts;

    private Long billDetailsId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getParticular() {
        return particular;
    }

    public void setParticular(String particular) {
        this.particular = particular;
    }

    public Integer getAmounts() {
        return amounts;
    }

    public void setAmounts(Integer amounts) {
        this.amounts = amounts;
    }

    public Long getBillDetailsId() {
        return billDetailsId;
    }

    public void setBillDetailsId(Long billDetailsId) {
        this.billDetailsId = billDetailsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LineItomsDTO lineItomsDTO = (LineItomsDTO) o;
        if(lineItomsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), lineItomsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LineItomsDTO{" +
            "id=" + getId() +
            ", serialNumber=" + getSerialNumber() +
            ", particular='" + getParticular() + "'" +
            ", amounts=" + getAmounts() +
            "}";
    }
}
