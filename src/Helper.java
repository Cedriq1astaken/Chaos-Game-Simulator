import java.util.HashMap;

public class Helper {
    public static int mod(int a, int b){
        return (a % b + b) % b;
    }
    public static HashMap<String, Integer> hashMapBuilder(int proximity, int occurrence){
        HashMap<String, Integer> hash = new HashMap<>();
        hash.put("proximity", proximity);
        hash.put("occurrence", occurrence);
        return hash;
    }
}
