package oslomet.testing;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import oslomet.testing.API.AdminKontoController;

import oslomet.testing.DAL.AdminRepository;

import oslomet.testing.Models.Konto;


import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class EnhetstestAdminKontoController {

    @InjectMocks
    private AdminKontoController adminKontoController;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private Sikkerhet sjekk;

    // Tester om henting av alle kontoer fungerer som forventet når brukeren er innlogget.
    @Test
    public void hentAlleKonti() {
        // Opprett to kontoobjekter
        Konto konto = new Konto("010109292", "1982928323", 720, "lønnskonto", "NOK", null);
        Konto konto1 = new Konto("012122192", "1221223", 122, "lønnskonto", "NOK", null);

        // Legg kontoobjektene til i en liste
        List<Konto> kontoListe = new ArrayList<>();
        kontoListe.add(konto);
        kontoListe.add(konto1);

        // Simulerer at brukeren er innlogget
        when(sjekk.loggetInn()).thenReturn("Innloggettt");

        // Simulerer henting av kontoer fra adminRepository
        when(adminRepository.hentAlleKonti()).thenReturn(kontoListe);

        // Kall metoden for å hente alle kontoer
        List<Konto> resultat = adminKontoController.hentAlleKonti();

        // Sjekk om resultatet er lik den forventede konto listen
        assertEquals(resultat, kontoListe);
    }


    // Tester om henting av alle kontoer mislykkes når brukeren ikke er innlogget.
    @Test
    public void hentAlleKonti_IkkeLoggetInn() {
        // Simulerer at brukeren ikke er innlogget
        when(sjekk.loggetInn()).thenReturn(null);

        // Henter kontoer og forventer at resultatet er null
        List<Konto> resultat = adminKontoController.hentAlleKonti();

        assertNull(resultat);
    }

    // Tester om registrering av konto fungerer som forventet når brukeren er innlogget.
    @Test
    public void registrerKonto() {
        // Opprett et kontoobjekt
        Konto kontoen =  new Konto("0101292", "1982928323", 720, "lønnskonto", "NOK", null);

        // Simulerer at brukeren er innlogget
        when(sjekk.loggetInn()).thenReturn("innlogget");

        // Simulerer vellykket registrering av konto
        when(adminRepository.registrerKonto(kontoen)).thenReturn("ok");

        // Registrerer konto og sjekker om resultatet er "ok"
        String resultat = adminKontoController.registrerKonto(kontoen);

        assertEquals("ok", resultat);
    }

    // Tester at registrering av konto mislykkes når brukeren ikke er innlogget.
    @Test
    public void registrertKonto_IkkeLoggetInn() {
        // Opprett et kontoobjekt
        Konto kontoen =  new Konto("0101292", "1982928323", 720, "lønnskonto", "NOK", null);

        // Simulerer at brukeren ikke er innlogget
        when(sjekk.loggetInn()).thenReturn(null);

        // Prøver å registrere konto og forventer "Ikke innlogget" som resultat
        String resultat = adminKontoController.registrerKonto(kontoen);

        assertEquals("Ikke innlogget", resultat);
    }

    // Tester at registrering av konto mislykkes når kontoobjektet er null.
    @Test
    public void registrerKonto_feil() {
        // Opprett et kontoobjekt
        Konto kontoen =  new Konto("0101292", "1982928323", 720, "lønnskonto", "NOK", null);

        // Simulerer at brukeren er innlogget
        when(sjekk.loggetInn()).thenReturn("innlogget");

        // Simulerer at registrering av null kontoobjekt feiler
        when(adminRepository.registrerKonto(null)).thenReturn("feil");

        // Prøver å registrere konto med null objekt og forventer "feil" som resultat
        String resultat = adminKontoController.registrerKonto(null);

        assertEquals("feil", resultat);
    }

    // Tester endring av konto og forventer vellykket resultat.
    @Test
    public void endreKonto() {
        // Opprett et kontoobjekt
        Konto kontoen =  new Konto("0101292", "1982928323", 720, "lønnskonto", "NOK", null);

        // Simulerer at brukeren er innlogget
        when(sjekk.loggetInn()).thenReturn("innlogget");

        // Simulerer vellykket endring av konto
        when(adminRepository.endreKonto(kontoen)).thenReturn("ok");

        // Utfører endring av konto og forventer "ok" som resultat
        String resultat = adminKontoController.endreKonto(kontoen);

        assertEquals("ok", resultat);
    }

    // Tester generell feil ved endring av konto.
    @Test
    public void endreKontoFeiletGenerlt() {
        // Opprett et kontoobjekt
        Konto kontoen =  new Konto("0101292", "1982928323", 720, "lønnskonto", "NOK", null);

        // Simulerer at brukeren er innlogget
        when(sjekk.loggetInn()).thenReturn("innlogget");

        // Simulerer generell feil ved endring av konto
        when(adminRepository.endreKonto(null)).thenReturn("feil");

        // Utfører endring av konto med null objekt og forventer "feil" som resultat
        String resultat = adminKontoController.endreKonto(null);

        assertEquals("feil", resultat);
    }

    // Tester feil i personnummer ved endring av konto.
    @Test
    public void endreKontoFeiletPersonnummer() {
        // Opprett et kontoobjekt
        Konto kontoTo = new Konto("0101292", "1982928323", 720, "lønnskonto", "NOK", null);

        // Simulerer at brukeren er innlogget
        when(sjekk.loggetInn()).thenReturn("innlogget");

        // Simulerer feil i personnummer ved endring av konto
        when(adminRepository.endreKonto(kontoTo)).thenReturn("feil i personnummer");

        // Utfører endring av konto og forventer "feil i personnummer" som resultat
        String resultat = adminKontoController.endreKonto(kontoTo);

        assertEquals("feil i personnummer", resultat);
    }

    // Tester feil i kontonummer ved endring av konto.
    @Test
    public void endreKontoFeiletKontonummer() {
        // Opprett et kontoobjekt
        Konto kontoTo = new Konto("0101292", "1982928323", 720, "lønnskonto", "NOK", null);

        // Simulerer at brukeren er innlogget
        when(sjekk.loggetInn()).thenReturn("innlogget");

        // Simulerer feil i kontonummer ved endring av konto
        when(adminRepository.endreKonto(kontoTo)).thenReturn("feil i kontonummer");

        // Utfører endring av konto og forventer "feil i kontonummer" som resultat
        String resultat = adminKontoController.endreKonto(kontoTo);

        assertEquals("feil i kontonummer", resultat);
    }

    // Tester endring av konto når brukeren ikke er innlogget.
    @Test
    public void endreKontoIkkeLoggetInn() {
        // Opprett et kontoobjekt
        Konto kontoen = new Konto("0101292", "1982928323", 720, "lønnskonto", "NOK", null);

        // Simulerer at brukeren ikke er innlogget
        when(sjekk.loggetInn()).thenReturn(null);

        // Utfører endring av konto og forventer "Ikke innlogget" som resultat
        String resultat = adminKontoController.endreKonto(kontoen);

        assertEquals("Ikke innlogget", resultat);
    }

    // Tester sletting av konto.
    @Test
    public void slettKonto() {
        // Simulerer at brukeren er innlogget med en bestemt ID
        when(sjekk.loggetInn()).thenReturn("7848773443");

        // Simulerer vellykket sletting av konto med en gitt kontonummer
        when(adminRepository.slettKonto(anyString())).thenReturn("ok");

        // Utfører sletting av konto og forventer "ok" som resultat
        String resultat = adminKontoController.slettKonto("7848773443");

        assertEquals("ok", resultat);
    }

    // Tester feilet sletting av konto på grunn av feil kontonummer.
    @Test
    public void slettKontoFeilet() {
        // Opprett et kontoobjekt
        Konto kontoen = new Konto("0101292", "1982928323", 720, "lønnskonto", "NOK", null);

        // Simulerer at brukeren er innlogget med en bestemt ID
        when(sjekk.loggetInn()).thenReturn("7848773443");

        // Simulerer feilet sletting av konto med en gitt kontonummer
        when(adminRepository.slettKonto(anyString())).thenReturn("Feil kontonummer");

        // Utfører sletting av konto og forventer "Feil kontonummer" som resultat
        String resultat = adminKontoController.slettKonto("7848773443");

        assertEquals("Feil kontonummer", resultat);
    }

    // Tester sletting av konto når brukeren ikke er innlogget.
    @Test
    public void slettKontoIkkeLoggetInn() {
        // Opprett et kontoobjekt
        Konto kontoen = new Konto("0101292", "1982928323", 720, "lønnskonto", "NOK", null);

        // Simulerer at brukeren ikke er innlogget
        when(sjekk.loggetInn()).thenReturn(null);

        // Utfører sletting av konto og forventer "Ikke innlogget" som resultat
        String resultat = adminKontoController.slettKonto("7848773443");

        assertEquals("Ikke innlogget", resultat);
    }

}
