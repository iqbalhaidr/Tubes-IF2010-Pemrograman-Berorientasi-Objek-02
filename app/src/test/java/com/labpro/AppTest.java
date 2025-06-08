package com.labpro;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {
//
//    @Test
//    void testAppInitialization() {
//        App app = new App();
//        assertNotNull(app);
//    }

    @Test
    void testParselJson() throws IOException {
        Adapter<Parsel> parselAdapter = new Adapter<>("src/main/resources/testParseljson.json", Parsel.class);
        ArrayList<Parsel> parselArrayList = (ArrayList<Parsel>) parselAdapter.parseList();

        assertNotNull(parselArrayList);

        assertEquals(1, (int) parselArrayList.get(0).getID());
        assertEquals(ParselStatus.REGISTERED,  parselArrayList.get(0).getStatus());

        assertArrayEquals(new int[] {10, 20, 15}, parselArrayList.get(0).getDimensi());

        assertEquals(2.5, parselArrayList.get(0).getBerat());

        assertEquals("Elektronik", parselArrayList.get(0).getJenisBarang());
    }
}