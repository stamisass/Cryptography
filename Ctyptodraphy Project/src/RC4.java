
class rc4 {
    
    	private byte[] S;
        private int the_i;
        private int the_j;
        private int next_j = -666;
        
        public rc4() {
            S = new byte[256];
            the_i = the_j = 0;
        }
        public rc4(byte[] S) {
            this.S = S;
            the_i = the_j = 0;
        }

        public void ksa(int n, byte[] key, boolean printstats) {
            int keylength = key.length;
            int i = 0;
            for (i = 0; i<256; i++) S[i] = (byte) i;
            the_j = 0;
            for (the_i=0; the_i<n; the_i++) {
    	        the_j = (the_j + posify(S[the_i]) + posify(key[the_i % keylength])) % 256;
    	        sswap(S, the_i,the_j);
    	    }
    	    if (n!= 256) {
    	    	next_j = (the_j + posify(S[n]) + posify(key[n % keylength])) % 256;
    	    }
            if (printstats) {
                System.out.print("S_" + (n-1) + ":");
    	        for (int k = 0; k<= n; k++) System.out.print(" " + posify(S[k]));
    	        System.out.print("   j_" + (n-1) + "=" + the_j);
    	        System.out.print("   j_" + n + "=" + next_j);
    	        System.out.print("   S_"+(n-1) + "[j_" + (n-1) + "]=" + posify(S[the_j]));
    	        System.out.print("   S_"+(n-1) + "[j_" + (n) + "]=" + posify(S[next_j]));
    	        if (S[1] != 0) System.out.print("   S[1]!=0");
    	        System.out.println();
            }
        }
        
        public void ksa(byte[] key) {
            ksa(256, key, false);
        }
        
        public void init() {
            the_i = the_j = 0;
        }
        
        byte nextVal() {
            the_i = (the_i+1) % 256;
            the_j = (the_j + posify(S[the_i])) % 256;
            sswap(S,the_i,the_j);
            byte value = S[ (posify(S[the_i]) + posify(S[the_j])) % 256 ];
            return value;
        }
        
        // returns i for which x = S[i]
        byte inverse(byte x) {
            int i = 0;
            while (i<256) {
                if (x == S[i]) return (byte) i;
                i++;
            }
            return (byte) 0;	
        }
        
        int the_i() {return this.the_i;}
        int the_j() {return this.the_j;}
        int next_j() {return this.next_j;}
        int S(int n) {return posify(S[(byte) n]);}
        

    private static void sswap(byte[] S, int i, int j) {
    	    byte temp = S[i];
    	    S[i] = S[j];
    	    S[j] = temp;
    } 
    
    public static int posify (byte b) {
    	if (b>=0) return b;
    	else return 256 + b;
    }

    static public byte[] encrypt(byte[] key, byte[] message) {
        byte[] outbuf = new byte[message.length];
        rc4 r = new rc4();
        r.ksa(key);
        r.init();
        for (int i = 0; i < message.length; i++) {
            outbuf[i] = (byte) (message[i] ^ r.nextVal());
        }
        return outbuf;
    }
}
 