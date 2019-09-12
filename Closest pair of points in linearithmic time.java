import java.util.Arrays;
import java.util.List;
import java.util.Comparator;
import java.util.ArrayList;

public class Kata {
  static Comparator<Point> comparerX = Comparator.<Point>comparingDouble(p -> p.x);
  static Comparator<Point> comparerY = Comparator.<Point>comparingDouble(p -> p.y);
  
  public static List<Point> closestPair(List<Point> points) {
    return divideAndConquer(points).toList();
	}
  
  public static Pair bruteForce(List<? extends Point> points)
  {
    int numPoints = points.size();
    if (numPoints < 2)
        return null;
    Pair ret = new Pair(points.get(0),points.get(1));
    double minDist = ret.distance;
    if (numPoints == 2)      
    {
        return ret;
    }
    for (int i = 0; i < numPoints - 1; i++)
    {
      Point point1 = points.get(i);
      for (int j = i + 1; j < numPoints; j++)
      {
        Point point2 = points.get(j);
        double distance = dist(point1, point2);
        if (distance < minDist){
            minDist = distance;
            ret.update(point1,point2);
        }
      }
    }
    
    return ret;
  }
  
  public static void sortByX(List<? extends Point> points)
  {
      points.sort(comparerX);
  }
  
  public static void sortByY(List<? extends Point> points)
  {
      points.sort(comparerY);
  }
  
  public static Pair divideAndConquer(List<? extends Point> points)
  {
    List<Point> pointsSortedByX = new ArrayList<Point>(points);
    sortByX(pointsSortedByX);
    List<Point> pointsSortedByY = new ArrayList<Point>(points);
    sortByY(pointsSortedByY);
    return divideAndConquer(pointsSortedByX, pointsSortedByY);
  }
  
  public static class Pair {
      Point point1;
      Point point2;
      double distance;
      public Pair() {
          point1 = null;
          point2 = null;
          distance = 0;
      }
      
      public Pair (Point a, Point b){
          point1 = a;
          point2 = b;
          distance = dist(a,b);
      }
      
      public void update(Point a,Point b){
          point1 = a;
          point2 = b;
          distance = dist(a,b);
      }  
      
      public List<Point> toList() {
          List <Point> ret = new ArrayList<Point>();
          ret.add(point1);
          ret.add(point2);
          return ret;
      }
      
  }
  
  private static Pair divideAndConquer(List<? extends Point> pointsSortedByX, List<? extends Point> pointsSortedByY)
  {
    int numPoints = pointsSortedByX.size();
    if (numPoints <= 3)
      return bruteForce(pointsSortedByX);
 
    int dividingIndex = numPoints >>> 1;
    List<? extends Point> leftOfCenter = pointsSortedByX.subList(0, dividingIndex);
    List<? extends Point> rightOfCenter = pointsSortedByX.subList(dividingIndex, numPoints);
 
    List<Point> tempList = new ArrayList<Point>(leftOfCenter);
    sortByY(tempList);
    Pair closestPair = divideAndConquer(leftOfCenter, tempList);
 
    tempList.clear();
    tempList.addAll(rightOfCenter);
    sortByY(tempList);
    Pair closestPairRight = divideAndConquer(rightOfCenter, tempList);
 
    if (closestPairRight.distance < closestPair.distance)
        closestPair = closestPairRight;
 
    tempList.clear();
    double shortestDistance = closestPair.distance;
    double centerX = rightOfCenter.get(0).x;
    
    for (Point point : pointsSortedByY){
        if (Math.abs(centerX - point.x) < shortestDistance){
            tempList.add(point);
        }
    }
 
    for (int i = 0, n = tempList.size(); i < n - 1; i++)
    {
      Point point1 = tempList.get(i);
      for (int j = i + 1;j<n; j++)
      {
          Point point2 = tempList.get(j);
          if ((point2.y - point1.y) >= shortestDistance)
              break;
          double distance = dist(point1, point2);
          if (distance < closestPair.distance)
          {
              closestPair.update(point1, point2);
              shortestDistance = distance;
          }
      }
    }
    return closestPair;
  }
  
  public static double dist(Point p, Point o) {
      return Math.sqrt(distSquare(p,o));
  }
  
  
  public static double distSquare ( Point p ,Point o) {
    double dx = o.x-p.x;
    double dy = o.y-p.y;
    return (dx*dx + dy*dy);
  }
}