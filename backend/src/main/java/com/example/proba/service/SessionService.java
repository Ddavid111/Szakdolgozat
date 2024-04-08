package com.example.proba.service;

import com.example.proba.dao.SessionDao;
import com.example.proba.entity.Session;
import com.example.proba.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class SessionService {

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    SessionService sessionService;

    @Autowired
    UserService userService;

    @Autowired
    EmailSenderService emailSenderService;


    public Optional<Session> findSessionById (Integer id) {
        Optional<Session> session = sessionDao.findById(id);

        return session;
    }


    public Session addSessions(Session session)
    {
        Session savedSession = sessionDao.save(session);

        Integer sessionId = session.getId();
        Optional<Session> sessionFromSql = sessionService.findSessionById(sessionId);
        List<User> membersFromSession = session.getMembers();
        for(User member : membersFromSession){
            User memberWithEmail = userService.findUserById(member.getId());
            membersFromSession.set(membersFromSession.indexOf(member), memberWithEmail);

        }
        List<User> studentsFromSession = session.getStudents();
        for(User student: studentsFromSession)
        {
            User studentWithEmail = userService.findUserById(student.getId());
            studentsFromSession.set(studentsFromSession.indexOf(student), studentWithEmail);
        }
        User chairman = session.getChairman();
        chairman = userService.findUserById(chairman.getId());
        User secretary = session.getSecretary();
        secretary = userService.findUserById(secretary.getId());


        String zvLocation = session.getLocation();
        Date zvDate = session.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // A kívánt dátum formátum
        String formattedDate = sdf.format(zvDate);
        Integer startHour = session.getStartHour();
        Integer endHour = session.getEndHour();

        if(sessionFromSql.isPresent()){
            for (User member: membersFromSession) {

                String email = member.getEmail();

                String emailSubject = "Záróvizsga időpontja";
                String emailBody = "Önt felkérték, hogy vegyen részt a következő záróvizsgán. (Member)";
                emailBody += "\nA Záróvizsga helye: " + zvLocation;

                emailSenderService.sendEmail(email, emailSubject, emailBody);
            }

            for (User student: studentsFromSession) {

                String email = student.getEmail();
                String studentName = student.getFullname();

                String emailSubject = "Záróvizsga időpontja";
                String emailBody = "Kedves " + studentName + "!";
                emailBody += "\n";
                emailBody += "\nÖrömmel értesítünk arról, hogy Önt felkérték, hogy vegyen részt a következő záróvizsgán.";
                emailBody += "\n";
                emailBody += "\nIdőpont: " + formattedDate;
                emailBody += "\n";
                emailBody += "\nHelyszín: " + zvLocation;
                emailBody += "\nA záróvizsga kezdete: " + startHour;
                emailBody += "\nA záróvizsga vége: " + endHour;


                emailSenderService.sendEmail(email, emailSubject, emailBody);
            }

            String emailToSecretary = secretary.getEmail();
            String emailSubjectToSecretary = "Záróvizsga időpontja";
            String emailBodyToSecretary = "Önt felkérték, hogy vegyen részt a következő záróvizsgán.(secretary)";
            emailBodyToSecretary += "\nA Záróvizsga helye: " + zvLocation;

            emailSenderService.sendEmail(emailToSecretary, emailSubjectToSecretary, emailBodyToSecretary);





            String email = chairman.getEmail();
            String emailSubject = "Záróvizsga időpontja";
            String emailBody = "Önt felkérték, hogy vegyen részt a következő záróvizsgán.(chairman)";
            emailBody += "\nA Záróvizsga helye: " + zvLocation;

            emailSenderService.sendEmail(email, emailSubject, emailBody);

        }

        else {
            try {
                throw new Exception("Reviewer user or the thesis to be reviewed was not found in the DB!");
            } catch (Exception e) {
                e.printStackTrace();

            }

        }
        return savedSession;
    }

    public List<Session> getSessionList() {
        Iterable<Session> sessionIterable = sessionDao.findAll();
        List<Session> sessions = new ArrayList<>();

        sessionIterable.forEach(sessions::add);

        return sessions;
    }

    public List<Session> getSessionsListToDisplay() {
        Iterable<Session> sessionIterable = sessionDao.getSessionsListToDisplay();

        List<Session> session = new ArrayList<>();

        sessionIterable.forEach((actualSession) ->
        {

            User secretary = actualSession.getSecretary();
            User tempSecretary = new User();
            tempSecretary.setId(secretary.getId());
            tempSecretary.setFullname(secretary.getFullname());

            User president = actualSession.getChairman();
            User tempPresident = new User();
            tempPresident.setId(president.getId());
            tempPresident.setFullname(president.getFullname());

            actualSession.setChairman(tempPresident);
            actualSession.setSecretary(tempSecretary);

            List<User> students = actualSession.getStudents();
            List<User> tempStudents = new ArrayList<>();
            for(User student: students)
            {
               User tempStudent = new User();
               tempStudent.setId(student.getId());
               tempStudent.setFullname(student.getFullname());

               tempStudents.add(tempStudent);
            }

            actualSession.setStudents(tempStudents);


            List<User> members = actualSession.getMembers();
            List<User> tempMembers = new ArrayList<>();
            for(User member: members)
            {
                User tempMember = new User();
                tempMember.setId(member.getId());
                tempMember.setFullname(member.getFullname());

                tempMembers.add(tempMember);
            }

            actualSession.setMembers(tempMembers);


            session.add(actualSession);


        });

        return session;
    }


}
