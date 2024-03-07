package oslomet.testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import oslomet.testing.API.AdminKontoController;

import oslomet.testing.API.AdminKundeController;
import oslomet.testing.DAL.AdminRepository;
import oslomet.testing.Models.Kunde;
import oslomet.testing.Sikkerhet.Sikkerhet;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class EnhetstestAdminKundeController {


    @InjectMocks
    private AdminKundeController adminKundeController;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private Sikkerhet sjekk;

    @Test
    public void lagreKunde_LoggetInn(){

        Kunde kunde1= new Kunde("01010110523", "Lene", "Jensen", "Askerveien 22",
                "3270", "Asker","22224444", "HeiHei");

        when(sjekk.loggetInn()).thenReturn("01010110523");

        when(adminRepository.registrerKunde(any(Kunde.class))).thenReturn("ok");


        String res= adminKundeController.lagreKunde(kunde1);
        assertEquals("ok",res);


    }


    @Test
    public void lagreKunde_LoggetInnFeil(){
        // Oppretter en ny instans av Kunde-objektet med testdata
        Kunde enKunde = new Kunde("01010110523", "Lene", "Jensen",
                "Askerveien 22", "3270", "Asker", "22224444", "HeiHei");

        // Setter opp mock-objektet 'sjekk' til å returnere et spesifikt personnummer når 'loggetInn' kalles
        // Dette simulerer at ingen bruker er logget inn
        when(sjekk.loggetInn()).thenReturn("01010110523");


        // Dette simulerer en feil i registreringsprosessen
        when(adminRepository.registrerKunde(any(Kunde.class))).thenReturn("Feil");

        // Kaller metoden 'lagreKunde' på 'adminKundeController' med 'enKunde'-objektet
        // Forventer å få "Feil" tilbake siden 'registrerKunde' i 'adminRepository' er stubbet til å returnere "Feil"
        String resultat = adminKundeController.lagreKunde(enKunde);


        // Dette bekrefter at når 'registrerKunde' feiler, vil 'lagreKunde' også indikere feilen korrekt
        assertEquals("Feil", resultat);
    }

    @Test
    public void lagreKunde_IkkeLoggetInn() {
        // Oppretter en ny kunde med testdata.
        Kunde enKunde = new Kunde("01010110523", "Lene",
                "Jensen", "Askerveien 22", "3270",
                "Asker", "22224444", "HeiHei");

        // Setter opp mocking for sjekk.loggetInn() til å returnere null,
        // som simulerer at ingen bruker er logget inn.
        when(sjekk.loggetInn()).thenReturn(null);

        // Kaller lagreKunde-metoden på adminKundeController med den opprettede kunden,
        // og lagrer resultatet for å verifisere det senere.
        String resultat = adminKundeController.lagreKunde(enKunde);

        // returnerer forventet feilmelding.
        assertEquals("Ikke logget inn", resultat);
    }
    @Test
    public void hentAlle_LoggetInn(){
        // Opprett to kundeobjekter med testdata.
        Kunde kundeen = new Kunde("01010110523", "Lene", "Jensen", "Askerveien 22", "3270", "Asker", "22224444", "HeiHei");
        Kunde kundeto = new Kunde("01010110524", "Ole", "Hansen", "Osloveien 22", "0970", "Oslo", "89675432", "HeiHallo");

        // Initialiserer en liste for å holde på kundeobjekter.
        List<Kunde> kundeListe = new ArrayList<>();
        kundeListe.add(kundeen); // Legger til første kunde i listen.
        kundeListe.add(kundeto); // Legger til andre kunde i listen.

        // Konfigurerer mock-objektet til å returnere en spesifikk brukers ID når sjekk.loggetInn() kalles.
        when(sjekk.loggetInn()).thenReturn("01010110523");

        // Konfigurerer mock-objektet til å returnere vår forhåndsdefinerte liste av kunder når adminRepository.hentAlleKunder() kalles.
        when(adminRepository.hentAlleKunder()).thenReturn(kundeListe);

        // Utfører handlingen som skal testes, i dette tilfellet å hente alle kunder.
        List<Kunde> resultat = adminKundeController.hentAlle();

        // Verifiserer at resultatet fra adminKundeController.hentAlle() er lik den forhåndsdefinerte listen av kunder.
        assertEquals(resultat, kundeListe);
    }

    @Test
    public void hentAlle_LoggetInnFeil(){
        // Konfigurerer mock-objektet 'sjekk' til å returnere en spesifikk brukers ID når metoden loggetInn() kalles.
        when(sjekk.loggetInn()).thenReturn("01010110523");

        // Konfigurerer mock-objektet 'adminRepository' til å returnere null når metoden hentAlleKunder() kalles.
        when(adminRepository.hentAlleKunder()).thenReturn(null);

        // Utfører metoden hentAlle() for å få en liste over alle kunder.
        List<Kunde> resultat = adminKundeController.hentAlle();

        // Verifiserer at resultatet fra metoden hentAlle() er null.
        assertNull(resultat);
    }
    @Test
    public void hentAlle_IkkeLoggetInn(){
        // Setter opp mock slik at 'loggetInn()' returnerer null, som simulerer at ingen bruker er logget inn.
        when(sjekk.loggetInn()).thenReturn(null);

        // Utfører metoden 'hentAlle()' fra 'adminKundeController' for å få en liste over alle kunder.
        List<Kunde> resultat = adminKundeController.hentAlle();

        // Sjekker at 'resultat' er null, som forventes når ingen bruker er logget inn.
        assertNull(resultat);
    }

    @Test
    public void endre_LoggetInn(){
        // Oppretter en ny Kunde-instans med testdata.
        Kunde enKunde = new Kunde("01010110523", "Lene", "Jensen", "Askerveien 22", "3270", "Asker", "22224444", "HeiHei");

        // Setter opp mock slik at 'loggetInn()' returnerer en spesifikk brukers ID for å simulere en innlogging.
        when(sjekk.loggetInn()).thenReturn("01010110523");

        // Setter opp mock slik at 'endreKundeInfo' returnerer "ok" når som helst et Kunde-objekt blir sendt inn.
        when(adminRepository.endreKundeInfo(any(Kunde.class))).thenReturn("ok");

        // Utfører metoden 'endre' med enKunde som parameter.
        String resultat = adminKundeController.endre(enKunde);

        // Sjekker at resultatet av å endre kundeinformasjon er "ok".
        assertEquals("ok", resultat);
    }

    @Test
    public void endre_LoggetInnFeil(){
        // Oppretter et nytt Kunde-objekt for test.
        Kunde enKunde = new Kunde("01010110523", "Lene", "Jensen", "Askerveien 22", "3270", "Asker", "22224444", "HeiHei");

        // Setter opp mock-objektet 'sjekk' til å returnere en spesifikk brukers ID som logget inn bruker.
        when(sjekk.loggetInn()).thenReturn("01010110524");

        // Setter opp mock-objektet 'adminRepository' til å returnere "Feil" når 'endreKundeInfo()' kalles med hvilken som helst Kunde.
        when(adminRepository.endreKundeInfo(any(Kunde.class))).thenReturn("Feil");

        // Utfører 'endre()' metoden med 'enKunde' objektet og lagrer resultatet i en String-variabel.
        String resultat = adminKundeController.endre(enKunde);

        // Verifiserer at resultatet av 'endre()' metoden er "Feil".
        assertEquals("Feil", resultat);
    }
    @Test
    public void endre_IkkeLoggetInn(){
        // Oppretter et nytt Kunde-objekt for testing.
        Kunde enKunde = new Kunde("01010110523", "Lene", "Jensen", "Askerveien 22", "3270", "Asker", "22224444", "HeiHei");

        // Simulerer at ingen bruker er logget inn ved å sette opp mock til å returnere null.
        when(sjekk.loggetInn()).thenReturn(null);

        // Utfører endre-operasjonen, noe som bør mislykkes siden ingen bruker er logget inn.
        String resultat = adminKundeController.endre(enKunde);

        // Verifiserer at forsøket på å endre kundeinformasjonen feiler på grunn av ikke innlogging.
        assertEquals("Ikke logget inn", resultat);
    }
    @Test
    public void slett_LoggetInn(){
        // Oppretter en ny Kunde-instans for testen.
        Kunde enKunde = new Kunde("01010110523", "Lene", "Jensen", "Askerveien 22", "3270", "Asker", "22224444", "HeiHei");

        // Konfigurerer mock til å simulere at en bruker med ID "01010110523" er logget inn.
        when(sjekk.loggetInn()).thenReturn("01010110523");

        // Konfigurerer mock slik at kallet til slettKunde metoden i adminRepository returnerer "ok".
        when(adminRepository.slettKunde(anyString())).thenReturn("ok");

        // Utfører slett operasjonen med kunde ID og lagrer resultatet i variabelen 'resultat'.
        String resultat = adminKundeController.slett("01010110523");

        // Sjekker at resultatet av slett operasjonen returnerer strengen "ok".
        assertEquals("ok", resultat);
    }

    @Test
    public void slett_LoggetInnFeil(){
        // Oppretter en ny Kunde-instans for å bruke i testen.
        Kunde enKunde = new Kunde("01010110523", "Lene", "Jensen", "Askerveien 22", "3270", "Asker", "22224444", "HeiHei");

        // Simulerer at en bruker med ID "01010110524" er logget inn.
        when(sjekk.loggetInn()).thenReturn("01010110524");

        // Simulerer en feil ved sletting av kunde ved å returnere "Feil" når slettKunde-metoden kalles på adminRepository.
        when(adminRepository.slettKunde(anyString())).thenReturn("Feil");

        // Utfører slett-operasjonen og lagrer resultatet i en variabel.
        String resultat = adminKundeController.slett("01010110523");

        // Sjekker at resultatet av slett-operasjonen er "Feil".
        assertEquals("Feil", resultat);
    }

    @Test
    public void slett_IkkeLoggetInn(){
        // Oppretter en ny Kunde-instans for å bruke i testen.
        Kunde enKunde = new Kunde("01010110523", "Lene", "Jensen", "Askerveien 22", "3270", "Asker", "22224444", "HeiHei");

        // Konfigurerer mock slik at sjekk.loggetInn() returnerer null, noe som indikerer at ingen bruker er logget inn.
        when(sjekk.loggetInn()).thenReturn(null);

        // Prøver å utføre slett-operasjonen mens ingen bruker er logget inn.
        String resultat = adminKundeController.slett("01010110523");

        // Sjekker at metoden returnerer meldingen "Ikke logget inn" når ingen er logget inn.
        assertEquals("Ikke logget inn", resultat);
    }






}





