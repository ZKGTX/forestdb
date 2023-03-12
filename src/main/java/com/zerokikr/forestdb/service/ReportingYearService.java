package com.zerokikr.forestdb.service;

import com.zerokikr.forestdb.entity.Action;
import com.zerokikr.forestdb.entity.ReportingYear;
import com.zerokikr.forestdb.exception.ActionNotFoundException;
import com.zerokikr.forestdb.exception.ReportingYearNotFoundException;
import com.zerokikr.forestdb.exception.RiskNotFoundException;
import com.zerokikr.forestdb.repository.ActionRepository;
import com.zerokikr.forestdb.repository.ReportingYearRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class ReportingYearService {

    private ReportingYearRepository reportingYearRepository;

    public ReportingYearService(ReportingYearRepository reportingYearRepository) {
        this.reportingYearRepository = reportingYearRepository;
    }

    public List<ReportingYear> getAllReportingYearsByActionId (Long actionId) {
        return reportingYearRepository.findByActionIdOrderByYearAsc(actionId);
    }

    public List<ReportingYear> getAllReportingYearsByActionIdAndKeyword (Specification<ReportingYear> specs) {
        return reportingYearRepository.findAll(specs, Sort.by(Sort.Direction.ASC, "year"));
    }

    public ReportingYear getReportingYearById(Long id) {
        return reportingYearRepository.findById(id).orElseThrow(() -> new ReportingYearNotFoundException(id));
    }

    public ReportingYear getReportingYearByYear (Integer year) {
        return reportingYearRepository.findByYear(year);
    }


    public void saveReportingYear (ReportingYear reportingYear) {
        reportingYearRepository.save(reportingYear);
    }

    public void deleteReportingYearById (Long id) {
        reportingYearRepository.deleteById(id);
    }

    public String noComments(String comment) {
        String message = "";
        if (comment == null) {
            message = "фактический объем работ меньше планового. укажите в примечании причину выполнения мероприятия не в полном объёме";
        }
        return message;
    }

    public String noPlanComments (String comment) {
        String message = "";
        if (comment == null) {
            message = "для изменения планового показателя укажите основание (нормативно-правовой акт) в примечании";
        }
        return message;
    }

    public String workAmountsDiffer (String plannedWorkAmount, String actualWorkAmount, String comment) {
        String message = "";
        BigDecimal plannedAmount = cleanValue(plannedWorkAmount);
        BigDecimal actualAmount = cleanValue(actualWorkAmount);
        if (actualAmount.compareTo(plannedAmount) < 0) {
            message = noComments(comment);
        }
        return message;
    }

    public String workPlansChanged (String currentWorkAmount, String plannedWorkAmount, String comment) {
        String message = "";
        BigDecimal currentWork = cleanValue(currentWorkAmount);
        BigDecimal plannedWork = cleanValue(plannedWorkAmount);
        if (currentWork.compareTo(plannedWork) !=0 ) {
            message = noPlanComments(comment);
        }
        return message;
    }

    public String costPlansChanged (String currentWorkCost, String plannedWorkCost, String comment) {
        String message = "";
        BigDecimal currentCost = cleanValue(currentWorkCost);
        BigDecimal plannedCost = cleanValue(plannedWorkCost);
        if (currentCost.compareTo(plannedCost) !=0 ) {
            message = noPlanComments(comment);
        }
        return message;
    }

    public BigDecimal cleanValue (String rawValue) throws NumberFormatException {
        String cleanValue = convertCommasToDots(removeStars(rawValue));
        return new BigDecimal(cleanValue);
    }

    public String convertCommasToDots (String s) {
        return s.replaceAll("[,]+",".");
    }

    public String removeStars (String s) {
        return s.replaceAll("[*]+", "");
    }



}
