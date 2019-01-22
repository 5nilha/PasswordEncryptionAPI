import java.util.ArrayList;
import java.util.Arrays;

public class Encrypt {

    private String regularPasswordInBits;
    private String encryptedPasswordInBits;
    private String encryptedPasswordInHex;
    private int key;
    private int subKey;

    protected char[] alphaNumeric = "#${[}]|:;'_-+=abcdefghijklmnopqrstuvwxyz0123456789/^&*()ABCDEFGHIJKLMNOPQRSTUVWXYZ<,>.?!@%".toCharArray();

    protected String password;
    private String encryptedPassword;


    public Encrypt(String password) {
        this.password = password;
    }

    public String getPasswordEncrypted() {
        return encryptPassword();
    }

    private String encryptPassword() {
        generateKey();
        String hexEncryptedPassword = "";
        String bitsEncliptedPassword = "";
        String newPassword = "";
        String binaryPassword = getBinaryFromPassword(this.password);
        String rotatedBinary = rotateRight(binaryPassword, this.key);
        int rotatedBinaryModulus = rotatedBinary.length() % 8;
        ArrayList<String> bytes = new ArrayList<>();
        int bitIndex = 0;

        for (int i = 0; i < rotatedBinary.length() / 8; i++) {
            String binaryByte = "";
            int decimal = 0;
            for (int j = 0; j < 8; j++) {
                binaryByte += rotatedBinary.charAt(bitIndex);
                bitIndex += 1;
            }
            bytes.add(binaryByte);
            hexEncryptedPassword += nimbleToHex(binaryByte);
            newPassword += getAphaNumericFromDigit(decimal);
        }
        if (rotatedBinaryModulus != 0) {
            int modulesDifference = rotatedBinaryModulus + (rotatedBinary.length() - rotatedBinaryModulus);
            String lastBinaryByte = "";
            for (int i = 0; i < rotatedBinaryModulus; i++) {
                lastBinaryByte += rotatedBinary.charAt(bitIndex);
                bitIndex += 1;
            }
            bytes.add(lastBinaryByte);
            hexEncryptedPassword += lastBinaryByte;
        }
        getDecimalsFromBinaryData(bytes);

        this.regularPasswordInBits = binaryPassword;
        this.encryptedPasswordInBits = rotatedBinary;
        this.encryptedPasswordInHex = hexEncryptedPassword;

        return this.encryptedPassword;
    }


    protected String getBinaryFromDecimal(int decimal) {
        String decimalByte = "";
        ArrayList<Integer> binary = new ArrayList<>();
        int n = decimal;

        while (n > 0) {
            int remainder = n % 2;
            n = n / 2;
            binary.add(0, remainder);
        }

        while (binary.size() < 8) {
            binary.add(0, 0); // Im inserting a zero at the first bit because the maximum size is 127
        }

        for (int i = 0; i < binary.size(); i++) {
            decimalByte += binary.get(i);
        }
        return decimalByte;
    }


    private void getDecimalsFromBinaryData(ArrayList<String> binaryData) {
        int decimal = 0;
        int lastByteRemainder = 0;
        String encryptedAlphanumeric = "";
        for (int i = 0; i < binaryData.size() - 1; i++) {
             decimal = byteToDecimal(binaryData.get(i));
             encryptedAlphanumeric += getAphaNumericFromDigit(decimal);
        }

        String lastByte = binaryData.get(binaryData.size() - 1);
        int lastByteBitsSize = lastByte.length();
        lastByteRemainder = 8 - lastByteBitsSize;

        decimal = byteToDecimal(binaryData.get(binaryData.size() - 1));
        encryptedAlphanumeric += getAphaNumericFromDigit(decimal);
        encryptedAlphanumeric += getAphaNumericFromDigit(lastByteRemainder);
        this.encryptedPassword = encryptedAlphanumeric;
    }

    private String getAphaNumericFromDigit(int digit) {

        int element1position = digit % (alphaNumeric.length - 1);
        int element2position = (int) digit / (alphaNumeric.length - 1);
        char letter = alphaNumeric[element1position];
        char letter2 = alphaNumeric[element2position];
        return "" + letter + letter2;
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

    private String rotateRight(String binary, int numberOfRotations) {

        ArrayList <Character> binaryRotated = new ArrayList<Character>();

         //converting String to an array of chars
        for (int i = 0; i < binary.length(); i ++) {
            binaryRotated.add(i, binary.charAt(i));
        }

        // Rotating the binary numbers right
        char temp;
        int n = 0;
        int i = binaryRotated.size() - 1;
        while (n <= numberOfRotations) {
            temp = binaryRotated.get(i);
            binaryRotated.remove(i);
            binaryRotated.add(0, temp);
            n++;
        }

        //converting the array of chars back to string
        String rotatedBinaryString = "";
        for (int j = 0; j < binaryRotated.size(); j++) {
           rotatedBinaryString += binaryRotated.get(j);
        }
        return rotatedBinaryString;
    }



    private String getBinaryFromPassword(String password) {
        ArrayList<Character> passwordDigits = new ArrayList<Character>();

        for (int i = 0; i < password.length(); i++) {
            passwordDigits.add(password.charAt(i));
        }

        String passwordBinary = "";

        for (int i = 0; i < password.length(); i++) {
            char passwordDigit = passwordDigits.get(i);
            int charAscii = (int) passwordDigit;
            passwordBinary += getBinaryFromDecimal(charAscii);
            passwordBinary += "";
        }

        return passwordBinary;
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

    private String nimbleToHex(String binaryByte) {

        String nimbleA = "";
        String nimbleB = "";
        String hexadecimalValue = "";
        int hexValueA = 0;
        int hexValueB = 0;
        double exponent = 3.0;
        double base = 2.0;

        for (int i = 0; i < binaryByte.length(); i++) {
            String digitString = binaryByte.charAt(i) + "";
            if (i < 4) {
                nimbleA += digitString;
            }
            else {
                nimbleB += digitString;
            }
        }

        for (int i = 0; i < nimbleA.length(); i ++) {
            String digitString = nimbleA.charAt(i) + "";

            int digit = Integer.parseInt(digitString);

            double result = digit * (Math.pow(base, exponent));
            hexValueA += (int) result;
            exponent -= 1.0;
        }

        exponent = 3.0;
        for (int i = 0; i < nimbleB.length(); i ++) {
            String digitString = nimbleB.charAt(i) + "";
            int digit = Integer.parseInt(digitString);
            double result = digit * (Math.pow(base, exponent));
            hexValueB += (int) result;
            exponent -= 1.0;
        }

        if (hexValueA < 10) {
            hexadecimalValue += hexValueA;
        }
        else {
            if (hexValueA >= 10 && hexValueA < 11) {
                hexadecimalValue += 'A';
            } else if  (hexValueA >= 11 && hexValueA < 12) {
                hexadecimalValue += 'B';
            } else if  (hexValueA >= 12 && hexValueA < 13) {
                hexadecimalValue += 'C';
            } else if  (hexValueA >= 13 && hexValueA < 14) {
                hexadecimalValue += 'D';
            } else if  (hexValueA >= 14 && hexValueA < 15) {
                hexadecimalValue += 'E';
            } else if  (hexValueA >= 15 && hexValueA < 16) {
                hexadecimalValue += 'F';
            }
        }
        if (hexValueB < 10) {
            hexadecimalValue += hexValueB;
        }
        else {
            if (hexValueB >= 10 && hexValueB < 11) {
                hexadecimalValue += 'A';
            } else if (hexValueB >= 11 && hexValueB < 12) {
                hexadecimalValue += 'B';
            } else if (hexValueB >= 12 && hexValueB < 13) {
                hexadecimalValue += 'C';
            } else if (hexValueB >= 13 && hexValueB < 14) {
                hexadecimalValue += 'D';
            } else if (hexValueB >= 14 && hexValueB < 15) {
                hexadecimalValue += 'E';
            } else if (hexValueB >= 15 && hexValueB < 16) {
                hexadecimalValue += 'F';
            }
        }
        return hexadecimalValue;
    }

}
