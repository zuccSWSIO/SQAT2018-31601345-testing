package com.zucc.chenfan.util;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;

public class DialogDemo implements ActionListener{

    private JFrame frame;
    private Panel panel, panelButton, panelText;
    private JLabel labelHight, labelWidth;
    //�̶����ſ�Ⱥͳ���
    private JTextField textHight;
    private JTextField textWidth;
    //������¼��ʾ��
    private JTextArea textArea;
    //������ť
    private JButton buttonReduce, buttonEnlarge, buttonZoom;
    //�˵�������ͼƬ������ͼƬ�����ڡ��˳�
    private JMenuItem itemSave, itemOpen,itemAbout, itemExit;
    //��ͼƬ���ڣ�����ͼƬ����
    private FileDialog dialogOpen;
    private FileDialog dialogSave;
  //BufferedImage���ڱ���ͼƬ
      private BufferedImage bufferedImage;
  //ͼƬ��ʾimageCanvas�ࣨ�̳�Canvas��
      private imageCanvas canvas;
      private Image image;
      private Graphics graphics;
    
        
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DialogDemo window = new DialogDemo();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    //ͼƬ��ʾimageCanvas�ࣨ�̳�Canvas��������ͼƬ���»���
    class imageCanvas extends Canvas
    {
         //��дCanvas��paint����
         public void paint(Graphics g)
         {
               //��image���Ƶ��������
               g.drawImage(bufferedImage, 0, 0, null);
               //f.setVisible(true);
         }
    }
    
    //���캯����ʼ��ͼ�����
    public DialogDemo() {

        frame = new JFrame();
        frame.setBounds(100, 100, 900, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        
        canvas = new imageCanvas();
        canvas.setPreferredSize(new Dimension(800, 600));
        frame.getContentPane().add(canvas, BorderLayout.CENTER);
        
        panel = new Panel();
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        panel.setLayout(new GridLayout(1, 0, 0, 0));
        
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setText("\u63D0\u793A\uFF1A\r\n");
        panel.add(textArea);
        
        panelButton = new Panel();
        panel.add(panelButton);
        panelButton.setLayout(new GridLayout(3, 1, 0, 0));
        
        buttonReduce = new JButton("\u56FE\u7247\u7F29\u5C0F\u4E00\u500D");
        panelButton.add(buttonReduce);buttonReduce.addActionListener(this);
        
        buttonEnlarge = new JButton("\u56FE\u7247\u653E\u5927\u4E00\u500D");
        panelButton.add(buttonEnlarge);buttonEnlarge.addActionListener(this);
        
        panelText = new Panel();
        panelButton.add(panelText);
        panelText.setLayout(new GridLayout(1, 0, 0, 0));
        
        labelHight = new JLabel("\u957F\u5EA6(px)");
        panelText.add(labelHight);
        
        textHight = new JTextField();
        panelText.add(textHight);
        textHight.setColumns(10);
        
        labelWidth = new JLabel("\u5BBD\u5EA6(px)");
        panelText.add(labelWidth);
        
        textWidth = new JTextField();
        panelText.add(textWidth);
        textWidth.setColumns(10);
        
        buttonZoom = new JButton("\u56FA\u5B9A\u7F29\u653E");
        panelText.add(buttonZoom);
        buttonZoom.addActionListener(this);
        
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        
        JMenu newMenu = new JMenu("\u6587\u4EF6\r\n");
        menuBar.add(newMenu);
        
        itemOpen = new JMenuItem("\u6253\u5F00\u56FE\u7247\r\n");
        newMenu.add(itemOpen);itemOpen.addActionListener(this);
        
        itemSave = new JMenuItem("\u4FDD\u5B58\u56FE\u7247\r\n");
        newMenu.add(itemSave);itemSave.addActionListener(this);
        
        itemAbout = new JMenuItem("\u5173\u4E8E");
        newMenu.add(itemAbout);itemAbout.addActionListener(this);
        
        JSeparator separator = new JSeparator();
        newMenu.add(separator);
        
        itemExit = new JMenuItem("\u9000\u51FA\r\n");
        newMenu.add(itemExit);itemExit.addActionListener(this);
        
        dialogOpen = new FileDialog(frame, "ѡ��һ��ͼƬ", FileDialog.LOAD);
        dialogSave = new FileDialog(frame, "ѡ�񱣴�ͼƬ��·��", FileDialog.SAVE);
    }

    /**
     * ���潻������Ӧ�¼������ö�Ӧ�ĺ�����
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == itemSave) {
            saveImage();
        } else if (e.getSource() == itemOpen) {
            openImage();
        } else if (e.getSource() == itemExit) {
            System.exit(0);
        } else if (e.getSource() == itemAbout) {
            JOptionPane.showMessageDialog(null, "ͼƬ���ų���PhotoZoomer 1.0",
                    "�汾", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == buttonEnlarge) {
            enlargeImage();
        } else if (e.getSource() == buttonReduce) {
            reduceImage();
        } else if (e.getSource() == buttonZoom) {
            zoomImage();
        }
        
    }

    /**
     * ��Ӧ�¼���װ�ɺ���
     */
    //��ͼƬ
    private void openImage() {
        try {
            // ����һ������͸��ɫ��BufferedImage����
             bufferedImage = new BufferedImage(1920, 890, BufferedImage.TYPE_INT_RGB);
             bufferedImage.flush();
             graphics = bufferedImage.getGraphics();
             //�򿪶Ի���
             dialogOpen.setVisible(true);
             image = ImageIO.read(new File(dialogOpen.getDirectory() + dialogOpen.getFile()));
             //�ж�ͼƬ�Ƿ����
             if (image != null) {
                 graphics.drawImage(image,0,0, null);
                 canvas.repaint();
             }
             //�����ʾ
             textArea.append("��ͼƬ�ɹ���\nͼƬ·����" +
                     dialogOpen.getDirectory()+"\n"+"ͼƬ���ƣ�"+dialogOpen.getFile()+"\n");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("��ͼƬ��������");
        }
    }
    
    //����ͼƬ
    private void saveImage() {
        try {
            dialogSave.setVisible(true);
            ImageIO.write(bufferedImage, "jpeg", 
                    new File(dialogSave.getDirectory() + dialogSave.getFile()));
            //�����ʾ
            textArea.append("���ͼƬ�ɹ���\n����Ŀ¼��"+dialogSave.getDirectory()+"\n");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("����ͼƬ��������");
        }    
    }
    
    //�̶�����ͼƬ
    private void zoomImage() {
        int height = Integer.parseInt(textHight.getText());
        int width = Integer.parseInt(textWidth.getText());
        //�ж������Ƿ��������
        if (height > 0 && width > 0 && height <= 890 && width <= 1920 ) {
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            graphics = bufferedImage.getGraphics();
            graphics.drawImage(image, 0, 0, width, height, null);
            canvas.repaint();
            textArea.append("\nͼƬ����Ϊ�ߣ�"+height+"px����"+width+"px\n");
            textHight.setText("");
            textWidth.setText("");
        } else {
            textArea.append("\n��������ȷ��ͼƬ��Ⱥͳ��ȣ�");
            textHight.setText("");
            textWidth.setText("");
        }
    }
    
    //�Ŵ�ͼƬһ��
    private void enlargeImage() {
        int height = image.getHeight(null) * 2;
        int width = image.getWidth(null) * 2;
        //�ж������Ƿ��������
        if (height > 0 && width > 0 && height <= 890 && width <= 1920 ) {
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            graphics = bufferedImage.getGraphics();
            graphics.drawImage(image, 0, 0, width, height, null);
            canvas.repaint();
            textArea.append("\nͼƬ����Ϊ�ߣ�"+height+"px����"+width+"px\n");
            textHight.setText("");
            textWidth.setText("");
        } else {
            textArea.append("\n�����ٽ��зŴ��ˣ�");
            textHight.setText("");
            textWidth.setText("");
        }
    }
    
    //��СͼƬһ��
    private void reduceImage() {
        int height = image.getHeight(null) / 2;
        int width = image.getWidth(null) / 2;
        //�ж������Ƿ��������
        if (height > 0 && width > 0 && height <= 890 && width <= 1920 ) {
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            graphics = bufferedImage.getGraphics();
            graphics.drawImage(image, 0, 0, width, height, null);
            canvas.repaint();
            textArea.append("\nͼƬ����Ϊ�ߣ�"+height+"px����"+width+"px\n");
            textHight.setText("");
            textWidth.setText("");
        } else {
            textArea.append("\n�����ٽ�����С�ˣ�");
            textHight.setText("");
            textWidth.setText("");
        }
    }
}