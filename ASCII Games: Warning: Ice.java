import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class IceMaze {

  private static final int START = 0;
  private static final int EXIT = 1;
  private static final int BLOCK = 4;
  private static final int FLOOR = 3;
  
  private static int M;
  private static int N;
  
  public static String solve(String map) {
      
      
      int [][] grid = mapString2Grid(map);
      if (grid == null){
          return null;
      }
      M = grid[0].length;
      N = grid.length;
      
      Position pos = findPosition(grid,START);
      if (pos == null){
          return null;
      }
      Position exitPos = findPosition(grid, EXIT);
      if (exitPos == null){
          return null;
      }
      //System.out.println("\n" + stringify(grid) + pos);
      ArrayList <Solution> solutions = new ArrayList<Solution>();
      int [][] beenHere = new int [N][M];
      
      walkAround(grid,pos,exitPos,"",1,solutions,beenHere,0);
      Collections.sort(solutions,Comparator.comparing(Solution::movesCount));
      
      
      
      if (solutions.size()>0){
        int movesCount = solutions.get(0).movesCount();
        ArrayList<Solution> picked = new ArrayList<Solution>();
        for (Solution sol : solutions) {
            if (sol.movesCount() != movesCount){
                break;
            }
            picked.add(sol);          
        }
        Collections.sort(picked,Comparator.comparing(Solution::distance));
        return picked.get(0).steps;
      }
      return null;
  }
  
  private static <T> void println(T p){
      System.out.println(p);
  }
  
  private static void walkAround (final int [][] grid,final Position pos,final Position exitPos,final String steps,final int iteration,final ArrayList <Solution> solutions,final int [][] beenHere, final int distance){
      if (solutions.size() > 0 && solutions.get(0).movesCount() < steps.length()){
        return;
      }
      if (pos.equals(exitPos)){
          //println("hit exit! " + steps);
          
          
          Solution solution = new Solution();
          solutions.add(solution);
          solution.steps = steps + "";
          solution.distance = distance;
          
          Collections.sort(solutions,Comparator.comparing(Solution::movesCount));
          return;
      }
      
      if (beenHere[pos.y][pos.x]>0){
        return;
      }
      
      if (iteration>25){
        return;
      }
      
      
      beenHere[pos.y][pos.x]+=1;
      char lastDir;
      if (steps.length()==0){
          lastDir = 'X';
      }else{
          lastDir = steps.charAt(steps.length()-1);
      }
      //println("StartWalking" + pos.toString());
      char [] directions = randomizedDirections();
      for (char dir : directions){
          
          switch (dir) {
              case 'u':
              if (lastDir == 'd'){
                continue;
              }
              break;
              case 'd':
              if (lastDir == 'u'){
                continue;
              }
              break;
              case 'r':
              if (lastDir == 'l'){
                continue;
              }
              break;
              case 'l':
              if (lastDir == 'r'){
                continue;
              }
              break;
              
          }
          Position newPos = calculateNextPos(grid,pos,dir);
          if (newPos == null){
              continue;
          } 
          if (Math.abs(newPos.x - pos.x) < 1 && Math.abs(newPos.y - pos.y) < 1) {
              continue;
          }
          
          //println("Walk in direction: " + dir + " new position : " +newPos);
          walkAround(grid,newPos,exitPos,steps+dir,iteration+1,solutions,beenHere,distance + Math.abs(newPos.x - pos.x) + Math.abs(newPos.y - pos.y) );
          
      }
      beenHere[pos.y][pos.x]=0;
  }
  
  private static char [] randomizedDirections () {
      char [] directions = new char [] {'u','d','l','r'};
      int [] used = new int [directions.length];
      char [] ret = new char [directions.length];
      int count = 0;
      while(count<directions.length){
          int index = (int)(Math.random()*ret.length);
          if (used[index] > 0){
              
              continue;
          }
          
          used[index] = 1;
          ret[count++] = directions[index];          
      }
      
      return ret;
  }
  
  private static Position calculateNextPos(int [][] grid,Position pos,char dir){
      Position newPos = new Position(pos);
      Position direction = new Position(0,0);
      direction.x = dir =='l' ? - 1 : dir == 'r' ? 1 : 0;
      direction.y = dir =='u' ? - 1 : dir == 'd' ? 1 : 0;
      boolean shouldStop = false;
      while (canMove(newPos,direction,grid)&&!shouldStop){
          newPos.add(direction);
          shouldStop = grid[newPos.y][newPos.x] == FLOOR || grid[newPos.y][newPos.x] == EXIT;
      }
      
      if (newPos.equals(pos)){
          return null;
      }
      return newPos;
  }
  
  private static boolean canMove(Position pos,Position direction,int [][] grid){
      Position tmp = new Position(pos);
      tmp.add(direction);
      if (tmp.x >= M || tmp.x < 0) return false;
      if (tmp.y >= N || tmp.y < 0) return false;
      if (grid[tmp.y][tmp.x] == BLOCK) return false;
      return true;
  }
  
  private static Position findPosition (int [][] grid,int symbol){
      for (int i = 0;i<grid.length;++i){
            for (int j = 0;j<grid[i].length;++j){
                if (grid[i][j] == symbol){
                    return new Position(j,i);
                }
            }            
      }
      return null;
  }
  
  private static class Position {
      public int x;
      public int y;
      
      public Position (Position o) {
          x = o.x;
          y = o.y;
      }
      
      public Position (int px,int py){
          x = px;
          y = py;
      }
      
      public String toString() {
          return String.format("(%d,%d)",x,y);
      }
      
      public boolean equals(Position o) {
          boolean r = x == o.x && y == o.y;          
          return r;
      }
      
      public void add (Position o) {
          x+=o.x;
          y+=o.y;
      }
      
  }
  
  private static class Solution {
      public String steps;
      public int distance;  
      
      public int movesCount() {
          return steps.length();
      }
      
      public int distance() {
          return distance;
      }
  }
  
  private static int [][] mapString2Grid(String map) {
      if (map.length() == 0) {
          return null;
      }
      String [] rows = map.split("\n");
      int [][] ret = new int [rows.length][];
      for (int i =0,n=rows.length;i<n;++i){
          char [] cols = rows[i].toCharArray();
          ret[i] = new int[cols.length];
          for (int j = 0,m=cols.length;j<m;++j){
              switch (cols[j]){
                  case 'S':
                    ret[i][j] = START;
                  break;
                  case 'E':
                    ret[i][j] = EXIT;
                  break;
                  case ' ':
                    ret[i][j] = 2;
                  break;
                  case 'x':
                    ret[i][j] = 3;
                  break;
                  case '#':
                    ret[i][j] = 4;
                  break;
                  default:
                    throw new IllegalArgumentException();
              }
          }
      }
      return ret;
  }
  private static String stringify(int [][] puzzle) {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0;i<puzzle.length;++i){
            sb.append("  {");
            for (int j = 0;j<puzzle[i].length;++j){
                if (j>0){
                    sb.append(",");
                }
                sb.append(puzzle[i][j]);
            }
            sb.append("}");
            if (i<puzzle.length-1){
              sb.append(",");
            }else{
              sb.append(" ");
            }
            sb.append("//" + i);
            sb.append("\n");
        }
       
        return sb.toString();
    }
}