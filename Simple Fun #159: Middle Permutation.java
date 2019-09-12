import java.util.Arrays;
import java.math.BigInteger;

class MiddlePermutation {
    
    public static String findMidPerm(String strng) {
        char [] characters = strng.toCharArray();
        BigInteger N = calculateSize(characters.length);        
        
        BigInteger target=caluclateTarget(N);
        Arrays.sort(characters);
        String out = "";
        
        while (characters.length > 1){
          BigInteger n = calculateSize(characters.length-1);
          
          int index = Integer.valueOf(target.divide(n).toString());
          out+=characters[index];
          
          characters = (new String(characters)).replace(characters[index]+"","").toCharArray();
          Arrays.sort(characters);
          target = target.mod(n); 
        }
        out+=characters[0];
        return out;

        
    }
    
    public static BigInteger caluclateTarget (BigInteger N){
      BigInteger target;
      BigInteger two = new BigInteger("2");
      if (N.mod(two).equals(new BigInteger("0") )){
          target = N.divide(two).subtract(new BigInteger("1"));
        }else{
          target = N.divide(two);
        }
      return target;
    }
    
    
    
    public static BigInteger calculateSize (int n){
      
      BigInteger tmp = new BigInteger("1");
      while (n>1){
        tmp=tmp.multiply(new BigInteger(""+n));
        n--;
      }
      return tmp;
    }
}