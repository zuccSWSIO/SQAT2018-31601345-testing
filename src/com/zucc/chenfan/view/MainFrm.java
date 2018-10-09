package com.zucc.chenfan.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JDesktopPane;
import java.awt.SystemColor;
import javax.swing.JSplitPane;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.Box;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JProgressBar;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Color;

public class MainFrm extends JFrame {

	private JPanel contentPane;
	private JDesktopPane jdp_desktop;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrm frame = new MainFrm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrm() {
		/*设置最大化*/
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		setTitle("\u5BA0\u7269\u670D\u52A1\u7CFB\u7EDF\u4E3B\u754C\u9762");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("\u5E97\u94FA\u6570\u636E\u7EF4\u62A4");
		menuBar.add(menu);
		
		JMenu menu_1 = new JMenu("\u5E97\u5458\u7BA1\u7406");
		menu.add(menu_1);
		
		JMenuItem mi_addOperator = new JMenuItem("\u65B0\u589E\u5E97\u5458");
		mi_addOperator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*调用“AddOperatorFrm”*/
				AddOperatorInterFrm addOperatorFrm = new AddOperatorInterFrm();
				addOperatorFrm.setVisible(true);
				jdp_desktop.add(addOperatorFrm);
			}
		});
		menu_1.add(mi_addOperator);
		
		JMenuItem jmi_maintainOperator = new JMenuItem("\u5E97\u5458\u4FE1\u606F\u7EF4\u62A4");
		jmi_maintainOperator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*创建店员信息维护“MaintainOperatorInterFrm”窗体*/
				MaintainOperatorInterFrm maintainOperatorInterFrm = new MaintainOperatorInterFrm();
				maintainOperatorInterFrm.setVisible(true);
				jdp_desktop.add(maintainOperatorInterFrm);
				
			}
		});
		menu_1.add(jmi_maintainOperator);
		
		JMenu menu_3 = new JMenu("\u5546\u54C1\u7BA1\u7406");
		menu.add(menu_3);
		
		JMenu menu_4 = new JMenu("\u5546\u54C1\u7C7B\u522B\u7BA1\u7406");
		menu_3.add(menu_4);
		
		JMenuItem menuItem_2 = new JMenuItem("\u65B0\u589E\u5546\u54C1\u7C7B\u522B");
		menuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AddGoodsTypeInterFrm addGoodsTypeInterFrm = new AddGoodsTypeInterFrm();
				addGoodsTypeInterFrm.setVisible(true);
				jdp_desktop.add(addGoodsTypeInterFrm);
			}
		});
		menu_4.add(menuItem_2);
		
		JMenuItem menuItem_8 = new JMenuItem("\u5546\u54C1\u7C7B\u522B\u7EF4\u62A4");
		menuItem_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*新建商品类别维护窗口*/
				MaintainGoodsTypeInterFrm maintainGoodsTypeInterFrm = new MaintainGoodsTypeInterFrm();
				maintainGoodsTypeInterFrm.setVisible(true);
				jdp_desktop.add(maintainGoodsTypeInterFrm);
			}
		});
		menu_4.add(menuItem_8);
		
		JMenu menu_5 = new JMenu("\u7279\u5B9A\u5546\u54C1\u7BA1\u7406");
		menu_3.add(menu_5);
		
		JMenuItem menuItem_9 = new JMenuItem("\u65B0\u589E\u5546\u54C1");
		menuItem_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AddGoodsInterFrm addGoodsInterFrm = new AddGoodsInterFrm();
				addGoodsInterFrm.setVisible(true);
				jdp_desktop.add(addGoodsInterFrm);
			}
		});
		menu_5.add(menuItem_9);
		
		JMenuItem menuItem_10 = new JMenuItem("\u5546\u54C1\u4FE1\u606F\u7EF4\u62A4");
		menuItem_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MaintainGoodsInterFrm maintainGoodsInterFrm = new MaintainGoodsInterFrm();
				maintainGoodsInterFrm.setVisible(true);
				jdp_desktop.add(maintainGoodsInterFrm);
			}
		});
		menu_5.add(menuItem_10);
		
		JMenu menu_6 = new JMenu("\u670D\u52A1\u7BA1\u7406");
		menu.add(menu_6);
		
		JMenu menu_7 = new JMenu("\u670D\u52A1\u7C7B\u522B\u7BA1\u7406");
		menu_6.add(menu_7);
		
		JMenuItem menuItem_4 = new JMenuItem("\u65B0\u589E\u670D\u52A1\u7C7B\u522B");
		menuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*新建窗口“新增服务类别”*/
				AddServiceTypeInterFrm addServiceTypeInterFrm = new AddServiceTypeInterFrm();
				addServiceTypeInterFrm.setVisible(true);
				jdp_desktop.add( addServiceTypeInterFrm );
			}
		});
		menu_7.add(menuItem_4);
		
		JMenuItem menuItem_11 = new JMenuItem("\u670D\u52A1\u7C7B\u522B\u7EF4\u62A4");
		menuItem_11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MaintainServiceTypeInterFrm maintainServiceTypeInterFrm = new MaintainServiceTypeInterFrm();
				maintainServiceTypeInterFrm.setVisible(true);
				jdp_desktop.add(maintainServiceTypeInterFrm);
			}
		});
		menu_7.add(menuItem_11);
		
		JMenu menu_8 = new JMenu("\u7279\u5B9A\u670D\u52A1\u7BA1\u7406");
		menu_6.add(menu_8);
		
		JMenuItem menuItem_12 = new JMenuItem("\u65B0\u589E\u7279\u5B9A\u670D\u52A1");
		menuItem_12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AddServiceInterFrm addServiceInterFrm = new AddServiceInterFrm();
				addServiceInterFrm.setVisible(true);
				jdp_desktop.add(addServiceInterFrm);
			}
		});
		menu_8.add(menuItem_12);
		
		JMenuItem menuItem_13 = new JMenuItem("\u670D\u52A1\u7C7B\u522B\u7EF4\u62A4");
		menuItem_13.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MaintainServiceInterFrm maintainServiceInterFrm = new MaintainServiceInterFrm();
				maintainServiceInterFrm.setVisible(true);
				jdp_desktop.add(maintainServiceInterFrm);
			}
		});
		menu_8.add(menuItem_13);
		
		JMenu mnNewMenu_1 = new JMenu("\u8FD0\u8425\u4FE1\u606F\u4E2D\u5FC3");
		mnNewMenu_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				try {
					OperationCenterInterFrm operationCenterInterFrm = new OperationCenterInterFrm();
					operationCenterInterFrm.setVisible(true);
					jdp_desktop.add(operationCenterInterFrm);
				} catch (PropertyVetoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		menuBar.add(mnNewMenu_1);
		
		JMenu mnNewMenu = new JMenu("\u7CFB\u7EDF");
		menuBar.add(mnNewMenu);
		
		JMenuItem jmi_exit = new JMenuItem("\u9000\u51FA\u7CFB\u7EDF");
		jmi_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int choice = JOptionPane.showConfirmDialog(null, "是否退出系统？");
				if(choice == 0) {
					dispose();
				}
				
			}
		});
		
		JMenuItem menuItem_3 = new JMenuItem("\u5173\u4E8E\u7CFB\u7EDF");
		mnNewMenu.add(menuItem_3);
		mnNewMenu.add(jmi_exit);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		jdp_desktop = new JDesktopPane();
		jdp_desktop.setBackground(Color.GRAY);
		contentPane.add(jdp_desktop, BorderLayout.CENTER);
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
