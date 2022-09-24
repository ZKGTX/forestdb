package com.zerokikr.forestdb.service;

import com.zerokikr.forestdb.entity.Action;
import com.zerokikr.forestdb.exception.ActionNotFoundException;
import com.zerokikr.forestdb.repository.ActionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActionService {

    private ActionRepository actionRepository;

    public ActionService(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public List<Action> getAllActionsByMeasureId (Long measureId) {
        return actionRepository.findByMeasureIdOrderByNameAsc(measureId);
    }

    public Action getActionById(Long id) {
        return actionRepository.findById(id).orElseThrow(() -> new ActionNotFoundException(id));
    }

    public void saveAction (Action action) {
        actionRepository.save(action);
    }

    public void deleteActionById (Long id) {
        actionRepository.deleteById(id);
    }


}
