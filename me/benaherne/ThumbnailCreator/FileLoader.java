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
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Loads all image files from a given directory/folder, Allowed formats are
 * .jpg, .gif, .png, .bmp, /tiff
 */
public class FileLoader {
	private File dir;
	private ArrayList<File> files = new ArrayList<>();

	/**
	 * Create a FileLoader Object using a path to the directory to load.
	 * 
	 * @param path
	 *            - the path to the working directory in String form.
	 */
	public FileLoader(String path) {
		dir = new File(path);
		this.retrieveFiles();
	}

	/* put any allowed formats here */
	private final String[] IMAGE_EXTENSIONS = new String[] { "jpg", "JPG",
			"gif", "GIF", "png", "PNG", "bmp", "BMP", "tiff", "TIFF" };

	/**
	 * Getter method for the ArrayList of files in the given directory.
	 * 
	 * @return files - the files ArrayList.
	 */
	public ArrayList<File> getFiles() {
		return files;
	}

	/* Checks if a file in the given directory is an image. */
	private final FilenameFilter IMAGE_FILTER = new FilenameFilter() {
		public boolean accept(final File dir, final String name) {
			for (final String extension : IMAGE_EXTENSIONS) {
				if (name.endsWith("." + extension)) {
					return (true);
				}
			}
			return (false);
		}
	};

	/**
	 * Adds all valid image files to the internal ArrayList to allow for
	 * processing.
	 */
	public void retrieveFiles() {
		if (dir.isDirectory()) {
			Collections.addAll(files, dir.listFiles(IMAGE_FILTER));
		} else {
			System.out.println("not a directory");
		}
	}

}
