package com.zerokikr.forestdb.entity;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "reporting_years")
public class ReportingYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "year")
    private Integer year;

    @ManyToOne
    @JoinColumn(name = "action_id")
    private Action action;

    @Column(name = "planned_work_amount")
    private Double plannedWorkAmount;

    @Column(name = "actual_work_amount")
    private Double actualWorkAmount;

    @Column(name = "work_measuring_units")
    private String workMeasuringUnits;

    @Column(name = "planned_work_cost")
    private BigDecimal plannedWorkCost;

    @Column(name = "actual_work_cost")
    private BigDecimal actualWorkCost;

    @Column(name = "cost_measuring_units")
    private String costMeasuringUnits;

    @Column(name = "commentary")
    private String commentary;

    public ReportingYear() {
    }

    public ReportingYear(Integer year, Action action, Double plannedWorkAmount, Double actualWorkAmount, BigDecimal plannedWorkCost, BigDecimal actualWorkCost) {
        this.year = year;
        this.action = action;
        this.plannedWorkAmount = plannedWorkAmount;
        this.actualWorkAmount = actualWorkAmount;
        this.plannedWorkCost = plannedWorkCost;
        this.actualWorkCost = actualWorkCost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Double getPlannedWorkAmount() {
        return plannedWorkAmount;
    }

    public void setPlannedWorkAmount(Double plannedWorkAmount) {
        this.plannedWorkAmount = plannedWorkAmount;
    }

    public Double getActualWorkAmount() {
        return actualWorkAmount;
    }

    public void setActualWorkAmount(Double actualWorkAmount) {
        this.actualWorkAmount = actualWorkAmount;
    }

    public String getWorkMeasuringUnits() {
        return workMeasuringUnits;
    }

    public void setWorkMeasuringUnits(String workMeasuringUnits) {
        this.workMeasuringUnits = workMeasuringUnits;
    }

    public BigDecimal getPlannedWorkCost() {
        return plannedWorkCost;
    }

    public void setPlannedWorkCost(BigDecimal plannedWorkCost) {
        this.plannedWorkCost = plannedWorkCost;
    }

    public BigDecimal getActualWorkCost() {
        return actualWorkCost;
    }

    public void setActualWorkCost(BigDecimal actualWorkCost) {
        this.actualWorkCost = actualWorkCost;
    }

    public String getCostMeasuringUnits() {
        return costMeasuringUnits;
    }

    public void setCostMeasuringUnits(String costMeasuringUnits) {
        this.costMeasuringUnits = costMeasuringUnits;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportingYear)) return false;
        ReportingYear that = (ReportingYear) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getYear(), that.getYear()) && Objects.equals(getAction(), that.getAction()) && Objects.equals(getPlannedWorkAmount(), that.getPlannedWorkAmount()) && Objects.equals(getActualWorkAmount(), that.getActualWorkAmount()) && Objects.equals(getWorkMeasuringUnits(), that.getWorkMeasuringUnits()) && Objects.equals(getPlannedWorkCost(), that.getPlannedWorkCost()) && Objects.equals(getActualWorkCost(), that.getActualWorkCost()) && Objects.equals(getCostMeasuringUnits(), that.getCostMeasuringUnits()) && Objects.equals(getCommentary(), that.getCommentary());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getYear(), getAction(), getPlannedWorkAmount(), getActualWorkAmount(), getWorkMeasuringUnits(), getPlannedWorkCost(), getActualWorkCost(), getCostMeasuringUnits(), getCommentary());
    }

    @Override
    public String toString() {
        return "ReportingYear{" +
                "id=" + id +
                ", year=" + year +
                ", action=" + action +
                ", plannedWorkAmount=" + plannedWorkAmount +
                ", actualWorkAmount=" + actualWorkAmount +
                ", workMeasuringUnits='" + workMeasuringUnits + '\'' +
                ", plannedWorkCost=" + plannedWorkCost +
                ", actualWorkCost=" + actualWorkCost +
                ", costMeasuringUnits='" + costMeasuringUnits + '\'' +
                ", commentary='" + commentary + '\'' +
                '}';
    }
}
