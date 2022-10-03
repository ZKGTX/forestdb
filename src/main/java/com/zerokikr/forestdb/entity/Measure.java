package com.zerokikr.forestdb.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "measures")
public class Measure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull(message = "название адаптационной меры не может быть пустым")
    @Pattern(regexp="[а-яёА-ЯЁ ,.()-]+", message = "название адаптационной меры должно состоять только из русских букв")
    private String name;

    @ManyToOne
    @JoinColumn(name = "risk_id")
    private Risk risk;

    @OneToMany(mappedBy = "measure", fetch = FetchType.LAZY)
    private List<Action> actions;

    public Measure() {
    }

    public Measure(String name) {
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

    public Risk getRisk() {
        return risk;
    }

    public void setRisk(Risk risk) {
        this.risk = risk;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Measure)) return false;
        Measure measure = (Measure) o;
        return Objects.equals(getId(), measure.getId()) && Objects.equals(getName(), measure.getName()) && Objects.equals(getRisk(), measure.getRisk()) && Objects.equals(getActions(), measure.getActions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getRisk(), getActions());
    }

    @Override
    public String toString() {
        return "Measure{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", risk=" + risk +
                ", actions=" + actions +
                '}';
    }
}
