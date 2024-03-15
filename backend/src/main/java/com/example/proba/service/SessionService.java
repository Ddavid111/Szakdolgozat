package com.example.proba.service;

import com.example.proba.dao.SessionDao;
import com.example.proba.entity.Session;
import com.example.proba.entity.Theses;
import com.example.proba.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
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
            User memberWithEmail = userService.findUserById(member.getUserId());
            membersFromSession.set(membersFromSession.indexOf(member), memberWithEmail);

        }
        List<User> studentsFromSession = session.getStudents();
        for(User student: studentsFromSession)
        {
            User studentWithEmail = userService.findUserById(student.getUserId());
            studentsFromSession.set(studentsFromSession.indexOf(student), studentWithEmail);
        }
        User president = session.getPresident();
        president = userService.findUserById(president.getUserId());
        User notary = session.getNotary();
        notary = userService.findUserById(notary.getUserId());


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

            String emailToNotary = notary.getEmail();
            String emailSubjectToNotary = "Záróvizsga időpontja";
            String emailBodyToNotary = "Önt felkérték, hogy vegyen részt a következő záróvizsgán.(notary)";
            emailBodyToNotary += "\nA Záróvizsga helye: " + zvLocation;

            emailSenderService.sendEmail(emailToNotary, emailSubjectToNotary, emailBodyToNotary);





            String email = president.getEmail();
            String emailSubject = "Záróvizsga időpontja";
            String emailBody = "Önt felkérték, hogy vegyen részt a következő záróvizsgán.(president)";
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

            User notary = actualSession.getNotary();
            User tempNotary = new User();
            tempNotary.setUserId(notary.getUserId());
            tempNotary.setFullname(notary.getFullname());

            User president = actualSession.getPresident();
            User tempPresident = new User();
            tempPresident.setUserId(president.getUserId());
            tempPresident.setFullname(president.getFullname());

            actualSession.setPresident(tempPresident);
            actualSession.setNotary(tempNotary);

            List<User> students = actualSession.getStudents();
            List<User> tempStudents = new ArrayList<>();
            for(User student: students)
            {
               User tempStudent = new User();
               tempStudent.setUserId(student.getUserId());
               tempStudent.setFullname(student.getFullname());

               tempStudents.add(tempStudent);
            }

            actualSession.setStudents(tempStudents);


            List<User> members = actualSession.getMembers();
            List<User> tempMembers = new ArrayList<>();
            for(User member: members)
            {
                User tempMember = new User();
                tempMember.setUserId(member.getUserId());
                tempMember.setFullname(member.getFullname());

                tempMembers.add(tempMember);
            }

            actualSession.setMembers(tempMembers);


            session.add(actualSession);


        });

        return session;
    }


}
