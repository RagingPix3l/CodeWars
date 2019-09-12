import java.util.List;
import java.util.ArrayList;
import java.awt.Point;

public class CakeCutter {
    
    final int N;
    final int M;
    final int AREA;
    final int NRAISINS;
    int [][] grid;
    int [][] cake;
    
    public CakeCutter(String cake) {
        String [] cakeRows = cake.split("\n");
        N = cakeRows.length;
        M = cakeRows[0].length();
        AREA = N * M;
        
        grid = new int [N][];
        int raisins = 0;
        for (int i = 0;i<N;++i){
            char [] cakeRow = cakeRows[i].toCharArray();
            grid[i] = new int [M];
            for (int j = 0;j<M;++j){
                if (cakeRow[j] == '.'){
                    grid[i][j] = 0;
                }else{
                    grid[i][j] = 1;
                    raisins++;
                }
            }
            
        }
        NRAISINS = raisins;
    }
    public List<String> cut() {
        
        final int pieceArea = (AREA/NRAISINS);
        if (pieceArea*NRAISINS!=AREA){
          return null;
        }
//         println(stringify(grid));
//         println("raisins: " + NRAISINS);
//         println("Piece area: " + pieceArea);
        final List <String> output = new ArrayList<String>();
        
        final List <Point> dimensions = new ArrayList<Point>();
        for (int h = 1; h <=N;++h){
            for (int w =1;w<=M;++w){
                if (pieceArea == w*h){
                    //println("Posssible size: " + w + "x" + h);
                    dimensions.add (new Point(w,h));
                }
            }
        }
        int remainingRaisins = NRAISINS;
        final List <Rect> solution = new ArrayList <Rect> ();
        cake = gridClone(grid);
        recursiveSolve(remainingRaisins, dimensions,output,solution);
        return output;
    }
    private int [][] gridClone (int [][]grid) {
        int [][] ret = grid.clone();
        for (int i = 0;i<ret.length;++i){
            ret [i] = grid[i].clone();
        }
        return ret;
    }
    
    private int nFilled() {
        int count = 0;
        for (int i = 0;i<N;++i){
            for (int j=0;j<M;++j){
              if (grid[i][j] == 8){
                  count++;
              }
            }
        }
        return count;
    }
    private void recursiveSolve(final int remainingRaisins,final List <Point> dimensions,final List <String> output,List <Rect> solution) {
        if (remainingRaisins<=0){
            
            if (nFilled()==AREA){
              for (Rect rect:solution){
                output.add(cutPiece(rect.position,rect.size));
              }
            }
            return;
        }
        
        Point currentCoords = topLeftAvaiblePoint();
        if (currentCoords==null) {
            return;
        }
        if (output.size()>0){
            return;
        }
        int [][] gridCopy = gridClone(grid);
        
        
        for (Point rect:dimensions){
            if (containsOneRaisin(currentCoords,rect)){                    
                
                markArea(currentCoords,rect);
                solution.add(new Rect(currentCoords,rect));
                //println(stringify(grid));
                recursiveSolve(remainingRaisins-1,dimensions,output,solution);
                if (output.size()>0){
                    return;
                }
                solution.remove(solution.size()-1);
                grid = gridClone(gridCopy);
        
            }
        }                        
        
    }
    
    private class Rect {
        public Point position;
        public Point size;
        
        public Rect() {
            position = new Point(0,0);
            size = new Point(0,0);
        }
        
        public Rect(Point pos) {
            position = new Point(pos);
            size = new Point(0,0);
        }
        
        public Rect(Point pos,Point psize) {
            position = new Point(pos);
            size = new Point(psize);
        }
        
    }
    
    private Point topLeftAvaiblePoint () {
        for (int i = 0;i<N;++i){
            for (int j=0;j<M;++j){
            
                if (grid[i][j]!=8){
                    return new Point(j,i);
                }
            }
        }
        return null;
    }
    
    private String cutPiece (Point coords,Point size) {
        StringBuilder sb = new StringBuilder(size.x*size.y + size.y);
        for (int i = coords.y,di=coords.y+size.y;i<di&&i<N;++i){
            for (int j = coords.x,dj=coords.x+size.x;j<dj &&j<M;++j){
                if (cake[i][j] == 1){
                    sb.append("o");
                }else{
                    sb.append(".");
                }
            }
            sb.append(i<N-1 && i<coords.y+size.y-1 ? "\n" : "");
        }
        return sb.toString();
    }
    
    private void markArea (Point coords,Point size) {        
        for (int i = coords.y,di=coords.y+size.y;i<di&&i<N;++i){
            for (int j = coords.x,dj=coords.x+size.x;j<dj &&j<M;++j){
                grid[i][j] = 8;
            }
        }        
    }
    
    private boolean containsOneRaisin(Point coords,Point size) {
        int count = 0;
        for (int i = coords.y,di=coords.y+size.y;i<di;++i){
            if (i>=N){
                return false;
            }
            for (int j = coords.x,dj=coords.x+size.x;j<dj ;++j){
                if (j>=M){
                  return false;
                }
                if (grid[i][j] == 1){
                    count++;
                }
            }
        }
        return count == 1;
    }
    
    private static <T> void println(T p){
        System.out.println(p);
    }
    
    private static String stringify(int [][] puzzle) {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0;i<puzzle.length;++i){
            sb.append("  {");
            for (int j = 0;j<puzzle[i].length;++j){
                if (j>0){
                    //sb.append(",");
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