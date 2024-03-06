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
import oslomet.testing.Models.Transaksjon;
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

    @Test
    public void hentAlleKonti(){
        Konto konto = new Konto("010109292","1982928323",720,"lønnskonto", "NOK",null);
        Konto konto1 = new Konto("012122192","1221223",122,"lønnskonto", "NOK",null);

        List<Konto> kontoListe = new ArrayList<>();
        kontoListe.add(konto);
        kontoListe.add(konto1);
        when(sjekk.loggetInn()).thenReturn("Innloggettt");

        when(adminRepository.hentAlleKonti()).thenReturn(kontoListe);

        List<Konto> resultat = adminKontoController.hentAlleKonti();

        assertEquals(resultat,kontoListe);

    }

    @Test
    public void hentAlleKonti_IkkeLoggetInn(){
        when(sjekk.loggetInn()).thenReturn(null);
        List<Konto> resultat = adminKontoController.hentAlleKonti();

        assertNull(resultat);
    }
    @Test
    public void registrerKonto(){
        Konto kontoen =  new Konto("0101292","1982928323",720,"lønnskonto", "NOK",null);
        when(sjekk.loggetInn()).thenReturn("innlogget");

        when(adminRepository.registrerKonto(kontoen)).thenReturn("ok");

        String resultat = adminKontoController.registrerKonto(kontoen);

        assertEquals("ok", resultat);
}
    @Test
    public void registrertKonto_IkkeLoggetInn(){
        Konto kontoen =  new Konto("0101292","1982928323",720,"lønnskonto", "NOK",null);

        when(sjekk.loggetInn()).thenReturn(null);

        String resultat = adminKontoController.registrerKonto(kontoen);

        assertEquals("Ikke innlogget",resultat);
    }
    @Test
    public void registrerKonto_feil(){
        Konto kontoen =  new Konto("0101292","1982928323",720,"lønnskonto", "NOK",null);

        when(sjekk.loggetInn()).thenReturn("innlogget");

        when(adminRepository.registrerKonto(null)).thenReturn("feil");

        String resultat = adminKontoController.registrerKonto(null);

        assertEquals("feil",resultat);
    }
    @Test
    public void endreKonto(){
        Konto kontoen =  new Konto("0101292","1982928323",720,"lønnskonto", "NOK",null);
        when(sjekk.loggetInn()).thenReturn("innlogget");

        when(adminRepository.endreKonto(kontoen)).thenReturn("ok");

        String resultat = adminKontoController.endreKonto(kontoen);

        assertEquals("ok",resultat);
    }

    @Test
    public void endreKontoFeiletGenerlt(){
        Konto kontoen =  new Konto("0101292","1982928323",720,"lønnskonto", "NOK",null);
        when(sjekk.loggetInn()).thenReturn("innlogget");

        when(adminRepository.endreKonto(null)).thenReturn("feil");

        String resultat = adminKontoController.endreKonto(null);

        assertEquals("feil",resultat);
    }
    @Test
    public void endreKontoFeiletPersonummer(){
        Konto kontoTo =  new Konto("0101292","1982928323",720,"lønnskonto", "NOK",null);
        when(sjekk.loggetInn()).thenReturn("innlogget");

        when(adminRepository.endreKonto(kontoTo)).thenReturn("feil i personnummer");

        String resultat = adminKontoController.endreKonto(kontoTo);

        assertEquals("feil i personnummer",resultat);
    }
}
