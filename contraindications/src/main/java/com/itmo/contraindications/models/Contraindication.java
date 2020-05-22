package com.itmo.contraindications.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
public class Contraindication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    @Enumerated(EnumType.STRING)
    private ContraindicationSeverity severity;

    @Enumerated(EnumType.STRING)
    private ContraindicationFrequency frequency;

    private String food;

    private Integer minAge;

    private Integer maxAge;

    @Enumerated(EnumType.STRING)
    private ContraindicationSex sex;

    private String infoSource;

    @OneToOne
    private Medicine medicineSource;

    @OneToOne
    private ActiveSubstance activeSubstanceSource;

    @OneToOne
    private Medicine medicineTarget;

    @OneToOne
    private ActiveSubstance activeSubstanceTarget;

    @OneToOne
    private Disease diseaseTarget;

    @Enumerated(EnumType.STRING)
    private ContraindicationType type;

    public enum ContraindicationFrequency {
        VERY_FREQUENT("Very frequent (>10%)"),
        FREQUENT("Frequent (1 - 10%)"),
        LESS_FREQUENT("Less frequent (0.1 - 1%)"),
        RARE("Rare (0.01 - 0.1%)"),
        VERY_RARE("Very rare (<0.01%)");

        private final String readable;

        ContraindicationFrequency(String readable) {
            this.readable = readable;
        }

        public String getReadable() {
            return this.readable;
        }

        @Override
        public String toString() {
            return this.readable;
        }
    }

    public enum ContraindicationSeverity {
        MINOR("Minor", 1),
        MODERATE("Moderate", 2),
        MAJOR("Major", 3),
        EXTREME("Extreme",4);

        private final String readable;
        private final int severity;

        ContraindicationSeverity(String readable, int severity) {
            this.readable = readable;
            this.severity = severity;
        }

        public String getReadable() {
            return this.readable;
        }

        public int getSeverityNumber() {
            return this.severity;
        }

        public static ContraindicationSeverity getFromNumber(int number) {
            switch (number) {
                case 1: return MINOR;
                case 2: return MODERATE;
                case 3: return MAJOR;
                case 4: return EXTREME;
                default: throw new IllegalArgumentException("No enum with given number");
            }
        }

        @Override
        public String toString() {
            return this.readable;
        }
    }

    public enum ContraindicationSex {
        FEMALE("Female"),
        MALE("Male"),
        BOTH("Both");

        private final String readable;

        ContraindicationSex(String readable) {
            this.readable = readable;
        }

        public String getReadable() {
            return this.readable;
        }

        public String toString() {
            return this.readable;
        }
    }

    public enum ContraindicationType {
        MEDICINE_TO_DISEASE,
        ACTIVE_SUBSTANCE_TO_DISEASE,
        MEDICINE_TO_SEX,
        ACTIVE_SUBSTANCE_TO_SEX,
        MEDICINE_TO_FOOD,
        ACTIVE_SUBSTANCE_TO_FOOD,
        MEDICINE_TO_AGE,
        ACTIVE_SUBSTANCE_TO_AGE,
        ACTIVE_SUBSTANCE_TO_ACTIVE_SUBSTANCE
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contraindication)) return false;
        Contraindication that = (Contraindication) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Contraindication{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", severity=" + severity +
                ", frequency=" + frequency +
                ", food='" + food + '\'' +
                ", minAge=" + minAge +
                ", maxAge=" + maxAge +
                ", sex=" + sex +
                ", infoSource='" + infoSource + '\'' +
                ", type=" + type +
                '}';
    }
}
