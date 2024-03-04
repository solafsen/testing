package oslomet.testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import oslomet.testing.API.AdminKundeController;
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
public class EnhetstestAdminKundeController {

    @InjectMocks
    // denne skal testes
    private AdminKundeController adminKundeController;

    @Mock
    // denne skal Mock'es
    private AdminRepository repo;

    @Mock
    // denne skal Mock'es
    private Sikkerhet sjekk;

    @Test
    public void test_lagreKundeOK(){
        //arrange
        Kunde kunde1 = new Kunde("1010103256", "Truls", "Andersen", "Trondheimsveien 25", "0125", "Oslo", "45698712", "passord123");
        Mockito.when(sjekk.loggetInn()).thenReturn("Admin");

        Mockito.when(repo.registrerKunde(any(Kunde.class))).thenReturn("OK");

        //act
        String resultat = adminKundeController.lagreKunde(kunde1);

        //assert
        assertEquals("OK", resultat);

    }

    @Test
    public void test_lagreKundeFeil(){
        //arrange
        Kunde kunde1 = new Kunde("1010103256", "Truls", "Andersen", "Trondheimsveien 25", "0125", "Oslo", "45698712", "passord123");
        Mockito.when(sjekk.loggetInn()).thenReturn(null);

    //act
        String resultat = adminKundeController.lagreKunde(kunde1);
    //assert
        assertEquals("Ikke logget inn", resultat);
    }


    @Test
    public void test_hentAlleKunderInnlogget(){

        List<Kunde> kundeliste = new ArrayList<>();

        Kunde kunde1 = new Kunde("1010103256", "Truls", "Andersen", "Trondheimsveien 25", "0125", "Oslo", "45698712", "passord123");
        Kunde kunde2 = new Kunde("2020103256", "Trude", "Tobiasen", "Heimdalsgata 12", "0523", "Oslo", "46569052", "123passord");

        kundeliste.add(kunde1);
        kundeliste.add(kunde2);

        Mockito.when(sjekk.loggetInn()).thenReturn("1010103256");
        Mockito.when(repo.hentAlleKunder()).thenReturn(kundeliste);

        //act
        List<Kunde> resultat = adminKundeController.hentAlle();
        //assert
        assertEquals(kundeliste, resultat);
    }

    @Test
    public void test_hentAlleKunder_IkkeInnlogget(){
        //arrange
        when(sjekk.loggetInn()).thenReturn(null);
        //act
        List<Kunde> resultat = adminKundeController.hentAlle();
        //assert
        assertNull(resultat);
    }

    @Test
    public void test_endreKunde_Innlogget(){
        Kunde kunde1 = new Kunde("1010103256", "Truls", "Andersen", "Trondheimsveien 25", "0125", "Oslo", "45698712", "passord123");
        //arrange
        when(sjekk.loggetInn()).thenReturn("1010103256");
        when(repo.endreKundeInfo(any(Kunde.class))).thenReturn("OK");
        //act
        String resultat = adminKundeController.endre(kunde1);
        //assert
        assertEquals("OK", resultat);
    }

    @Test
    public void test_endreKunde_IkkeInnlogget(){
        Kunde kunde1 = new Kunde("1010103256", "Truls", "Andersen", "Trondheimsveien 25", "0125", "Oslo", "45698712", "passord123");
        //arrange
        when(sjekk.loggetInn()).thenReturn(null);
        //act
        String resultat = adminKundeController.endre(kunde1);
        //assert
       assertEquals("Ikke logget inn", resultat);
    }

    @Test
    public void test_slettKunde_Innlogget(){
        Kunde kunde1 = new Kunde("1010103256", "Truls", "Andersen", "Trondheimsveien 25", "0125", "Oslo", "45698712", "passord123");

        //arrange
        when(sjekk.loggetInn()).thenReturn("1010103256");
        when(repo.slettKunde(anyString())).thenReturn("OK");

        //act
        String resultat = adminKundeController.slett("1010103256");

        //assert
        assertEquals("OK", resultat);

    }
    @Test
    public void test_slettKunde_IkkeInnlogget(){

        //arrange
        when(sjekk.loggetInn()).thenReturn(null);

        //act
        String resultat = adminKundeController.slett("1010103256");

        //assert
        assertEquals("Ikke logget inn", resultat);

    }


}