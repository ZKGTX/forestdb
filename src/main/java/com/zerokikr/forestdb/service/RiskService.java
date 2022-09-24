package com.zerokikr.forestdb.service;

import com.zerokikr.forestdb.entity.Risk;
import com.zerokikr.forestdb.exception.RiskNotFoundException;
import com.zerokikr.forestdb.repository.RiskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiskService {

    private RiskRepository riskRepository;

    public RiskService(RiskRepository riskRepository) {
        this.riskRepository = riskRepository;
    }

    public List<Risk> getAllRisksBySubjectId (Long subjectId) {
        return riskRepository.findBySubjectIdOrderByNameAsc(subjectId);
    }

    public Risk getRiskById(Long id) {
        return riskRepository.findById(id).orElseThrow(() -> new RiskNotFoundException(id));
    }

    public void saveRisk (Risk risk) {
        riskRepository.save(risk);
    }

    public void deleteRiskById (Long id) {
        riskRepository.deleteById(id);
    }


}
