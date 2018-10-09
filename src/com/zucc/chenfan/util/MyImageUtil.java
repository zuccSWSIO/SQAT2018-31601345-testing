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
 * ��Ŀ���ƣ�PetServiceManagementSystem �����ƣ�ImageUtil �������� �����װ�����к�ͼƬ�����йصĹ���
 * �����ˣ�Administrator ����ʱ�䣺2018��9��7�� ����3:14:50 �޸��ˣ�Administrator �޸�ʱ�䣺2018��9��7��
 * ����3:14:50 �޸ı�ע��
 * 
 * @version
 * 
 */
public class MyImageUtil {
//	FileUtil fileUtil = new FileUtil();
	/* ��ͼƬ���� */
	private FileDialog dialogOpen;
	/* ͼƬ */
	private Image image;

	public MyImageUtil() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* �������������� */
	/*
	 * public ImageUtil(JFrame frame) { frame = new JFrame();
	 * 
	 * dialogOpen = new FileDialog(frame , "ѡ��һ��ͼƬ", FileDialog.LOAD);
	 * dialogOpen.setVisible(true); }
	 */

	public String getPath() {
		// ���ڱ���ͼƬ·��
		StringBuffer path = new StringBuffer("");
		// �½�ѡ����
		dialogOpen = new FileDialog(new JFrame(), "ѡ��һ��ͼƬ", FileDialog.LOAD);
		dialogOpen.setVisible(true);
		path.append(dialogOpen.getDirectory());
		path.append(dialogOpen.getFile());
		/* �жϸ�ѡ����ļ��Ƿ���һ��ͼƬ */
		try {
			image = ImageIO.read(new File(dialogOpen.getDirectory() + dialogOpen.getFile()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (image == null) {
			JOptionPane.showMessageDialog(null, "��ѡ����ȷ��ͼƬ");
			return null;
		}
		

		return path.toString();
	}
	
	/*String���͵�ͼƬ·��ת��ΪInputStream���͵�������������ɹ��򷵻�true*/
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
	
	
	
	

	/* ���´���ο���https://www.cnblogs.com/warrior4236/p/5682830.html */
	// ��ȡ����ͼƬ��ȡ������
	public static FileInputStream readImage(String path) throws IOException {
		return new FileInputStream(new File(path));
	}

	// ������������Ϊ�����ͼƬ���ö�Ӧ�ĸ߶ȡ�����Graphics
	public static Graphics readBinToImage(InputStream in, int height) {
		Graphics result = null;
		BufferedImage bufferedImage = null;
		Image image = null;
		
		try {
			image = ImageIO.read(in);
			int preWidth = image.getHeight(null);
			int preHeight = image.getHeight(null);
			/*??������
					width - ��ͼ�����ŵ��Ŀ�ȡ�
					height - ��ͼ�����ŵ��ĸ߶ȡ�
					hints - ָʾ����ͼ������ȡ�����㷨���͵ı�־��*/
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

	//������JLabel����ʾͼƬ
	public static void showImageInJLabel(JLabel jlabel ,Blob image) throws IOException {
		byte[] byteImage = FileUtil.blobToBytes( image ) ;
		InputStream in = new ByteArrayInputStream(byteImage);
		Image imagetemp = ImageIO.read(in);
		ImageIcon icon = new ImageIcon(imagetemp);
		jlabel.setIcon(icon);
	}
	
}
