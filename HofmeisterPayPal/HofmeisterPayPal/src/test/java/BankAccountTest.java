import de.hofmeister.entity.bank.BankAccount;
import org.junit.Test;

public class BankAccountTest extends BankAccount {

        String IBAN = "DE90700500002901190315";

        @Test
        public void testCalculateIbanModulo() {
            int number = this.calculateIBANModulo(IBAN);
            assert number == 1 : "IBAN isn't translated correctly";
        }

        @Test
        public void testValidateIBAN() {
            boolean isValid = this.validateIBAN(IBAN);
            assert isValid : "IBAN isn't validated correctly";
        }

}

