package testsUnitaires;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.Test;

public class LocalDateTests {

    @Test
    void testInvalidDateFormat() {
        assertThrows(DateTimeParseException.class, () -> {
            LocalDate.parse("2025/12/31"); // format invalide
        });
    }
    
    @Test
    void testImpossibleDate() {
        assertThrows(DateTimeParseException.class, () -> {
            LocalDate.parse("2025-02-31");
        });
    }

}
