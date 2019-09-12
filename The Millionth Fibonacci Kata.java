import java.math.BigInteger;

public class Fibonacci {
  
  public static BigInteger fib(BigInteger m) {
    if (m.compareTo(BigInteger.ZERO) < 0){
        //https://en.wikipedia.org/wiki/Fibonacci_number#Negafibonacci
        return BigInteger.valueOf(-1).pow(m.abs().add(BigInteger.ONE).intValue()).multiply(fib(m.abs()));
    }
    if (m.compareTo(BigInteger.TWO) < 0){
        return m;
    }
    //Matrix form
    //https://en.wikipedia.org/wiki/Fibonacci_number
    //Fn = A^n*F0
    BigInteger[][] startMatrix = new BigInteger [][] { 
        {BigInteger.ZERO, BigInteger.ONE},
        {BigInteger.ONE, BigInteger.ONE}
    };
    BigInteger[][] resultMatrix = matrixFib(startMatrix, m.subtract(BigInteger.ONE));
		return resultMatrix[0][0].add(resultMatrix[0][1]);
    
  }

  //fast A^m for matrix
  private static BigInteger [][] matrixFib (BigInteger [][] matrix, BigInteger m){
      if (m.compareTo(BigInteger.ONE)==0){
          return matrix;
      }
      BigInteger [][] res;
      if (m.mod(BigInteger.TWO).intValue() == 0){
          res = matrixFib(matrix,m.divide(BigInteger.TWO));
          return matrixmul(res,res);
      }else{
          res = matrixFib(matrix,m.subtract(BigInteger.ONE));
          return matrixmul(res,matrix);
      }
  
  }
  
  private static BigInteger[][] matrixmul(BigInteger[][] l, BigInteger[][] r) {
		
		BigInteger[][] output = {{l[0][0].multiply(r[0][0]).add(l[0][1].multiply(r[1][0])) ,
		                          l[0][0].multiply(r[0][1]).add(l[0][1].multiply(r[1][1]))},
                              
                             {l[1][0].multiply(r[0][0]).add(l[1][1].multiply(r[1][0])),
                             
		                          l[1][0].multiply(r[0][1]).add(l[1][1].multiply(r[1][1]))}};
		return output;
	}
  
}