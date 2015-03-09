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

/**
 * Creates Picture and Fileloader objects and runs the program.
 * Implements Runnable interface.
 */
public class ThumbnailCreator implements Runnable {
    private ArrayList<File> imageList;

    public ThumbnailCreator(){
        this.imageList = new ArrayList<>();
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
            Picture pic = new Picture(file, 100, 0);
            pic.makeThumbnail();
        }
        System.out.println("*********** Execution complete ***********");
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("ThumbnailCreator: Bendat 08/03/15");
        if (args.length < 1) {
            System.err.println("No valid image file found. Please provide one.");
            System.exit(0);
        }
        ThumbnailCreator tnc = new ThumbnailCreator();
        String sourceFile = args[0];
        Thread thread;
        System.out.println("*** File located: execution commencing ***");
        FileLoader images = new FileLoader(sourceFile);
        images.retrieveFiles();
        tnc.imageList = FileLoader.getFiles();
        thread = new Thread(tnc);
        thread.start();
    }


}
