package com.zerokikr.forestdb.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "actions")
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotNull(message = "укажите название нового мероприятия")
    private String name;

    @Column(name = "commentary")
    private String commentary;

    @Column(name = "original")
    private Boolean original;

    @OneToMany(mappedBy = "action", fetch = FetchType.LAZY)
    private List<ReportingYear> reportingYears;

    @ManyToOne
    @JoinColumn(name ="measure_id")
    private Measure measure;

    @Column(name = "last_update")
    private String lastUpdate;


    public Action() {
    }

    public Action(String name)
    {
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

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public Boolean getOriginal() {
        return original;
    }

    public void setOriginal(Boolean original) {
        this.original = original;
    }

    public List<ReportingYear> getReportingYears() {
        return reportingYears;
    }

    public void setReportingYears(List<ReportingYear> reportingYears) {
        this.reportingYears = reportingYears;
    }

    public Measure getMeasure() {
        return measure;
    }

    public void setMeasure(Measure measure) {
        this.measure = measure;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Action)) return false;
        Action action = (Action) o;
        return Objects.equals(getId(), action.getId()) && Objects.equals(getName(), action.getName()) && Objects.equals(getReportingYears(), action.getReportingYears()) && Objects.equals(getMeasure(), action.getMeasure());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getReportingYears(), getMeasure());
    }

    @Override
    public String toString() {
        return "Action{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", reportingYears=" + reportingYears +
                ", measure=" + measure +
                '}';
    }
}
