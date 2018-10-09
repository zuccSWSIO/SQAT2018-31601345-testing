package com.zucc.chenfan.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.zucc.chenfan.dao.OperatorDao;
import com.zucc.chenfan.model.Operator;
import com.zucc.chenfan.util.DbUtil;
import com.zucc.chenfan.util.IntegerUtil;
import com.zucc.chenfan.util.StringUtil;;

public class LoginFrm extends JFrame {

	DbUtil dbUtil = new DbUtil();
	OperatorDao operatorDao = new OperatorDao();

	private JPanel contentPane;
	private JPasswordField tx_pwd;
	private JTextField tx_id;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrm frame = new LoginFrm();
					frame.setVisible(true);
					/* 在主函数中设置窗体居中 */
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginFrm() {
		setBackground(SystemColor.windowBorder);
		setIconImage(Toolkit.getDefaultToolkit().getImage(LoginFrm.class.getResource("/resource/images/pet_5.png")));
		setForeground(Color.WHITE);
		// 改变系统默认字体（美化为windows字体）
		Font font = new Font("Dialog", Font.PLAIN, 12);
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource) {
				UIManager.put(key, font);
			}
		}

		/* 去除最大化 */
		setResizable(false);

		setTitle("\u64CD\u4F5C\u5458\u767B\u5F55");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 281, 197);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 182, 193));
		contentPane.setToolTipText("");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("\u64CD\u4F5C\u5458\u7F16\u53F7\uFF1A");
		lblNewLabel.setIcon(new ImageIcon(LoginFrm.class.getResource("/resource/icon/user_snow.png")));
		lblNewLabel.setBounds(23, 63, 95, 18);
		contentPane.add(lblNewLabel);

		JLabel label = new JLabel("\u767B\u5F55\u5BC6\u7801\uFF1A");
		label.setIcon(new ImageIcon(LoginFrm.class.getResource("/resource/icon/key_snow.png")));
		label.setBounds(23, 88, 87, 18);
		contentPane.add(label);

		tx_pwd = new JPasswordField();
		tx_pwd.setEchoChar('＊');
		tx_pwd.setBounds(117, 88, 136, 21);
		contentPane.add(tx_pwd);

		tx_id = new JTextField();
		tx_id.setBounds(117, 63, 136, 21);
		contentPane.add(tx_id);
		tx_id.setColumns(10);

		JButton jb_login = new JButton("\u767B\u5F55");
		jb_login.setBackground(SystemColor.controlShadow);
		jb_login.setIcon(new ImageIcon(LoginFrm.class.getResource("/resource/icon/login_snow.png")));
		/* 触发登录 */
		jb_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (StringUtil.isEmpty(tx_id.getText())) {
					JOptionPane.showMessageDialog(null, "用户编号未填，请检查");
					return;
				} else if (!IntegerUtil.isNumeric(tx_id.getText())) {
					JOptionPane.showMessageDialog(null, "操作员编号为纯数字，请重新填写");
					tx_id.setText("");
					return;
				}
				String temp = tx_id.getText();
				int operatorId = Integer.parseInt(temp);

				if (StringUtil.isEmpty(tx_pwd.getText())) {
					JOptionPane.showMessageDialog(null, "密码未填，请检查");
					return;
				}
				char[] temp2 = tx_pwd.getPassword();
				String password = new String(temp2);

				/* 似乎在不填写用户名和密码的情况下tx_pwd和tx_id不会自动实例化，而造成空指针的情况 */

				// if(StringUtil.isEmpty(temp)) {
				// JOptionPane.showMessageDialog(null, "用户编号未填，请检查");
				// }
				// else if(StringUtil.isEmpty(password)) {
				// JOptionPane.showMessageDialog(null, "密码未填，请检查");
				// }

				Operator operator = new Operator(operatorId, password);
				Connection con = null;
				try {
					con = dbUtil.getCon();
					Operator currentOperator = operatorDao.login(con, operator);
					if (currentOperator != null) {
						/*销毁当前frame并创建主界面frame*/
						dispose();
						MainFrm mainFrm = new MainFrm();//创建主界面
						mainFrm.setVisible(true);//设置主界面可见
						
						if(!currentOperator.getOperator_group().equals("店长")) {
							/*设置店员菜单不可见*/
							mainFrm.getJMenuBar().getMenu(0).getMenuComponent(0).setVisible(false);
						}
						
						
					} else {
						JOptionPane.showMessageDialog(null, "用户名或密码错误");
						tx_pwd.setText("");
					}

				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "系统错误，登录失败");
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "系统错误，登录失败");
					e1.printStackTrace();
				}finally {
					try {
						dbUtil.closeCon(con);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		});
		jb_login.setBounds(33, 123, 95, 25);
		contentPane.add(jb_login);

		JButton jb_reset = new JButton("\u91CD\u586B");
		jb_reset.setBackground(SystemColor.controlShadow);
		jb_reset.setIcon(new ImageIcon(LoginFrm.class.getResource("/resource/icon/reset_snow.png")));
		jb_reset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				tx_id.setText("");
				tx_pwd.setText("");
			}
		});
		jb_reset.setBounds(147, 123, 95, 25);
		contentPane.add(jb_reset);
		
		JLabel label_1 = new JLabel("\u5BA0\u7269\u670D\u52A1\u7CFB\u7EDF");
		label_1.setIcon(new ImageIcon(LoginFrm.class.getResource("/resource/images/pet_5.png")));
		label_1.setFont(new Font("微软雅黑 Light", Font.BOLD, 20));
		label_1.setBounds(49, 10, 178, 46);
		contentPane.add(label_1);
	}
}
