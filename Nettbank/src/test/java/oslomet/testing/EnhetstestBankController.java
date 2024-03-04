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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestBankController {

    @InjectMocks
    // denne skal testes
    private BankController bankController;

    @Mock
    // denne skal Mock'es
    private BankRepository repository;

    @Mock
    // denne skal Mock'es
    private Sikkerhet sjekk;

    @Test
    public void hentKundeInfo_loggetInn() {

        // arrange
        Kunde enKunde = new Kunde("01010110523",
                "Lene", "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444", "HeiHei");

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentKundeInfo(anyString())).thenReturn(enKunde);

        // act
        Kunde resultat = bankController.hentKundeInfo();

        // assert
        assertEquals(enKunde, resultat);
    }

    @Test
    public void hentKundeInfo_IkkeloggetInn() {

        // arrange
        when(sjekk.loggetInn()).thenReturn(null);

        //act
        Kunde resultat = bankController.hentKundeInfo();

        // assert
        assertNull(resultat);
    }

    @Test
    public void hentKonti_LoggetInn() {
        // arrange
        List<Konto> konti = new ArrayList<>();
        Konto konto1 = new Konto("105010123456", "01010110523",
                720, "Lønnskonto", "NOK", null);
        Konto konto2 = new Konto("105010123456", "12345678901",
                1000, "Lønnskonto", "NOK", null);
        konti.add(konto1);
        konti.add(konto2);

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(repository.hentKonti(anyString())).thenReturn(konti);

        // act
        List<Konto> resultat = bankController.hentKonti();

        // assert
        assertEquals(konti, resultat);
    }

    @Test
    public void hentKonti_IkkeLoggetInn() {
        // arrange

        when(sjekk.loggetInn()).thenReturn(null);

        // act
        List<Konto> resultat = bankController.hentKonti();

        // assert
        assertNull(resultat);
    }

    @Test
    public void testHentSaldi() {
        // Opprett dummydata for testen
        String personnummer = "123456789";
        Konto konto1 = new Konto("105010123456", "01010110523", 720, "Lønnskonto", "NOK", null);
        Konto konto2 = new Konto("105010123457", "01010110524", 820, "Sparekonto", "NOK", null);
        List<Konto> konti = Arrays.asList(konto1, konto2);

        // Sett opp mock-oppførselen for sikkerhet
        when(sjekk.loggetInn()).thenReturn(personnummer);

        // Sett opp mock-oppførselen for repository
        when(repository.hentSaldi(personnummer)).thenReturn(konti);

        // Kjør metoden som skal testes
        List<Konto> resultat = bankController.hentSaldi();

        // Sjekk om resultatet er det samme som det forventede
        assertEquals(konti.size(), resultat.size());
        assertEquals(konti.get(0), resultat.get(0));
        assertEquals(konti.get(1), resultat.get(1));
    }

    @Test
    public void testRegistrerBetaling() {
        // Opprett dummydata for testen
        Transaksjon betaling = new Transaksjon(/* legg til relevant data */);

        // Sett opp mock-oppførselen for sikkerhet
        when(sjekk.loggetInn()).thenReturn("123456789");

        // Sett opp mock-oppførselen for repository
        when(repository.registrerBetaling(betaling)).thenReturn("OK");

        // Kjør metoden som skal testes
        String resultat = bankController.registrerBetaling(betaling);

        // Sjekk om resultatet er det samme som det forventede
        assertEquals("OK", resultat);
    }
    @Test
    public void testHentBetalinger() {
        // Opprett dummydata for testen
        String personnummer = "123456789";
        List<Transaksjon> transaksjoner = new ArrayList<>();
        // Legg til dummytransaksjoner i listen

        // Sett opp mock-oppførselen for sikkerhet
        when(sjekk.loggetInn()).thenReturn(personnummer);

        // Sett opp mock-oppførselen for repository
        when(repository.hentBetalinger(personnummer)).thenReturn(transaksjoner);

        // Kjør metoden som skal testes
        List<Transaksjon> resultat = bankController.hentBetalinger();

        // Sjekk om resultatet er det samme som det forventede
        assertEquals(transaksjoner.size(), resultat.size());
        // Legg til flere asserter om nødvendig
    }
    @Test
    public void UtforBetaling() {
        // Opprett dummydata for testen
        int txID = 123;
        String personnummer = "123456789";
        Transaksjon transaksjon = new Transaksjon(); // Opprett en dummytransaksjon

        // Sett opp mock-oppførselen for sikkerhet
        when(sjekk.loggetInn()).thenReturn(personnummer);

        // Sett opp mock-oppførselen for repository
        when(repository.utforBetaling(txID)).thenReturn("OK");
        when(repository.hentBetalinger(personnummer)).thenReturn(Arrays.asList(transaksjon)); // Legg til transaksjonen i listen

        // Kjør metoden som skal testes
        List<Transaksjon> resultat = bankController.utforBetaling(txID);

        // Sjekk om resultatet er det samme som det forventede
        assertNotNull(resultat);
        assertEquals(1, resultat.size());
        // Legg til flere asserter om nødvendig
    }
}

