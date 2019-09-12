public class Boolfuck {

  
  
  public static String interpret (String code, String input) {
    
    int memorySize = 30000;
    int [] memory = new int [memorySize];
    int memoryPointer = memorySize >> 1;
    char [] codeChars = code.toCharArray();
    int codePointer = 0;
    int [] codeLevels = new int [codeChars.length];
    int level = 0;
    
    InputStream is = new InputStream(input);
    OutputStream os = new OutputStream();
    
    for (int i = 0;i<codeLevels.length;++i){
        if (codeChars[i] == '['){
            level++;
            codeLevels[i] = level;
        }else if (codeChars[i] == ']'){
            codeLevels[i] = level;
            level --;
        }else{
            codeLevels[i] = level;
        }
    }
    
    while (codePointer<codeChars.length&&codePointer>=0){
        final char c = codeChars[codePointer];
        switch (c){
            case '+':
                memory[memoryPointer]=memory[memoryPointer] == 0 ? 1 : 0;
            break;
            case ',':
                memory[memoryPointer] = is.readBit();
            break;
            case ';':
                os.writeBit(memory[memoryPointer]);
            break;
            case '<':
                memoryPointer--;
            break;
            case '>':
                memoryPointer++;
            break;
            case '[':
                if (memory[memoryPointer]==0){
                    codePointer = rewindRight(codeChars,codeLevels,codePointer);
                }
            break;
            case ']':
                if (memory[memoryPointer]==1){
                    codePointer = rewindLeft(codeChars,codeLevels,codePointer);
                }
            break;
            
        }
        codePointer++;
        
    }
    return os.toString();
  }
  
  private static int rewindTo (final char [] codeChars,final int [] codeLevels,int codePointer,final char stopCharacter,final int dir){
      int level = codeLevels[codePointer];
      codePointer+=dir;
      while (codePointer<codeChars.length){
          if (codeChars[codePointer]==stopCharacter && codeLevels[codePointer]==level){
            return codePointer;
          }
          codePointer += dir;
      }
      return codePointer;
  }
  
  private static int rewindRight (final char [] codeChars,final int [] codeLevels,final int codePointer){
      return rewindTo(codeChars,codeLevels,codePointer,']',1);
  }
  
  private static int rewindLeft (final char [] codeChars,final int [] codeLevels,final int codePointer){
      return rewindTo(codeChars,codeLevels,codePointer,'[',-1);
  }
  
  private static class OutputStream {
      
      int data = 0;
      int bitShift = -1;
      String output = "";
      boolean hasOutput = false;
      public void writeBit(int bit){
          hasOutput = true;
          bitShift++;
          if (bitShift>=8){
              output += (char)data;  
              hasOutput = false;
              data = 0;
              bitShift = 0;
          }else{
              hasOutput = true;
          }
          if (bit>0){
            data^=1<<bitShift;
          }
      }
      
      public String toString(){
          if (hasOutput){
              output += (char)data;              
          }
          return output;
      }
  }
  
  private static class InputStream {
      
      final int [] data;
      final int length;
      int pointer = 0;
      int bitShift = -1;
      
      public InputStream (String input){
          if ((length = input.length()) == 0){
              data = null;
              return;
          }
          data = new int [length];
          char [] asChars = input.toCharArray();
          for (int i = 0;i<length;++i){
              data[i] = (int)asChars[i];
          }
      }
      
      public int readBit() {
          bitShift++;
          
          if (bitShift>=8){
              pointer++;
              bitShift = 0;
          }
          
          if (pointer >= length){
              return 0;
          }
          
          int ret = 0;
          if ((data[pointer] & (1<<bitShift))!=0){
            ret = 1;
          }
                    
          return ret;
      }
  }
}