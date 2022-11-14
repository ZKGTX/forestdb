package com.zerokikr.forestdb.entity;


import net.bytebuddy.implementation.bind.annotation.Default;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;

@Entity
@Table(name = "reporting_years")
public class ReportingYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "year")
    @NotNull(message = "необходимо указать год")
    @Digits(integer = 4, fraction = 0, message="только 4 цифры")
    @Min(value = 2000, message = "некорректный год. только 4 цифры")
    private Integer year;

    @ManyToOne
    @JoinColumn(name = "action_id")
    private Action action;

    @Column(name = "true_work_plan")
    private String trueWorkPlan;

    @Column(name = "true_cost_plan")
    private String trueCostPlan;

    @Column(name = "planned_work_amount")
    @NotNull(message = "необходимо указать запланированный объем работ")
    @Pattern(regexp="[0-9,.*]+", message = "допустимые символы: цифры (0-9), разделители (.) (,), примечания (*)")
    private String plannedWorkAmount;

    @Column(name = "actual_work_amount")
    @NotNull(message = "необходимо указать выполненный объем работ")
    @Pattern(regexp="[0-9,.*]+", message = "допустимые символы: цифры (0-9), разделители (.) (,), примечания (*)")
    private String actualWorkAmount;

    @Column(name = "work_measuring_units")
    @NotNull(message = "необходимо указать единицу измерения работ")
    @Pattern(regexp="[а-яёА-ЯЁ ,.()-]+", message = "только русские буквы")
    private String workMeasuringUnits;

    @Column(name = "planned_work_cost")
    @NotNull(message = "необходимо указать запланированную стоимость работ")
    @Pattern(regexp="[0-9,.*]+", message = "допустимые символы: цифры (0-9), разделители (.) (,), примечания (*)")
    private String plannedWorkCost;


    @Column(name = "actual_work_cost")
    @NotNull(message = "необходимо указать фактическую стоимость работ")
    @Pattern(regexp="[0-9,.*]+", message = "допустимые символы: цифры (0-9), разделители (.) (,), примечания (*)")
    private String actualWorkCost;

    @Column(name = "cost_measuring_units")
    @NotNull(message = "необходимо указать единицу измерения стоимости")
    private String costMeasuringUnits;

    @Column(name = "commentary")
    private String commentary;

    public ReportingYear() {
    }

    public ReportingYear(Integer year, Action action, String plannedWorkAmount, String actualWorkAmount, String plannedWorkCost, String actualWorkCost) {
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

    public String getTrueWorkPlan() {
        return trueWorkPlan;
    }

    public void setTrueWorkPlan(String trueWorkPlan) {
        this.trueWorkPlan = trueWorkPlan;
    }

    public String getTrueCostPlan() {
        return trueCostPlan;
    }

    public void setTrueCostPlan(String trueCostPlan) {
        this.trueCostPlan = trueCostPlan;
    }

    public String getPlannedWorkAmount() {
        return plannedWorkAmount;
    }

    public void setPlannedWorkAmount(String plannedWorkAmount) {
        this.plannedWorkAmount = plannedWorkAmount;
    }

    public String getActualWorkAmount() {
        return actualWorkAmount;
    }

    public void setActualWorkAmount(String actualWorkAmount) {
        this.actualWorkAmount = actualWorkAmount;
    }

    public String getWorkMeasuringUnits() {
        return workMeasuringUnits;
    }

    public void setWorkMeasuringUnits(String workMeasuringUnits) {
        this.workMeasuringUnits = workMeasuringUnits;
    }

    public String getPlannedWorkCost() {
        return plannedWorkCost;
    }

    public void setPlannedWorkCost(String plannedWorkCost) {
        this.plannedWorkCost = plannedWorkCost;
    }

    public String getActualWorkCost() {
        return actualWorkCost;
    }

    public void setActualWorkCost(String actualWorkCost) {
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
