package oslomet.testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import oslomet.testing.API.BankController;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Models.Konto;
import oslomet.testing.Models.Kunde;
import oslomet.testing.Models.Transaksjon;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestBankController {

    @InjectMocks
    // denne skal testes
    private BankController bankController;

    @Mock
    // denne skal Mock'es
    private BankRepository repo;

    @Mock
    // denne skal Mock'es
    private Sikkerhet sjekk;

    @Test // Test nr. 1.1 - hentTransaksjoner - Logget inn
    public void test_hentTransaksjoner_LoggetInn() {

        List<Transaksjon> transaksjonListe = new ArrayList<>();

        Transaksjon betaling1 = new Transaksjon(1, "20167348913",
                250, "2023-12-16", "Telia", null, "22334412345");
        Transaksjon betaling2 = new Transaksjon(2, "20226581465",
                8500, "2023-12-25", "Husleie", null, "22334412345");

        transaksjonListe.add(betaling1);
        transaksjonListe.add(betaling2);

        Konto kontoen = new Konto("01010110523", "22334412345",
                15000, "Brukskonto", "NOK", transaksjonListe);

        // Mock respons fra sikkerhet - innlogget
        Mockito.when(sjekk.loggetInn()).thenReturn("01010110523");

        // Mock respons fra repository
        Mockito.when(repo.hentTransaksjoner(anyString(), anyString(),anyString())).thenReturn(kontoen);

        // Act
        Konto resultat = bankController.hentTransaksjoner("22334412345", "2023-12-01", "2023-12-31");

        // Assert
        assertEquals(kontoen, resultat);
    }

    @Test // Test nr. 1.2 - hentTransaksjoner - ikke innlogget
    public void test_hentTransaksjoner_IkkeLoggetInn() {

        // Mock respons fra sikkerhet - ikke innlogget
        Mockito.when(sjekk.loggetInn()).thenReturn(null);

        // Act
        Konto resultat = bankController.hentTransaksjoner("22334412345", "2023-12-01", "2023-12-31");

        // Assert
        assertNull(resultat);
    }

    @Test // Test nr. 2.1 - hentKonti - innlogget
    public void test_hentKonti_Innlogget()  {

        // Arrange
        List<Konto> kontiListe = new ArrayList<>();

        Konto konto1 = new Konto("01010110523", "105010123456",
                720, "Lønnskonto", "NOK", null);
        Konto konto2 = new Konto("01010110523", "22334412345",
                10234.5, "Brukskonto", "NOK", null);

        kontiListe.add(konto1);
        kontiListe.add(konto2);

        // Mock respons fra sikkerhet - innlogget
        when(sjekk.loggetInn()).thenReturn("01010110523");

        // Mock respons fra repository
        when(repo.hentKonti(any(String.class))).thenReturn(kontiListe);

        // act
        List<Konto> resultat = bankController.hentKonti();

        // assert
        assertEquals(kontiListe, resultat);
    }

    @Test // Test nr. 2.2 - hentKonti - ikke innlogget
    public void hentKonti_IkkeLoggetInn()  {

        // Mock respons fra sikkerhet - ikke innlogget
        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Konto> resultat = bankController.hentKonti();

        // assert
        assertNull(resultat);
    }

    @Test // Test nr. 3.1 - hentSaldi - innlogget
    public void test_hentSaldi_Innlogget(){

        // Arrange
        List<Konto> kontiListe = new ArrayList<>();

        Konto konto1 = new Konto("01010110523", "105010123456",
                720, "Lønnskonto", "NOK", null);
        Konto konto2 = new Konto("01010110523", "22334412345",
                10234.5, "Brukskonto", "NOK", null);

        kontiListe.add(konto1);
        kontiListe.add(konto2);

        // Mock respons fra sikkerhet - innlogget
        when(sjekk.loggetInn()).thenReturn("01010110523");

        // Mock respons fra repository
        Mockito.when(repo.hentSaldi(any(String.class))).thenReturn(kontiListe);

        // Act
        List<Konto> resultat = bankController.hentSaldi();

        // Assert
        assertEquals(kontiListe, resultat);
    }

    @Test // Test nr. 3.2 - hentSaldi - ikke innlogget
    public void test_hentSaldi_IkkeLoggetInn() {

        // Mock respons fra sikkerthet - ikke innlogget
        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Konto> resultat = bankController.hentSaldi();

        // assert
        assertNull(resultat);
    }

    @Test // Test nr. 4.1 - registrerBetaling - innlogget
    public void test_registrerBetaling_Innlogget() {

        // Arrange
        Transaksjon betaling = new Transaksjon(1, "20206782459", 3000, "2024-01-01",
                "Haflund", "1", "22334412345");

        // Mock respons fra sikkerhet - innlogget
        Mockito.when(sjekk.loggetInn()).thenReturn("01010110523");

        // Mock respons fra repository
        Mockito.when(repo.registrerBetaling(any(Transaksjon.class))).thenReturn("OK");

        // Act
        String resultat = bankController.registrerBetaling(betaling);

        // Assert
        assertEquals("OK", resultat);
    }

    @Test // Test nr. 4.2 - registrerBetaling - ikke logget inn
    public void test_registrerBetaling_IkkeLoggetInn() {

        // Arrange
        Transaksjon betaling = new Transaksjon(1, "20206782459", 3000, "2024-01-01",
                "Haflund", "1", "22334412345");

        // Mock respons fra sikkerhet - ikke innlogget
        Mockito.when(sjekk.loggetInn()).thenReturn(null);

        // Act
        String resultat = bankController.registrerBetaling(betaling);

        // Assert
        assertNull(resultat);
    }

    @Test // Test nr. 5.1 - hentBetalinger - innlogget
    public void test_hentBetalinger_Innlogget() {

        // Arrange
        List<Transaksjon> transaksjonListe = new ArrayList<>();

        Transaksjon betaling1 = new Transaksjon(1, "20167348913", 250,
                "2023-12-16", "Telia", "1", "105010123456");
        Transaksjon betaling2 = new Transaksjon(2, "20226581465", 8500,
                "2023-12-25", "Husleie", "1", "105010123456");

        transaksjonListe.add(betaling1);
        transaksjonListe.add(betaling2);

        // Mock respons fra sikkerhet - innlogget
        Mockito.when(sjekk.loggetInn()).thenReturn("01010110523");

        // Mock respons fra repository
        Mockito.when(repo.hentBetalinger(any(String.class))).thenReturn(transaksjonListe);

        // Act
        List<Transaksjon> resultat = bankController.hentBetalinger();

        // Assert
        assertEquals(transaksjonListe, resultat);
    }

    @Test // Test nr. 5.2 - hentBetalinger - ikke logget inn
    public void test_hentBetalinger_IkkeLoggetInn() {

        // Mock respons fra sikkerhet - ikke innlogget
        Mockito.when(sjekk.loggetInn()).thenReturn(null);

        // Act
        List<Transaksjon> resultat = bankController.hentBetalinger();

        // Assert
        assertNull(resultat);
    }

    @Test // Test nr. 6.1 - utforBetaling - innlogget
    public void test_utforBetaling_Innlogget() {

        // Arrange - lager objekter av klassen Transaksjon
        List<Transaksjon> transaksjonList = new ArrayList<>();

        Transaksjon betaling1 = new Transaksjon(1, "20167348913", 250,
                "2023-12-16", "Telia", "1", "105010123456");
        Transaksjon betaling2 = new Transaksjon(2, "20226581465", 8500,
                "2023-12-25", "Husleie", "1", "105010123456");

        transaksjonList.add(betaling1);
        transaksjonList.add(betaling2);

        // Mock respons fra sikkerhet - innlogget
        Mockito.when(sjekk.loggetInn()).thenReturn("01010110523");

        // Mock respons fra repository for utforBetaling (int txID)
        Mockito.when(repo.utforBetaling(any(Integer.class))).thenReturn("OK");

        // Mock respons fra repository for hentBetalinger (String personnummer)
        Mockito.when(repo.hentBetalinger(any(String.class))).thenReturn(transaksjonList);

        // Act
        List<Transaksjon> resultat = bankController.utforBetaling(1);

        // Assert *
        assertEquals(transaksjonList, resultat);
    }

    @Test // Test nr. 6.2 - utforBetaling - ikke logget inn
    public void test_utforBetaling_IkkeLoggetInn() {

        // Arrange - lager objekter av klassen Transaksjon
        List<Transaksjon> transaksjonList = new ArrayList<>();

        Transaksjon betaling1 = new Transaksjon(1, "20167348913", 250,
                "2023-12-16", "Telia", "1", "105010123456");
        Transaksjon betaling2 = new Transaksjon(2, "20226581465", 8500,
                "2023-12-25", "Husleie", "1", "105010123456");

        transaksjonList.add(betaling1);
        transaksjonList.add(betaling2);

        // Mock respons fra sikkerhet - ikke innlogget
        Mockito.when(sjekk.loggetInn()).thenReturn(null);

        // Act
        List<Transaksjon> resultat = bankController.utforBetaling(1);

        // Assert
        assertNull(resultat);
    }

    @Test // Test nr. 7.1 - hentKundeInfo - innlogget
    public void test_hentKundeInfo_InnLogget() {

        // arrange
        Kunde enKunde = new Kunde("01010110523",
                "Lene", "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444", "HeiHei");

        // Mock respons fra sikkerhet - innlogget
        when(sjekk.loggetInn()).thenReturn("01010110523");

        // Mock respons fra repository
        when(repo.hentKundeInfo(anyString())).thenReturn(enKunde);

        // Act
        Kunde resultat = bankController.hentKundeInfo();

        // Assert
        assertEquals(enKunde, resultat);
    }

    @Test // Test nr. 7.2 - hentKundeInfo - Ikke innlogget
    public void test_hentKundeInfo_IkkeloggetInn() {

        // Mock respons fra sikkerhet - ikke innlogget
        when(sjekk.loggetInn()).thenReturn(null);

        // Act
        Kunde resultat = bankController.hentKundeInfo();

        // Assert
        assertNull(resultat);
    }

    @Test // Test nr. 8.1 - endre - Innlogget
    public void test_endre_Innlogget() {

        // Arrange
        Kunde kunde1 = new Kunde("01010110523",
                "Lene Marie", "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444", "HeiHei");

        // Mock respons fra sikkerhet - innlogget
        when(sjekk.loggetInn()).thenReturn("01010110523");

        // Mock respons fra repository
        when(repo.endreKundeInfo(any(Kunde.class))).thenReturn("OK");

        // Act
        String resultat = bankController.endre(kunde1);

        // Assert
        assertEquals("OK", resultat);
    }

    @Test // Test nr. 8.2 - endre - Ikke innlogget
    public void test_endre_IkkeLoggetInn() {

        // Arrange
        Kunde kunde1 = new Kunde("01010110523",
                "Lene Marie", "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444", "HeiHei");

        // Mock respons fra sikkerhet - ikke innlogget
        when(sjekk.loggetInn()).thenReturn(null);

        // Act
        String resultat = bankController.endre(kunde1);

        // Assert
        assertNull(resultat);
    }

}

