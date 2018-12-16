/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.flo.shadespireCards;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

@Configuration
//@Scope("singleton")
@PropertySource("classpath:cards.properties")
public class ManipulatePdf {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ManipulatePdf.class);

    private static Document document;

    @Autowired
    private Environment environment;

    @PostConstruct
    public void initialize() {
        logger.info("initializing class");
        //PdfWriter.getInstance(document, new FileOutputStream(environment.getProperty("pdf.path")));
    }

    protected List<Path> getSourceImages(String dir) throws IOException {
        List<Path> result = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir), "*.png")) {
            for (Path entry : stream) {
                result.add(entry);
            }
        } catch (DirectoryIteratorException ex) {
            // I/O error encounted during the iteration, the cause is an IOException
            throw ex.getCause();
        }
        result.sort(Comparator.comparing(Path::toString));
        return result;
    }
    
        protected void addImagesParagraphPath(String dest, List<Path> images) throws Exception {
        logger.info("init pdfwriter for pdf: " + environment.getProperty("pdf.path") + dest);
        //Image image = new Image(ImageDataFactory.create(images[0]));
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(environment.getProperty("pdf.path") + dest));
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4));
        doc.setMargins(20, 20, 20, 20);
        //, new PageSize(image.getImageWidth(), image.getImageHeight()));
        //for (int i = 0; i < IMAGES.length; i++) {
        //pdfDoc.addNewPage();
        Paragraph p = new Paragraph();
        for (Path img : images) {
            logger.info("Adding image " + img);
            Image image = new Image(ImageDataFactory.create(img.toAbsolutePath().toString()));
            //shadespire carte: 6,4x8,9 (cm) -> 181x251
            image.scaleToFit(181, 251);
            image.setMargins(1, 1, 1, 1);
            p.add(image);
        }
        doc.add(p);
        doc.close();
        //addPageNumber(dest);
    }

    protected void addImagesParagraph(String dest, List<String> images) throws Exception {
        logger.info("init pdfwriter for pdf: " + environment.getProperty("pdf.path") + dest);
        //Image image = new Image(ImageDataFactory.create(images[0]));
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(environment.getProperty("pdf.path") + dest));
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4));
        doc.setMargins(20, 20, 20, 20);
        //, new PageSize(image.getImageWidth(), image.getImageHeight()));
        //for (int i = 0; i < IMAGES.length; i++) {
        //pdfDoc.addNewPage();
        Paragraph p = new Paragraph();
        for (String img : images) {
            logger.info("Adding image " + img);
            Image image = new Image(ImageDataFactory.create(img));
            //shadespire carte: 6,4x8,9 (cm) -> 181x251
            image.scaleToFit(181, 251);
            image.setMargins(1, 1, 1, 1);
            p.add(image);
        }
        doc.add(p);
        doc.close();
        //addPageNumber(dest);
    }



    protected void addPageNumber(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(environment.getProperty("pdf.path") + dest));
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4));
        int n = pdfDoc.getNumberOfPages();
        for (int i = 1; i <= n; i++) {
            doc.showTextAligned(new Paragraph(String.format("page %s of %s", i, n)),
                    559, 806, i, TextAlignment.RIGHT, VerticalAlignment.TOP, 0);
        }
        doc.close();
    }

    public void closePdf() {
        this.document.close();
    }

}
