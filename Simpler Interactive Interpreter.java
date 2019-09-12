import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Deque;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter {

  private Map <String,Double> map = new HashMap<String,Double>();
  
  private String expr = "";
  private String exprRPN = "";
  
  //https://en.wikipedia.org/wiki/Reverse_Polish_notation
  
  public Double input(String input) {
      check(input);
      Deque<String> tokens = tokenize(input);
      
      expr = "";
      exprRPN = "";
      while(tokens.size() > 0){
          expr+=(tokens.removeFirst().trim());
      }
      toRPN();
      
      if (exprRPN.trim().length() <= 0){
          return null;
      }
      
      return new Double(calculateRPN());
  }
  
  private void check(String input){
      String [] parts = input.split(" ");
      for (int i =0,n=parts.length;i<n-1;++i){
          if (isAlphaNum(parts[i].charAt(0)) && isAlphaNum(parts[i+1].charAt(0))){
              throw new IllegalArgumentException();
          }
      }
  }
  
  private double calculateRPN () {
      
      String [] expr = exprRPN.split(" ");
      ArrayList <Double> stack = new ArrayList<Double>();
      String firstVariable = "";
      for (int i = 0,n=expr.length;i<n;++i){
          String token = expr[i].trim();
          if (token.length() <= 0){
              continue;
          }
          if (isOperator(token)) {
              double b = (double)pop(stack);
              double a = stack.size() > 0 ? (double)pop(stack) : 0;
              double v = 0;
              switch (token.charAt(0)){
                  case '+':
                    v = a+b;
                    break;
                  case '-':
                    v = a-b;
                    break;
                  case '*':
                    v = a*b;
                    break;
                  case '/':
                    v = a/b;
                    break;
                  case '%':
                    v = a%b;
                    break;
                  case '=':
                    v = b;
                    storeValue(firstVariable, v);
                    break;                  
              }
              stack.add(new Double(v));
          }else{
              if (isAlpha(token.charAt(0))){
                  if (firstVariable.length() <= 0){
                      firstVariable = token;
                  }
                  if (hasValue(token)){
                      stack.add(new Double(getValue(token)));
                  }
              }else{
                  stack.add(new Double(Double.valueOf(token)));
              }
          }
      }
      if (stack.size() != 1){
          throw new IllegalArgumentException();
      }
      return pop(stack);
  }
  
  private void toRPN () {
      List <Character> o = new ArrayList<Character> ();
      
      char [] chars = expr.toCharArray();
      for (char c : chars){
          o.add(new Character(c));
      }
      toRPN(o);
  }
  
  private boolean hasValue(String n) {
      return map.containsKey(n);
  }
  
  private Double getValue(String n) {
      return map.get(n);
  }
  
  private void storeValue(String n,Double v) {
      map.put(n,v);
  }
  
  private boolean isAlpha(char c) {
      return (c >= 'A' && c<='Z') || (c>='a' && c<='z');
  }
  
  private boolean isAlphaNum(char c){
      return (c >= '0' && c<='9') || isAlpha(c);
  }
  private boolean isLeftConnected (char c) {
      switch (c){
          case '+':
          case '-':
          case '=':
            return false;
      }
      return true;
  }
  private void toRPN(List <Character> o){
      List <Character> out = new ArrayList<Character> ();
      List <Character> stack = new ArrayList<Character> ();
      boolean lastAlphaNum = false;
      while ( o.size() > 0){
          char c = (char) o.remove(0);
          if (isAlphaNum(c)){
              if (!lastAlphaNum){
                  out.add( ' ' );
              }
              lastAlphaNum = true;
              out.add(new Character(c));
          }else if (isOperator(""+c)){
              lastAlphaNum=false;
              String op1 = "" + c;              
              boolean shouldPop = false;
              
              if (stack.size() > 0){
                 shouldPop=isLeftConnected(c) ? precedence(peek(stack)+"")<=precedence(op1) : precedence(peek(stack)+"")<precedence(op1);
              }
              while (stack.size() > 0 && isOperator(peek(stack)+"") && shouldPop){
                  out.add(new Character(' '));
                  out.add(new Character(pop(stack)));
                  if (stack.size() > 0){
                    shouldPop=isLeftConnected(c) ? precedence(peek(stack)+"")<=precedence(op1) : precedence(peek(stack)+"")<precedence(op1);
                  }
              }
              stack.add(new Character(c));
          }else if (c =='('){
              lastAlphaNum=false;
              stack.add(new Character(c));
          }else if (c == ')'){
              lastAlphaNum=false;
              while ( (stack.size() > 0) && !peek(stack).equals(new Character('('))){
                  char d = pop(stack);
                  out.add(new Character(' '));
                  out.add(new Character(d));
              }
              if (stack.size() > 0){
                char d = pop(stack);              
              }
          }
      }
      while (stack.size() > 0){
          out.add(new Character(' '));
          out.add(new Character(pop(stack)));
      }

      while ( out.size() > 0){
          exprRPN += out.remove(0);
      }
      
      return;
  }
  private <T> T pop (List<T> stack) {
      if (stack.size() <= 0){
          return null;
      }
      return stack.remove(stack.size()-1);
  }
  private <T> T peek (List<T> stack) {
      if (stack.size() <= 0){
          return null;
      }
      return stack.get(stack.size()-1);
  }
  
  private boolean isOperator(String p) {
      if (p.length()<=0){
          return false;
      }
      switch (p.charAt(0)){
          case '+':
          case '-':
          case '*':
          case '/':
          case '%':
          case '=':
            return true;
      }
      return false;
  }
  
  private int precedence (String operator) {
      char op = operator.charAt(0);
      switch (op) {
          case '*':
          case '/':
          case '%':
            return 0;
          case '+':
          case '-':
            return 1;
          case '=':
            return 2;
      }
      return 3;
  }
  
  private static Deque<String> tokenize(String input) {
      Deque<String> tokens = new LinkedList<>();
      Pattern pattern = Pattern.compile("=>|[-+*/%=\\(\\)]|[A-Za-z_][A-Za-z0-9_]*|[0-9]*(\\.?[0-9]+)");
      Matcher m = pattern.matcher(input);
      while (m.find()) {
          tokens.add(m.group());
      }
      return tokens;
  }
}