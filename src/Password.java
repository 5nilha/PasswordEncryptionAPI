public class Password {
    private String password = "test";

    public Password(String password) {
        this.password = password;
    }

    public String encryptPassword() {
        Encrypt encryption = new Encrypt(this.password);

        return encryption.getPasswordEncrypted();
    }

    public String decryptPassword(String encryptedPassword) {
        Decrypt decryption = new Decrypt(this.password, encryptedPassword);

        return decryption.getDecryptedPassword();
    }
}
