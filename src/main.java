public class main {

    public static void main(String[] args) {

        String myPassword = "FabioQuintanilhaIsAwesome1234567890";
        Password password = new Password(myPassword);

        String encrytedPassword = password.encryptPassword();

        System.out.println("My first password = " + myPassword);
        System.out.println("Encrypted password = " + encrytedPassword);

        System.out.println("Decrypted password = " + password.decryptPassword(encrytedPassword));

    }
}
