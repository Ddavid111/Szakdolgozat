package com.example.proba.service;

import com.example.proba.dao.ThesesDao;
import com.example.proba.entity.Theses;
import com.example.proba.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ThesesService {
    @Autowired
    private ThesesDao thesesDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Theses> getThesesList() {
        Iterable<Theses> thesesIterable = thesesDao.findAll();
        List<Theses> theses = new ArrayList<>();

        thesesIterable.forEach(theses::add);

        return theses;
    }

    public List<Theses> getThesesListToDisplay() {
        Iterable<Theses> thesesIterable = thesesDao.getThesesListToDisplay();
        List<Theses> theses = new ArrayList<>();

        thesesIterable.forEach(theses::add);

        return theses;
    }

    public Optional<Theses> findThesesById(Integer id) {
        Optional<Theses> theses = thesesDao.findById(id);

        return theses;
    }


    public Theses addTheses(Theses theses) {
        return thesesDao.save(theses);
    }


    public Theses updateTheseses(Theses theses) {
        Integer id = theses.getId();
        Theses temp = thesesDao.findById(id).get();
        temp.setFinalScore(theses.getFinalScore());
        temp.setSubjectScore(theses.getSubjectScore());
        temp.setDefenseScore(theses.getDefenseScore());
        temp.setTopicScore(theses.getTopicScore());
        temp.setAnswer(theses.getAnswer());
        temp.setSubmissionDate(theses.getSubmissionDate());
        temp.setSupplementId(theses.getSupplementId());
        temp.setThesisPdfId(theses.getThesisPdfId());
        temp.setHasMscApply(theses.getHasMscApply());
        temp.setLanguage(theses.getLanguage());
        temp.setSpeciality(theses.getSpeciality());
        temp.setDepartment(theses.getDepartment());
        temp.setFaculty(theses.getFaculty());
        temp.setTitle(theses.getTitle());
        temp.setSupervisor(theses.getSupervisor());
        temp.setUser(theses.getUser());
        temp.setSessions(theses.getSessions());
        temp.setTopics(theses.getTopics());
//temp.setTheseses(theses.getTheseses()); // commented out at: 2024.02.13. - why was it here?
        temp.setUserId(theses.getUserId());
        temp.setSupervisorId(theses.getSupervisorId());
        temp.setConsultantId(theses.getConsultantId());
        temp.setConsultant(theses.getConsultant());


        return thesesDao.save(theses);

    }


    public void deleteThesesById(Integer id) {
        String query = "delete from theses where id=" + id;
        jdbcTemplate.update(query);
    }

}
