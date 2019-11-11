package control;

import robot.Robot;

//Robot Assignment for Programming 1 s2 2019
//Adapted by Caspar and Ross from original Robot code written by Dr Charles Thevathayan
public class RobotControl implements Control
{
	// we need to internally track where the arm is
	private int height = Control.INITIAL_HEIGHT;
	private int width = Control.INITIAL_WIDTH;
	private int depth = Control.INITIAL_DEPTH;
	private int leftHeight = 0;
	private int rightHeight = 0;
	private int maxBarHeight = 0;
	private int leftIdx = 0;
	private int rightIdx = 0;
	private int currentCol = 0;
	// Direction of the movement
	// 0 is from left to right
	// 1 is from right to left
	private int direction = 0; 

	private int[] barHeights;
	private int[] blockHeights;

	private Robot robot;

	// called by RobotImpl
	@Override
	public void control(Robot robot, int barHeightsDefault[], int blockHeightsDefault[])
	{
		this.robot = robot;

		// some hard coded init values you can change these for testing
		this.barHeights = new int[] { 3, 4, 1, 5, 2, 3, 2, 6 };
		this.blockHeights = new int[] { 3, 2, 1, 2, 1, 1, 2, 2, 1, 1, 2, 1, 2, 3 };
		
		//this.barHeights = new int[] { 6, 6, 6, 6 };
		//this.blockHeights = new int[] { 1, 1 };

		// FOR SUBMISSION: uncomment following 2 lines
		//this.barHeights = barHeightsDefault;
		//this.blockHeights = blockHeightsDefault;

		// Compute the initial heights of block piles
		for (int i = 0; i < this.blockHeights.length; i++) {
			if (i % 2 == 0) leftHeight += this.blockHeights[i];
			else rightHeight += this.blockHeights[i];
		}
		
		// Get the maximum height from the bars
		maxBarHeight = 0;
		for (int h : this.barHeights) {
			if (h > maxBarHeight) maxBarHeight = h;
		}
		
		height = Math.max(rightHeight, leftHeight);
		height = Math.max(height, maxBarHeight) + 1;
		
		// initialise the robot
		robot.init(this.barHeights, this.blockHeights, height, width, depth);

		// a simple private method to demonstrate how to control robot
		// note use of constant from Control interface
		// You should remove this method call once you start writing your code
		//extendToWidth(Control.MAX_WIDTH);
		

		// ADD ASSIGNMENT PART A METHOD CALL(S) HERE
		moveBlocks();
	}

	// simple example method to help get you started
	private void extendToWidth(int width)
	{
		while (this.width < width)
		{
			robot.extend();
			this.width++;
		}
	}

	// WRITE THE REST OF YOUR METHODS HERE!
	private void moveBlocks() {
		
		// Starts from the left column
		extendToWidth(0);
		
		if (blockHeights.length % 2 == 0) {
			rightIdx = blockHeights.length - 1;
			leftIdx = blockHeights.length - 2;
		} else {
			rightIdx = blockHeights.length -2;
			leftIdx = blockHeights.length - 1;
		}
		
		
		/* Keep moving the blocks until two columns are empty */
		while (leftHeight > 0 || rightHeight > 0) {
			if (leftIdx >= 0) moveLeftBlock();		
			if (rightIdx >= 0) moveRightBlock();
		}
	}
	
	private void getNextCol() {
		// Move from left to right
		if (direction == 0 ) {
			if (currentCol < barHeights.length - 1) {
				currentCol++;
			}
			else {
				direction = 1;
			}
		} 
		// Move from right to left
		else {
			if (currentCol > 0) {
				currentCol--;
			} else {
				direction = 0;
			}
		}
	}
	
	private void updateHeight() {
		int maxHeight = Math.max(rightHeight, leftHeight);
		maxHeight = Math.max(maxHeight, maxBarHeight) + 1;
		if (maxHeight > height) {
			for (int h = height; h < maxHeight ; h++) robot.up();
		} else {
			for (int h = height; h > maxHeight; h--) robot.down();
		}
		height = maxHeight;
	}
	
	private void dropBlock(int blockHeight) {
		robot.drop();
		barHeights[currentCol] += blockHeight;
		if (barHeights[currentCol] > maxBarHeight) maxBarHeight = barHeights[currentCol];
		depth = height - barHeights[currentCol] - 1;
		width = currentCol + 2;
	}
	
	// Compute the next height upto a given column
	private int getNextHeight(int column, int blockHeight) {
		int nextHeight = leftHeight;
		for (int i = 0; i <= column; i++) {
			if (nextHeight < barHeights[i] + blockHeight) nextHeight = barHeights[i] + blockHeight;
		}
		return nextHeight + 1;
	}
	
	// Move the right block
	private void moveRightBlock() {
		// Move the hand to the top level
		for (int d = depth; d > 0; d--) robot.raise();
		
		// Adjust the height
		updateHeight();
		
		// Get the block from the right column
		extendToWidth(Control.MAX_WIDTH);
		
		// Move to the top of the right column
		for (int h = height -1 ; h > rightHeight; h--) {
			robot.lower();
		}
		
		// Pick up the block
		robot.pick();
		int blockHeight = blockHeights[rightIdx];
		
		// Move the hand to the top height
		for (int h = rightHeight + 1;  h < height; h++) robot.raise();
		rightHeight -= blockHeights[rightIdx];
		rightIdx -= 2;
		
		// Move to the target column
		for (int w = width; w > currentCol + 2; w--) { 
			int nextHeight = getNextHeight(w-3, blockHeight);
			if (nextHeight > height) {
				for (int h = height; h < nextHeight; h++) robot.up();
				height = nextHeight;
			}			
			robot.contract();
		}
		
		// Move the block to the top of target column
		for (int h = height - 1 ; h > barHeights[currentCol] + blockHeight; h--) robot.lower();
		
		// Drop the block
		dropBlock(blockHeight);
		
		// Point to the next target
		getNextCol();
		
	}
	
	// Move the left block
	private void moveLeftBlock() {
		// Move the hand to the top level
		for (int d = depth; d > 0; d--) { 
			robot.raise();
		}
		
		// Move to the left column
		for (int w = width; w > 1; w--) {
			robot.contract();
		}
		
		// Move to the top of the left column
		for (int h = height -1 ; h > leftHeight; h--) {
			robot.lower();
		}
		
		robot.pick();
		int blockHeight = blockHeights[leftIdx];
		
		// Move the hand to the top height
		for (int h = leftHeight + 1;  h < height; h++) robot.raise();
		leftHeight -= blockHeights[leftIdx];
		leftIdx -= 2;
		
		// Move to the target column
		for (int w = 0; w < currentCol + 1; w++) {
			int nextHeight = getNextHeight(w, blockHeight);
			if (nextHeight > height) {
				for (int h = height; h < nextHeight; h++) robot.up();
				height = nextHeight;
			}
			robot.extend();
		}
		
		// Move the block to the top of target column
		for (int h = height - 1 ; h > barHeights[currentCol] + blockHeight; h--) robot.lower();
		
		//Drop the block
		dropBlock(blockHeight);
		
		// Point to the next target
		getNextCol();
				
	}
	

}
