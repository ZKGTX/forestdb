package com.zerokikr.forestdb.entity;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotNull(message = "название субъекта РФ не может быть пустым")
    @Pattern(regexp="[а-яёА-ЯЁ ,.()-]+", message = "название субъекта РФ должно состоять только из русских букв")
    private String name;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private List<Risk> risks;

    public Subject() {
    }

    public Subject(String name) {
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

    public List<Risk> getRisks() {
        return risks;
    }

    public void setRisk(List<Risk> risks) {
        this.risks = risks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subject)) return false;
        Subject subject = (Subject) o;
        return Objects.equals(getId(), subject.getId()) && Objects.equals(getName(), subject.getName()) && Objects.equals(getRisks(), subject.getRisks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getRisks());
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", risk=" + risks +
                '}';
    }
}
