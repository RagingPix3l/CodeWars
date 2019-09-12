public class Finder {

	public static int pathFinder(String maze) {
		String [] rows = maze.split("\n");
    int [][] mazeCells = getMaze(rows);
    final int N = rows.length;
    boolean reachedExit = false;
    boolean madeMove = false;
    if (N==1){
      return 0;
    }
    do {
      madeMove = false;
      for (int i = 0;i<N;++i){
        for (int j = 0;j<N;++j){  
           int cell = mazeCells[i][j];
           if(cell == -1){
             continue;
           } else if (cell == 0){
             int max = max(mazeCells,i,j);
             if (max>0){
               mazeCells[i][j] = max + 1;
               madeMove = true;
               if (i==N-1 && j == N-1){
                 reachedExit = true;
                 return max;
               }
             }
           }
        }
      }
    } while ((!reachedExit)&&madeMove);

    
    return -1;
	}
  
  public static boolean inBounds(int [][] cells, int y,int x){
    return y>=0 && y < cells.length && x >= 0 && x < cells[0].length;
  }
  
  public static int max(int [][] cells,int y,int x) {
    int max = -1;
    for (int dy = -1;dy<2;dy++){
      for (int dx = -1;dx<2;dx++){
        if (dy*dx == 0 && dy!=dx) {
          if (inBounds(cells,y+dy,x+dx)){
            max = Math.max(cells[y+dy][x+dx],max);
          }
        }
      }
    }
    return max;
  }
  
  public static int [][] getMaze(String [] rows){
    final int N = rows.length;
    int [][] maze = new int[N][N];
    for (int i = 0;i<N;++i){
      for (int j = 0;j<N;++j){
        maze[i][j] = rows[i].charAt(j) == 'W' ? -1 : 0;
      }
    }
    maze [0][0] = 1;
    return maze;
  }
}