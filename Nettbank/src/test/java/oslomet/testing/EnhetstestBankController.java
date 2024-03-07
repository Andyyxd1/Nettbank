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
    public void HentTransaksjoner_LoggetInn() {
        // Opprett dummydata for testen
        String kontoNr = "123456789";
        String fraDato = "2022-01-01";
        String tilDato = "2022-02-01";

        Transaksjon transaksjon1 = new Transaksjon(1, "23233232", 332332, "21-02-2030", "Betaling for varer", "105010123456", "23");
        Transaksjon transaksjon2 = new Transaksjon(1, "2321122", 1232, "21-02-2020", "Betaling for varer", "105010123456", "23");
        List<Transaksjon> transaksjoner = Arrays.asList(transaksjon1, transaksjon2);

        Konto konto = new Konto("242","105010123456", 4434, "nok", "nok",transaksjoner);
        konto.setTransaksjoner(transaksjoner);


        // Setter opp mock-oppførsel for repository.hentTransaksjoner()
        when(repository.hentTransaksjoner(kontoNr, fraDato, tilDato)).thenReturn(konto);

        // Setter opp mock-oppførsel for sjekk.loggetInn()
        when(sjekk.loggetInn()).thenReturn("Innlogget"); // Simulerer at noen er logget inn

        // Kall metoden som skal testes
        Konto resultat = bankController.hentTransaksjoner(kontoNr, fraDato, tilDato);

        // Sjekk om resultatet er som forventet
        assertNotNull(resultat);
        assertEquals(konto, resultat);
        assertEquals(transaksjoner.size(), resultat.getTransaksjoner().size());
    }

    @Test
    public void hentTransaksjoner_IkkeLoggetInn() {
        when(sjekk.loggetInn()).thenReturn(null);
        when(bankController.hentTransaksjoner("32878738732", "28.01-2024", "30.01.2024")).thenReturn(null);

        Konto resultat = bankController.hentTransaksjoner("32878738732", "28.01-2024", "30.01.2024");

        assertNull(resultat);
    }

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
    public void HentSaldi_Loggetinn() {
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
    public void hentSaldi_IkkeLoggetInn(){
        when(sjekk.loggetInn()).thenReturn(null);

        List<Konto> resulstat = bankController.hentSaldi();

        assertNull(resulstat);
    }

    @Test
    public void hentSaldi_Feilet(){
        when(sjekk.loggetInn()).thenReturn("Innlogget");

        when(repository.hentSaldi(anyString())).thenReturn(null);

        List<Konto> resulstat = bankController.hentSaldi();

        assertNull(resulstat);

    }

    @Test
    public void RegistrerBetaling_Loggetinn() {
        Transaksjon transaksjon1 = new Transaksjon(1, "23233232", 332332, "21-02-2030", "Betaling for varer", "105010123456", "23");

        when(sjekk.loggetInn()).thenReturn("Innlogget");

        when(repository.registrerBetaling(transaksjon1)).thenReturn("OK");

        String resultat = bankController.registrerBetaling(transaksjon1);

        assertEquals("OK",resultat);
    }
    @Test
    public void RegistrerBetaling_IkkeLoggetInn(){
        Transaksjon transaksjon1 = new Transaksjon(1, "23233232", 332332, "21-02-2030", "Betaling for varer", "105010123456", "23");

        when(sjekk.loggetInn()).thenReturn(null);

        String resultat = bankController.registrerBetaling(transaksjon1);

        assertNull(resultat);
    }
    @Test
    public void HentBetalinger_LoggetInn() {
        Transaksjon transaksjon1 = new Transaksjon(1, "23233232", 332332, "21-02-2030", "Betaling for varer", "105010123456", "23");

        List<Transaksjon> trannsaksjon = new ArrayList<>();
        trannsaksjon.add(transaksjon1);

        when(sjekk.loggetInn()).thenReturn("Innlogget");

        when(repository.hentBetalinger(anyString())).thenReturn(trannsaksjon);

        List<Transaksjon> resultat = bankController.hentBetalinger();

        assertEquals(trannsaksjon,resultat);
    }

    @Test
    public void HentBetalinger_IkkeLoggetInn(){
        when(sjekk.loggetInn()).thenReturn(null);

        List<Transaksjon> resultat = bankController.hentBetalinger();

        assertNull(resultat);
    }
    @Test
    public void HentBetalinger_Feilet(){
        when(sjekk.loggetInn()).thenReturn("Innlogget");

        when(repository.hentBetalinger(anyString())).thenReturn(null);

        List<Transaksjon> resultat = bankController.hentBetalinger();

        assertNull(resultat);
    }


    @Test
    public void UtforBetaling_LoggetInn_OKtxID() {
        // Din testmetode
            // Opprett dummydata for testen
            Transaksjon transaksjon1 = new Transaksjon(1, "23233232", 332332, "21-02-2030", "Betaling for varer", "105010123456", "23");

            List<Transaksjon> transaksjonListe = new ArrayList<>();
            transaksjonListe.add(transaksjon1);

            // Anta at "sjekk" og "repository" er riktig initialisert

            when(sjekk.loggetInn()).thenReturn("Innlogget");
            when(repository.utforBetaling(transaksjon1.getTxID())).thenReturn("OK");
            when(repository.hentBetalinger(anyString())).thenReturn(transaksjonListe);

            List<Transaksjon> resultat = bankController.utforBetaling(transaksjon1.getTxID());

            assertEquals(transaksjonListe, resultat);
        }

        @Test
        public void UtforBetaling_LoggetInn_IkkeOKtxID(){
            Transaksjon transaksjon1 = new Transaksjon(1, "23233232", 332332, "21-02-2030", "Betaling for varer", "105010123456", "23");

            List<Transaksjon> transaksjon = new ArrayList<>();

            transaksjon.add(transaksjon1);

            when(sjekk.loggetInn()).thenReturn("Innlogget");
            when(repository.utforBetaling(transaksjon1.getTxID())).thenReturn("Feil");

            List<Transaksjon> resultat = bankController.utforBetaling(transaksjon1.getTxID());

            assertNull(resultat);
        }

        @Test
        public void UtforBetaling_IkkeLoggetInn(){
        when(sjekk.loggetInn()).thenReturn(null);

        List<Transaksjon> resultat = bankController.utforBetaling(1);

        assertNull(resultat);
        }
    @Test
    public void HentKundeInfo_LoggetInn() {
        // Opprett dummydata for testen
        Kunde kunde = new Kunde("982398298332","john", "John", "oslogate", "123", "oslo", "545", "34434");
       when(sjekk.loggetInn()).thenReturn("982398298332");

       when(repository.hentKundeInfo(anyString())).thenReturn(kunde);

       Kunde resultat = bankController.hentKundeInfo();

       assertEquals(kunde,resultat);
    }

    @Test
    public void HentKundeInfo_IkkeLoggetInn(){
        when(sjekk.loggetInn()).thenReturn(null);

        Kunde resultat = bankController.hentKundeInfo();

        assertNull(resultat);
    }
    @Test
    public void EndreKundeInfo_LoggetInn() {
        // Opprett dummykunde for testen
        Kunde kunde = new Kunde("982398298332","john", "John", "oslogate", "123", "oslo", "545", "34434");

        when(sjekk.loggetInn()).thenReturn("Innlogget");
        when(repository.endreKundeInfo(kunde)).thenReturn("OK");

        String resultat = bankController.endre(kunde);

        assertEquals("OK",resultat);
    }

    @Test
    public void EndreKundeInfo_IkkeLoggetInn(){
        Kunde kunde = new Kunde("982398298332","john", "John", "oslogate", "123", "oslo", "545", "34434");

        when(sjekk.loggetInn()).thenReturn(null);

        String resultat = bankController.endre(kunde);

        assertNull(resultat);
    }

    @Test
    public void EndreKundeInfo_Feilet(){
        Kunde kunde = new Kunde("982398298332","john", "John", "oslogate", "123", "oslo", "545", "34434");

        when(sjekk.loggetInn()).thenReturn("Innlogget");

        when(repository.endreKundeInfo(kunde)).thenReturn(null);

        String resultat = bankController.endre(kunde);

        assertNull(resultat);
    }
}

