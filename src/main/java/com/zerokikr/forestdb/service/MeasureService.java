package com.zerokikr.forestdb.service;

import com.zerokikr.forestdb.entity.Measure;
import com.zerokikr.forestdb.exception.MeasureNotFoundException;
import com.zerokikr.forestdb.repository.MeasureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeasureService {

    private MeasureRepository measureRepository;

    public MeasureService(MeasureRepository measureRepository) {
        this.measureRepository = measureRepository;
    }

    public List<Measure> getAllMeasuresByRiskId (Long riskId) {
        return measureRepository.findByRiskIdOrderByNameAsc(riskId);
    }

    public Measure getMeasureById(Long id) {
        return measureRepository.findById(id).orElseThrow(() -> new MeasureNotFoundException(id));
    }

    public void saveMeasure (Measure measure) {
        measureRepository.save(measure);
    }

    public void deleteMeasureById (Long id) {
        measureRepository.deleteById(id);
    }


}
