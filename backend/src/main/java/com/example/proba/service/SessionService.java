package com.example.proba.service;

import com.example.proba.dao.SessionDao;
import com.example.proba.entity.Session;
import com.example.proba.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

        if (sessionFromSql.isPresent()) {
            try {
                for (User student : studentsFromSession) {
                    BufferedReader reader = new BufferedReader(new FileReader("email/zv_create.txt"));
                    StringBuilder emailBodyBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.contains("[Név]")) {
                            line = line.replace("[Név]", student.getFullname());
                        }

                        if (line.contains("[Dátum]")) {
                            line = line.replace("[Dátum]", formattedDate);
                        }

                        if (line.contains("[Helyszín]")) {
                            line = line.replace("[Helyszín]", zvLocation);
                        }

                        if (line.contains("[Kezdési időpont]")) {
                            line = line.replace("[Kezdési időpont]", startHour.toString());
                        }

                        if (line.contains("[Befejezési időpont]")) {
                            line = line.replace("[Befejezési időpont]", endHour.toString());
                        }

                        emailBodyBuilder.append(line).append("\n");
                    }
                    reader.close();
                    String emailBody = emailBodyBuilder.toString();

                    String email = student.getEmail();
                    String emailSubject = "Záróvizsga időpontja";

                    emailSenderService.sendEmail(email, emailSubject, emailBody);
                }

                for (User member : membersFromSession) {

                    BufferedReader reader = new BufferedReader(new FileReader("email/zv_create.txt"));
                    StringBuilder emailBodyBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {

                        if (line.contains("[Név]")) {
                            line = line.replace("[Név]", member.getFullname());
                        }

                        if (line.contains("[Dátum]")) {
                            line = line.replace("[Dátum]", formattedDate);
                        }
                        // Helyszín helyettesítése
                        if (line.contains("[Helyszín]")) {
                            line = line.replace("[Helyszín]", zvLocation);
                        }
                        // Kezdési időpont helyettesítése
                        if (line.contains("[Kezdési időpont]")) {
                            line = line.replace("[Kezdési időpont]", startHour.toString());
                        }
                        // Befejezési időpont helyettesítése
                        if (line.contains("[Befejezési időpont]")) {
                            line = line.replace("[Befejezési időpont]", endHour.toString());
                        }

                        emailBodyBuilder.append(line).append("\n");
                    }
                    reader.close();
                    String emailBody = emailBodyBuilder.toString();

                    String email = member.getEmail();
                    String emailSubject = "Záróvizsga időpontja";

                    emailSenderService.sendEmail(email, emailSubject, emailBody);
                }

                // Jegyző
                BufferedReader reader = new BufferedReader(new FileReader("email/zv_create.txt"));
                StringBuilder emailBodyBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {

                    if (line.contains("[Név]")) {
                        line = line.replace("[Név]", secretary.getFullname());
                    }

                    if (line.contains("[Dátum]")) {
                        line = line.replace("[Dátum]", formattedDate);
                    }

                    if (line.contains("[Helyszín]")) {
                        line = line.replace("[Helyszín]", zvLocation);
                    }

                    if (line.contains("[Kezdési időpont]")) {
                        line = line.replace("[Kezdési időpont]", startHour.toString());
                    }

                    if (line.contains("[Befejezési időpont]")) {
                        line = line.replace("[Befejezési időpont]", endHour.toString());
                    }


                    emailBodyBuilder.append(line).append("\n");
                }
                reader.close();
                String emailBodySecretary = emailBodyBuilder.toString();

                String emailToSecretary = secretary.getEmail();
                String emailSubjectToSecretary = "Záróvizsga időpontja";

                emailSenderService.sendEmail(emailToSecretary, emailSubjectToSecretary, emailBodySecretary);

                // Elnök
                reader = new BufferedReader(new FileReader("email/zv_create.txt"));
                emailBodyBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    // Az [Tag neve] helyére beillesztjük az elnök nevét
                    if (line.contains("[Név]")) {
                        line = line.replace("[Név]", chairman.getFullname());
                    }

                    if (line.contains("[Dátum]")) {
                        line = line.replace("[Dátum]", formattedDate);
                    }

                    if (line.contains("[Helyszín]")) {
                        line = line.replace("[Helyszín]", zvLocation);
                    }

                    if (line.contains("[Kezdési időpont]")) {
                        line = line.replace("[Kezdési időpont]", startHour.toString());
                    }

                    if (line.contains("[Befejezési időpont]")) {
                        line = line.replace("[Befejezési időpont]", endHour.toString());
                    }

                    emailBodyBuilder.append(line).append("\n");
                }
                reader.close();
                String emailBodyChairman = emailBodyBuilder.toString();

                String email = chairman.getEmail();
                String emailSubject = "Záróvizsga időpontja";

                emailSenderService.sendEmail(email, emailSubject, emailBodyChairman);


            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
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
