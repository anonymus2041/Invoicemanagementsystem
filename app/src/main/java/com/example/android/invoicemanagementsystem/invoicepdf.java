package com.example.android.invoicemanagementsystem;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class invoicepdf extends AppCompatActivity {

        // Creating a PdfDocument object

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String dest = "D:/itextExamples/addInvoice.pdf";
        File file = new File(dest);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN,
                12, Font.BOLD, new BaseColor(0, 0, 0));
        Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN,
                12);
        Document document = new Document();
        try {
            PdfWriter.getInstance(document,
                    new FileOutputStream(file.getAbsoluteFile()));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        document.open();
        //using add method in document to insert a paragraph
        try {
            document.add(new Paragraph("My First Pdf !"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        try {
            document.add(new Paragraph("Hello World"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        // close document
        document.close();
    }




//        PdfWriter writer = new PdfWriter(dest);
//
//        // Creating a PdfDocument object
//        PdfDocument pdf = new PdfDocument(writer);
//
//        // Creating a Document object
//        Document doc = new Document(pdf);
//
//        // Creating a table
//        float [] pointColumnWidths = {150F, 150F, 150F};
//        Table table = new Table(pointColumnWidths);
//
//        // Adding cells to the table
//        table.addCell(new Cell().add("Name"));
//        table.addCell(new Cell().add("Raju"));
//        table.addCell(new Cell().add("Id"));
//        table.addCell(new Cell().add("1001"));
//        table.addCell(new Cell().add("Designation"));
//        table.addCell(new Cell().add("Programmer"));
//
//        // Adding Table to document
//        doc.add(table);
//
//        // Closing the document
//        doc.close();
//        System.out.println("Table created successfully..");
    }


