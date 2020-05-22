package com.itmo.contraindications.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class ActiveSubstance {
    @Id
    private Integer id;

    private String name;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "activeSubstances")
    private Set<Medicine> medicines;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActiveSubstance)) return false;
        ActiveSubstance substance = (ActiveSubstance) o;
        return getId().equals(substance.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "ActiveSubstance{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
