package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import personnel.DateException;
import personnel.DroitsInsuffisants;
import personnel.Employe;
import personnel.GestionPersonnel;
import personnel.Ligue;
import personnel.SauvegardeImpossible;

import java.time.LocalDate;
import java.util.Collections;
import java.util.SortedSet;

class TestEmployeSetters {
	
	GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();

    GestionPersonnel gestion;
    Ligue ligue;
    Employe e;

	private int id;

    @BeforeEach
    void init() throws Exception {
        gestion = GestionPersonnel.getGestionPersonnel();
        ligue = gestion.addLigue("Test Ligue");
        e = ligue.addEmploye("Nom", "Prenom", "mail@test.fr", "pass", null); 
        e.setDateArrivee(LocalDate.of(2022, 1, 10));
        e.setDateDepart(LocalDate.of(2023, 1, 10));
    }

    @Test
    void testSetNom() {
        e.setNom("Durand");
        assertEquals("Durand", e.getNom());
    }

    @Test
    void testSetPrenom() {
        e.setPrenom("Paul");
        assertEquals("Paul", e.getPrenom());
    }

    @Test
    void testSetMail() {
        e.setMail("paul@ex.fr");
        assertEquals("paul@ex.fr", e.getMail());
    }

    @Test
    void testSetPassword() {
        e.setPassword("1234");
        assertTrue(e.checkPassword("1234"));
    }

    @Test
    void testSetDateArrivee() throws DateException {
        LocalDate d = LocalDate.of(2022, 1, 10);
        e.setDateArrivee(d);
        assertEquals(d, e.getDateArrivee());
    }

    @Test
    void testSetDateDepartValide() throws DateException {
        e.setDateArrivee(LocalDate.of(2020, 1, 1));
        e.setDateDepart(LocalDate.of(2021, 1, 1));
        assertEquals(LocalDate.of(2021, 1, 1), e.getDateDepart());
    }

    @Test
    void testSetDateDepartAvantArrivee() {

        assertDoesNotThrow(() -> e.setDateArrivee(LocalDate.of(2020, 5, 1)));

        assertThrows(DateException.class, () -> {
            e.setDateDepart(LocalDate.of(2020, 4, 30));
        });
    }

    @Test
    void testSuppressionEmploye() throws Exception {
    	
		Ligue l = gestionPersonnel.addLigue("Foot");

        Employe e1 = l.addEmploye("A", "A", "a@a.fr", "pass", null);
        Employe e2 = l.addEmploye("B", "B", "b@b.fr", "pass", null);

        assertEquals(2, l.getEmployes().size());

        e1.remove();

        assertEquals(1, l.getEmployes().size());
        assertFalse(l.getEmployes().contains(e1));
    }
    
    @Test
    void testSuppressionLigue() throws Exception {
        Ligue l = gestion.addLigue("Handball");
        assertTrue(gestion.getLigues().contains(l));
        l.remove();
        assertFalse(gestion.getLigues().contains(l));
    }
    
    @Test
    void testModificationAdministrateur() throws Exception {
        Ligue l = gestion.addLigue("Basket");

        Employe e1 = l.addEmploye("Dupont", "Jean", "dj@test.fr", "123", null);
        Employe e2 = l.addEmploye("Durand", "Paul", "dp@test.fr", "456", null);

        l.setAdministrateur(e1);
        assertEquals(e1, l.getAdministrateur());

        l.setAdministrateur(e2);
        assertEquals(e2, l.getAdministrateur());
    }
    
    @Test
    void testSuppressionAdminDevientRoot() throws Exception {
        Ligue l = gestion.addLigue("Basket");

        Employe e1 = l.addEmploye("Dupont", "Jean", "dj@test.fr", "123", null);
        l.setAdministrateur(e1);
        assertEquals(e1, l.getAdministrateur());

        e1.remove();

        assertEquals(gestion.getRoot(), l.getAdministrateur());
        assertFalse(l.getEmployes().contains(e1));
    }
 
    @Test
    void testSetNomligue() {
        ligue.setNom("NouvelleLigue");
        assertEquals("NouvelleLigue", ligue.getNom());
    }

    @Test
    void testSetAdministrateurValide() throws DroitsInsuffisants {
        ligue.setAdministrateur(e);
        assertEquals(e, ligue.getAdministrateur());
    }

    @Test
    void testSetAdministrateurInvalide() throws SauvegardeImpossible {
        Ligue autreLigue = gestion.addLigue("AutreLigue");
        Employe e2 = autreLigue.addEmploye("A", "B", "a@b.fr", "pass", null);

        assertThrows(DroitsInsuffisants.class, () -> {
            ligue.setAdministrateur(e2);
        });
    }

    @Test
    void testSetAdministrateurRoot() throws DroitsInsuffisants {
        Employe root = gestion.getRoot();
        ligue.setAdministrateur(root);
        assertEquals(root, ligue.getAdministrateur());
    }


    @Test
    void testGetNom() {
        assertEquals("Nom", e.getNom());
    }

    @Test
    void testGetPrenom() {
        assertEquals("Prenom", e.getPrenom());
    }

    @Test
    void testGetMail() {
        assertEquals("mail@test.fr", e.getMail());
    }

    @Test
    void testGetDateArrivee() {
        assertEquals(LocalDate.of(2022, 1, 10), e.getDateArrivee());
    }

    @Test
    void testGetDateDepart() {
        assertEquals(LocalDate.of(2023, 1, 10), e.getDateDepart());
    }

    @Test
    void testGetLigue() {
        assertEquals(ligue, e.getLigue());
    }

}
