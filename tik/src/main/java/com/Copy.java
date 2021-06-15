package com;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * @作者 ChengShi
 * @日期 2021年4月24日
 * @版本 1.0
 * @描述 拷贝类
 */
public class Copy extends JFrame{
	private static final long serialVersionUID = 1L;
	private final String NULLSTR = "";
	private static String IN = null;
	private static String OUT = null;
	private static boolean isRun = false;
	private static short timeOut = Short.MAX_VALUE;
	public static void main(String[] args) {new Copy();}
	public Copy() {init();}
	private void init() {
		this.setSize(300, 200);
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				try {copyDir(IN, OUT);} catch (Throwable e1) {}
				finally {System.exit(0);}
			}
		});
		this.setTitle("定时拷贝文件夹");
		this.setLocationRelativeTo(null);
		this.setLayout(null);
		JTextField in = getText(20);
		JTextField out = getText(70);
		JTextField time = getText(110);
		time.setEditable(true);
		this.add(in);
		this.add(out);
		this.add(time);
		this.add(getButton("输入", 20, in));
		this.add(getButton("输出", 70, out));
		this.add(getRunButton(110, time));
		this.repaint();
		new Time().start();
	}
	
	/*文本组件*/
	private JTextField getText(int top) {
		JTextField jTextField = new JTextField();
		jTextField.setSize(180, 30);
		jTextField.setLocation(10, top);
		jTextField.setEditable(false);
		return jTextField;
	}
	
	/*选择文件按钮*/
	private JButton getButton(final String text, int top, final JTextField field) {
		JButton button = new JButton();
		button.setSize(70, 30);
		button.setLocation(200, top);
		button.setText(text);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setApproveButtonText("确认");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setDialogTitle("请选择" + text + "文件夹...");
				int showOpenDialog = chooser.showOpenDialog(null);
				if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
					field.setText(chooser.getSelectedFile().getAbsolutePath());
				}
				if ("输入".equals(text)) {IN = field.getText();}
				else {OUT = field.getText();}
			}
		});
		return button;
	}
	
	/*运行按钮*/
	private JButton getRunButton(int top, final JTextField field) {
		final JButton button = new JButton();
		button.setSize(70, 30);
		button.setLocation(200, top);
		button.setText("开始");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {timeOut = Short.parseShort(field.getText());} catch (Exception e2) {}
				isRun = !isRun;
				synchronized (Time.class) {Time.class.notifyAll();}
				if (isRun) {button.setText("停止");}
				else {button.setText("开始");}
				try {copyDir(IN, OUT);} catch (Throwable e1) {}
			}
		});
		return button;
	}
	
	/**
	 * @作者 ChengShi
	 * @日期 2021年4月24日
	 * @版本 1.0
	 * @描述 定时拷贝线程
	 */
	private class Time extends Thread{
		@Override
		public void run() {
			while(true) {
				try {
					synchronized (Time.class) {Time.class.wait(timeOut * 1000);}
					if (isRun) {copyDir(IN, OUT);}
				} catch (Throwable e) {}
			}
		}
	}
	
	/**
	 * @描述 拷贝文件夹
	 * @param inDir 输入文件夹
	 * @param outDir 输出文件夹
	 * @param notEndWidth 不拷贝包含此后缀的文件
	 * @throws Exception
	 */
	private void copyDir(String inDir, String outDir, String...notEndWidth) throws Exception{
		if (inDir == null || outDir == null) {return;}
		File inDirFile = new File(inDir);
		File outDirFile = new File(outDir);
		if (!inDirFile.exists() || inDirFile.isFile()) {throw new FileNotFoundException("输入的文件夹不存在或者非文件夹！");}
		if (!outDirFile.exists()) {outDirFile.mkdirs();}
		
		outDir = outDir + File.separator;
		notEndWidth = notEndWidth == null ? new String[0] : notEndWidth;
		File[] listFiles = inDirFile.listFiles();
		for (File file : listFiles) {copyDir(file, outDir, NULLSTR, notEndWidth);}
	}
	private void copyDir(File inFile, String outDir, String curPath, String...notEndWidth) throws Exception{
		curPath += inFile.getName();
		if (inFile.isDirectory()) {
			curPath += File.separator;
			File[] listFiles = inFile.listFiles();
			for (File file : listFiles) {copyDir(file, outDir, curPath, notEndWidth);}
		}else{
			for (String NEW : notEndWidth) {if (inFile.getName().endsWith(NEW)) {return;}}
			copyFile(inFile.getAbsolutePath(), outDir + curPath);
		}
	}
	
	/**
	 * @描述 拷贝文件
	 * @param inFilePathName 输入文件地址与名称
	 * @param outFilePathName 输出文件地址与名称
	 * @throws Exception
	 */
	private void copyFile(String inFilePathName, String outFilePathName) throws Exception{
		if (inFilePathName == null || outFilePathName == null) {return;}
		File inFile = new File(inFilePathName);
		File outFile = new File(outFilePathName);
		if (!inFile.exists() || inFile.isDirectory()) {throw new FileNotFoundException("输入的文件不存在或者非文件！");}
		if (!outFile.getParentFile().exists()) {outFile.getParentFile().mkdirs();}
		/*零拷贝*/
		try(FileInputStream inputStream = new FileInputStream(inFile);FileOutputStream outputStream = new FileOutputStream(outFile)){
			inputStream.getChannel().transferTo(0, inFile.length(), outputStream.getChannel());
		}
	}
}
