/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Weronika
 */
public class Utils {

    public static String loadResource(String fileName) throws Exception {

        String result;
        InputStream in = Utils.class.getClassLoader().getResourceAsStream("resources/" + fileName);
        Scanner scanner = new Scanner(in, "UTF-8");
        result = scanner.useDelimiter("\\A").next();

        //System.out.println(result);
        return result;

    }

    public static List<String> readAllLines(String fileName) throws Exception {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Utils.class.getClassLoader().getResourceAsStream("resources/" + fileName)))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
    }
    
      public static float[] listToArray(List<Float> list) {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for (int i = 0; i < size; i++) {
            floatArr[i] = list.get(i);
        }
        return floatArr;
    }
        
}
