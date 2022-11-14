package com.zerokikr.forestdb.service;

import com.zerokikr.forestdb.entity.Subject;
import com.zerokikr.forestdb.exception.SubjectNotFoundException;
import com.zerokikr.forestdb.repository.SubjectRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    private SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> getAllSubjects () {
        return subjectRepository.findAllByOrderByNameAsc();
    }

    public List<Subject> getSubjectsByKeyword (Specification <Subject> spec) {
        return subjectRepository.findAll(spec, Sort.by(Sort.Direction.ASC, "name"));
    }


    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id).orElseThrow(() -> new SubjectNotFoundException(id));
    }

    public void saveSubject (Subject subject) {
        subjectRepository.save(subject);
    }

    public void deleteSubjectById (Long id) {
        subjectRepository.deleteById(id);
    }




}
