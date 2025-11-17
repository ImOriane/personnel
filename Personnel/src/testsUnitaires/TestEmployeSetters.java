package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import personnel.DateException;
import personnel.DroitsInsuffisants;
import personnel.Employe;
import personnel.GestionPersonnel;
import personnel.Ligue;

import java.time.LocalDate;

class TestEmployeSetters {
	
	GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();

    GestionPersonnel gestion;
    Ligue ligue;
    Employe e;

    @BeforeEach
    void init() throws Exception {
        gestion = GestionPersonnel.getGestionPersonnel();
        ligue = gestion.addLigue("Test Ligue");
        e = ligue.addEmploye("Nom", "Prenom", "mail@test.fr", "pass", null);
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
    void testModificationAdministrateur() throws Exception {
        Ligue l = gestionPersonnel.addLigue("Basket");

        Employe e1 = l.addEmploye("Dupont", "Jean", "dj@test.fr", "123", null);
        Employe e2 = l.addEmploye("Durand", "Paul", "dp@test.fr", "456", null);

        assertEquals(e1, l.getAdministrateur());

        l.setAdministrateur(e2);

        assertEquals(e2, l.getAdministrateur());
    }
    
}
