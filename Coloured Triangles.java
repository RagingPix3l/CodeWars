public class Kata {
  
  static char output = '?';
  
  public static char triangle(final String row) {
    triangalize(row.toCharArray());
    return output;
  }
  
  public static void triangalize (char [] input){
    if (input.length==1){
      output = input[0];
      return;
    }
    char [] nextRow = new char [input.length-1];
    for (int i = 0,n=nextRow.length;i<n;++i){
        nextRow[i] = color(input[i],input[i+1]);
    }
    triangalize(nextRow);
  }
  
  public static char color(char L, char R){
      if (L == R){
        return L;
      }
      boolean hasR = L == 'R' || R == 'R';
      boolean hasG = L == 'G' || R == 'G';
      boolean hasB = L == 'B' || R == 'B';
      
      return !hasR ? 'R' : !hasG ? 'G' : 'B';
  }
  
  
}