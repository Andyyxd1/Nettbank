package oslomet.testing;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import oslomet.testing.DAL.BankRepository;
import oslomet.testing.Sikkerhet.Sikkerhet;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
// Klassen 'EnhetstestSikkerhetsController' inneholder tester for 'Sikkerhet'-komponenten.
public class EnhetstestSikkerhetsController {

    @InjectMocks
    // 'sikkerhetsKomponent' er den komponenten som skal testes.
    private Sikkerhet sikkerhet;

    @Mock
    // 'bankRepo' er en mock-versjon av 'BankRepository' som brukes for testing.
    private BankRepository bankRepo;

    @Mock
    // 'mockSession' er en mocket HttpSession som brukes for testing.
    private HttpSession session;

    @Before
    // Oppsettmetode kjøres før hver test for å konfigurere de mockete objektene.
    public void initSession() {
        // Et kart som lagrer attributtene som skal være tilgjengelige i den mockete sesjonen.
        Map<String, Object> attributes = new HashMap<>();

        // Konfigurerer oppførsel av getSession() for å hente attributter fra det lokale kartet.
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                // Henter nøkkelen fra metodekallet for å kunne returnere den korrekte attributten.
                String key = (String) invocation.getArguments()[0];
                return attributes.get(key);
            }
        }).when(session).getAttribute(anyString());

        // Konfigurerer oppførsel av setAttribute() for å lagre attributter i det lokale kartet.
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                // Henter nøkkel og verdi fra metodekallet for å lagre attributten i kartet.
                String key = (String) invocation.getArguments()[0];
                Object value = invocation.getArguments()[1];
                attributes.put(key, value);
                // Returnerer null fordi dette er standard oppførsel for setAttribute-metoden.
                return null;
            }
        }).when(session).setAttribute(anyString(), anyString());
        // Det er viktig å merke seg at det siste argumentet bør være any() for å fange opp alle typer objekter, ikke bare String.
        doAnswer(invocation -> {
            String key = (String) invocation.getArguments()[0];
            attributes.remove(key);
            return null;
        }).when(session).removeAttribute(anyString());

    }


    // Verifiserer innlogging
    @Test
    public void sjekkLoggInn() {
        when(bankRepo.sjekkLoggInn(anyString(), anyString())).thenReturn("OK");


        String verifikasjonResultat = sikkerhet.sjekkLoggInn("01234567890", "HeiHeiHei");
        assertEquals("OK", verifikasjonResultat);
    }

    // Verifiserer at feil personnummer gir riktig feilmelding
    @Test
    public void sjekkLoginFeilIPersonnummer() {
 lenient().when(bankRepo.sjekkLoggInn(anyString(), anyString())).thenReturn("Feil i personnummer");

        String resultat = sikkerhet.sjekkLoggInn("0123456789", "HeiHeiHei");
        assertEquals("Feil i personnummer", resultat);
    }

    // Bekrefter at feil passord gir korrekt feilmelding
    @Test
    public void sjekkLoginFeilIPassord() {
    lenient().when(bankRepo.sjekkLoggInn(anyString(), anyString())).thenReturn("Feil i passord");

        String resultat = sikkerhet.sjekkLoggInn("01234567890", "Hei");
        assertEquals("Feil i passord", resultat);
    }

    // Testtilfelle for å verifisere at innlogging feiler som forventet når det oppstår en feil i repositoriet.
    @Test
    public void sjekkLoginFeilerIRepo() {
        // Konfigurerer mock-objektet for bankRepositoriet til å returnere "Feil" for ethvert innloggingsforsøk.
        when(bankRepo.sjekkLoggInn(anyString(), anyString())).thenReturn("Feil");

        // Prøver å logge inn med et test personnummer og passord.
        String resultat = sikkerhet.sjekkLoggInn("01234567890", "HeiHeiHei");

        // Verifiserer at resultatet av innloggingsforsøket er den forventede feilmeldingen.
        // Denne meldingen bør vises når enten personnummeret eller passordet er feil.
        assertEquals("Feil i personnummer eller passord", resultat);
    }



    //Testtilfelle for å verifisere at en brukersesjon blir logget ut som forventet.
    @Test
    public void loggUt() {
        // Setter en attributt i sesjonen for å simulere at en bruker er logget inn.
        session.setAttribute("Innlogget", "01234567890");

        // Utfører utloggingsmetoden som skal fjerne "Innlogget"-attributten fra sesjonen.
        sikkerhet.loggUt();

        // Henter attributten "Innlogget" fra sesjonen etter utloggingen har blitt utført.
        Object resultat = session.getAttribute("Innlogget");

        // Verifiserer at "Innlogget"-attributten er fjernet fra sesjonen, forventer null.
        assertNull(resultat);
    }

    @Test
// Testmetoden for å verifisere at en administrator kan logge inn korrekt.
    public void loggInnAdmin() {
        // Setter en attributt i sesjonen for å simulere at en admin er logget inn.
        // Dette er nødvendig for å kunne teste logikk som avhenger av sesjonsattributter.
        session.setAttribute("Innlogget", "Admin");

        // Utfører handlingen for å logge inn som admin ved å kalle metoden 'loggInnAdmin' på 'sikkerhet'-objektet.
        // Metoden forventer å motta brukernavn og passord, her begge satt til "Admin" for testformål.
        String resultat = sikkerhet.loggInnAdmin("Admin", "Admin");

        // Verifiserer at resultatet av innloggingsforsøket er "Logget inn",
        // noe som indikerer at innloggingen var vellykket og at admin-brukeren nå er autentisert.
        assertEquals("Logget inn", resultat);
    }


    @Test
// Testmetoden for å verifisere at et feilaktig forsøk på å logge inn som admin håndteres riktig.
    public void loggInnAdminFeiler() {
        // Setter "Innlogget"-attributten til null for å simulere at ingen bruker er logget inn fra før.
        session.setAttribute("Innlogget", null);

        // Forsøker å logge inn som admin med feil brukernavn og passord for å teste feilhåndtering.
        String resultat = sikkerhet.loggInnAdmin("bla", "bla");

        // Kontrollerer at metoden returnerer "Ikke logget inn" som bekreftelse på at innloggingen feilet.
        assertEquals("Ikke logget inn", resultat);
    }

    @Test
//Testmetoden for å verifisere at metoden 'loggInn()' fungerer som forventet.
    public void loggetInn() {
        // Arrange: Forbereder testen ved å simulere at en bruker allerede er logget inn.
        session.setAttribute("Innlogget", "12345678901");

        // Act: Utfører selve handlingen som skal testes, i dette tilfellet å hente den logget inn brukerens id.
        String resultat = sikkerhet.loggetInn();

        // Assert: Bekrefter at metoden returnerer korrekt brukerid som indikerer at brukeren allerede er logget inn.
        assertEquals("12345678901", resultat);
    }


    @Test
    public void loggetInnFeilerlogging(){

        session.setAttribute("innlogget",null);
        String resultat= sikkerhet.loggetInn();
        assertNull(resultat);
    }















}

