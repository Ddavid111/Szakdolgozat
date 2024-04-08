package com.example.proba.service;

import com.example.proba.dao.FileDao;
import com.example.proba.dao.ReviewDao;
import com.example.proba.dao.ThesisDao;
import com.example.proba.dao.UserDao;
import com.example.proba.entity.Review;
import com.example.proba.entity.Role;
import com.example.proba.entity.Thesis;
import com.example.proba.entity.User;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class ReviewService {
    @Autowired
    private ReviewDao reviewDao;

    @Autowired
    private ThesisDao thesisDao;

    @Autowired
    private FileDao fileDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    public Review addReview(Review review) throws Exception {

        Optional<User> user = userDao.findById(review.getUser().getId());
        Optional<Thesis> thesis = thesisDao.findById(review.getTheseses().getId()); //.iterator().next().getId());
        if(user.isPresent() && thesis.isPresent()) {
            review.setUser(user.get());
            Thesis thesis_ = thesis.get();
            System.out.println(thesis_);

            thesis_.decreaseRemainingReviews();
            if(thesis_.getReviewsRemaining() < 1)
            {
                thesis_.setUnderReview(false);
            }
            System.out.println(thesis_);
            System.out.println(thesisDao.save(thesis_));
            return reviewDao.save(review);
        }

        throw new Exception("User was not found.");
    }

    public List<Review> getReviewData() {
        List<Review> reviews = new ArrayList<>();

        Iterable<Review> reviewIterable = reviewDao.findAll();
        reviewIterable.forEach(reviews::add);

        return reviews;
    }

    public List<Thesis> findThesesByUserIdAndReviewerId(Integer userId, Integer reviewerId) {
        List<Thesis> theses = new ArrayList<>();


        if (userDao.findById(reviewerId).get().getRole().equals(Role.Bíráló))
        {
            Iterable<Review> reviewIterable = reviewDao.findThesesByUserIdAndReviewerId(userId, reviewerId);
        for (Review review : reviewIterable) {
            if (review.getScore() == null) {
                theses.add(review.getTheseses());
            }
        }
        }
        else if (userDao.findById(reviewerId).get().getRole().equals(Role.Témavezető))
        {
            Iterable<Thesis> thesesIterable = thesisDao.findThesesBySupervisorId(reviewerId);
            thesesIterable.forEach(theses::add);
        }

        return theses;
    }


    public List<Review> findReviewByThesisId(Integer thesisId) {
        return reviewDao.findReviewByThesisId(thesisId);
    }

    public UUID Proba(Object object) {
        try {
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
            String studentName = remaining[0];

            objectInString = remaining[1];
            remaining = objectInString.split(", description=");
            String thesisTitle = remaining[0];

            objectInString = remaining[1];
            remaining = objectInString.split(", score=");
            String description = remaining[0];

            objectInString = remaining[1];
            remaining = objectInString.split(", city=");
            String score = remaining[0];


            objectInString = remaining[1];
            remaining = objectInString.split(", Id=");
            String city = remaining[0];

            System.out.println(score + "\t");

            String userId = remaining[1];

            Integer userIdInt = Integer.parseInt(userId);



            // THESES ID megy már a thesisTitle-be fentebb az angular form-ból
            Thesis thesis = thesisDao.findById(Integer.parseInt(thesisTitle)).get();
//            review.setTheseses(theses);

            // Review table:
            User user_ = userService.findUserById(userIdInt);
            Review review = new Review();
            if(user_.getRole().equals(Role.Témavezető)) {
                review.setInvitationDate(new Date());
                review.setUser(user_);

            }
            else {
                List <Review> reviewList = reviewDao.findReviewsByThesisIdAndReviewerId(Integer.parseInt(thesisTitle), userIdInt);
                if(reviewList.size() > 1)
                {
                    for(Review tempReview : reviewList) {
                        if(tempReview.getScore() == null)
                        {
                            review = tempReview;
                        }
                    }
                }
                else{
                    review = reviewDao.findReviewByThesisIdAndReviewerId(Integer.parseInt(thesisTitle), userIdInt);
                }
            }
                review.setScore(Integer.parseInt(score));
                review.setDescription(description);
                review.setCity(city);
                review.setTheseses(thesis);

            thesis.decreaseRemainingReviews();
            if(thesis.getReviewsRemaining() < 1)
            {
                thesis.setUnderReview(false);
            }

            thesisDao.save(thesis);

            thesisTitle = thesis.getTitle();


            review.setSubmissionDate(new Date());

            System.out.println(studentName + " " + thesisTitle + " " + description + " " + city + " " + userId);


            User student = userService.findUserById(Integer.parseInt(studentName));
            studentName = student.getFullname();

            User user = userService.findUserById(userIdInt);
            String biraloname = user.getFullname();
            String workplace = user.getWorkplace();
            String post = user.getPosition();

            review.setUser(user);
            reviewDao.save(review);

            // Build and generate the docx file:

            XWPFDocument document = new XWPFDocument();

            //Szakdolgozat címe
            XWPFParagraph paragraph = document.createParagraph();
            XWPFParagraph paragraph2 = document.createParagraph();
            XWPFParagraph paragraph3 = document.createParagraph();
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run = paragraph.createRun();
            run.setFontFamily("Times New Roman");
            run.setFontSize(20);
            run.setBold(true);
            run.setText("Szakdolgozat bírálat");

            //Hallgató neve
            run = paragraph2.createRun();
            paragraph2.setAlignment(ParagraphAlignment.LEFT);
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);
            run.addBreak();
            run.setText("Jelölt neve: " + studentName);
            run.addBreak();

            //Szakdolgozat címe
            run.setText("Szakdolgozat címe: " + thesisTitle);
            run.addBreak();
            run.addBreak();

            //Bíráló neve
            XWPFParagraph paragraph23 = document.createParagraph();
            XWPFRun run23 = paragraph23.createRun();;
            run23 = paragraph2.createRun();
            paragraph23.setAlignment(ParagraphAlignment.LEFT);
            run23.setFontFamily("Times New Roman");
            run23.setFontSize(12);
            run23.setText("Bíráló neve: " + biraloname);
            run23.addBreak();

            //Bíráló munkahelye
            paragraph23.setAlignment(ParagraphAlignment.LEFT);
            run23.setFontFamily("Times New Roman");
            run23.setFontSize(12);
            run23.setText("Munkahelye, beosztása: " + workplace + ", " + post);
            run23.addBreak();

            //Tényleges bírálat --> MySQL-ben módosítani kell a Review tábla description mezőjét LONGTEXT-re VARCHAR(255)-ről, mert exception lesz hosszú bírálatnál.
            run = paragraph2.createRun();
            run.setFontSize(12);
            run.setFontFamily("Times New Roman");
            paragraph2.setAlignment(ParagraphAlignment.LEFT);
            run.addBreak();
            //test.replaceAll("\n", "\r\n");
            //System.out.println(run.text());
            List<Character> s = new ArrayList<>();
            int lengthOfDescriptionField = description.toCharArray().length;
            for(char c : description.toCharArray()) {
                s.add(c);
                if(c == '\n' || (lengthOfDescriptionField) == s.size()){
                    StringBuilder sb = new StringBuilder();
                    for(char c1 : s) {
                        sb.append(c1);
                    }
                    run.setText(sb.toString());
                    run.addBreak();
                    lengthOfDescriptionField -= s.size();
                    s.clear();
                }
            }


            //Érdemjegy
            run = paragraph2.createRun();
            paragraph2.setAlignment(ParagraphAlignment.LEFT);
            run.addBreak();
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);
            run.setBold(true);
            run.setText("Javasolt érdemjegy: ");
            run = paragraph2.createRun();
            run.setFontFamily("Times New Roman");
            run.setFontSize(12);
            Integer scoreToInt = Integer.parseInt(score);
            if(scoreToInt == 1)
            {
                run.setText("Elégtelen (1)");
            }
            else if(scoreToInt == 2)
            {
                run.setText("Elégséges (2)");
            }
            else if(scoreToInt == 3)
            {
                run.setText("Közepes (3)");
            }
            else if(scoreToInt == 4)
            {
                run.setText("Jó (4)");
            }
            else if(scoreToInt == 5)
            {
                run.setText("Jeles (5)");
            }
            run.addBreak();

            //Bíráló aláírása
            run = paragraph3.createRun();
            paragraph3.setAlignment(ParagraphAlignment.RIGHT);
            run.addBreak();
            run.setFontSize(12);
            run.setFontFamily("Times New Roman");
            run.setText("Bíráló aláírása");

            //Város
            XWPFParagraph paragraph25 = document.createParagraph();
            XWPFRun run25 = paragraph25.createRun();
            run25 = paragraph25.createRun();
            run25.setFontSize(12);
            run25.setFontFamily("Times New Roman");
            paragraph25.setAlignment(ParagraphAlignment.LEFT);
            run25.setText(city + ", ");

            // Időpont
            LocalDate aktDatum = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            String formatumosDatum = aktDatum.format(formatter);
            run25 = paragraph25.createRun();
            run25.setFontSize(12);
            run25.setFontFamily("Times New Roman");
            paragraph25.setAlignment(ParagraphAlignment.LEFT);
            run25.setText(formatumosDatum);



            // create header start
            CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
            XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(document, sectPr);

            XWPFHeader header = headerFooterPolicy.createHeader(XWPFHeaderFooterPolicy.DEFAULT);
            XWPFTable tableHeader = header.createTable(1,2);
            setColumnWidth(tableHeader,0,7.5);
            setColumnWidth(tableHeader,1,9.11);
            XWPFParagraph paragraphHeader;
            paragraphHeader = tableHeader.getRow(0).getCell(0).getParagraphs().get(0);
            XWPFRun runHeader = paragraphHeader.createRun();
            runHeader.setFontSize(12);
            runHeader.setFontFamily("Times New Roman");
            runHeader.setText("Miskolci Egyetem");
            runHeader.addBreak();
            runHeader.setText("Alkalmazott Matematikai Intézeti Tanszék");

            XWPFParagraph paragraphHeader2;
            paragraphHeader2 = tableHeader.getRow(0).getCell(1).getParagraphs().get(0);
            paragraphHeader2.setAlignment(ParagraphAlignment.RIGHT);
            XWPFRun runHeader2 = paragraphHeader2.createRun();

            String imgFile="egyetem.png";
            XWPFPicture picture = runHeader2.addPicture(new FileInputStream(imgFile), XWPFDocument.PICTURE_TYPE_PNG, imgFile, Units.toEMU(100), Units.toEMU(100));


            String blipID = "";
            for(XWPFPictureData picturedata : header.getAllPackagePictures()) {
                blipID = header.getRelationId(picturedata);

            }
            picture.getCTPicture().getBlipFill().getBlip().setEmbed(blipID); //now they have a blipID also


            aktDatum = LocalDate.now();
            formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd");
            formatumosDatum = aktDatum.format(formatter);

            UUID uuid = UUID.randomUUID();

            String filename = "reviews" + File.separator + "Review_ " + formatumosDatum +  "_" + studentName + "_" + uuid + ".docx";

            FileOutputStream out = new FileOutputStream(filename);
            document.write(out);
            out.close();
            System.out.println("Word created succesfull");
            com.example.proba.entity.File file = new com.example.proba.entity.File();

            file.setName(filename);
            file.setUploadTime(new Date());

            file.setUuid(uuid.toString());
            file.setThesis(thesis);

            fileDao.save(file);

            System.out.println("File saved succesfull");

            return uuid;


        } catch (Exception e) {
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

}