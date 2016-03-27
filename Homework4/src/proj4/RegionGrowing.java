 package proj4;

import java.util.ArrayList;
import java.util.Stack;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * This program performs image segmentation using a simple
 * region growing algorithm. The user needs to select seed points
 * in each region that should be detected (at least one seed point
 * required in the region). A region is defined as the adjacent pixels
 * with very similar grayscale values.   
 * 
 * Once the algorithm is run each region is shown in a grayscale value
 * corresponding to the seed that originated that region. All pixels
 * that do not belong to any of the regions are shown in dark red color. 
 * 
 * The user selects the seeds by clicking on the image with a mouse. 
 * The program responds to the following key strokes
 * r - resets the image to the original
 * x - select random seed points
 * d - run the region growing algorithm (using depth first search) and show the results 
 * b - run the region growing algorithm (using breadth first search) and show the results 
 * 
 * 
 * @author Joanna Klukowska & Kevin Naughton Jr.
 *
 */
@SuppressWarnings("serial")
public class RegionGrowing extends PApplet {
	//image objects 
	private PImage img;
	private PImage imgCopy;
	//x and y coordinates of pixels, used in various loops 
	private int x, y;
	//array to store original copy of the "pixels" of the image being segmented
	private int[] imagePixelsCopy;
	//color used for 
	private int back = color(100,0,0);
	//arrays list used for storing the locations of seeds
	ArrayList<Integer> seeds; 
	//list of all the image file names (FEEL FREE TO MODIFY THIS ARRAY) 
	private  String [] allFileNames = {"ChessBoard.png", "BW_Pattern.png",
			"Gray_Pattern.jpg", "Lighthouse.jpg", "brain_image_MRI.jpg",
			"Head_CT_scan.jpg", "CT_image.jpg", "hubble1.png",
			"cameraman.jpg", "lightning_bolt.jpg", "pollen.jpg"};
	//list of the image files used for testing - DO NOT MODIFY
	String [] testFileNames = {"ChessBoard.png", "BW_Pattern.png",
			"Gray_Pattern.jpg", "Lighthouse.jpg", "brain_image_MRI.jpg",
			 "cameraman.jpg"};
	//list of the seeds corresponding to testFileNames - DO NOT MODIFY 
	int [][] testSeeds = { {3821, 54226, 55445, 3617, 26029, 26060, 33014, 33466},
			{7684, 19715, 24973, 42713, 31122},
			{78291, 68464, 83523, 76846, 55080, 46308, 37577, 45298},
			{42923, 81143, 185800},
			{127635, 183592, 264109, 157737, 188793, 323983, 382185, 387104, 368337, 395943},
			{23117, 42804, 12913, 29300, 50288, 47533, 40326, 24217, 32092, 27795}
			};
	
	//name of the image file to be used in the interactive run
	String fileName = allFileNames[3];
	
	//flags controlling the mode of the program
	//  if false, run in interactive mode
	//  if either is true, run in the corresponding test mode 
	public static boolean RUN_TESTS_DFS = false; 
	public static boolean RUN_TESTS_BFS = false;
	
	
	/**
	 * This method is ignored when the program is run as an applet.
	 * It is provided to allow for command line runs as an application. 
	 * @param args
	 */
	public static void main(String args[]) {
		if (args.length > 0) {
			System.out.println("Running in test mode. Running both tests. "); 
			RUN_TESTS_DFS = true;
			RUN_TESTS_BFS = true; 
		}
		PApplet.main(new String[] { "RegionGrowing" });
    }
	

	
	/**
	 * Setup to program to either run in the interactive mode or
	 * in the test mode. 
	 * This method is run once to setup the window and initialize 
	 * the data fields. 
	 */
	public void setup() {
		if( RUN_TESTS_DFS )
			runTestsDFS();
		if (RUN_TESTS_BFS )
			runTestsBFS();
		if (!RUN_TESTS_DFS && !RUN_TESTS_BFS)
			runInteractive(); 
		
	}

	/**
	 * Runs the actual applications. The method is runs in a continuous 
	 * loop (loop provided in other code). It provides the graphical
	 * user interface for the  interactive mode of the program. 
	 */
	public void draw() {
		
		//display seeds in the image
		fill(250,0,0); 
		stroke(250,0,0); 
		for (int i =0; i < seeds.size(); i++){
			int seed = seeds.get(i);
			ellipse(seed%img.width, seed/img.width, 2,2);
		}
		
		//reset the image to original and remove the seeds
		if (keyPressed == true && key == 'r') {
			imageReset(pixels, imagePixelsCopy);
			seeds = new ArrayList<Integer>();
		}
		
		//generate 10 random seeds within the image
		if (keyPressed == true && key == 'x'&&seeds.size() < 10) {
			for (int i = 0; i < 10; i++) {
				seeds.add( (int)random( pixels.length)); 
			}
		}
		
		//apply region growing algorithm ( depth first search)
		if (keyPressed == true && key == 'd') {
			//set all the pixels to the background color
			imageBlank(pixels);
			//grow a region around each seed
			while (!seeds.isEmpty() ) {
				int seed = seeds.remove(seeds.size()-1);
				int value = (int) red(imagePixelsCopy[seed]);
				getRegionDFS( seed%img.width, seed/img.width, value, pixels, imagePixelsCopy);
				updatePixels();
			}
		}
		
		//apply region growing algorithm ( breadth first search)
		if (keyPressed == true && key == 'b') {
			//set all the pixels to the background color
			imageBlank(pixels);
			//grow a region around each seed
			while (!seeds.isEmpty() ) {
				int seed = seeds.remove(seeds.size()-1);
				int value = (int) red(imagePixelsCopy[seed]);
				getRegionBFS( seed%img.width, seed/img.width, value, pixels, imagePixelsCopy);
				updatePixels();
			}
		}
	} //end draw() 
	
	/** 
	 * Handles mouse click event by adding a seed corresponding to the 
	 * x and y coordinates of the cursor. 
	 */
	public void mouseClicked() {
		int index = mouseY * img.width + mouseX;
		seeds.add(index);
	}
	
	/**
	 * Resets the current image to the original. 
	 * @param pixels the current pixels array
	 * @param imagePixelsCopy the original pixels array
	 */
	public void imageReset ( int [] pixels, int [] imagePixelsCopy ) { 
		for (int w = 0; w < img.width; w++) {
			for (int h = 0; h < img.height; h++) {
				pixels[h*img.width + w] = imagePixelsCopy[h*img.width + w];
			}
		}
		updatePixels();
	}
	
	/**
	 * Sets all the pixels in the current image to the background color.
	 * @param pixels the pixels of the current image
	 */
	public void imageBlank ( int [] pixels ) { 
		for (int h = 0; h < img.height; h++) {
			for (int w = 0; w < img.width; w++) {
				pixels[h*img.width + w] = back ;
			}
		}
	}
	
	/**
	 * Sets a pixel in a pixels array to a particular gray level value
 	 * @param x coordinate of the pixel to be set 
	 * @param y y coordinate of the pixel to be set
	 * @param grayLevel gray level to be assigned to the pixel
	 * @param pixels array of pixels in which a pixel is modified 
	 */
	public void setPixelColor( int x, int y, float grayLevel, int [] pixels ) { 
		pixels[y*img.width + x] = color ( grayLevel, grayLevel, grayLevel);
	}	
	
	/**
	 * Retrieves the value of a specified pixel from the pixels array. 
	 * Assumes that all three channels (R, G, B) have the same values. 
	 * @param x x coordinate of the pixel
	 * @param y y coordinate of the pixel
	 * @return  the gray level of the pixel 
	 */
	public float getPixelColor( int x, int y, int [] pixels ) { 
		return red(pixels[y*img.width + x]);
	}
	
	
	/**
	 * Uses a depth first search approach to grow a region around a specified seed. All pixels
	 * added to the region have gray level values that fall within a +/- threshold of the 
	 * gray level value of the seed. The threshold is computed as a standard deviation
	 * of the pixels in the 11x11 neighborhood of the seed. 
	 * @param x x coordinate of the seed 
	 * @param y y coordinate of the seed
	 * @param value gray level to assign to the pixels in the generated region
	 * @param pixels array of current pixels to which the region is added
	 * @param imagePixelsCopy array of original pixels based on which the region is 
	 *                 calculated 
	 */
	public void getRegionDFS( int x, int y, int value, int [] pixels, int [] imagePixelsCopy ) {
		int px, py, index;
		//compute the threshold based on the standard deviation of the grayscale values
		//of the pixels in the 11x11 neighborhood of the seed
		float threshold = 0;
		for (int nx = x - 5; nx < x + 5; nx++) {
			for (int ny = y - 5; ny < y + 5; ny++) {
				if (ny < 0 || ny >= img.height || nx < 0 || nx >= img.width) continue;
				threshold +=  red(imagePixelsCopy[ny*img.width+nx]);
			}
		}
		threshold = 2 * (float)  Math.sqrt(threshold/121);
		
		//apply the depth first search algorithm for region growing
		
		NewStack<Integer> pixelLocations = new NewStack<Integer>();
		
		float pixDifference;
		pixelLocations.push(y*img.width + x );
		while (!pixelLocations.empty()) {
			index = pixelLocations.pop();
			px = index % img.width;
			py = index / img.width;
			pixels[index] = color(value,value,value);
			
			if (px!=0 && px!=img.width-1 && py!=0 && py!=img.height-1) {
				for (int nx = px-1; nx <= px+1; nx++) {
					for (int ny = py-1; ny <= py+1; ny++) {
						pixDifference = Math.abs( red(imagePixelsCopy[y*img.width+x]) - red(imagePixelsCopy[ny*img.width+nx]));
						if ( pixDifference <= threshold && pixels[ny*img.width+nx] == back ) {
							pixelLocations.push(ny*img.width+nx);
						}
					}
				}			
			}
		}		
	} //end getRegionDFS
	
	/**
	 * Uses a breadth first search approach to grow a region around a specified seed. All pixels
	 * added to the region have gray level values that fall within a +/- threshold of the 
	 * gray level value of the seed. The threshold is computed as a standard deviation
	 * of the pixels in the 11x11 neighborhood of the seed. 
	 * @param x x coordinate of the seed 
	 * @param y y coordinate of the seed
	 * @param value gray level to assign to the pixels in the generated region
	 * @param pixels array of current pixels to which the region is added
	 * @param imagePixelsCopy array of original pixels based on which the region is calculated 
	 */
	public void getRegionBFS( int x, int y, int value, int [] pixels, int [] imagePixelsCopy ) {
		/**
		 * @author Kevin Naughton Jr.
		 */
		
		//declare variables px, py, and index
		int px, py, index; 
		
		//create a queue
		NewQueue<Integer> pixelLocations = new NewQueue<Integer>(); //stores pixel locations
		
		//compute the threshold based on the standard deviation of the grayscale values
		//of the pixels in the 11x11 neighborhood of the seed
		float threshold = 0;
		for (int nx = x - 5; nx < x + 5; nx++) {
			for (int ny = y - 5; ny < y + 5; ny++) {
				if (ny < 0 || ny >= img.height || nx < 0 || nx >= img.width) continue;
				threshold +=  red(imagePixelsCopy[ny*img.width+nx]);
			}
		}
		threshold = 2 * (float) Math.sqrt(threshold/121);
		
		//apply the breadth first search algorithm for region growing

		pixelLocations.enqueue(y*img.width + x );
		float pixDifference;
		while(!pixelLocations.empty()) {
			index = pixelLocations.dequeue();
			px = index % img.width;
			py = index / img.width;
			if (px!=0 && px!=img.width-1 && py!=0 && py!=img.height-1) {
				for (int nx = px-1; nx <= px+1; nx++) {
					for (int ny = py-1; ny <= py+1; ny++) {
						pixDifference = Math.abs( red(imagePixelsCopy[y*img.width+x]) - red(imagePixelsCopy[ny*img.width+nx]));
						if ( pixDifference <= threshold && pixels[ny*img.width+nx] == back ) {
							pixelLocations.enqueue(ny*img.width+nx);
							pixels[ny*img.width+nx] = color(value,value,value);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Setup the interactive run of the program. 
	 */
	public void runInteractive() {
		// load the image from file
		img = loadImage(fileName); 

		// set the canvas size to match the image size
		size(img.width, img.height);
		
		//print an error message if image was not loaded correctly and terminate
		if (img == null) {
			System.err.print("The image you are trying to use is inaccessible.\n" +
						"If using Eclipse, put your image in the src directory, \n" +
						"or specify the full path.\n\n") ;
			exit();
		}
		
		//if image is too large for the screen, resize it
		float resizeFactor;
		if (img.height > 0.8*displayHeight ) {
			resizeFactor = (0.8f*displayHeight) / img.height;
			img.resize((int)(img.width * resizeFactor), 
					(int)(img.height*resizeFactor) );
		}
		if (img.width > 0.8*displayWidth ) {
			resizeFactor = (0.8f*displayWidth) / img.width;
			img.resize((int)(img.width * resizeFactor), 
					(int)(img.height*resizeFactor) );
		}

		//convert image to grayscale
		img.filter(GRAY);
			
		// get the pixels from the image into a pixels[] array of colors
		loadPixels();
		
		//copy the image pixels to a duplicate array for manipulation
		imagePixelsCopy = new int [img.pixels.length];
		for (int i =0; i < img.pixels.length; i++){
			imagePixelsCopy[i] = img.pixels[i];
		}
		//instantiate array of seeds
		seeds = new ArrayList<Integer>();
		
		//display image in the canvas
		image(img, 0, 0);
		
	} //end runInteractive
	
	
	/**
	 * Setup and run the test mode of the program. The depth first search approach to 
	 * region growing is applied to several predefined images (testFileNames array) using
	 * the predefined seeds (testSeeds array). The resulting images are saved to the
	 * disk with names matching the input files, followed by "_DFS_output" followed by the 
	 * original file extension. 
	 */
	public void runTestsDFS() {
		//generate the directory name to which the files are saved 
		String outDir = (System.getProperty("user.dir")).replace("bin", "src") +
				System.getProperty("file.separator");
		
		//run the test for all images in the testFileNames array
		for (int im = 0; im < testFileNames.length; im++) {
			
			String file = testFileNames[im];
			img = loadImage(file); 
	
			// set the canvas size to match the image size
			size(img.width, img.height);
				
			if (img == null) {
				System.err.printf("The image %s  is inaccessible.\n", file) ;
			}
			//convert image to grayscale
			img.filter(GRAY);
			// get the pixels from the image into a pixels[] array of colors
			loadPixels();
			//copy the image pixels to a duplicate array for manipulation
			imagePixelsCopy = new int [img.pixels.length];
			for (int i =0; i < pixels.length; i++){
				imagePixelsCopy[i] = img.pixels[i];
			}
			//get the seeds to be used for the image 
			seeds = new ArrayList<Integer>();
			for (int i =0; i < testSeeds[im].length; i++)
				seeds.add(testSeeds[im][i]);
			
			//set image to the background (non-region) color
			imageBlank(img.pixels);
			//generate a region for each seed
			while (!seeds.isEmpty() ) {
				int seed = seeds.remove(seeds.size()-1);
				int value = (int) red(imagePixelsCopy[seed]);
				getRegionDFS( seed%img.width, seed/img.width, value, img.pixels, imagePixelsCopy);
			}
			//generate the name of the output file
			String outFile = outDir + file.replace(".", "_DFS_output.");
			//save the output file
			img.updatePixels();
			img.save(outFile);
		
		}
		//terminate the program 
		exit();
	} //end runTestsDFS()
	
	/**
	 * Setup and run the test mode of the program. The breadth first search approach to 
	 * region growing is applied to several predefined images (testFileNames array) using
	 * the predefined seeds (testSeeds array). The resulting images are saved to the
	 * disk with names matching the input files, followed by "_BFS_output" followed by the 
	 * original file extension. 
	 */
	public void runTestsBFS() {
		//generate the directory name to which the files are saved 
		String outDir = (System.getProperty("user.dir")).replace("bin", "src") +
				System.getProperty("file.separator");
		
		//run the test for all images in the testFileNames array
		for (int im = 0; im < testFileNames.length; im++) {
			
			String file = testFileNames[im];
			img = loadImage(file); 
	
			// set the canvas size to match the image size
			size(img.width, img.height);
				
			if (img == null) {
				System.err.printf("The image %s  is inaccessible.\n", file) ;
			}
			//convert image to grayscale
			img.filter(GRAY);
			// get the pixels from the image into a pixels[] array of colors
			loadPixels();
			//copy the image pixels to a duplicate array for manipulation
			imagePixelsCopy = new int [img.pixels.length];
			for (int i =0; i < pixels.length; i++){
				imagePixelsCopy[i] = img.pixels[i];
			}
			//get the seeds to be used for the image 
			seeds = new ArrayList<Integer>();
			for (int i =0; i < testSeeds[im].length; i++)
				seeds.add(testSeeds[im][i]);
			
			//set image to the background (non-region) color
			imageBlank(img.pixels);
			//generate a region for each seed
			while (!seeds.isEmpty() ) {
				int seed = seeds.remove(seeds.size()-1);
				int value = (int) red(imagePixelsCopy[seed]);
				getRegionBFS( seed%img.width, seed/img.width, value, img.pixels, imagePixelsCopy);
			}
			//generate the name of the output file
			String outFile = outDir + file.replace(".", "_BFS_output.");
			//save the output file
			img.updatePixels();
			img.save(outFile);
		
		}
		//terminate the program 
		exit();
	} //end runTestsDFS()
	
}//end class
