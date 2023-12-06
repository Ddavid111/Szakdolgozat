package com.example.proba.service;

import java.io.FileOutputStream;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;

@Service
public class GenerateDocxService {
    public void Proba(Object object) {
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
            remaining = objectInString.split(", gradeId=");
            String description = remaining[0];

            objectInString = remaining[1];
            remaining = objectInString.split(", city=");
            String gradeId = remaining[0];

            String city = remaining[1];

            System.out.println(studentName + " " + thesisTitle + " " + description + " " + gradeId + " " + city);

            // Build and generate the docx file:

            XWPFDocument document = new XWPFDocument();
            FileOutputStream out = new FileOutputStream("proba.docx");
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText(thesisTitle);
          //  run.setText("Szakdolgozat review");
            run.setBold(true);
            paragraph.setAlignment(ParagraphAlignment.CENTER);

        /*    File image = new File("D://tigris.jpg"); // temp removed!
            FileInputStream imageData = new FileInputStream(image);
            int imageType = XWPFDocument.PICTURE_TYPE_JPEG;
            String imageFileName = image.getName();
            int width = 450;
            int height = 400;
            run.addPicture(imageData, imageType, imageFileName,
                    Units.toEMU(width),
                    Units.toEMU(height));*/

            //create table
            XWPFTable table = document.createTable();
            //create first row
            XWPFTableRow tableRowOne = table.getRow(0);
            tableRowOne.getCell(0).setText("col one, row one");
            tableRowOne.addNewTableCell().setText("col two, row one");
            tableRowOne.addNewTableCell().setText("col three, row one");
            //create second row
            XWPFTableRow tableRowTwo = table.createRow();
            tableRowTwo.getCell(0).setText("col one, row two");
            tableRowTwo.getCell(1).setText("col two, row two");
            tableRowTwo.getCell(2).setText("col three, row two");
            //create third row
            XWPFTableRow tableRowThree = table.createRow();
            tableRowThree.getCell(0).setText("col one, row three");
            tableRowThree.getCell(1).setText("col two, row three");
            tableRowThree.getCell(2).setText("col three, row three");
            XWPFRun paragraphOneRunThree = paragraph.createRun();
            // paragraphOneRunThree.setStrike(true);
            paragraphOneRunThree.setFontSize(30);
            paragraphOneRunThree.setSubscript(VerticalAlign.SUBSCRIPT);

            paragraphOneRunThree.setText(description);

         //   paragraphOneRunThree.setText(" Font Styles");
            document.write(out);
            out.close();
            System.out.println("Word created succesfull");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
