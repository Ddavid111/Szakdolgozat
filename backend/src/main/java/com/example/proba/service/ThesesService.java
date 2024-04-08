package com.example.proba.service;

import com.example.proba.dao.ThesisDao;
import com.example.proba.entity.Thesis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ThesesService {
    @Autowired
    private ThesisDao thesisDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Thesis addTheses(Thesis thesis) {

        thesis.setSubmissionDate(new Date());

        return thesisDao.save(thesis);
    }

    public Thesis updateTheseses(Thesis thesis) {
        Integer id = thesis.getId();
        Thesis temp = thesisDao.findById(id).get();
        temp.setTopicScore(thesis.getTopicScore());
        temp.setAnswer(thesis.getAnswer());
        temp.setSubmissionDate(thesis.getSubmissionDate());
        temp.setHasMscApply(thesis.getHasMscApply());
        temp.setLanguage(thesis.getLanguage());
        temp.setSpeciality(thesis.getSpeciality());
        temp.setDepartment(thesis.getDepartment());
        temp.setFaculty(thesis.getFaculty());
        temp.setTitle(thesis.getTitle());
        temp.setSupervisor(thesis.getSupervisor());
        temp.setUser(thesis.getUser());
        temp.setSessions(thesis.getSessions());
        temp.setTopics(thesis.getTopics());
        temp.setUserId(thesis.getUserId());
        temp.setSupervisorId(thesis.getSupervisorId());
        temp.setConsultantId(thesis.getConsultantId());
        temp.setConsultant(thesis.getConsultant());


        return thesisDao.save(thesis);

    }

    public void deleteThesesById(Integer id) {
        String query = "delete from thesis where id=" + id;
        jdbcTemplate.update(query);
    }

    public List<Thesis> getThesesList() {
        Iterable<Thesis> thesesIterable = thesisDao.findAll();
        List<Thesis> theses = new ArrayList<>();

        thesesIterable.forEach(theses::add);

        return theses;
    }

    public List<Thesis> getThesesListToDisplay() {
        Iterable<Thesis> thesesIterable = thesisDao.getThesesListToDisplay();
        List<Thesis> theses = new ArrayList<>();

        thesesIterable.forEach(theses::add);

        return theses;
    }

    public Thesis findThesesById(Integer id) {
        Optional<Thesis> theses = thesisDao.findById(id);

        return theses.get();
    }

    public List<Thesis> findThesesByUserId(Integer userId){

        Iterable<Thesis> thesesIterable = thesisDao.findThesesByUserId(userId);
        List<Thesis> theses = new ArrayList<>();

        thesesIterable.forEach(theses::add);

        return theses;

    }

}
