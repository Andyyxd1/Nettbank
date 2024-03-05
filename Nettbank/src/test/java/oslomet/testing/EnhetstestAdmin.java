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
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnhetstestAdmin {
    @InjectMocks
    //Denne skal testes
    private AdminKontoController adminKontoController;


    // Denne skal mockes
    @Mock
    private AdminRepository adminRepository;

    @Mock
    private Sikkerhet sjekk;

    @Test
    public void hentAlleKonti(){
        Konto konto1= new Konto("105010123456", "01010110523", 720, "Lønnskonto",
                "NOK", null);

        Konto konto2= new Konto("105020123456", "01010110523",
                100500, "Sparekonto", "NOK",null);

        Konto konto3= new Konto("22334412345", "01010110523", 10234.5,
                "Brukskonto", "NOK",null);

        //Lager ArrayList av Konto

        List<Konto> kontoListe = new ArrayList<>();
        kontoListe.add(konto1);
        kontoListe.add(konto2);
        kontoListe.add(konto3);

        when(sjekk.loggetInn()).thenReturn("Innlogget");
        when(adminRepository.hentAlleKonti()).thenReturn(kontoListe);

        List<Konto> resultat= adminKontoController.hentAlleKonti();
        assertEquals(resultat, kontoListe);

    }

    @Test
    public void hentAlleKonti_ikke(){

        when(sjekk.loggetInn()).thenReturn(null);
        List<Konto> resultat= adminKontoController.hentAlleKonti();
        assertNull(resultat);
    }
    //Legge inn kono
    @Test
    public void registrerKonto() {
        Konto nyKonto = new Konto("20032023456", "2334", 300,
                "Lønnkonto", "NOK", null);

        String resultat = adminKontoController.registrerKonto(nyKonto);
        assertEquals("Ikke innlogget", resultat);
    }

    @Test
    public void regisrerKonto_feil() {
        String resultat = adminKontoController.registrerKonto(null);
        assertEquals("Ikke innlogget", resultat);
    }

    // her Kommer logget inn går igjennom

    @Test
    public void endreKonto(){
        Konto enKonto= new Konto("20032023456","2334",300,
                "Lønnkonto","NOK",null);

        when(sjekk.loggetInn()).thenReturn("innlogget");

        when(adminRepository.endreKonto(enKonto)).thenReturn("OK");

        String resultat= adminKontoController.endreKonto(enKonto);
        assertEquals("OK", resultat);
    }

    @Test
    public void endreKontoFeilt(){
        Konto enKonto = new  Konto("20032023456","2334",300,
                "Lønnkonto","NOK",null);

        when(sjekk.loggetInn()).thenReturn("innlogget");

        when(adminRepository.endreKonto(null)).thenReturn("Feil");

        String resultat = adminKontoController.endreKonto(null);

        assertEquals("Feil", resultat);
    }


    //Logget inn, men går ikke gjennom pga feil personnummer

    @Test
    public void endreKontoFeilPersonnummer(){
        Konto toKonto= new Konto("0","105010123456",720,
                  "Lønnskonto","Nok",null);

        when(sjekk.loggetInn()).thenReturn("innlogget");

        when(adminRepository.endreKonto(toKonto)).
                thenReturn("Feil i personnummer");

        String resultat = adminKontoController.endreKonto(toKonto);

        assertEquals("Feil i personnummer", resultat);

    }
    /* Testmetoden sjekker om funksjonalitet for å endre kontoinformasjon,
    * Men det forventer at endringer feiler på grunn av feil kontonummer*/
 //Hassan
    @Test
    public void endreKontoFeiletKontonummer(){
        /*Oppretter en ny konto, med testdata
        * Her simuleres en situasjon hvor kontonummer er 0.  */
        Konto toKonto= new Konto("01010110423","0",
                720, "LønnsKonto","NOK",null);

        /* Når 'logget' metoden på "sjekk" kalles, returnere den "innloget".
        * Dette simulrer en situason der brukeren er logget inn  */
        when(sjekk.loggetInn()).thenReturn("innlogget");

        /* Når "endreKonto" metoden på adminRepository  med toKonto som argument blir kalt,
        * skal den returnerne "Feil i kontonummer".Dette simulerer en feil
        * ved endrinb av kontonummer */
        when(adminRepository.endreKonto(toKonto)).thenReturn("Feil i kontonummer");

        /*Utfører handlingen som testes: forsøk på endre konto med det opprettede konto
        * og lagrer resultatet */
        String resultat = adminKontoController.endreKonto(toKonto);
        /*Sjekker om at resultatet av å endre kontoen returner den forventede
        feilMeldingen "Feil i kontonummer"*/
        assertEquals("Feil i kontonummer",resultat);
    }

   //Hassan
   /* Denne testmetoden sjekker at forsøk på å endre kontooplysninger
   uten å være logget inn, returnerer riktig feilmelding */
   @Test
   public void endreKontoIkkeLoggetInn() {
       Konto enKonto = new Konto("765432216", "105010123456", 720,
               "LønnsKonto", "NOK", null);

       String resultat = adminKontoController.endreKonto(enKonto);
       assertEquals("Ikke innlogget", resultat);
   }

   //Hassan
    // sjekker om at en konto kan slettes suksessfullt
   @Test
   public void slettKonto(){
       /* Simulerer at brukeren er logget inn
       ved returnere et gyldig personnummer */
       when(sjekk.loggetInn()).thenReturn("105010123456");

       /*Når "slettKonto" metoden på "adminRepository" blir kalt med
       * hvilken som helst streng som argument, simulerer vi en vellykket
       * sletting ved å retunrer "OK"*/
       when(adminRepository.slettKonto(anyString())).thenReturn("OK");
       /*Utfører handlingen som testes:
       sletting av konto med spesifikt kontonummer, og lagrer resultatet*/
       String resultat = adminKontoController.slettKonto("105010123456");
       // sjekker om at sletting av konto returnerer den forventede responsen "OK".
       assertEquals("OK",resultat);
   }

   //Hassan

   @Test
   public void slettKontoFeilet() {
       // Oppretter en konto.
       Konto enKonto = new Konto("01010110523","105010123456",720,
                      "Lønnskonto","NOK", null);
       // Simulerer at brukeren er logget inn ved å returnere et gyldig personnummer.
       when(sjekk.loggetInn()).thenReturn("105010123456");
       /* Når 'slettKonto' metoden på 'adminRepository' blir kalt med hvilken som helst streng som argument,
          simulerer vi en feil ved å returnere "Feil kontonummer".   */
       when(adminRepository.slettKonto(anyString())).thenReturn("Feil kontonummer");
       // Handlingen som testes: forsøk på sletting av konto og lagrer resultatet.
       String resultat = adminKontoController.slettKonto("105010123456");

       /* Forsøket på sletting av konto med Feil kontonummer og
       returner den forvented feilmeldingen "feil Kontonummer" */
       assertEquals("Feil kontonummer",resultat);

   }







}
