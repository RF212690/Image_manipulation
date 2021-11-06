package com.company;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Scanner;
import java.lang.Math;

public class Main {
    private static BufferedImage image;
    private static File f;

    private static void upsidedown(int width, int height) {
        int gx1;int gx2;int gy1;int gy2;
        int red;int green;int blue;
        int[][] output= new int[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int redx=0;int greenx=0;int bluex=0;
                int redy=0;int greeny=0;int bluey=0;
                for (int k = -1; k <2; k++) {
                    try{gx1=image.getRGB(j-1,i+k);}catch(Exception e){gx1=0;}
                    try{gx2=image.getRGB(j+1,i+k);}catch(Exception e){gx2=0;}
                    try{gy1=image.getRGB(j+k,i-1);}catch(Exception e){gy1=0;}
                    try{gy2=image.getRGB(j+k,i+1);}catch(Exception e){gy2=0;}
                    if(k==0){
                        redx+=2*((gx1>>16)&0xff)-2*((gx2>>16)&0xff);
                        greenx+=+2*((gx1>>8)&0xff)-2*((gx2>>8)&0xff);
                        bluex+=2*(gx1&0xff)-2*(gx2&0xff);
                        redy+=2*((gy1>>16)&0xff)-2*((gy1>>16)&0xff);
                        greeny+=2*((gy1>>8)&0xff)-2*((gy2>>8)&0xff);
                        bluey+=2*(gy1&0xff)-2*(gy2&0xff);
                    }
                    else {
                        redx += ((gx1 >> 16) & 0xff) - ((gx2 >> 16) & 0xff);
                        greenx += ((gx1 >> 8) & 255) - ((gx2 >> 8) & 0xff);
                        bluex += (gx1 & 0xff) - (gx2 & 0xff);
                        redy += ((gy1 >> 16) & 0xff) - ((gy1 >> 16) & 0xff);
                        greeny += ((gy1 >> 8) & 0xff) - ((gy2 >> 8) & 0xff);
                        bluey += (gy1 & 0xff) - (gy2 & 0xff);
                    }
                }
                red=(int)Math.sqrt(redx*redx+redy*redy);
                if(red>255){red=255;}
                green=(int)Math.sqrt(greenx*greenx+greeny*greeny);
                if(green>255){green=255;}
                blue=(int)Math.sqrt(bluex*bluex+bluey*bluey);
                if(blue>255){blue=255;}
                output[j][i]=(output[j][i] | (image.getRGB(j,i)<<24))+(output[j][i] | (red<<16))+(output[j][i] | (green<<8))+(output[j][i] | blue);
            }
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                image.setRGB(j,i,output[j][i]);
            }
        }
    }

    private static void mirror(int width, int height) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width/2; j++) {
                int temp= image.getRGB(j,i);
                image.setRGB(j,i,image.getRGB(width-j-1,i));
                image.setRGB(width-j-1,i,temp);
            }
        }
    }

    private static void greyscale(int width, int height) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int temp=image.getRGB(j,i);
                int average= (((temp>>16) & 0xff)+((temp>>8) & 0xff)+(temp & 0xff))/3;
                temp=0;
                temp=(temp | (average<<16))+(temp | (average<<8))+(temp | average);
                image.setRGB(j,i,temp);
            }
        }
    }

    private static void transparent(int width, int height) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int temp=image.getRGB(j,i);
                int transparency=128;
                temp= temp-((temp>>24)&0xff);
                temp= temp | (transparency<<24);
                image.setRGB(j,i,temp);
            }
        }
    }


    public static void main(String[] args) {
        boolean valid = false;
        Scanner input = new Scanner(System.in);
        String function = "";
        String inputloc="";
        String outputloc="";
        try{
            System.out.println("Insert Input image location: ");
            inputloc= input.next();
        }catch(Exception e){
            System.out.println("Error: "+e);
        }
        try{
            System.out.println("Insert output image location: ");
            outputloc= input.next();
        }catch(Exception e){
            System.out.println("Error: "+e);
        }
        try {
            f = new File(inputloc);
            image = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println("error " + e);
        }
        catch(Exception e){
            System.out.println("Error: "+e);
        }
        int width = image.getWidth();
        int height = image.getHeight();
        while (!valid) {
            try {
                System.out.println("What function do you want to run?\n" +
                        "if you don't know all the possible functions write help");
                function = input.next();
            } catch (Exception e) {
                System.out.println("Error: " + e);
                input.next();
            }
            if (function.equalsIgnoreCase("help")) {
                System.out.println("there are 4 different functions: Upside-down, Greyscale, Mirror and Transparent");
            } else if (function.equalsIgnoreCase("upside-down")) {
                valid = true;
                upsidedown(width, height);
            } else if (function.equalsIgnoreCase("greyscale")) {
                valid = true;
                greyscale(width, height);
            } else if (function.equalsIgnoreCase("mirror")) {
                valid = true;
                mirror(width, height);
            } else if (function.equalsIgnoreCase("transparent")) {
                valid = true;
                transparent(width, height);
            }
            else{
                System.out.println("Not a function");
            }
        }
        try{
            File output= new File(outputloc);
            ImageIO.write(image,"jpg",output);
            System.out.println("Done!");
        }catch(IOException e){
            System.out.println("Error: "+e);
        }
        catch(Exception e){

            System.out.println("error: "+e);
        }

    }
}
