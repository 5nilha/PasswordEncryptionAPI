import java.util.ArrayList;

public class Decrypt extends Encrypt {

    private String encryptedPassword;
    private int lastByteDigit;
    private ArrayList<String> encryptedBinaryData = new ArrayList<>();
    private int alphaNumericLength = alphaNumeric.length;
    private int key;


    public Decrypt(String password, String encryptedPassword) {
        super(password);
        this.encryptedPassword = encryptedPassword;
    }

    String getDecryptedPassword() {
        return decryptPassword();
    }

    protected void generateKey() {
        int passwordLength = this.password.length();
        int charIndex = passwordLength - 1;
        char lastDigit = this.password.charAt(charIndex);
        char firstDigit = this.password.charAt(0);
        int asciiLast = (int) lastDigit;
        int asciiFirst = (int) firstDigit;

        this.key = asciiLast + asciiFirst + passwordLength;
    }


    private String decryptPassword() {
        generateKey();
        String newPassword = "";
        int encryptedPasswordLength = this.encryptedPassword.length();

        int j = 0;
        for (int i = 0; i < encryptedPasswordLength / 2; i++) {
            int asciiDigit ;
            String asciiDigitByte;
            char element1 = this.encryptedPassword.charAt(j);
            j += 1;
            char element2 = this.encryptedPassword.charAt(j);
            j +=1;

            if (j != encryptedPasswordLength) {
                asciiDigit = getDecimalASCII(element1, element2, false);
                asciiDigitByte = getBinaryFromDecimal(asciiDigit);
                encryptedBinaryData.add(asciiDigitByte);
            }
            else {
                int alphaNumericElementIndex = getDecimalASCII(element1, element2, true);
                this.lastByteDigit = alphaNumericElementIndex;
            }
        }

        newPassword = getASCIILettersFromBinary(convertBinaryArrayToBinaryString(encryptedBinaryData));
        return newPassword;
    }

    private String getASCIILettersFromBinary(String encryptedByte) {
        String initialBinaryState = rotateLeft(encryptedByte, this.key);
        String password = "";
        int bitIndex = 0;
        for (int i = 0; i < initialBinaryState.length() / 8; i++) {
            int decimal;

            String binaryByte = "";
            for (int j = 0; j < 8; j++) {
                binaryByte += initialBinaryState.charAt(bitIndex);
                bitIndex += 1;
            }
            decimal = byteToDecimal(binaryByte);
            password += (char) decimal + "";
        }

        return password;
    }

    private int getDecimalASCII(char element1, char element2, boolean areLastElements) {
        int position1 = 0;
        int position2 = 0;

        for (int i = 0; i < alphaNumericLength; i++) {
            if (alphaNumeric[i] == element1) {
                position1 = i;
            }
            if (alphaNumeric[i] == element2) {
                position2 = i;
            }
        }

        if (areLastElements) {
            return position1;
        } else {
            int decimalASCII = getInverseModulus(position1, position2);

            return decimalASCII;
        }
    }

    private int byteToDecimal(String binaryByte) {
        double exponent = 7.0;
        double base = 2.0;
        double sum = 0.0;

        for (int i = 0; i < binaryByte.length(); i++) {
            String digitString = binaryByte.charAt(i) + "";
            int digit = Integer.parseInt(digitString);
            double result = digit * (Math.pow(base, exponent));
            exponent -= 1.0;
            sum += result;
        }

        return (int) sum;
    }

    private int getInverseModulus(int modulesElementPosition, int quotientElementPosition) {
        int result;
        int divisor;

        result = (quotientElementPosition * (this.alphaNumericLength - 1) ) + modulesElementPosition;
        return result;
    }


    private String convertBinaryArrayToBinaryString(ArrayList<String> binaryData) {
        String binaryRotated = "";
        for (int i = 0; i < binaryData.size() - 1; i ++) {
            binaryRotated += binaryData.get(i);
        }

        String lastByte = binaryData.get(binaryData.size() - 1);

        for (int i = 0; i < lastByte.length() - lastByteDigit; i++) {
            binaryRotated += lastByte.charAt(i);
        }
        return binaryRotated;
    }

    private String rotateLeft(String binary, int numberOfRotations) {

        ArrayList<Character> binaryRotated = new ArrayList<Character>();

        //converting String to an array of chars
        for (int i = 0; i < binary.length(); i ++) {
            binaryRotated.add(i, binary.charAt(i));
        }

        // Rotating the binary numbers right
        char temp;
        int n = 0;
        int i = 0;
        while (n <= numberOfRotations) {
            temp = binaryRotated.get(i);
            binaryRotated.remove(i);
            binaryRotated.add(temp);
            n++;
        }

        //converting the array of chars back to string
        String rotatedBinaryString = "";
        for (int j = 0; j < binaryRotated.size(); j++) {
            rotatedBinaryString += binaryRotated.get(j);
        }

        return rotatedBinaryString;
    }

}
