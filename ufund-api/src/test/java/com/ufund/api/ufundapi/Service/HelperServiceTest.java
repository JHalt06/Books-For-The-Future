package com.ufund.api.ufundapi.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.BeforeEach;

import com.ufund.api.ufundapi.DAO.FileCupboardDAO;

public class HelperServiceTest {
    private HelperService helperService;
    private FileCupboardDAO cupboardDao;
    private File cupboardFile;

    @BeforeEach
    void setup() throws IOException {
        cupboardFile = File.createTempFile("test-cupboard", ".json");
        Files.writeString(cupboardFile.toPath(), """
                                                {
                                                "cupboard": [{\r
                                                    "id" : 1,\r
                                                    "name" : "Twinkies",\r
                                                    "quantity" : 120,\r
                                                    "fundingAmount" : 27.0\r
                                                }]}"""
        );
        cupboardFile.deleteOnExit();
        cupboardDao = new FileCupboardDAO(cupboardFile.getAbsolutePath());
        helperService = new HelperService(cupboardDao);
    }

    // @Test
    // void testSearchNeeds() throws IOException {
    //     Need n = new Need("Twinkies", 120, 27.0);
    //     helperService.addNeed(n);
    //     ArrayList<Need> needs = new ArrayList<>();
    //     needs.addAll(helperService.searchNeedsByName("Atoms"));
    //     assertEquals(needs.size(), 1);
    // }
}
