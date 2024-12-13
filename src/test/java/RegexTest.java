import data.user.UserName;
import org.junit.jupiter.api.Test;
import service.serviceexception.validateexception.ValidateBetragException;
import service.serviceexception.validateexception.ValidateUsernameException;
import validator.Validator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RegexTest {

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


}
