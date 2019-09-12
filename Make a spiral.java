public class Spiralizor {
    public static int[][] spiralize(int size) {
        int [][] ret = new int [size][size];
        final int EMPTY = 0;
        final int SNAKE = 1;
        
        Vector2 pos = new Vector2(0,0);
        Vector2 dir = Vector2.right();
        
        do {
          ret [pos.y][pos.x] = SNAKE;
          
          if (outOfBounds(pos,dir,size)){
            dir = dir.turn();
          }
          
          Vector2 doubleDir = dir.copy().mul(2);
          if (!outOfBounds(pos,doubleDir,size) && getAt(pos,doubleDir,ret) == SNAKE){
            dir = dir.turn();
          }
          
          doubleDir = dir.copy().mul(2);
          if (!outOfBounds(pos,doubleDir,size) && getAt(pos,doubleDir,ret) == SNAKE){
            break;
          }
          
          pos.add(dir);
        
        } while ( !outOfBounds(pos,size) && getAt(pos,ret) == EMPTY);
        
        if (size == 2){
          ret[1][0] = 0;
        }
        return ret;
    }
    
    public static int getAt(Vector2 pos,Vector2 dir, int [][] arr){
      return getAt(pos.copy().add(dir),arr);
    }
    
    public static int getAt(Vector2 pos,int [][] arr){
      return arr[pos.y][pos.x];
    }
    
    public static boolean outOfBounds(Vector2 pos, Vector2 dir, int size){
      return outOfBounds(pos.copy().add(dir), size);
    }
    
    public static boolean outOfBounds(Vector2 pos, int size){
      return pos.x < 0 || pos.y < 0 || pos.x >= size || pos.y >= size;
    }
    
    public static class Vector2 {
      public int x;
      public int y;
      
      public Vector2(int x,int y){
        this.x = x;
        this.y = y;
      }
      
      public Vector2 copy () {
        return new Vector2(x,y);
      }
      
      public Vector2 mul (int v) {
        x*=v;
        y*=v;
        return this;
      }
      
      public Vector2 add (Vector2 o) {
        x+=o.x;
        y+=o.y;
        return this;
      }
      
      public Vector2 sub (Vector2 o) {
        x-=o.x;
        y-=o.y;
        return this;
      }
      
      public boolean equal(Vector2 o) {
        return o.x == x && o.y == y;
      }
      
      public Vector2 turn () {
        if (equal(right())){
          return down();
        }
        if (equal(down())){
          return left();
        }
        if (equal(left())){
          return up();
        }
        if (equal(up())){
          return right();
        }
        return null;
        
        
      }
      
      public static Vector2 right () {
        
        return new Vector2(1,0);
      }
      public static Vector2 left () {
        
        return new Vector2(-1,0);
      }
      public static Vector2 up () {
        
        return new Vector2(0,-1);
      }
      public static Vector2 down () {
        
        return new Vector2(0,1);
      }
      
    }
}