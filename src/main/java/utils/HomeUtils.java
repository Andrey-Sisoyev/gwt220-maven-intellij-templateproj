package utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

public class HomeUtils {
    protected HomeUtils() {}
    public static <E> List<E> it2list(Iterator<E> it, List<E> list) {
        while(it.hasNext()) list.add(it.next());
        return list;
    }

    public static String prettyPrintMap(Map map, String name ) {
        String ret = name + " {";
        for(Map.Entry me : (Set<Map.Entry>) map.entrySet()) {
            ret += "\n   KEY : " + me.getKey().toString();
            ret += "\n   VAL : " + me.getValue().toString();
            ret += "\n";
        }
        ret += "\n}";
        return ret;
    }
    
    /* 
    // GWT won't eat this 
    public static String printStackTrace_ToStr(Throwable e) {
        StringWriter writerStr = new StringWriter();
        PrintWriter myPrinter = new PrintWriter(writerStr);
        e.printStackTrace(myPrinter);
        return writerStr.toString();
    }*/
}
