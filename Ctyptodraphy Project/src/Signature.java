import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Signature {
	public BigInteger d = new BigInteger("89489425009274444368228545921773093919669586065"
			         + "88425744549785445648767483962981839093494197326"
			         + "28796167979706089172836798754993315741611138540"
			         + "88813275488110588247193077582527278437906504015"
			         + "68062342355006724004246666565423238350292221549"
			         + "36232894721388664458187891279461234078077257026"
			         + "26644091036502372545139713");
	
	
	private BigInteger e = BigInteger.valueOf(65537);
	
	
	private BigInteger p = new BigInteger("1213107243921127189732367153161244042847242763"
			         + "3701410925634549312301964373042085619324197365"
			         + "3224168665410170573613652141717117137979742993"
			         + "34871062829803541");
	
	private BigInteger q = new BigInteger("1202752425547874888595622079373451212873338780"
		             + "36820754336538999839551798509887978998691469008"
			         + "09131611153346817050832096022160146366346391812"
			         + "470987105415233");
	
	
	private BigInteger n = new BigInteger("1459067680075833232301869393490706352924018723753"
			         + "5716439958187101987343879900535893836957140267014"
			         + "9802121818086292467422828157022922076746906543401"
			         + "2248896724724079269699871005812901031993178587536"
			         + "6371086235765651050788371429711563734278891146353"
			         + "5102712032765166518411726859837988672111837205085526346618740053");
	
	
	public Signature() {}
	public String GetE() throws UnsupportedEncodingException{
		return new String(e.toByteArray(),"UTF-8");
	}
	public String GetN() throws UnsupportedEncodingException{
		return new String(n.toByteArray(),"UTF-8");
	}
	public BigInteger GetBE(){
		return e;
	}
	public BigInteger GetBN(){
		return n;
	}
	
	public String Sign(String mm) throws UnsupportedEncodingException{
		BigInteger m = new BigInteger(mm.getBytes("UTF-8"));
		m = m.modPow(d,n);
		mm = new String(m.toByteArray(),"UTF-8");
		return mm;
		}
		
		 
    public String getHash(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		       MessageDigest digest = MessageDigest.getInstance("SHA-256");
		       digest.reset();
		       byte[] input = digest.digest(password.getBytes("UTF-8"));
		       String str = new String(input, StandardCharsets.UTF_8);
			return str;
		 }
	public String SignCheck(String msg , String y, String ee) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		String msgg = getHash(msg);
		BigInteger yy = new BigInteger(y.getBytes("UTF-8"));
		yy = yy.modPow(e,n);
		y = new String(yy.toByteArray(),"UTF-8");
		System.out.println("check: "+y);
		System.out.println(msgg);
		if(yy.equals(msgg)){return "Good Message!!";}else{return "invlid Message!!";}
		
	}
}
