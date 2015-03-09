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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Processes an image file and converts it into a thumbnail.
 * Implements Runnable and can be used in a new Thread.
 */
public class Picture implements Runnable{
    private final File IMAGE_FILE;
    private final double SCALE_RATIO;
    private final int THUMBNAIL_WIDTH;
    private final int THUMBNAIL_HEIGHT;
    private final boolean IS_RATIO;
    private BufferedImage image;
    private String imageThumb;

    /**
     * Creates a Picture Object for processing.
     * @param IMAGE_FILE - The image to generate a thumbnail of.
     * @param THUMBNAIL_WIDTH - The desired width of the thumbnail.
     * @param THUMBNAIL_HEIGHT - The desired Height of the thumbnail. If set to 0 the height will scale with width
     * @throws IOException
     */
    public Picture(final File IMAGE_FILE, final int THUMBNAIL_WIDTH, final int THUMBNAIL_HEIGHT) throws IOException {
        this.IMAGE_FILE = IMAGE_FILE;
        this.THUMBNAIL_WIDTH = THUMBNAIL_WIDTH;
        this.THUMBNAIL_HEIGHT = THUMBNAIL_HEIGHT;
        image = loadImage(this.IMAGE_FILE);
        this.SCALE_RATIO = 1;
        IS_RATIO = false;
    }

    /**
     * Creates a Picture Object for processing.
     * @param imageSource - The image to generate a thumbnail of.
     * @param scaleRatio - The percentage to scale the image by.
     * @throws IOException
     */
    public Picture(final File imageSource, final double scaleRatio) throws IOException {
        this.IMAGE_FILE = imageSource;
        this.SCALE_RATIO = scaleRatio;
        image = loadImage(this.IMAGE_FILE);
        this.THUMBNAIL_WIDTH = image.getWidth();
        this.THUMBNAIL_HEIGHT = image.getHeight();
        IS_RATIO = true;
    }

    private String getFileName(File file){
        String name = file.getName();
        int finalPosition = name.lastIndexOf(".");
        if(finalPosition > 0){
            name = name.substring(0, finalPosition);
        }
        return name;
    }

    /**
     * Runs the Picture object
     * Overrides Runnable run() method, can be used in a new thread.
     *
     */
    @Override
    public void run() {
        String thing = IMAGE_FILE.getParentFile().getAbsolutePath();
        File output = new File(thing + "/thumbnails");
        output.mkdir();
        imageThumb = getFileName(IMAGE_FILE) + "_thumb.jpg";
        try {
            ImageIO.write(image, "jpg", new File(output, imageThumb));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void makeThumbnail(){
        if(IS_RATIO) {
            image = scaleImage(image, SCALE_RATIO);
            image.createGraphics();
        }else{
            if(THUMBNAIL_HEIGHT == 0){
                int tempHeight;
                double ratio = aspectRatio(image.getWidth(), image.getHeight());
                tempHeight = (int)(100 * ratio);
                image = scaleImage(image, THUMBNAIL_WIDTH, tempHeight);
            }else{
                image = scaleImage(image, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
                image.createGraphics();
            }
        }
        run();
        System.out.println(imageThumb + " created");
    }
    private double aspectRatio(double width, double height){
        return height / width;
    }

    private BufferedImage loadImage(File src) throws IOException{
        BufferedImage temp = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        try{
            temp = ImageIO.read(src);
        }catch (IOException e){
            System.out.println("Trouble loading file");
        }
        return temp;
    }


    private BufferedImage scaleImage(BufferedImage source, double ratio) {
        final int SCALED_WIDTH = (int) (source.getWidth() * ratio);
        final int SCALED_HEIGHT = (int) (source.getHeight() * ratio);
        final BufferedImage TEMPLATE = getCompatibleImage(SCALED_WIDTH, SCALED_HEIGHT);
        Graphics2D g2d = TEMPLATE.createGraphics();
        double xScale = (double) SCALED_WIDTH / THUMBNAIL_WIDTH;
        double yScale = (double) SCALED_HEIGHT / THUMBNAIL_HEIGHT;
        AffineTransform at = AffineTransform.getScaleInstance(xScale,yScale);
        g2d.drawRenderedImage(source, at);
        g2d.dispose();
        return TEMPLATE;
    }

    private BufferedImage scaleImage(BufferedImage source, final int SCALED_WIDTH, final int SCALED_HEIGHT) {
        final BufferedImage TEMPLATE = getCompatibleImage(SCALED_WIDTH, SCALED_HEIGHT);
        Graphics2D g2d = TEMPLATE.createGraphics();
        double xScale = (double) SCALED_WIDTH / source.getWidth();
        double yScale = (double) SCALED_HEIGHT / source.getHeight();
        AffineTransform at = AffineTransform.getScaleInstance(xScale,yScale);
        g2d.drawRenderedImage(source, at);
        g2d.dispose();
        return TEMPLATE;
    }

    private BufferedImage getCompatibleImage(final int WIDTH, final int HEIGHT) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        return gc.createCompatibleImage(WIDTH, HEIGHT);
    }


}
