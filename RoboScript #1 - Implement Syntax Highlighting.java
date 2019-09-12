import java.util.HashMap;
public class RoboScript {
    private static HashMap <Character,String> map = new HashMap <Character,String>();
    static {
        map.put('F', "pink");
        map.put('L', "red");
        map.put('R', "green");
        map.put('0', "orange");
        map.put('1', "orange");
        map.put('2', "orange");
        map.put('3', "orange");
        map.put('4', "orange");
        map.put('5', "orange");
        map.put('6', "orange");
        map.put('7', "orange");
        map.put('8', "orange");
        map.put('9', "orange");
        map.put('0', "orange");
    }
    
    public static String highlight(String code) {
        String color = "";
        String colored = "";
        boolean tagisopen = false;
        char [] codeChars = code.toCharArray();
        for (int i = 0,n = codeChars.length;i<n;++i){
            if (map.containsKey(codeChars[i])){
                String newColor = map.get(codeChars[i]);
                if (newColor.equals(color) == false){
                    color = newColor;
                    if (tagisopen){
                        colored+="</span>";
                    }
                    tagisopen = true;
                    colored += "<span style=\"color: " + color + "\">" + codeChars[i];
                }else{
                    colored += codeChars[i];
                }
            }else{
                if (tagisopen){
                    colored+="</span>";
                }
                tagisopen = false;
                color = "";
                colored += codeChars[i];
            }
        }
        if (tagisopen){
            colored+="</span>";
        }
        return colored;
    }

}