package com.labpro;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void testAppInitialization() {
        App app = new App();
        assertNotNull(app);
    }

    @Test
    void testParselConstructor(){
        Parsel p = new Parsel(1, ParselStatus.REGISTERED, new int[]{12,23,34}, 12, "Elektronik");
        assertEquals(1, p.getID());
    }

    @Test
    void testParselListIsNotNull() throws IOException {
        Adapter<Parsel> adapter = new Adapter<>("src/main/resources/testParseljson.json", Parsel.class);
        assertNotNull(adapter.parseList());
    }

    @Test
    void testParselHasCorrectID() throws IOException {
        Parsel parsel = getFirstParsel();
        assertEquals(1, (int) parsel.getID());
    }

    @Test
    void testParselHasCorrectStatus() throws IOException {
        Parsel parsel = getFirstParsel();
        assertEquals(ParselStatus.REGISTERED, parsel.getStatus());
    }

    @Test
    void testParselHasCorrectDimensi() throws IOException {
        Parsel parsel = getFirstParsel();
        assertArrayEquals(new int[]{10, 20, 15}, parsel.getDimensi());
    }

    @Test
    void testParselHasCorrectBerat() throws IOException {
        Parsel parsel = getFirstParsel();
        assertEquals(2.5, parsel.getBerat());
    }

    @Test
    void testParselHasCorrectJenisBarang() throws IOException {
        Parsel parsel = getFirstParsel();
        assertEquals("Elektronik", parsel.getJenisBarang());
    }

    @Test
    public void testParselSetID() throws IOException {
        Parsel parsel = getFirstParsel();
        parsel.setID(7);
        assertEquals(7, (int) parsel.getID());
    }

    @Test
    public void testParselSetDimensiValid() throws IOException {
        Parsel parsel = getFirstParsel();
        parsel.setDimensi(new int[]{17, 18, 19});
        assertArrayEquals(new int[]{17, 18, 19}, parsel.getDimensi());
    }

    @Test
    public void testParselSetDimensiTidakValid() throws IOException {
        Parsel parsel = getFirstParsel();
        assertThrows(IllegalArgumentException.class, () -> {parsel.setDimensi(new int[]{17, 18});});
    }

    @Test
    public void testParselSetBeratValid() throws IOException {
        Parsel parsel = getFirstParsel();
        parsel.setBerat(17);
        assertEquals(17, parsel.getBerat());
    }

    @Test
    public void testParselSetBeratTidakValid() throws IOException {
        Parsel parsel = getFirstParsel();
        assertThrows(IllegalArgumentException.class, () -> {parsel.setBerat(-2);});
    }

    @Test
    public void testParselSetJenisBarang() throws IOException {
        Parsel parsel = getFirstParsel();
        parsel.setJenisBarang("Sabun");
        assertEquals("Sabun", parsel.getJenisBarang());
    }

    @Test
    public void testParselgetDeleteStatus() throws IOException {
        Parsel parsel = getFirstParsel();
        assertFalse(parsel.getDeleteStatus());
    }

    @Test
    public void testParselSetDeleteStatus() throws IOException {
        Parsel parsel = getFirstParsel();
        parsel.setDeleteStatus(true);
        assertTrue(parsel.getDeleteStatus());
    }


    @Test
    public void testParselSetStatus() throws IOException {
        Parsel parsel = getFirstParsel();
        parsel.setStatus(ParselStatus.UNREGISTERED);
        assertEquals(ParselStatus.UNREGISTERED, parsel.getStatus());
    }

    public void testParselEquals() throws IOException {
        Parsel p = new Parsel(1, ParselStatus.REGISTERED, new int[]{12,23,34}, 12, "Elektronik");
        Parsel p1 = getFirstParsel();
        assertTrue(p.equals(p1));

        assertFalse(p1.equals(new ArrayList<Integer>()));

    }




    private Parsel getFirstParsel() throws IOException {
        Adapter<Parsel> adapter = new Adapter<>("src/main/resources/testParseljson.json", Parsel.class);
        return adapter.parseList().get(0);
    }


    @Test
    void testKurirListIsNotNull() throws IOException {
        Adapter<Kurir> adapter = new Adapter<>("src/main/resources/testKurirJson.json", Kurir.class);
        assertNotNull(adapter.parseList());
    }

    @Test
    void testKurirHasCorrectID() throws IOException {
        Kurir kurir = getFirstKurir();
        assertEquals(0, kurir.getID());
    }

    @Test
    void testKurirHasCorrectName() throws IOException {
        Kurir kurir = getFirstKurir();
        assertEquals("Budi", kurir.getName());
    }

    @Test
    void testKurirHasCorrectJenisKelamin() throws IOException {
        Kurir kurir = getFirstKurir();
        assertEquals(JenisKelamin.LAKI_LAKI, kurir.getJenisKelamin());
    }

    @Test
    void testKurirHasCorrectPathFoto() throws IOException {
        Kurir kurir = getFirstKurir();
        assertEquals("data/ke/path/foto", kurir.getPathFoto());
    }

    @Test
    void testKurirHasCorrectTanggalLahir() throws IOException {
        Kurir kurir = getFirstKurir();
        assertEquals(LocalDate.of(2007, 12, 3), kurir.getTanggalLahir());
    }

    private Kurir getFirstKurir() throws IOException {
        Adapter<Kurir> adapter = new Adapter<>("src/main/resources/testKurirJson.json", Kurir.class);
        return adapter.parseList().get(0);
    }


}