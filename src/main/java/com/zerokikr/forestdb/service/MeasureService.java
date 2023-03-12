package com.zerokikr.forestdb.service;

import com.zerokikr.forestdb.entity.Measure;
import com.zerokikr.forestdb.entity.Risk;
import com.zerokikr.forestdb.exception.MeasureNotFoundException;
import com.zerokikr.forestdb.repository.MeasureRepository;
import com.zerokikr.forestdb.repository.RiskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeasureService {

    private MeasureRepository measureRepository;

    @Autowired
    private RiskRepository riskRepository;

    public MeasureService(MeasureRepository measureRepository) {
        this.measureRepository = measureRepository;
    }

    public List<Measure> getAllMeasuresByRiskId (Long riskId) {
        return measureRepository.findByRiskIdOrderByNameAsc(riskId);
    }

    public List<Measure> getMeasuresByRiskIdAndKeyword (Specification<Measure> specs) {
        return measureRepository.findAll(specs, Sort.by(Sort.Direction.ASC, "name"));
    }

    public Measure getMeasureById(Long id) {
        return measureRepository.findById(id).orElseThrow(() -> new MeasureNotFoundException(id));
    }

    public List<Measure> sortMeasuresAccordingToForestPlan (Risk risk, List<Measure> measures) {
        List<Measure> sortedMeasures = new ArrayList<>();
        if (risk.getName().equals("Изменение продуктивности лесов в связи с изменениями средних значений температуры и количества выпадаемых осадков")) {
            sortedMeasures.add(measures.get(1));
            sortedMeasures.add(measures.get(2));
            sortedMeasures.add(measures.get(3));
            sortedMeasures.add(measures.get(0));
        }
        if (risk.getName().equals("Изменения в видовом (породном) составе лесов")) {
            sortedMeasures.add(measures.get(2));
            sortedMeasures.add(measures.get(1));
            sortedMeasures.add(measures.get(3));
            sortedMeasures.add(measures.get(0));
        }
        if (risk.getName().equals("Увеличение частоты возникновения (лесных) пожаров в лесах и площадей, пройденных пожарами")) {
            sortedMeasures.add(measures.get(1));
            sortedMeasures.add(measures.get(0));
        }
        if (risk.getName().equals("Увеличение частоты вспышек массового размножения вредных организмов в лесах")) {
            sortedMeasures.add(measures.get(1));
            sortedMeasures.add(measures.get(0));
        }
        if (risk.getName().equals("Увеличение частоты проявления последствий экстремальных погодных явлений в лесах")) {
            sortedMeasures.add(measures.get(0));
            sortedMeasures.add(measures.get(1));
            sortedMeasures.add(measures.get(2));
        }
        return sortedMeasures;
    }

    public void saveMeasure (Measure measure) {
        measureRepository.save(measure);
    }

    public void deleteMeasureById (Long id) {
        measureRepository.deleteById(id);
    }


}
