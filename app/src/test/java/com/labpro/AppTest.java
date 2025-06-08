package com.labpro;
//import jdk.vm.ci.meta.Local;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    AppTest() throws IOException {
    }

//    @Test
//    void testAppInitialization() {
//        App app = new App();
//        assertNotNull(app);
//    }

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

    @Test
    void testKurirConstructr() throws IOException {
        Kurir kurir = getFirstKurir();
        assertEquals(0, kurir.getID());
    }

    @Test
    void testKurirSetID() throws IOException {
        Kurir kurir = getFirstKurir();
        kurir.setId(2);
        assertEquals(2, kurir.getID());
    }

    @Test
    void testKurirSetName() throws IOException {
        Kurir kurir = getFirstKurir();
        kurir.setName("Andi");
        assertEquals("Andi", kurir.getName());
    }

    @Test
    void testKurirSetJenisKelamin() throws IOException {
        Kurir kurir = getFirstKurir();
        kurir.setJenisKelamin(JenisKelamin.PEREMPUAN);
        assertEquals(JenisKelamin.PEREMPUAN, kurir.getJenisKelamin());
    }

    @Test
    void testKurirSetPathFoto() throws IOException {
        Kurir kurir = getFirstKurir();
        kurir.setPathFoto("Andi");
        assertEquals("Andi", kurir.getPathFoto());
    }

    @Test
    void testKurirSetTanggalLahir() throws IOException {
        Kurir kurir = getFirstKurir();
        LocalDate tanggalLahir = LocalDate.of(2007, 12, 30);
        kurir.setTanggalLahir(tanggalLahir);
        assertEquals(tanggalLahir,  kurir.getTanggalLahir());
    }

    @Test
    void testKurirSetDeleteStatus() throws IOException {
        Kurir kurir = getFirstKurir();
        kurir.setDeleteStatus(true);
        assertTrue(kurir.getDeleteStatus());
    }

    @Test
    void testKurirEquals() throws IOException {
        Kurir kurir = getFirstKurir();
        assertTrue(kurir.equals(kurir));

        assertFalse(kurir.equals(new ArrayList<>()));
    }

    @Test
    void testKurirToString() throws IOException {
        Kurir kurir = getFirstKurir();
        StringBuilder sb = new StringBuilder();
        sb.append("Kurir ");
        sb.append(kurir.getName());
        sb.append(" dengan ID ");
        sb.append(kurir.getID());

        assertEquals(sb.toString(), kurir.toString());
    }

    private Kurir getFirstKurir() throws IOException {
        Adapter<Kurir> adapter = new Adapter<>("src/main/resources/testKurirJson.json", Kurir.class);
        return adapter.parseList().get(0);
    }


    @Test
    public void adapterPengiriman() throws IOException {
        Map<String, Class<? extends Pengiriman>> map = new HashMap<>();
        map.put("INTERNASIONAL", PengirimanInternasional.class);
        map.put("DOMESTIK", PengirimanDomestik.class);

        Adapter<Pengiriman> adapterPengiriman = new Adapter<Pengiriman>("src/main/java/com/labpro/dummyData/Pengiriman.json", Pengiriman.class, "tipe", map);

        List<Pengiriman> allPengiriman = adapterPengiriman.parseList();

        assertNotNull(allPengiriman);

    }

    public Pengiriman getFirstPengiriman() throws IOException {
        Map<String, Class<? extends Pengiriman>> map = new HashMap<>();
        map.put("INTERNASIONAL", PengirimanInternasional.class);
        map.put("DOMESTIK", PengirimanDomestik.class);

        Adapter<Pengiriman> adapterPengiriman = new Adapter<Pengiriman>("src/main/java/com/labpro/dummyData/Pengiriman.json", Pengiriman.class, "tipe", map);

        List<Pengiriman> allPengiriman = adapterPengiriman.parseList();

        return allPengiriman.get(0);
    }

    @Test
    public void testGetterPengirimanDomestik() throws IOException {
        PengirimanDomestik  pengiriman = (PengirimanDomestik) getFirstPengiriman();
        assertEquals(1001, pengiriman.getIdPengiriman());
        assertEquals("SPX10001", pengiriman.getNoResi());
        assertEquals("Jl. Mawar No. 10, Jakarta Selatan", pengiriman.getTujuan());
        assertEquals(StatusPengiriman.DIKIRIM, pengiriman.getStatusPengiriman());
        assertEquals(LocalDate.of(2025, 6, 1), pengiriman.getTanngalPembuatan());
        assertEquals("Andi Wijaya", pengiriman.getNamaPengiriman());
        assertEquals("081234567890", pengiriman.getNoTelp());
        assertEquals("Budi Cahyadi", pengiriman.getNamaPenerima());
        assertEquals("087654321098", pengiriman.getNoTelpPenerima());
        assertEquals("Domestik", pengiriman.getType());
    }

    @Test
    public void testAddAndRemoveParsel() throws IOException {
        PengirimanDomestik  pengiriman = (PengirimanDomestik) getFirstPengiriman();
        Parsel newParsel = getFirstParsel();
        pengiriman.addParsel(newParsel);
        assertTrue(pengiriman.getListOfParsel().contains(newParsel));
        assertTrue(pengiriman.getListIdParsel().contains(newParsel.getID()));

        pengiriman.removeParsel(newParsel);
        assertFalse(pengiriman.getListOfParsel().contains(newParsel));
        assertFalse(pengiriman.getListIdParsel().contains(103));
    }

    @Test
    public void testGetDetailsMap() throws IOException {
        PengirimanDomestik  pengiriman = (PengirimanDomestik) getFirstPengiriman();
        Map<String, String> details = pengiriman.getDetails();
        assertEquals("1001", details.get("idPengiriman"));
        assertEquals("SPX10001", details.get("noResi"));
        assertEquals("Jl. Mawar No. 10, Jakarta Selatan", details.get("tujuan"));
        assertEquals("Dikirim", details.get("statusPengiriman"));
        assertEquals("2025-06-01", details.get("tanggalPembuatan"));
        assertEquals("Andi Wijaya", details.get("namaPengiriman"));
        assertEquals("081234567890", details.get("noTelp"));
        assertEquals("Budi Cahyadi", details.get("namaPenerima"));
        assertEquals("087654321098", details.get("noTelpPenerima"));
        assertEquals("201", details.get("kurirId"));
    }



    PengirimanDomestik pengiriman = (PengirimanDomestik)getFirstPengiriman();

    @Test
    public void testSetIdPengiriman() {
        pengiriman.setIdPengiriman(42);
        assertEquals(42, pengiriman.getIdPengiriman());
    }

    @Test
    public void testSetNoResi() {
        pengiriman.setNoResi("NEWRESI001");
        assertEquals("NEWRESI001", pengiriman.getNoResi());
    }

    @Test
    public void testSetTujuan() {
        pengiriman.setTujuan("Bandung");
        assertEquals("Bandung", pengiriman.getTujuan());
    }

    @Test
    public void testSetStatusPengiriman() {
        pengiriman.setStatusPengiriman(StatusPengiriman.DIKIRIM);
        assertEquals(StatusPengiriman.DIKIRIM, pengiriman.getStatusPengiriman());
    }

    @Test
    public void testSetTanggalPembuatan() {
        LocalDate newDate = LocalDate.of(2025, 2, 2);
        pengiriman.setTanngalPembuatan(newDate);
        assertEquals(newDate, pengiriman.getTanngalPembuatan());
    }

    @Test
    public void testSetNamaPengiriman() {
        pengiriman.setNamaPengiriman("Budi");
        assertEquals("Budi", pengiriman.getNamaPengiriman());
    }

    @Test
    public void testSetNoTelp() {
        pengiriman.setNoTelp("08111222333");
        assertEquals("08111222333", pengiriman.getNoTelp());
    }

    @Test
    public void testSetNamaPenerima() {
        pengiriman.setNamaPenerima("Siti");
        assertEquals("Siti", pengiriman.getNamaPenerima());
    }

    @Test
    public void testSetNoTelpPenerima() {
        pengiriman.setNoTelpPenerima("0899000111");
        assertEquals("0899000111", pengiriman.getNoTelpPenerima());
    }

    @Test
    public void testSetListIdParsel() {
        List<Integer> idList = Arrays.asList(5, 6, 7);
        pengiriman.setListIdParsel(idList);
        assertEquals(idList, pengiriman.getListIdParsel());
    }

    @Test
    public void testSetListOfParsel() throws IOException {
        List<Parsel> parsels = Arrays.asList(getFirstParsel(), getFirstParsel());
        pengiriman.setListOfParsel(parsels);
        assertEquals(parsels, pengiriman.getListOfParsel());
    }

    @Test
    public void testSetKurirId() {
        pengiriman.setKurirId(555);
        assertEquals(555, pengiriman.getKurirId());
    }

    @Test
    public void testSetKurirName() {
        pengiriman.setKurirName("Kurir Baru");
        assertEquals("Kurir Baru", pengiriman.getKurirName());
    }



}