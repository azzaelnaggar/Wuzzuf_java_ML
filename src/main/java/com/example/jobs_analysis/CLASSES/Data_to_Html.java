package com.example.jobs_analysis.CLASSES;
import org.apache.commons.codec.binary.Base64;
import org.apache.spark.sql.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class Data_to_Html {
    private static HTMLBuilder builder ;

    public static String data_set(String []head, List<Row> ls){

        builder=new HTMLBuilder(null,true,3,head.length);
        builder.addTableHeader(head);
        for (Row r : ls) {
            String[] s = r.toString().replace("[","").replace("]","")
                    .split(",", head.length);
            builder.addRowValues(s);

        }
        return builder.build();


    }
    public static String view_chart(String path){

        FileInputStream img ;
        try {
            File f= new File(path);
            img = new FileInputStream(f);
            byte[] bytes =  new byte[(int)f.length()];
            img.read(bytes);
            String encodedfile = new String(Base64.encodeBase64(bytes) , "UTF-8");

            return "<div>" +
                    "<img src=\"data:image/png;base64, "+encodedfile+"\" alt=\"Red dot\" />" +
                    "</div>";
        } catch (IOException e) {
            return "error";
        }



    }

}
