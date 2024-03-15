package com.example.proba.service;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.proba.dao.FileDao;
import com.example.proba.dao.ReviewDao;
import com.example.proba.dao.ThesesDao;
import com.example.proba.dao.UserDao;
import com.example.proba.entity.*;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

import static java.lang.Math.round;

@Service
public class GenerateDocxService {
    @Autowired
    UserService userService;

    @Autowired
    SessionService sessionService;

    @Autowired
    ThesesService thesesService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    ReviewDao reviewDao;

    @Autowired
    UserDao userDao;

    @Autowired
    private ThesesDao thesesDao;

    @Autowired
    private FileDao fileDao;

    @Autowired
    EmailSenderService emailSenderService;




    //Zv-Reporthoz generálás
    public UUID Proba2(Object object) {

        DecimalFormat df = new DecimalFormat("0.00");
        df.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));

        try{

            String objectInString = object.toString();
            System.err.println(objectInString);

            // remove first character which is {
            StringBuilder stringBuilder = new StringBuilder(objectInString);
            stringBuilder.deleteCharAt(0);

            // remove last character which is }
            stringBuilder.deleteCharAt(objectInString.length()-2);

            // split the remaining string to get the student name, thesis title, description etc.
            // If you change the names (student, theses etc.) in the form at frontend, you'll need to change them here too.

            objectInString = stringBuilder.toString();
            String[] remaining = objectInString.split("student=");

            objectInString = remaining[1];

            remaining = objectInString.split(", theses=");
            String student = remaining[0];

            objectInString = remaining[1];
            remaining = objectInString.split(", session="); //dropdownból valamit áthozok akkor azt ide kell írni nem utolsónak mert meghal
            String thesis = remaining[0];

            objectInString = remaining[1];
            remaining = objectInString.split(", othermembers=");
            String session = remaining[0];

            objectInString = remaining[1];
            remaining = objectInString.split(", question=");
            String othermembers = remaining[0];

            objectInString = remaining[1];
            remaining = objectInString.split(", tgradeId=");
            String question = remaining[0];

            objectInString = remaining[1];
            remaining = objectInString.split(", gradeId=");
            String tgradeId = remaining[0];

            objectInString = remaining[1];
            remaining = objectInString.split(", description=");
            String gradeId = remaining[0];

            objectInString = remaining[1];
            remaining = objectInString.split(", userId=");
            String description = remaining[0];

            String userId = remaining[1];

            Integer gradeIdInt = Integer.parseInt(gradeId);

            Integer tgradeIdInt = Integer.parseInt(tgradeId);

            Integer studentToInt = Integer.parseInt(student);

            Integer userIdInt = Integer.parseInt(userId);

            Integer sessionIdInt = Integer.parseInt(session);

            Integer thesisIdInt = Integer.parseInt(thesis);


            System.out.println(student + " " + thesis + " " + userId + " " + session + " " + othermembers + " " + question + " " + description + " " + gradeId + " " + tgradeId);

            String gradeText;
            String mark = null;
            switch(gradeIdInt){
                case 1:
                    gradeText = "Elégtelen (1)";
                    mark = "1.00";
                    break;
                case 2:
                    gradeText = "Elégséges (2)";
                    mark = "2.00";
                    break;
                case 3:
                    gradeText = "Közepes (3)";
                    mark = "3.00";
                    break;
                case 4:
                    gradeText = "Jó (4)";
                    mark = "4.00";
                    break;
                case 5:
                    gradeText = "Jeles (5)";
                    mark = "5.00";
                    break;
                default:
                    gradeText = "Érvénytelen érték";
            }


            // A dropdownból kiválasztott szakdolgozatok adatai
            Theses theses = thesesService.findThesesById(thesisIdInt).get();

            List<Review> review = reviewService.findReviewByThesisId(thesisIdInt);

            System.out.println("Bíráló tömb:" + review);

            String thesisTitle = theses.getTitle();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date submissiondate = theses.getSubmissionDate();
            String formattedSubmissionDate = dateFormat.format(submissiondate);
            String department = theses.getDepartment();
            Topic topics = theses.getTopics();
            String topicName = topics.getTopic();
            //Integer topicGrade = topics.getGrade();
            Integer topicScore = theses.getTopicScore();
            String topicText;
            switch(topicScore) {
                case 1:
                    topicText = "Elégtelen (1)";
                    break;
                case 2:
                    topicText = "Elégséges (2)";
                    break;
                case 3:
                    topicText = "Közepes (3)";
                    break;
                case 4:
                    topicText = "Jó (4)";
                    break;
                case 5:
                    topicText = "Jeles (5)";
                    break;
                default:
                    topicText = "Érvénytelen érték";
            }

            // A dropdownból kiválasztott hallgató adatai
            User userr = userService.findUserById(studentToInt);

            String pedigreeNumber = userr.getPedigreeNumber();
            String studentName = userr.getFullname();
            String birthplace = userr.getBirthPlace();
            Date birthday = userr.getBirthday();
            String formattedBirthday = dateFormat.format(birthday);
            String mothersMaidenName = userr.getMothersMaidenName();

            // A dropdownból kiválasztott session adatai
            Session session1 = sessionService.findSessionById(sessionIdInt).get();

            User president = session1.getPresident();// elnök név alapján kikeresés

            String fullname = president.getFullname();
            String presidentTitle = president.getTitle();
            String presidentPost = president.getPost();
            String presidentWorkplace = president.getWorkplace();

            List<User> members = session1.getMembers();



            User notary = session1.getNotary();

            String notaryName = notary.getFullname();
            String notaryTitle = notary.getTitle();

            String code = session1.getCode();
            Date date = session1.getDate();
            String formattedDate = dateFormat.format(date);
            //List<String> students = session1.getStudents();
            /*List<String> members = session1.getMembers();
            User membersTomb = userService.findUserByName(members);
            String membersPost = membersTomb.getPost();*/




            //Záróvizsga jegy számítása
            Double topicScoretoDouble = topicScore.doubleValue();
            Double thesesScoretoDouble = tgradeIdInt.doubleValue();
            System.out.println(df.format(topicScoretoDouble));
            Double gradeIdtoDouble = gradeIdInt.doubleValue();
            df.format(gradeIdtoDouble);
            Double zvGrade = (0.4 * topicScoretoDouble) + (0.4 * thesesScoretoDouble) + (0.2 * gradeIdtoDouble);
            Integer zvGradeInt = (int) round(zvGrade);
            String zvQualification;
            switch(zvGradeInt){
                case 1:
                    zvQualification = "elégtelen";
                    break;
                case 2:
                    zvQualification = "elégséges";
                    break;
                case 3:
                    zvQualification = "közepes";
                    break;
                case 4:
                    zvQualification = "jó";
                    break;
                case 5:
                    zvQualification = "kiváló";
                    break;
                default:
                    zvQualification = "Érvénytelen érték";
            }

            // Aktuális user adatai
            //User user = userService.findUserById(userIdInt).get();
            //String biraloname = user.getName();
            //String workplace = userr.getWorkplace();
            //String pedigreeNumber = user.getPedigreeNumber();




            // Build and generate the docx file:
            XWPFDocument document = new XWPFDocument();


            //Bekezdések létrehozása





            //Címek
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            run.setFontFamily("Times New Roman");
            run.setFontSize(15);
            run.setBold(true);
            int numberOfTabs = 4;

            for (int i = 0; i < numberOfTabs; i++) {
                run.addTab();
            }
            run.setText("Oklevél száma:");
            run.addBreak();
            run.addBreak();
            run.addBreak();

            run = paragraph.createRun();
            run.setFontFamily("Times New Roman");
            run.setFontSize(20);
            run.setBold(true);
            run.setText("Záróvizsga-jegyzőkönyv");
            run.addBreak();



            XWPFParagraph paragraph2 = document.createParagraph();
            run = paragraph2.createRun();
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);
            run.setText("Felvétetett az " + code + " sz. Záróvizsga-Bizottság előtt, " + formattedDate + ". napján megtartott záróvizsgán.");



            //Hallgató neve
            run = paragraph2.createRun();
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);
            run.addBreak();
            run.addBreak();
            run.setText("Név: ");
            run.addTab();
            run.addTab();
            run.addTab();
            run.setText(studentName);
            run.addBreak();
            run.setText("Szül. hely, dátum: ");
            run.addTab();
            run.setText(birthplace + ", " + formattedBirthday);
            run.addBreak();
            run.setText("Anyja neve: ");
            run.addTab();
            run.addTab();
            run.setText(mothersMaidenName);
            run.addBreak();
            //run.setText("Felvett hallgatók: " + students);
            run = paragraph2.createRun();
            run.addBreak();
            run.setBold(true);
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);
            run.setText("Jelen vannak:");
            run.addBreak();
            run.addBreak();
            run = paragraph2.createRun();
            run.setSmallCaps(true);
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);
            run.setText("A Záróvizsga-Bizottság Elnöke:");
            run.addBreak();
            run.addBreak();
            run = paragraph2.createRun();
            run.setBold(true);
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);

            if(presidentTitle != null) {
                run.setText(presidentTitle + " ");
            }

            run.setText(fullname);
            run = paragraph2.createRun();
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);
            run.setText(", " + presidentPost);
            int numberOfTABB = 3;
            for (int i = 0; i < numberOfTABB; i++) {
                run.addTab();
            }
            run.setText(presidentWorkplace);
            run.addBreak();
            run.addBreak();
            run = paragraph2.createRun();
            run.setSmallCaps(true);
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);
            run.setText("A Záróvizsga-Bizottság Tagjai:");

            XWPFTable table666 = document.createTable(3, 2);
            setColumnWidth(table666, 0, 10);
            setColumnWidth(table666, 1, 6.61);

            for (int i=0; i< Math.min(members.size(), 3); i++) {
                User user = members.get(i);

                if (user != null) {
                    String title = user.getTitle();
                    String post = user.getPost();
                    String name = user.getFullname();
                    String workplace = user.getWorkplace();

                    String cell0Text;
                    if(title == null || title.isEmpty()) {
                        cell0Text = name + ", " + post;
                    }
                    else {
                        cell0Text = title + " " + name + ", " + post;
                    }

                    String cell1Text = workplace;

                    XWPFParagraph cell0Paragraph = table666.getRow(i).getCell(0).getParagraphs().get(0);
                    XWPFParagraph cell1Paragraph = table666.getRow(i).getCell(1).getParagraphs().get(0);


                    XWPFRun cell0Run = cell0Paragraph.createRun();
                    XWPFRun cell1Run = cell1Paragraph.createRun();

                    cell0Run.setFontSize(12);
                    cell1Run.setFontSize(12);

                    cell0Run.setFontFamily("Times New Roman");
                    cell1Run.setFontFamily("Times New Roman");

                    cell0Paragraph.setSpacingBefore(0);
                    cell0Paragraph.setSpacingAfter(0);
                    cell1Paragraph.setSpacingBefore(0);
                    cell1Paragraph.setSpacingAfter(0);

                    cell0Run.setText(cell0Text);
                    cell1Run.setText(cell1Text);
                }
            }


            XWPFParagraph paragraph3 = document.createParagraph();
            run = paragraph3.createRun();
            run.addBreak();
            run.setSmallCaps(true);
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);
            run.setText("A Záróvizsga-Bizottság külső tagja:");
            run = paragraph3.createRun();
            run.addBreak();
            run.setSmallCaps(false);
            run.setFontSize(12);
            run.setFontFamily("Times New Roman");
            if(othermembers.isEmpty())
            {
                run.setText("-");
            }
            else {
                run.setText(othermembers);
            }
            run.addBreak();
            run = paragraph3.createRun();

            XWPFTable table333 = document.createTable(4,2);
            setTableAlignment(table333, STJcTable.LEFT);

            setColumnWidth(table333, 0, 7.02);
            setColumnWidth(table333,1,8.99);

            XWPFParagraph paragraph333;
            if (!table333.getRow(0).getCell(0).getParagraphs().isEmpty()) {
                paragraph333 = table333.getRow(0).getCell(0).getParagraphs().get(0);
            } else {
                paragraph333 = table333.getRow(0).getCell(0).addParagraph();
            }
            paragraph333.setAlignment(ParagraphAlignment.LEFT);
            paragraph333.setSpacingBefore(0);
            paragraph333.setSpacingAfter(0);
            XWPFRun run333 = paragraph333.createRun();
            run333.setFontSize(12);
            run333.setFontFamily("Times New Roman");
            run333.setBold(true);
            run333.setText("A szakdolgozat címe: ");
            run333.setBold(false);

            XWPFParagraph paragraph334;
            if (!table333.getRow(0).getCell(1).getParagraphs().isEmpty()) {
                paragraph334 = table333.getRow(0).getCell(1).getParagraphs().get(0);
            } else {
                paragraph334 = table333.getRow(0).getCell(1).addParagraph();
            }
            paragraph334.setAlignment(ParagraphAlignment.LEFT);
            paragraph334.setSpacingBefore(0);
            paragraph334.setSpacingAfter(0);
            XWPFRun run334 = paragraph334.createRun();
            run334.setFontSize(12);
            run334.setFontFamily("Times New Roman");
            run334.setText( thesisTitle);

            XWPFParagraph paragraph335;
            if (!table333.getRow(1).getCell(0).getParagraphs().isEmpty()) {
                paragraph335 = table333.getRow(1).getCell(0).getParagraphs().get(0);
            } else {
                paragraph335 = table333.getRow(1).getCell(0).addParagraph();
            }
            paragraph335.setAlignment(ParagraphAlignment.LEFT);
            paragraph335.setSpacingBefore(0);
            paragraph335.setSpacingAfter(0);
            XWPFRun run335 = paragraph335.createRun();
            run335.setFontSize(12);
            run335.setFontFamily("Times New Roman");
            run335.setText("A beadás kelte: ");

            XWPFParagraph paragraph336;
            if (!table333.getRow(1).getCell(1).getParagraphs().isEmpty()) {
                paragraph336 = table333.getRow(1).getCell(1).getParagraphs().get(0);
            } else {
                paragraph336 = table333.getRow(1).getCell(1).addParagraph();
            }
            paragraph336.setAlignment(ParagraphAlignment.LEFT);
            paragraph336.setSpacingBefore(0);
            paragraph336.setSpacingAfter(0);
            XWPFRun run336 = paragraph336.createRun();
            run336.setFontSize(12);
            run336.setFontFamily("Times New Roman");
            run336.setText(formattedSubmissionDate);

            XWPFParagraph paragraph337;
            if (!table333.getRow(2).getCell(0).getParagraphs().isEmpty()) {
                paragraph337 = table333.getRow(2).getCell(0).getParagraphs().get(0);
            } else {
                paragraph337 = table333.getRow(2).getCell(0).addParagraph();
            }
            paragraph337.setAlignment(ParagraphAlignment.LEFT);
            paragraph337.setSpacingBefore(0);
            paragraph337.setSpacingAfter(0);
            XWPFRun run337 = paragraph337.createRun();
            run337.setFontSize(12);
            run337.setFontFamily("Times New Roman");
            run337.setText("A szakdolgozatot kiadó tanszék neve: ");

            XWPFParagraph paragraph338;
            if (!table333.getRow(2).getCell(1).getParagraphs().isEmpty()) {
                paragraph338 = table333.getRow(2).getCell(1).getParagraphs().get(0);
            } else {
                paragraph338 = table333.getRow(2).getCell(1).addParagraph();
            }
            paragraph338.setAlignment(ParagraphAlignment.LEFT);
            paragraph338.setSpacingBefore(0);
            paragraph338.setSpacingAfter(0);
            XWPFRun run338 = paragraph338.createRun();
            run338.setFontSize(12);
            run338.setFontFamily("Times New Roman");
            run338.setText(department);

            XWPFParagraph paragraph339;
            if (!table333.getRow(3).getCell(0).getParagraphs().isEmpty()) {
                paragraph339 = table333.getRow(3).getCell(0).getParagraphs().get(0);
            } else {
                paragraph339 = table333.getRow(3).getCell(0).addParagraph();
            }
            paragraph339.setAlignment(ParagraphAlignment.LEFT);
            paragraph339.setSpacingBefore(0);
            paragraph339.setSpacingAfter(0);
            XWPFRun run339 = paragraph339.createRun();
            run339.setFontSize(12);
            run339.setFontFamily("Times New Roman");
            run339.setText("A szakdolgozat bírálói: ");

            XWPFParagraph paragraph340;
            if (!table333.getRow(3).getCell(1).getParagraphs().isEmpty()) {
                paragraph340 = table333.getRow(3).getCell(1).getParagraphs().get(0);
            } else {
                paragraph340 = table333.getRow(3).getCell(1).addParagraph();
            }
            paragraph340.setAlignment(ParagraphAlignment.LEFT);
            paragraph340.setSpacingBefore(0);
            paragraph340.setSpacingAfter(0);
            XWPFRun run340 = paragraph340.createRun();
            run340.setFontSize(12);
            run340.setFontFamily("Times New Roman");
            for(Review review1: review)
            {
                User review1User = review1.getUser();
                String review1UserTitle = review1User.getTitle();
                String review1UserFullname = review1User.getFullname();
                System.out.println("Bíráló neve:" + review1UserTitle + " " + review1UserFullname + ", ");
                if(review1UserTitle == null)
                {
                    run340.setText(review1UserFullname + " ");
                }
                else {
                    run340.setText(review1UserTitle + " " + review1UserFullname + " ");
                }
            }

            XWPFParagraph paragraph4 = document.createParagraph();
            XWPFRun signatureLineRun = paragraph4.createRun();
            paragraph4.setAlignment(ParagraphAlignment.LEFT);
            signatureLineRun.setFontFamily("Times New Roman");
            signatureLineRun.setFontSize(12);
            signatureLineRun.setText("A bírálók által javasolt osztályzatok:");

            for(Review review1: review)
            {
                Integer score= review1.getScore();
                if(score == 1)
                {
                    signatureLineRun.setText(" elégtelen (1)");
                }
                else if(score == 2)
                {
                    signatureLineRun.setText(" elégséges (2)");
                }
                else if(score == 3)
                {
                    signatureLineRun.setText(" közepes (3)");
                }
                else if(score == 4)
                {
                    signatureLineRun.setText(" jó (4)");
                }
                else if(score == 5)
                {
                    signatureLineRun.setText(" jeles (5)");
                }
                System.out.println("Pontszám:" + score);
            }

            XWPFParagraph paragraph445 = document.createParagraph();
            XWPFRun signatureLineRun445 = paragraph445.createRun();
            paragraph445.setAlignment(ParagraphAlignment.RIGHT);
            signatureLineRun445.setFontFamily("Times New Roman");
            signatureLineRun445.setFontSize(12);
            for(int i = 0; i < 3; i++)
            {
                signatureLineRun445.addBreak();
            }
            signatureLineRun445.setText("Záróvizsga jegyző aláírása ");
            for (int i = 0; i < 30; i++) {
                signatureLineRun445.setText("_");
            }
            signatureLineRun445.addBreak();
            signatureLineRun445.setText("1/4 oldal.");

            XWPFParagraph paragraph5 = document.createParagraph();
            XWPFRun runn = paragraph5.createRun();
            paragraph5.setPageBreak(true);
            runn.setText("A szakdolgozattal kapcsolatban feltett kérdések, válaszok:");
            runn.setFontSize(12);
            runn.setFontFamily("Times New Roman");
            runn.addBreak();
            runn.addBreak();
            runn.setText("A feltett kérdésekre " + question + " válaszokat adott. ");


            XWPFTable table = document.createTable();
            //create first row
            XWPFTableRow tableRowOne = table.getRow(0);
            XWPFTableCell cell = tableRowOne.getCell(0);
            XWPFParagraph paragraph22 = cell.getParagraphs().get(0);
            XWPFRun run22 = paragraph22.createRun();
            run22.setFontSize(12);
            run22.setFontFamily("Times New Roman");
            run22.setText("A ZVB által a szakdolgozatra megállapított osztályzat (SZD): ");
            if(tgradeIdInt == 1)
            {
                run22.setText("elégtelen (1)");
            }
            else if(tgradeIdInt == 2)
            {
                run22.setText("elégséges (2)");
            }
            else if(tgradeIdInt == 3)
            {
                run22.setText("közepes (3)");
            }
            else if(tgradeIdInt == 4)
            {
                run22.setText("jó (4)");
            }
            else if(tgradeIdInt == 5)
            {
                run22.setText("jeles (5)");
            }
            setColumnWidthVisible(table, 0, 17.25);
            paragraph22.setSpacingAfter(0);

            XWPFParagraph paragraph6 = document.createParagraph();
            XWPFRun runnn = paragraph6.createRun();
            runnn.addBreak();
            runnn = paragraph6.createRun();
            runnn.setFontFamily("Times New Roman");
            runnn.setFontSize(12);
            runnn.setText("A ZVB-nak a fentiekkel kapcsolatos esetleges észrevételei: ");
            if(description.isEmpty())
            {
                runnn.setText("-");
            }
            else {
                runnn.setText(description);
            }
            runnn.addBreak();
            runnn.addBreak();
            runnn.addBreak();
            runnn.setText("Záróvizsga tárgyak:");

            XWPFTable table2 = document.createTable();
            XWPFTableRow tableRowONE = table2.getRow(0);
            XWPFParagraph paragraph789 = tableRowONE.getCell(0).getParagraphs().get(0);
            XWPFRun run789 = paragraph789.createRun();
            run789.setFontFamily("Times New Roman");
            run789.setFontSize(12);
            run789.setText("Programtervezési ismeretek,");
            run789.addBreak();
            run789.setText("Programozás alapjai,");
            run789.addBreak();
            run789.setText("Objektum orientált programozás,");
            run789.addBreak();
            run789.setText("Szoftvertechnológia,");
            run789.addBreak();
            run789.setText("Számítógépi grafika,");
            run789.addBreak();
            run789.setText("Számítógép architektúrák,");
            run789.addBreak();
            run789.setText("Operációs rendszerek,");
            run789.addBreak();
            run789.setText("Számítógép hálózatok,");
            run789.addBreak();
            run789.setText("Adatbázis rendszerek I.,");
            run789.addBreak();
            run789.setText("Párhuzamos algoritmusok");


            XWPFTableCell cell2 = tableRowONE.addNewTableCell();
            XWPFParagraph paragraph790;
            if (!cell2.getParagraphs().isEmpty()) {
                paragraph790 = cell2.getParagraphs().get(0);
            } else {
                paragraph790 = cell2.addParagraph();
            }
            XWPFRun run790 = paragraph790.createRun();
            run790.setFontSize(12);
            run790.setFontFamily("Times New Roman");
            run790.setText("Feltett kérdések, ");
            run790.addBreak();
            run790.setText("válaszok (a kérdező ");
            run790.addBreak();
            run790.setText("neve, a feltett ");
            run790.addBreak();
            run790.setText("kérdések és válaszok)");

            XWPFTableCell cell3 = tableRowONE.addNewTableCell();
            XWPFParagraph paragraph791;
            if (!cell3.getParagraphs().isEmpty()) {
                paragraph791 = cell3.getParagraphs().get(0);
            } else {
                paragraph791 = cell3.addParagraph();
            }
            XWPFRun run791 = paragraph791.createRun();
            run791.setFontSize(12);
            run791.setFontFamily("Times New Roman");
            run791.setText("A feleletre adott osztályzat ");
            run791.addBreak();
            run791.setText("(számmal és betűvel) ");
            run791.addBreak();
            run791.setText("____" + topicText + "_______");

            XWPFTableRow tableRowTwo = table2.createRow();
            tableRowTwo.getCell(0).setText("");

            XWPFTableRow tableRowThree = table2.createRow();
            tableRowThree.getCell(0).setText("");

            XWPFTableRow tableRowFour = table2.createRow();
            XWPFTableCell cell4 = tableRowFour.getCell(0);
            XWPFParagraph paragraph792;
            if (!cell4.getParagraphs().isEmpty()) {
                paragraph792 = cell4.getParagraphs().get(0);
            } else {
                paragraph792 = cell4.addParagraph();
            }
            XWPFRun run792 = paragraph792.createRun();
            run792.setFontFamily("Times New Roman");
            run792.setFontSize(12);
            run792.setText("Kérdés: " + topicName);

            XWPFTableRow tableRowFive = table2.createRow();
            XWPFTableCell cell5 = tableRowFive.getCell(0);
            XWPFParagraph paragraph793;
            if (!cell5.getParagraphs().isEmpty()) {
                paragraph793 = cell5.getParagraphs().get(0);
            } else {
                paragraph793 = cell5.addParagraph();
            }
            XWPFRun run793 = paragraph793.createRun();
            run793.setFontSize(12);
            run793.setFontFamily("Times New Roman");
            run793.setItalic(true);
            run793.setText("Válaszok: ");
            run793 = paragraph793.createRun();
            run793.setFontSize(12);
            run793.setFontFamily("Times New Roman");
            run793.setText("A feltett kérdésekre " + question + " válaszokat adott.");


            mergeCellHorizontally(table2, 1, 0, 2);
            mergeCellHorizontally(table2, 2, 0, 2);
            mergeCellHorizontally(table2, 3, 0, 2);
            mergeCellHorizontally(table2, 4, 0, 2);


            for (int columnIndex = 0; columnIndex < table2.getRow(0).getTableCells().size(); columnIndex++) {
                setColumnWidth(table2, columnIndex, 6.62);
                setColumnWidth(table2, columnIndex, 3.6);
                setColumnWidth(table2, columnIndex, 5.42);
            }


            XWPFParagraph paragraph7 = document.createParagraph();
            XWPFRun signatureLineRun2 = paragraph7.createRun();
            signatureLineRun2.setFontFamily("Times New Roman");
            signatureLineRun2.setFontSize(12);
            for(int i = 0; i < 8; i++)
            {
                signatureLineRun2.addBreak();
            }
            paragraph7.setAlignment(ParagraphAlignment.RIGHT);
            signatureLineRun2.setText("Záróvizsga jegyző aláírása ");
            for (int i = 0; i < 30; i++) {
                signatureLineRun2.setText("_");
            }
            signatureLineRun2.addBreak();
            signatureLineRun2.setText("2/4 oldal.");


            XWPFParagraph paragraph10 = document.createParagraph();
            XWPFRun newPageRun = paragraph10.createRun();
            paragraph10.setAlignment(ParagraphAlignment.CENTER);
            paragraph10.setPageBreak(true);


            XWPFTable table3 = document.createTable(2, 4);

            mergeCellHorizontally(table3, 0, 0, 1);
            setColumnWidth(table3, 0, 2.00);
            setColumnWidth(table3, 1, 5.00);
            setColumnWidth(table3, 2, 3.62);
            setColumnWidth(table3, 3, 5.78);



            XWPFTableCell cell00 = table3.getRow(0).getCell(0);
            XWPFParagraph paragraph00;
            if (!cell00.getParagraphs().isEmpty()) {
                paragraph00 = cell00.getParagraphs().get(0);
            } else {
                paragraph00 = cell00.addParagraph();
            }
            XWPFRun run00 = paragraph00.createRun();
            run00.setFontFamily("Times New Roman");
            run00.setFontSize(12);
            run00.setText("A tanulmányi évek alatt teljesített");
            run00.addBreak();
            run00.setText("kötelező szigorlatok");
            paragraph00.setAlignment(ParagraphAlignment.CENTER);
            paragraph00.setSpacingBefore(0);
            paragraph00.setSpacingAfter(0);
            cell00.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

            XWPFTableCell cell02 = table3.getRow(0).getCell(1);
            XWPFParagraph paragraph02 = cell02.addParagraph();
            XWPFRun run02 = paragraph02.createRun();
            run02.setFontSize(12);
            run02.setFontFamily("Times New Roman");
            run02.setText("");
            paragraph02.setAlignment(ParagraphAlignment.CENTER);
            paragraph02.setSpacingBefore(0);
            paragraph02.setSpacingAfter(0);
            cell02.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

            XWPFTableCell cell03 = table3.getRow(0).getCell(2);
            XWPFParagraph paragraph03 = cell03.addParagraph();
            XWPFRun run03 = paragraph03.createRun();
            paragraph03.setAlignment(ParagraphAlignment.CENTER);
            paragraph03.setSpacingBefore(0);
            paragraph03.setSpacingAfter(0);
            cell03.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

            XWPFTableCell cell069 = table3.getRow(0).getCell(3);
            XWPFParagraph paragraph69;
            if (!cell069.getParagraphs().isEmpty()) {
                paragraph69 = cell069.getParagraphs().get(0);
            } else {
                paragraph69 = cell069.addParagraph();
            }
            XWPFRun run08 = paragraph69.createRun();
            run08.setFontFamily("Times New Roman");
            run08.setFontSize(12);
            run08.setText("A szigorlat osztályzata ");
            run08.addBreak();
            run08.setText("számmal és betűvel");
            paragraph69.setAlignment(ParagraphAlignment.CENTER);
            paragraph69.setSpacingBefore(0);
            paragraph69.setSpacingAfter(0);
            cell069.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

            XWPFTableCell cell20 = table3.getRow(1).getCell(0);
            XWPFParagraph paragraph200;
            if (!cell20.getParagraphs().isEmpty()) {
                paragraph200 = cell20.getParagraphs().get(0);
            } else {
                paragraph200 = cell20.addParagraph();
            }
            XWPFRun run200 = paragraph200.createRun();
            run200.setFontSize(12);
            run200.setFontFamily("Times New Roman");
            run200.setText("1. Tárgy: ");
            paragraph200.setAlignment(ParagraphAlignment.LEFT);
            paragraph200.setSpacingBefore(0);
            paragraph200.setSpacingAfter(0);
            cell20.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

            XWPFTableCell cell21 = table3.getRow(1).getCell(1);
            XWPFParagraph paragraph210;
            if (!cell21.getParagraphs().isEmpty()) {
                paragraph210 = cell21.getParagraphs().get(0);
            } else {
                paragraph210 = cell21.addParagraph();
            }
            XWPFRun run210 = paragraph210.createRun();
            run210.setFontFamily("Times New Roman");
            run210.setFontSize(12);
            run210.setText("Programtervezés");
            paragraph210.setAlignment(ParagraphAlignment.LEFT);
            paragraph210.setSpacingBefore(0);
            paragraph210.setSpacingAfter(0);
            cell21.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

            XWPFTableCell cell22 = table3.getRow(1).getCell(2);
            XWPFParagraph paragraph220 = cell22.addParagraph();
            XWPFRun run220 = paragraph220.createRun();
            run220.setFontSize(12);
            run220.setFontFamily("Times New Roman");
            run220.setText("");
            paragraph220.setAlignment(ParagraphAlignment.LEFT);
            paragraph220.setSpacingBefore(0);
            paragraph220.setSpacingAfter(0);
            cell22.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

            XWPFTableCell cell23 = table3.getRow(1).getCell(3);
            XWPFParagraph paragraph230;
            if (!cell23.getParagraphs().isEmpty()) {
                paragraph230 = cell23.getParagraphs().get(0);
            } else {
                paragraph230 = cell23.addParagraph();
            }
            XWPFRun run230 = paragraph230.createRun();
            run230.setFontFamily("Times New Roman");
            run230.setFontSize(12);
            run230.setText("         " + gradeText);
            paragraph230.setAlignment(ParagraphAlignment.LEFT);
            paragraph230.setSpacingBefore(0);
            paragraph230.setSpacingAfter(0);
            cell23.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);





            XWPFParagraph paragraph11 = document.createParagraph();
            XWPFRun runnning = paragraph11.createRun();
            runnning.setFontFamily("Times New Roman");
            runnning.setFontSize(12);
            runnning.addBreak();
            runnning.addBreak();
            runnning.setText("A szigorlatok átlaga: ");
            int numberoftabs = 7;
            for(int i = 0; i < numberoftabs; i++)
            {
                runnning.addTab();
            }
            runnning.setText("    " + mark);
            //runnning.setText("haha: " + topicGrade);
            runnning.addBreak();
            runnning.addBreak();
            runnning.addBreak();
            runnning.setText("1. A záróvizsgatárgy érdemjegye (ZT):");
            int numberofTaBs = 6;
            for(int i = 0; i < numberofTaBs; i++) {
                runnning.addTab();
            }
            runnning.setText(" " + df.format(topicScoretoDouble));
            runnning.addBreak();
            runnning.addBreak();
            runnning.setText("2. A szakdolgozat osztályzata (SZD):");
            for(int i = 0; i < numberofTaBs; i++) {
                runnning.addTab();
            }
            runnning.setText(" " + df.format(thesesScoretoDouble));
            runnning.addBreak();
            runnning.addBreak();
            runnning.setText("3. A szigorlat átlaga (SZ):");
            numberofTaBs = 8;
            for(int i = 0; i < numberofTaBs; i++) {
                runnning.addTab();
            }
            runnning.setText(" " + mark);
            runnning.addBreak();
            runnning.addBreak();
            runnning.setText("A záróvizsga eredménye:");
            for(int i = 0; i < numberofTaBs; i++) {
                runnning.addTab();
            }
            runnning.setText(" " + df.format(zvGrade));
            runnning.addBreak();
            runnning.setText("(0.4*ZT+0.4*SZD+0.2*SZ)");
            runnning.addBreak();
            runnning.addBreak();
            runnning.setText("4. Az oklevél minősítése: " + zvQualification);

            XWPFParagraph paragraph13 = document.createParagraph();
            XWPFRun signatureLineRun3 = paragraph13.createRun();
            signatureLineRun3.setFontSize(12);
            signatureLineRun3.setFontFamily("Times New Roman");
            for(int i=0; i< 14; i++)
            {
                signatureLineRun3.addBreak();
            }
            paragraph13.setAlignment(ParagraphAlignment.RIGHT);
            signatureLineRun3.setText("Záróvizsga jegyző aláírása ");
            for (int i = 0; i < 30; i++) {
                signatureLineRun3.setText("_");
            }
            signatureLineRun3.addBreak();
            signatureLineRun3.setText("3/4 oldal.");

            XWPFParagraph paragraph14 = document.createParagraph();
            XWPFRun run1 = paragraph14.createRun();
            run1.setFontFamily("Times New Roman");
            run1.setFontSize(12);
            paragraph14.setPageBreak(true);
            run1.addBreak();
            run1.setText("Megállapítjuk, hogy " + studentName + " hallgató");
            run1.addBreak();
            run1.addBreak();
            run1.setText("Programtervező Informatikus BSc. szakon");
            run1.addBreak();
            run1.addBreak();
            run1.setText(zvQualification + " minősítéssel oklevelet szerzett.");
            run1.addBreak();
            run1.addBreak();
            run1.setText("Megjegyzés, külön vélemény:");
            run1.addBreak();
            run1.setText("(Itt kell többek között feltüntetni a hallgató jogosultságát a kitüntetéses oklevélre)");
            run1.addBreak();
            run1.addBreak();
            run1.setText("Az egyéb feltételek teljesülése esetén a záróvizsga eredménye alapján javasoljuk a");
            run1.addBreak();
            run1.setText("kitüntetéses diploma odaítélését.");
            run1.addBreak();
            run1.addBreak();
            run1.setText("A Záróvizsga-jegyzőkönyv csak a nyelvvizsgára vonatkozó azonos sorszámú melléklettel együtt érvényes!");
            run1.addBreak();
            run1.addBreak();

            XWPFTable table555 = document.createTable(2, 3);
            setTableAlignment(table555, STJcTable.CENTER);

            setColumnWidth(table555, 0, 5.0);
            setColumnWidth(table555, 1, 6.5);
            setColumnWidth(table555, 2, 5.0);

            for (int i = 0; i < 3; i++) {
                XWPFParagraph row0CellParagraph;
                if (!table555.getRow(0).getCell(i).getParagraphs().isEmpty()) {
                    row0CellParagraph = table555.getRow(0).getCell(i).getParagraphs().get(0);
                } else {
                    row0CellParagraph = table555.getRow(0).getCell(i).addParagraph();
                }

                row0CellParagraph.setAlignment(ParagraphAlignment.CENTER);
                row0CellParagraph.setSpacingBefore(0);
                row0CellParagraph.setSpacingAfter(0);
                XWPFRun row0CellRun = row0CellParagraph.createRun();
                row0CellRun.setFontSize(12);
                row0CellRun.setFontFamily("Times New Roman");
                for (int j = 0; j < 23; j++) {
                    row0CellRun.setText("_");
                }
            }

            for (int i=0; i< Math.min(members.size(), 3); i++) {
                User member = members.get(i);


                String title = member.getTitle();
                String name = member.getFullname();

                XWPFParagraph row0Cell1Paragraph = table555.getRow(0).getCell(i).addParagraph();
                row0Cell1Paragraph.setAlignment(ParagraphAlignment.CENTER);
                row0Cell1Paragraph.setSpacingBefore(0);
                row0Cell1Paragraph.setSpacingAfter(0);
                XWPFRun row0Cell1Run = row0Cell1Paragraph.createRun();
                row0Cell1Run.setFontFamily("Times New Roman");
                row0Cell1Run.setFontSize(12);

                if(title == null) {
                    row0Cell1Run.setText(name);
                } else {
                    row0Cell1Run.setText(title + " " + name);
                }

            }

            for (int i = 0; i < 3; i++) {
                XWPFParagraph row2CellParagraph;
                if (!table555.getRow(1).getCell(i).getParagraphs().isEmpty()) {
                    row2CellParagraph = table555.getRow(1).getCell(i).getParagraphs().get(0);
                } else {
                    row2CellParagraph = table555.getRow(1).getCell(i).addParagraph();
                }

                row2CellParagraph.setAlignment(ParagraphAlignment.CENTER);
                row2CellParagraph.setSpacingBefore(0);
                row2CellParagraph.setSpacingAfter(0);
                XWPFRun row2CellRun = row2CellParagraph.createRun();
                row2CellRun.setFontSize(12);
                row2CellRun.setFontFamily("Times New Roman");
                row2CellRun.setText("Záróvizsga - Bizottság tagja");
            }


            /*mergeCellVertically(table555, 0, 0, 1);
            mergeCellVertically(table555, 1, 0, 1);
            mergeCellVertically(table555, 2, 0, 1);*/




            XWPFParagraph paragraph16 = document.createParagraph();
            paragraph16.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun signatureLineRun5 = paragraph16.createRun();
            signatureLineRun5.setFontFamily("Times New Roman");
            signatureLineRun5.setFontSize(12);
            signatureLineRun5.addBreak();
            signatureLineRun5.addBreak();
            signatureLineRun5.addBreak();
            for (int i = 0; i < 23; i++) {
                signatureLineRun5.setText("_");
            }
            signatureLineRun5.addBreak();

            if(presidentTitle == null) {
                signatureLineRun5.setText(fullname);
            } else {
                signatureLineRun5.setText(presidentTitle + " " + fullname);
            }
            signatureLineRun5.addBreak();
            signatureLineRun5.setText("Záróvizsga - Bizottság elnöke");
            signatureLineRun5.addBreak();
            signatureLineRun5.addBreak();



            XWPFTable table444 = document.createTable(3, 1);
            setTableAlignment(table444, STJcTable.RIGHT);

            setColumnWidth(table444,0,5);

            XWPFParagraph paragraph17;
            if (!table444.getRow(0).getCell(0).getParagraphs().isEmpty()) {
                paragraph17 = table444.getRow(0).getCell(0).getParagraphs().get(0);
            } else {
                paragraph17 = table444.getRow(0).getCell(0).addParagraph();
            }
            paragraph17.setAlignment(ParagraphAlignment.CENTER);
            paragraph17.setSpacingBefore(0);
            paragraph17.setSpacingAfter(0);
            XWPFRun run17 = paragraph17.createRun();
            run17.setFontSize(12);
            run17.setFontFamily("Times New Roman");
            for (int i = 0; i < 23; i++) {
                run17.setText("_");
            }

            XWPFParagraph paragraph18;
            if (!table444.getRow(1).getCell(0).getParagraphs().isEmpty()) {
                paragraph18 = table444.getRow(1).getCell(0).getParagraphs().get(0);
            } else {
                paragraph18 = table444.getRow(1).getCell(0).addParagraph();
            }
            paragraph18.setAlignment(ParagraphAlignment.CENTER);
            paragraph18.setSpacingBefore(0);
            paragraph18.setSpacingAfter(0);
            XWPFRun run18 = paragraph18.createRun();
            run18.setFontFamily("Times New Roman");
            run18.setFontSize(12);

            if(notaryTitle != null) {
                run18.setText(notaryTitle + " " + notaryName);
            } else {
                run18.setText(notaryName);
            }



            XWPFParagraph paragraph19;
            if (!table444.getRow(2).getCell(0).getParagraphs().isEmpty()) {
                paragraph19 = table444.getRow(2).getCell(0).getParagraphs().get(0);
            } else {
                paragraph19 = table444.getRow(2).getCell(0).addParagraph();
            }
            paragraph19.setAlignment(ParagraphAlignment.CENTER);
            paragraph19.setSpacingBefore(0);
            paragraph19.setSpacingAfter(0);
            XWPFRun run19 = paragraph19.createRun();
            run19.setFontSize(12);
            run19.setFontFamily("Times New Roman");
            run19.setText("jegyzőkönyvvezető");

            XWPFParagraph paragraph20 = document.createParagraph();
            XWPFRun run69 = paragraph20.createRun();
            run69.addBreak();
            run69.addBreak();
            run69.addBreak();
            run69.setFontSize(12);
            run69.setFontFamily("Times New Roman");
            run69.setText(pedigreeNumber + "/FI87515/GI törzskönyvi számú Záróvizsga-jegyzőkönyv 4, " +
                    "azaz négy számozott oldalból áll.");
            run69.addBreak();

            XWPFParagraph paragraph21 = document.createParagraph();
            XWPFRun run70 = paragraph21.createRun();
            run70.setFontFamily("Times New Roman");
            run70.setFontSize(12);
            paragraph21.setAlignment(ParagraphAlignment.RIGHT);
            run70.setText("4/4 oldal.");



            //Fejléc
            CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
            XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(document, sectPr);

            XWPFHeader header = headerFooterPolicy.createHeader(XWPFHeaderFooterPolicy.DEFAULT);
            XWPFParagraph paragraph12 = header.createParagraph();
            paragraph12 = header.getParagraphArray(0);
            paragraph12.setAlignment(ParagraphAlignment.LEFT);

            CTTabStop tabStop = paragraph12.getCTP().getPPr().addNewTabs().addNewTab();
            int twipsPerInch = 1440;
            tabStop.setVal(STTabJc.RIGHT);
            tabStop.setPos(BigInteger.valueOf(6 * twipsPerInch));

            run = paragraph12.createRun();
            run.setFontFamily("Times New Roman");
            run.setFontSize(10);
            run.setText("Gépészmérnöki és Informatikai Kar");
            run.addTab();
            run.setText("Törzskönyvi szám:");
            run.addBreak();
            run.setText("Programtervező Informatikus BSc. Szak");
            run.addTab();
            run.setText(pedigreeNumber + "/FI87515/GI");
            run.addBreak();
            run.setText("nappali tagozat");

            // create footer start
            XWPFFooter footer = headerFooterPolicy.createFooter(XWPFHeaderFooterPolicy.DEFAULT);

            paragraph12 = footer.createParagraph();
            paragraph12.setAlignment(ParagraphAlignment.CENTER);

            run = paragraph12.createRun();
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);
            run.setText(pedigreeNumber + "/FI87515/GI Törzskönyvi számú");
            run.addBreak();
            run.setText("Záróvizsga-jegyzőkönyv");

            UUID uuid = UUID.randomUUID();

            String filename = "zv" + File.separator + "Záróvizsga_ " + studentName + "_" + uuid + ".docx";

            FileOutputStream out = new FileOutputStream(filename);
            document.write(out);
            out.close();
            System.out.println("Word created succesfull");
            com.example.proba.entity.File file = new com.example.proba.entity.File();

            file.setName(filename);
            file.setUploadTime(new Date());

            file.setUuid(uuid.toString());
            file.setThesis(theses);

            fileDao.save(file);

            System.out.println("File saved succesfull");


            String email = userr.getEmail();
            String emailSubject = "Záróvizsga jegyzőkönyv";
            String emailBody = "Kedves Hallgató!";
            emailBody += "\nMegérkezett a záróvizsga jegyzőkönyve amit a mellékletben talál.";
            emailBody += "\n";
            emailBody += "\nÜdvözlettel: Adminisztrátor";
            emailSenderService.sendEmailWithAttachment(email, emailSubject, emailBody, filename);

            return uuid;

        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    private static void setColumnWidth(XWPFTable table, int columnIndex, double widthInCentimeters) {
        // Convert centimeters to twips
        int widthInTwips = (int) (widthInCentimeters * 567);

        for (XWPFTableRow row : table.getRows()) {
            XWPFTableCell cell = row.getCell(columnIndex);
            if (cell != null) {
                CTTcPr cellProperties = cell.getCTTc().isSetTcPr() ? cell.getCTTc().getTcPr() : cell.getCTTc().addNewTcPr();
                cellProperties.addNewTcW().setW(BigInteger.valueOf(widthInTwips));

                CTTcBorders borders = cellProperties.isSetTcBorders() ? cellProperties.getTcBorders() : cellProperties.addNewTcBorders();

                borders.addNewTop().setVal(STBorder.NIL);
                borders.addNewBottom().setVal(STBorder.NIL);
                borders.addNewLeft().setVal(STBorder.NIL);
                borders.addNewRight().setVal(STBorder.NIL);
            }
        }
    }

    private static void setColumnWidthVisible(XWPFTable table, int columnIndex, double widthInCentimeters) {
        // Convert centimeters to twips
        int widthInTwips = (int) (widthInCentimeters * 567);

        for (XWPFTableRow row : table.getRows()) {
            XWPFTableCell cell = row.getCell(columnIndex);
            if (cell != null) {
                CTTcPr cellProperties = cell.getCTTc().isSetTcPr() ? cell.getCTTc().getTcPr() : cell.getCTTc().addNewTcPr();
                cellProperties.addNewTcW().setW(BigInteger.valueOf(widthInTwips));


            }
        }
    }
    private static void addEmptyCells(XWPFTableRow row, int count) {
        for (int i = 0; i < count; i++) {
            row.getCell(i);
        }
    }


    // A mergeCellVertically függvény implementációja
    private static void mergeCellVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            CTVMerge vmerge = CTVMerge.Factory.newInstance();
            if (rowIndex == fromRow) {
                // Az első mergelt cella RESTART értéket kap
                vmerge.setVal(STMerge.RESTART);
            } else {
                // A második mergelt cella és utána lévők CONTINUE értéket kapnak
                vmerge.setVal(STMerge.CONTINUE);
            }
            CTTcPr tcPr = cell.getCTTc().getTcPr();
            if (tcPr == null) tcPr = cell.getCTTc().addNewTcPr();
            tcPr.setVMerge(vmerge);
        }
    }

    private static void mergeCellHorizontally(XWPFTable table, int row, int colStart, int colEnd) {
        XWPFTableCell firstCell = table.getRow(row).getCell(colStart);
        for (int colIndex = colStart + 1; colIndex <= colEnd; colIndex++) {
            XWPFTableCell cell = table.getRow(row).getCell(colIndex);
            CTHMerge hmerge = CTHMerge.Factory.newInstance();
            hmerge.setVal(STMerge.CONTINUE);
            CTTcPr tcPr = cell.getCTTc().getTcPr();
            if (tcPr == null) tcPr = cell.getCTTc().addNewTcPr();
            tcPr.setHMerge(hmerge);
        }

        CTHMerge hmerge = CTHMerge.Factory.newInstance();
        hmerge.setVal(STMerge.RESTART);
        CTTcPr tcPr = firstCell.getCTTc().getTcPr();
        if (tcPr == null) tcPr = firstCell.getCTTc().addNewTcPr();
        tcPr.setHMerge(hmerge);
    }


    public void setTableAlignment(XWPFTable table, STJcTable.Enum justification) {
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        CTJcTable jc =  (tblPr.isSetJc() ?  tblPr.getJc() :  tblPr.addNewJc());
        jc.setVal(justification);
    }


}
