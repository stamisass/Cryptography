import java.security.SecureRandom;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;


public class DiffieHellman 
{
    private BigInteger modulus;
    private static BigInteger generator;
    private BigInteger privateKey;
    private BigInteger publicKey;


    private DiffieHellman() {}

    public static final BigInteger DEFAULT_MODULUS
        = new BigInteger("1551728981814736974712322577637155399157248019669"
                         +"154044797077953140576293785419175806512274236981"
                         +"889937278161526466314385615958256881888899512721"
                         +"588426754199503412587065565498035801048705376814"
                         +"767265132557470407658574792912915723345106432450"
                         +"947150072296210941943497839259847603755949858482"
                         +"53359305585439638443");

    public static final BigInteger DEFAULT_GENERATOR = BigInteger.valueOf(2);

    public static DiffieHellman getDefault()
    {
        BigInteger p = DiffieHellman.DEFAULT_MODULUS;
        BigInteger g = DiffieHellman.DEFAULT_GENERATOR;
        return new DiffieHellman(p, g);
    }

    private static SecureRandom random;
    static {
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e){
            throw new RuntimeException("No secure random available!");
        }
    }

    public BigInteger getPrivateKey()
    {
        return privateKey;
    }

    public BigInteger getPublicKey()
    {
        return publicKey;
    }


    public DiffieHellman(BigInteger mod, BigInteger gen)
    {
        modulus = (mod != null ? mod : DiffieHellman.DEFAULT_MODULUS);
        generator = (gen != null ? gen : DiffieHellman.DEFAULT_GENERATOR);

        int bits = modulus.bitLength();
        BigInteger max = modulus.subtract(BigInteger.ONE);
        while (true) {
            BigInteger pkey = new BigInteger(bits, random);
            if (pkey.compareTo(max) >= 0) { //too large
                continue;
            }
            else if (pkey.compareTo(BigInteger.ONE) <= 0) {//too small
                continue;
            } 
            privateKey = pkey;
            publicKey = generator.modPow(privateKey, modulus);
            break;
        }
    }

    public static DiffieHellman recreate(BigInteger privateKey, BigInteger modulus)
    {
        if (privateKey == null || modulus == null) {
            throw new IllegalArgumentException("Null parameter");
        }
        DiffieHellman dh = new DiffieHellman();
        dh.setPrivateKey(privateKey);
        dh.setModulus(modulus);
        dh.publicKey = generator.modPow(privateKey, modulus);
        return dh;
    }

    private void setPrivateKey(BigInteger privateKey)
    {
        this.privateKey = privateKey;
    }

    private void setModulus(BigInteger modulus)
    {
        this.modulus = modulus;
    }


    public BigInteger getSharedSecret(BigInteger composite)
    {
        return composite.modPow(privateKey, modulus);
    }

}