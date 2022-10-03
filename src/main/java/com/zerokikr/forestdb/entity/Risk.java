package com.zerokikr.forestdb.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "risks")
public class Risk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull(message = "название климатического риска не может быть пустым")
    @Pattern(regexp="[а-яёА-ЯЁ ,.()-]+", message = "название климатического риска должно состоять только из русских букв")
    private String name;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @OneToMany(mappedBy = "risk", fetch = FetchType.LAZY)
    private List<Measure> measures;

    public Risk() {
    }

    public Risk(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<Measure> getMeasures() {
        return measures;
    }

    public void setMeasures(List<Measure> measures) {
        this.measures = measures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Risk)) return false;
        Risk risk = (Risk) o;
        return Objects.equals(getId(), risk.getId()) && Objects.equals(getName(), risk.getName()) && Objects.equals(getSubject(), risk.getSubject()) && Objects.equals(getMeasures(), risk.getMeasures());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getSubject(), getMeasures());
    }

    @Override
    public String toString() {
        return name;
    }
}
