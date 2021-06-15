package com;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * @作者 ChengShi
 * @日期 2021年5月17日
 * @版本 1.0
 * @描述 
 */
public class MouseClick {
	public static void main(String[] args) throws AWTException, InterruptedException, IOException {
		Thread.sleep(1000 * 5);
		Robot robot = new Robot();
		robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
		Thread.sleep(1000 * 240);
		robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
	}
}
