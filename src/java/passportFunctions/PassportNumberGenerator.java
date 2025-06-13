package passportFunctions;
import java.util.Random;

public class PassportNumberGenerator {
    public static String generatePassportNumber() {
        Random random = new Random();
        char letter = (char) ('A' + random.nextInt(26));
        int number = random.nextInt(10000000);
        String numericPart = String.format("%07d", number);
        return letter + numericPart;
    }
}
