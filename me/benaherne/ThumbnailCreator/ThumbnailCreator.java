/*
 This file is part of Thumbnail Creator.

 Thumbnail Creator is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 2 of the License, or
 (at your option) any later version.

 Thumbnail Creator is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Thumbnail Creator.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.benaherne.ThumbnailCreator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * Creates Picture and Fileloader objects and runs the program.
 * Implements Runnable interface.
 */
public class ThumbnailCreator implements Runnable {
    private ArrayList<File> imageList;
    public final String SOURCE_DIRECTORY;
    private int WIDTH;
    private int HEIGHT;
    private final double RATIO;
    private final boolean IS_RATIO;
    
    public ThumbnailCreator(final String SOURCE_DIRECTORY, final int WIDTH, final int HEIGHT){
        this.imageList = new ArrayList<>();
        this.SOURCE_DIRECTORY = SOURCE_DIRECTORY;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        RATIO = 0;
        IS_RATIO = false;
    }
    
    public ThumbnailCreator(final String SOURCE_DIRECTORY, final double RATIO){
        this.imageList = new ArrayList<>();
        this.SOURCE_DIRECTORY = SOURCE_DIRECTORY;
        this.RATIO = RATIO;
        this.WIDTH = 0;
        this.HEIGHT = 0;
        IS_RATIO = true;
    }
    /**
     * Runs the Thumbnail Creator program, implements Runnable
     * Can be used in a new thread.
     */
    @Override
    public void run() {
        try {
            generateThumbnails();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /* Runs through the ArrayList of files, generates a thumbnail image for each file*/
    private void generateThumbnails() throws IOException {
        for(File file: imageList){
            System.out.println(file.getName());
            Picture pic;
            if(IS_RATIO){
            	pic = new Picture(file, RATIO);
            }else{
            	pic = new Picture(file, WIDTH, HEIGHT);
            }
            pic.makeThumbnail();
        }
        System.out.println("*********** Execution complete ***********");
    }
    
    private static ThumbnailCreator getInput(){
    	String sourceDir = "/";
    	int inputWidth = 100;
    	int inputHeight = 0;
    	double inputRatio;
    	System.out.print("Path to image directory: ");
    	Scanner src = new Scanner(System.in);
    	Scanner height;
    	sourceDir = src.nextLine();
    	
    	System.out.print("\nEnter a thumbnail width(use a decimal to scale using a percentage): ");
    	Scanner width = new Scanner(System.in);
    	if(width.hasNextDouble()){
    		inputRatio = width.nextDouble();
    		src.close();
    		width.close();
    		return new ThumbnailCreator(sourceDir, inputRatio);
    	}else{
	    	inputWidth = width.nextInt();
	    	System.out.print("\rEnter a thumbnail height(use 0 to keep height proportionate to width: ");
	    	height = new Scanner(System.in);
	    	inputHeight = width.nextInt();
    	}
    	src.close();
    	width.close();
    	height.close();
    	return new ThumbnailCreator(sourceDir, inputWidth, inputHeight);
    }
    
    public static void runCLI() throws InterruptedException {
        System.out.println("ThumbnailCreator: Bendat 08/03/15");
        ThumbnailCreator tnc = getInput();
        Thread thread;
        if(new File(tnc.SOURCE_DIRECTORY).exists()){
        	System.out.println("*** FILE EXISTS***");
        }else{
        	System.out.println("*** File not Found, exiting program ***");
        	System.exit(0);
        }
        FileLoader folder = new FileLoader(tnc.SOURCE_DIRECTORY);
        tnc.imageList = folder.getFiles();
        thread = new Thread(tnc);
        thread.start();
    }


}
