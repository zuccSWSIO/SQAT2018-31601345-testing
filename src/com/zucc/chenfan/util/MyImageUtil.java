package com.zucc.chenfan.util;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.io.*;
import java.sql.Blob;

import javax.imageio.ImageIO;

/**
 * 
 * 项目名称：PetServiceManagementSystem 类名称：ImageUtil 类描述： 该类封装了所有和图片操作有关的功能
 * 创建人：Administrator 创建时间：2018年9月7日 下午3:14:50 修改人：Administrator 修改时间：2018年9月7日
 * 下午3:14:50 修改备注：
 * 
 * @version
 * 
 */
public class MyImageUtil {
//	FileUtil fileUtil = new FileUtil();
	/* 打开图片窗口 */
	private FileDialog dialogOpen;
	/* 图片 */
	private Image image;

	public MyImageUtil() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* 构造器构建窗口 */
	/*
	 * public ImageUtil(JFrame frame) { frame = new JFrame();
	 * 
	 * dialogOpen = new FileDialog(frame , "选择一张图片", FileDialog.LOAD);
	 * dialogOpen.setVisible(true); }
	 */

	public String getPath() {
		// 用于保存图片路径
		StringBuffer path = new StringBuffer("");
		// 新建选择窗体
		dialogOpen = new FileDialog(new JFrame(), "选择一张图片", FileDialog.LOAD);
		dialogOpen.setVisible(true);
		path.append(dialogOpen.getDirectory());
		path.append(dialogOpen.getFile());
		/* 判断该选择的文件是否是一张图片 */
		try {
			image = ImageIO.read(new File(dialogOpen.getDirectory() + dialogOpen.getFile()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (image == null) {
			JOptionPane.showMessageDialog(null, "请选择正确的图片");
			return null;
		}
		

		return path.toString();
	}
	
	/*String类型的图片路径转换为InputStream类型的输入流，如果成功则返回true*/
	/*public  boolean pathToInStream(String path) {
		boolean result = true;
		InputStream in = null;
		try {
			in = MyImageUtil.readImage(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		if(in == null) {
			result = false;
		}
		
		return result;
	}*/
	
	
	
	

	/* 以下代码参考自https://www.cnblogs.com/warrior4236/p/5682830.html */
	// 读取本地图片获取输入流
	public static FileInputStream readImage(String path) throws IOException {
		return new FileInputStream(new File(path));
	}

	// 处理输入流，为输出的图片设置对应的高度。返回Graphics
	public static Graphics readBinToImage(InputStream in, int height) {
		Graphics result = null;
		BufferedImage bufferedImage = null;
		Image image = null;
		
		try {
			image = ImageIO.read(in);
			int preWidth = image.getHeight(null);
			int preHeight = image.getHeight(null);
			/*??参数：
					width - 将图像缩放到的宽度。
					height - 将图像缩放到的高度。
					hints - 指示用于图像重新取样的算法类型的标志。*/
			image = image.getScaledInstance(preWidth*(height/preHeight), height, 1);
			bufferedImage = new BufferedImage(preWidth*(height/preHeight), height, BufferedImage.TYPE_INT_RGB);
			bufferedImage.flush();
			result = bufferedImage.getGraphics();
			result.drawImage(image, 0, 0, null);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}

	//用来在JLabel中显示图片
	public static void showImageInJLabel(JLabel jlabel ,Blob image) throws IOException {
		byte[] byteImage = FileUtil.blobToBytes( image ) ;
		InputStream in = new ByteArrayInputStream(byteImage);
		Image imagetemp = ImageIO.read(in);
		ImageIcon icon = new ImageIcon(imagetemp);
		jlabel.setIcon(icon);
	}
	
}
