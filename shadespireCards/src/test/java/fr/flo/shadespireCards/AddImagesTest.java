/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.flo.shadespireCards;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author flocava
 */
//@RunWith(SpringRunner.class)
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration
@SpringBootTest
public class AddImagesTest {

    private static final Logger logger = Logger.getLogger(AddImagesTest.class);
    @Autowired
    private ManipulatePdf pdf;

    @Autowired
    private Environment environment;

    private String imageDir;

    List<String> items;

    public AddImagesTest() {
        this.items = Stream.of("421_FRE.png", "422_FRE.png", "423_FRE.png", "424_FRE.png", "425_FRE.png", "426_FRE.png", "427_FRE.png", "428_FRE.png", "429_FRE.png", "430_FRE.png", "431_FRE.png", "432_FRE.png", "433_FRE.png", "434_FRE.png", "435_FRE.png", "436_FRE.png").collect(Collectors.toList());
    }

    @Before
    public void init() {
        String currentDirectory;
        currentDirectory = System.getProperty("user.dir");
        imageDir = currentDirectory + "/src/test/resources/";
        logger.info("image path : " + imageDir);
        items.replaceAll(item -> item = imageDir + item);
        items.forEach(item -> logger.info("items: " + item));
    }

    @Test
    public void testGetSourceImages() throws IOException {
        List<String> test = new ArrayList();
        List<Path> testPath = this.pdf.getSourceImages(imageDir);
        testPath.forEach((path) -> {
            test.add(path.toAbsolutePath().toString());
        });
        test.forEach(item -> logger.info("test: " + item));
        Assert.assertEquals(test, items);

    }

    @Test
    public void testAddImagesParagraphPath() throws Exception {

        Objects.requireNonNull(pdf, "pdf must not be null");
        List<Path> testPath = this.pdf.getSourceImages(imageDir);
        this.pdf.addImagesParagraphPath("testmultipathv7.pdf", testPath);
        Assert.assertNotNull(new File(environment.getProperty("pdf.path") + "testmultipathv7.pdf"));
    }

    @Test
    public void testReal() throws Exception {

        Objects.requireNonNull(pdf, "pdf must not be null");
        List<Path> testPath = this.pdf.getSourceImages("/Users/flocava/Downloads/shadespire");
        this.pdf.addImagesParagraphPath("shadespireCards.pdf", testPath);
        Assert.assertNotNull(new File(environment.getProperty("pdf.path") + "shadespireCards.pdf"));
    }

    @Ignore
    @Test
    public void testAddImages() throws Exception {

        Objects.requireNonNull(pdf, "pdf must not be null");

        //this.pdf.addPageNumber("testmultiv7.pdf", items);
        this.pdf.addImagesParagraph("testmultiv7.pdf", items);
    }
}
