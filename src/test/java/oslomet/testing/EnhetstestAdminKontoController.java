package oslomet.testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import oslomet.testing.API.AdminKontoController;
import oslomet.testing.API.BankController;
import oslomet.testing.DAL.AdminRepository;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Models.Konto;
import oslomet.testing.Models.Kunde;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestAdminKontoController {

    @InjectMocks // legger til det som skal testes
    private AdminKontoController adminKontoController;

    @Mock // simulerer det som skal brukes
    private AdminRepository repo;

    @Mock // simulerer det som skal brukes
    private Sikkerhet sjekk;

    @Test // Test nr. 1.1 - hentAlleKonti - logget inn
    public void test_hentAlleKonti_LoggetInn() {

        // Arrange
        List<Konto> kontiListe = new ArrayList<>();

        Konto konto1 = new Konto("12345678901", "10108976011", 800.0, "Brukskonto", "NOK", null);
        Konto konto2 = new Konto("12345678901", "10119085922", 4000.0, "Regningskonto", "NOK", null);
        Konto konto3 = new Konto("12345678901", "10110344455", 150000.0, "Sparekonto", "NOK", null);

        kontiListe.add(konto1);
        kontiListe.add(konto2);
        kontiListe.add(konto3);

        // Mock respons fra sikkerhet -  innlogget
        Mockito.when(sjekk.loggetInn()).thenReturn("Admin");

        // Mock respons fra repository
        Mockito.when(repo.hentAlleKonti()).thenReturn(kontiListe);

        // Act
        List<Konto> resultat = adminKontoController.hentAlleKonti();

        // Assert
        assertEquals(kontiListe, resultat);
    }

    @Test // Test nr. 1.2 - hentAlleKonti - ikke innlogget
    public void test_hentAlleKonti_IkkeLoggetInn() {

        // Mock respons fra sikkerhet - ikke innlogget
        Mockito.when(sjekk.loggetInn()).thenReturn(null);

        // Act
        List<Konto> resultat = adminKontoController.hentAlleKonti();

        // Assert - objektet er tom
        assertNull(resultat);
    }

    @Test // Test nr 2.1 - registrerKonto - logget inn
    public void test_registrerKonto_LoggetInn() {

        // Arrange - antar at det er kunden med dette personnumeret i databasen 
        Konto konto1 = new Konto("12345678901", "10108976011", 800.0, "Brukskonto", "NOK", null);

        // Mock respons fra sikkerhet - innlogget
        Mockito.when(sjekk.loggetInn()).thenReturn("Admin");

        // Mock respons fra repository
        Mockito.when(repo.registrerKonto(any(Konto.class))).thenReturn("OK");

        // Act
        String resultat = adminKontoController.registrerKonto(konto1);

        // Assert
        assertEquals("OK", resultat);
    }

    @Test // Test nr 2.3 - registrerKonto - ikke innlogget
    public void test_registrerKonto_IkkeLoggetInn() {

        // Arrange
        Konto konto1 = new Konto("12345678901", "10108976011", 800.0, "Brukskonto", "NOK", null);

        // Mock respons fra sikkerhet - ikke innlogget
        Mockito.when(sjekk.loggetInn()).thenReturn(null);

        // Act
        String resultat = adminKontoController.registrerKonto(konto1);

        // Assert
        assertEquals("Ikke innlogget", resultat);
    }

    @Test // Test nr 3.1 - endreKonto - innlogget
    public void test_endreKonto_LoggetInn() {

        // Arrange
        Konto konto2 = new Konto("12345678901", "10119085922", 15000.0, "Regningskonto", "NOK", null);

        // Mock respons fra sikkerhet - logget inn
        Mockito.when(sjekk.loggetInn()).thenReturn("Admin");

        // Mock respons fra repository
        Mockito.when(repo.endreKonto(any(Konto.class))).thenReturn("OK");

        // Act
        String resultat = adminKontoController.endreKonto(konto2);

        // Assert
        assertEquals("OK", resultat);
    }

    @Test // Test nr 3.2 - endreKonto - ikke innlogget
    public void test_endreKonto_IkkeLoggetInn() {

        // Arrange
        Konto konto2 = new Konto("12345678901", "10119085922", 15000.0, "Regningskonto", "NOK", null);

        // Mock respons fra sikkerhet - ikke innlogget
        Mockito.when(sjekk.loggetInn()).thenReturn(null);

        // Act
        String resultat = adminKontoController.endreKonto(konto2);

        // Assert
        assertEquals("Ikke innlogget", resultat);
    }

    @Test // Test nr 4.1 - sletKonto - innlogget
    public void test_slettKonto_LoggetInn() {

        // Mock respons fra sikkerhet
        Mockito.when(sjekk.loggetInn()).thenReturn("Admin");

        // Mock repons fra repository
        Mockito.when(repo.slettKonto(any(String.class))).thenReturn("OK");

        // Act
        String resultat = adminKontoController.slettKonto("10108976011");

        // Assert
        assertEquals("OK", resultat);
    }

    @Test // Test nr 4.2 - slettKonto - ikke innlogget
    public void test_slettKonto_IkkeLoggetInn() {

        // Mock respons fra sikkerhet
        Mockito.when(sjekk.loggetInn()).thenReturn(null);

        // Act
        String resultat = adminKontoController.slettKonto("10108976011");

        // Assert
        assertEquals("Ikke innlogget", resultat);
    }

}
