package com.itmo.contraindications.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disease {
    @Id
    private Integer id;

    private String name;

    public Disease(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Disease)) return false;
        Disease disease = (Disease) o;
        return getId().equals(disease.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Disease{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
