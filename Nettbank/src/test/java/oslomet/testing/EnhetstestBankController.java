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

    // Tester henting av transaksjoner når brukeren ikke er logget inn.
    @Test
    public void hentTransaksjoner_IkkeLoggetInn() {
        // Simulerer at brukeren ikke er logget inn
        when(sjekk.loggetInn()).thenReturn(null);
        // Simulerer henting av transaksjoner med spesifikk informasjon
        when(bankController.hentTransaksjoner("32878738732", "28.01-2024", "30.01.2024")).thenReturn(null);

        // Utfører henting av transaksjoner og forventer null som resultat
        Konto resultat = bankController.hentTransaksjoner("32878738732", "28.01-2024", "30.01.2024");

        assertNull(resultat);
    }

    // Tester henting av kundeinformasjon når brukeren er logget inn.
    @Test
    public void hentKundeInfo_loggetInn() {
        // Opprett en kunde for testing
        Kunde enKunde = new Kunde("01010110523", "Lene", "Jensen", "Askerveien 22", "3270", "Asker", "22224444", "HeiHei");

        // Simulerer at brukeren er logget inn med en spesifikk ID
        when(sjekk.loggetInn()).thenReturn("01010110523");

        // Simulerer henting av kundeinformasjon fra databasen
        when(repository.hentKundeInfo(anyString())).thenReturn(enKunde);

        // Utfører henting av kundeinformasjon
        Kunde resultat = bankController.hentKundeInfo();

        // Sjekker om den returnerte kundeinformasjonen stemmer overens med den forventede kundeinformasjonen
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

    // Tester henting av saldoer når brukeren ikke er logget inn.
    @Test
    public void hentSaldi_IkkeLoggetInn(){
        // Simulerer at brukeren ikke er logget inn
        when(sjekk.loggetInn()).thenReturn(null);

        // Utfører henting av saldoer og forventer null som resultat
        List<Konto> resulstat = bankController.hentSaldi();

        assertNull(resulstat);
    }

    // Tester henting av saldoer når det oppstår feil.
    @Test
    public void hentSaldi_Feilet(){
        // Simulerer at brukeren er logget inn
        when(sjekk.loggetInn()).thenReturn("Innlogget");

        // Simulerer at det oppstår en feil under henting av saldoer
        when(repository.hentSaldi(anyString())).thenReturn(null);

        // Utfører henting av saldoer og forventer null som resultat
        List<Konto> resulstat = bankController.hentSaldi();

        assertNull(resulstat);
    }


    // Tester registrering av betaling når brukeren er logget inn.
    @Test
    public void RegistrerBetaling_Loggetinn() {
        // Opprett en transaksjon for testformål
        Transaksjon transaksjon1 = new Transaksjon(1, "23233232", 332332, "21-02-2030", "Betaling for varer", "105010123456", "23");

        // Simuler at brukeren er logget inn
        when(sjekk.loggetInn()).thenReturn("Innlogget");

        // Simulerer vellykket registrering av betaling
        when(repository.registrerBetaling(transaksjon1)).thenReturn("OK");

        // Utfører registrering av betaling og sammenligner med forventet resultat
        String resultat = bankController.registrerBetaling(transaksjon1);

        assertEquals("OK",resultat);
    }

    // Tester registrering av betaling når brukeren ikke er logget inn.
    @Test
    public void RegistrerBetaling_IkkeLoggetInn(){
        // Opprett en transaksjon for testformål
        Transaksjon transaksjon1 = new Transaksjon(1, "23233232", 332332, "21-02-2030", "Betaling for varer", "105010123456", "23");

        // Simuler at brukeren ikke er logget inn
        when(sjekk.loggetInn()).thenReturn(null);

        // Utfører registrering av betaling og forventer null som resultat
        String resultat = bankController.registrerBetaling(transaksjon1);

        assertNull(resultat);
    }

    // Tester henting av betalinger når brukeren er logget inn.
    @Test
    public void HentBetalinger_LoggetInn() {
        // Opprett en transaksjon for testformål
        Transaksjon transaksjon1 = new Transaksjon(1, "23233232", 332332, "21-02-2030", "Betaling for varer", "105010123456", "23");

        // Opprett en liste med transaksjoner for testformål
        List<Transaksjon> transaksjoner = new ArrayList<>();
        transaksjoner.add(transaksjon1);

        // Simuler at brukeren er logget inn
        when(sjekk.loggetInn()).thenReturn("Innlogget");

        // Simulerer henting av betalinger fra repository
        when(repository.hentBetalinger(anyString())).thenReturn(transaksjoner);

        // Utfører henting av betalinger og sammenligner med forventet resultat
        List<Transaksjon> resultat = bankController.hentBetalinger();

        assertEquals(transaksjoner, resultat);
    }

    // Tester henting av betalinger når brukeren ikke er logget inn.
    @Test
    public void HentBetalinger_IkkeLoggetInn(){
        // Simuler at brukeren ikke er logget inn
        when(sjekk.loggetInn()).thenReturn(null);

        // Utfører henting av betalinger og forventer null som resultat
        List<Transaksjon> resultat = bankController.hentBetalinger();

        assertNull(resultat);
    }

    // Tester feilhåndtering ved henting av betalinger når brukeren er logget inn.
    @Test
    public void HentBetalinger_Feilet(){
        // Simulerer at brukeren er logget inn
        when(sjekk.loggetInn()).thenReturn("Innlogget");

        // Simulerer at henting av betalinger fra repository feiler (returnerer null)
        when(repository.hentBetalinger(anyString())).thenReturn(null);

        // Utfører henting av betalinger og forventer null som resultat
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

    // Tester utføring av betaling når brukeren er logget inn, men transaksjonen ikke har en gyldig ID.
    @Test
    public void UtforBetaling_LoggetInn_IkkeOKtxID(){
        // Oppretter en transaksjon
        Transaksjon transaksjon1 = new Transaksjon(1, "23233232", 332332, "21-02-2030", "Betaling for varer", "105010123456", "23");

        // Oppretter en liste med transaksjoner og legger til den opprettede transaksjonen
        List<Transaksjon> transaksjon = new ArrayList<>();
        transaksjon.add(transaksjon1);

        // Simulerer at brukeren er logget inn
        when(sjekk.loggetInn()).thenReturn("Innlogget");

        // Simulerer at utføringen av betalingen feiler (returnerer "Feil")
        when(repository.utforBetaling(transaksjon1.getTxID())).thenReturn("Feil");

        // Utfører betalingen og forventer null som resultat
        List<Transaksjon> resultat = bankController.utforBetaling(transaksjon1.getTxID());

        assertNull(resultat);
    }

    // Tester utføring av betaling når brukeren ikke er logget inn.
    @Test
    public void UtforBetaling_IkkeLoggetInn(){
        // Simulerer at brukeren ikke er logget inn
        when(sjekk.loggetInn()).thenReturn(null);

        // Utfører betalingen og forventer null som resultat
        List<Transaksjon> resultat = bankController.utforBetaling(1);

        assertNull(resultat);
    }

    @Test
    public void HentKundeInfo_LoggetInn() {
        // Oppretter dummydata for testen
        Kunde kunde = new Kunde("982398298332","john", "John", "oslogate", "123", "oslo", "545", "34434");
        // Simulerer at brukeren er logget inn
        when(sjekk.loggetInn()).thenReturn("982398298332");
        // Simulerer henting av kundeinformasjon fra repository
        when(repository.hentKundeInfo(anyString())).thenReturn(kunde);
        // Henter kundeinformasjon via bankController
        Kunde resultat = bankController.hentKundeInfo();
        // Sjekker om den returnerte kundeinformasjonen er som forventet
        assertEquals(kunde,resultat);
    }

    @Test
    public void HentKundeInfo_IkkeLoggetInn(){
        // Simulerer at brukeren ikke er logget inn
        when(sjekk.loggetInn()).thenReturn(null);
        // Henter kundeinformasjon via bankController
        Kunde resultat = bankController.hentKundeInfo();
        // Sjekker om resultatet er null når brukeren ikke er logget inn
        assertNull(resultat);
    }

    @Test
    public void EndreKundeInfo_LoggetInn() {
        // Oppretter dummykunde for testen
        Kunde kunde = new Kunde("982398298332","john", "John", "oslogate", "123", "oslo", "545", "34434");
        // Simulerer at brukeren er logget inn
        when(sjekk.loggetInn()).thenReturn("Innlogget");
        // Simulerer endring av kundeinformasjon i repository
        when(repository.endreKundeInfo(kunde)).thenReturn("OK");
        // Utfører endring av kundeinformasjon via bankController
        String resultat = bankController.endre(kunde);
        // Sjekker om endringen var vellykket
        assertEquals("OK",resultat);
    }

    @Test
    public void EndreKundeInfo_IkkeLoggetInn(){
        // Oppretter dummykunde for testen
        Kunde kunde = new Kunde("982398298332","john", "John", "oslogate", "123", "oslo", "545", "34434");
        // Simulerer at brukeren ikke er logget inn
        when(sjekk.loggetInn()).thenReturn(null);
        // Utfører endring av kundeinformasjon via bankController
        String resultat = bankController.endre(kunde);
        // Sjekker at resultatet er null når brukeren ikke er logget inn
        assertNull(resultat);
    }

    @Test
    public void EndreKundeInfo_Feilet(){
        // Oppretter dummykunde for testen
        Kunde kunde = new Kunde("982398298332","john", "John", "oslogate", "123", "oslo", "545", "34434");
        // Simulerer at brukeren er logget inn
        when(sjekk.loggetInn()).thenReturn("Innlogget");
        // Simulerer feil under endring av kundeinformasjon i repository
        when(repository.endreKundeInfo(kunde)).thenReturn(null);
        // Utfører endring av kundeinformasjon via bankController
        String resultat = bankController.endre(kunde);
        // Sjekker at resultatet er null ved feil under endring
        assertNull(resultat);
    }

}

