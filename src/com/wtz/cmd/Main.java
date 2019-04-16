package com.wtz.cmd;

import com.wtz.cmd.Utils.StringUtil;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        System.out.println("Start filtering...");

        int needPramCount = 3;
        if (args == null || args.length < needPramCount) {
            System.out.println("Missing parameters!");
            System.out.println("Usage: java -jar CmdFilter.jar src_file out_file filter_parameters");
            return;
        }

        for (int i = 0; i < needPramCount; i++) {
            if (StringUtil.isEmpty(args[i])) {
                System.out.println("parameters[" + i + "] is empty");
                return;
            }
        }

        filterPrint(args);
    }

    public static void filterPrint(String[] args) {
        BufferedReader reader = null;
        FileWriter writer = null;
        Map<String, Integer> countMap = new LinkedHashMap<>();
        String lineEnd = System.getProperty("line.separator");

        try {
            FileInputStream fin = new FileInputStream(args[0]);
            BufferedInputStream bis = new BufferedInputStream(fin);
            reader = new BufferedReader(new InputStreamReader(bis));

            writer = new FileWriter(args[1], false);

            String tempRead = reader.readLine();
            while (tempRead != null) {
                for (int i = 1; i < args.length; i++) {
                    if (tempRead.contains(args[i])) {
                        Integer count = countMap.get(args[i]);
                        count = count == null ? 1 : count + 1;
                        countMap.put(args[i], count);

                        System.out.println(tempRead);
                        writer.write(tempRead);
                        writer.write(lineEnd);
                        writer.flush();
                        break;
                    }
                }
                tempRead = reader.readLine();
            }

            StringBuilder builder = new StringBuilder();
            builder.append("\nThe statistical results are as follows:\n");
            for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
                builder.append("[");
                builder.append(entry.getKey());
                builder.append("]: ");
                builder.append(entry.getValue());
                builder.append("\n");
            }
            String result = builder.toString();
            System.out.println(result);
            writer.write(result);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
