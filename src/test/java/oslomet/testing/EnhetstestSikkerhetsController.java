package oslomet.testing;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockHttpSession;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.HashMap;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class EnhetstestSikkerhetsController {

    @InjectMocks
    // denne skal testes
    private Sikkerhet sikkerhetsController;

    @Mock
    // denne skal Mock'es
    private BankRepository repository;

    @Mock
    // denne skal Mock'es
    private MockHttpSession session;


    @Before
    public void initSession() {
        Map<String,Object> attributes = new HashMap<String,Object>();

        doAnswer(new Answer<Object>(){
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String key = (String) invocation.getArguments()[0];
                return attributes.get(key);
            }
        }).when(session).getAttribute(anyString());

        doAnswer(new Answer<Object>(){
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String key = (String) invocation.getArguments()[0];
                Object value = invocation.getArguments()[1];
                attributes.put(key, value);
                return null;
            }
        }).when(session).setAttribute(anyString(), any());

    }

    @Test
    public void test_sjekkLoggetInnOK() {
        // arrange
        when(repository.sjekkLoggInn(anyString(),anyString())).thenReturn("OK");

        // setningen under setter ikke attributten, dvs. at det ikke er mulig å sette en attributt i dette oppsettet
        session.setAttribute("Innlogget", "12345678901");

        // act
        String resultat = sikkerhetsController.sjekkLoggInn("12345678901","HeiHeiHei");
        // assert
        assertEquals("OK", resultat);
    }

    @Test
    public void test_sjekkLoggInn(){
        // arrange
        when(repository.sjekkLoggInn(anyString(),anyString())).thenReturn("Feil i personnummer eller passord");

        // setningen under setter ikke attributten, dvs. at det ikke er mulig å sette en attributt i dette oppsettet
        session.setAttribute("Innlogget", "12345678901");

        // act
        String resultat = sikkerhetsController.sjekkLoggInn("12345678901","HeiHeiHei");
        // assert
        assertEquals("Feil i personnummer eller passord", resultat);

    }

    @Test
    public void sjekkLoggInn_Feil_i_personnummer(){

        // act (sender inn feil personnummer)
        String resultat = sikkerhetsController.sjekkLoggInn("ABC123456","HeiHeiHei");
        // assert
        assertEquals("Feil i personnummer", resultat);
    }

    @Test
    public void sjekkLoggInn_Feil_i_passord(){

        // act (sender inn feil passord)
        String resultat = sikkerhetsController.sjekkLoggInn("12345678901","Hei");
        // assert
        assertEquals("Feil i passord", resultat);
    }
    @Test
    public void test_LoggetInnOK(){

    // arrange
        session.setAttribute("Innlogget","12345678901");
    // act
    String resultat = sikkerhetsController.loggetInn();
    // assert
    assertEquals("12345678901", resultat);
}

@Test
public void test_loggUt(){
    // arrange
    session.setAttribute("Innlogget", null);
    //act
    String resultat = sikkerhetsController.loggetInn();
    // assert
    assertNull(resultat);
}

    @Test
    public void test_LoggInnAdminOK(){
        // arrange
        session.setAttribute("Innlogget","Admin");
        //assume
        String resultat = sikkerhetsController.loggInnAdmin("Admin", "Admin");
        //act
        assertEquals("Logget inn", resultat);
    }

    @Test
    public void test_LoggInnAdminFeilet(){

        // arrange
        session.setAttribute("Innlogget",null);

        //assume resultat1=feil passord, resultat2=feil brukernavn
        String resultat1 = sikkerhetsController.loggInnAdmin("Admin", "Admin123");
        String resultat2 = sikkerhetsController.loggInnAdmin("Admin123", "Admin");

        //act
        assertEquals("Ikke logget inn", resultat1);
        assertEquals("Ikke logget inn", resultat2);

    }


@Test
    public void test_LoggetInnFeilet(){
        //arrange
        when(session.getAttribute("Innlogget")).thenReturn(null);
        //assume
        String resultat = sikkerhetsController.loggetInn();
        //act
        assertNull(resultat);
}
}