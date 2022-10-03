package com.zerokikr.forestdb.service;

import com.zerokikr.forestdb.entity.Action;
import com.zerokikr.forestdb.entity.ReportingYear;
import com.zerokikr.forestdb.exception.ActionNotFoundException;
import com.zerokikr.forestdb.exception.ReportingYearNotFoundException;
import com.zerokikr.forestdb.exception.RiskNotFoundException;
import com.zerokikr.forestdb.repository.ActionRepository;
import com.zerokikr.forestdb.repository.ReportingYearRepository;
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

    public ReportingYear getReportingYearById(Long id) {
        return reportingYearRepository.findById(id).orElseThrow(() -> new ReportingYearNotFoundException(id));
    }

    public void saveReportingYear (ReportingYear reportingYear) {
        reportingYearRepository.save(reportingYear);
    }

    public void deleteReportingYearById (Long id) {
        reportingYearRepository.deleteById(id);
    }

    public String convertCommasToDots (String s) {
        return s.replace(',','.');
    }

    public String removeStars (String s) {
        return s.replaceAll("[*]+", "");
    }


}
