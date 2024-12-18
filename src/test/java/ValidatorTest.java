import data.user.Passwort;
import data.user.UserName;
import org.junit.jupiter.api.Test;
import service.serviceexception.validateexception.ValidateBeschreibungException;
import service.serviceexception.validateexception.ValidateBetragException;
import service.serviceexception.validateexception.ValidatePasswortException;
import service.serviceexception.validateexception.ValidateUsernameException;
import validator.Validator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {
    @Test
    void testBenutzernameValidationNegativ() {
        List<String> negativTest = List.of(
                "takatukaland"
                , "simens-de"
                , "hakara.com@d"
                , "tom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramostom.ramos@hsw-stud.de"
        );

        for (String s : negativTest) {
            ValidateUsernameException exception = assertThrows(ValidateUsernameException.class,
                    () -> Validator.isValidUserName(new UserName(s)));

            assertTrue(exception.getMessage().contains(
                    ValidateUsernameException.Message.USERNAME_INVALID.addInfo(new UserName(s)).getServiceErrorMessage()
            ));
        }
    }

    @Test
    void testBenutzerNameValidationPositiv() throws ValidateUsernameException {
        List<String> positivTest = List.of(
                "tom.ramos@hsw-stud.de"
                , "rebi.heim@gmail.com"
                , "marco-Barco@vodka.pulle.de"
                , "leni.take.nick@nananen23-fefe.com.com.de"
                , "shinjiiykari@nerv.jp"
                , "dernovoj10xDev@dozent.org"
        );

        for (String s : positivTest) {
            assertTrue(Validator.isValidUserName(new UserName(s)));
        }
    }

    @Test
    void testBetragValidationNegativ() {
        double[] zahlenreihe = {
                22.444
                , -122.0
                , 9999999999999999999999999999999999999999999999999999999999999999999999.0
                , -12.222};

        for (double z : zahlenreihe) {
            assertThrows(ValidateBetragException.class, () -> Validator.isValidBetrag(z));
        }
    }

    @Test
    void testBetragValidationPositiv() throws ValidateBetragException {
        double[] zahlenreihe = {
                22.33
                , 00.01
                , 33
        };

        for (double z : zahlenreihe) {
            assertTrue(Validator.isValidBetrag(z));
        }
    }

    @Test
    void testPasswordValidationNegativ() {
        List<Passwort> negativePasswortListe = List.of(
                new Passwort("DiesisteinnegativesPasswortweileszuvieleZeichenhat")
                , new Passwort("")

        );
        for (Passwort p : negativePasswortListe) {
            assertThrows(ValidatePasswortException.class, () -> Validator.isValidPasswort(p));
        }
    }

    @Test
    void testPasswordValidationPositive() throws ValidatePasswortException {
        List<Passwort> positivePasswortListe = List.of(
                new Passwort("PassswortmitäöüÄÖÜ")
                , new Passwort("1234568790")
                , new Passwort("!§$%&/()=")
                , new Passwort("ΩΩδκξψΔΦωιζγΩΒΦΗΔΨσι")
        );
        for (Passwort p : positivePasswortListe) {
            assertTrue(Validator.isValidPasswort(p));
        }
    }

    @Test
    void testBeschreibungPositiv() throws ValidateBeschreibungException {
        List<String> stringList = List.of(
                "HUHUHUHUH"
                ,"!???=)=)(&/(("
                ,"78997654132132123"
        );
        for (String beschreibung : stringList) {
            assertTrue(Validator.isValidBeschreibung(beschreibung));
        }
    }

    @Test
    void testBeschreibungNegativ(){
        List<String> stringList = List.of(
                ""
                ,"djhhhhhhddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd"
        );
        for (String beschreibung : stringList) {
            ValidateBeschreibungException e =
            assertThrows(ValidateBeschreibungException.class, () -> Validator.isValidBeschreibung(beschreibung));
            assertEquals(ValidateBeschreibungException.Message.BESCHREIBUNG.getServiceErrorMessage(), e.getMessage());
        }
    }
}