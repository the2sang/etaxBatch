package kr.co.kepco.etax30.selling.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class EtaxUtil {

    public static boolean genFileCreate(String fileName, String fileData, String encode) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));

            writer.write(fileData);
            return true;

        } catch (IOException ioe) {
            System.out.println("파일 쓰기 에러..!");
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean genFileCreate2(String fileName, String fileData, String encode) {

        FileOutputStream out = null;

        try {

            out = new FileOutputStream(fileName);
            BufferedOutputStream bout = new BufferedOutputStream(out);

            bout.write(fileData.getBytes(encode));
            bout.close();
            bout.flush();
            return true;


        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        } catch (Exception e) {
            try {
                out.close();
            } catch (Exception var13) {
                out = null;
            }
        }
        return false;
    }

    public static boolean genFileCreate3(String fileName, String fileData, String encode) {

        FileOutputStream out = null;

        try {

            out = new FileOutputStream(fileName);

            FileChannel fileChannel = out.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.wrap(fileData.getBytes(encode));
            fileChannel.write(byteBuffer);
            fileChannel.close();

            return true;


        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        } catch (Exception e) {
            try {
                out.close();
            } catch (Exception var13) {
                out = null;
            }
        }
        return false;
    }

}
