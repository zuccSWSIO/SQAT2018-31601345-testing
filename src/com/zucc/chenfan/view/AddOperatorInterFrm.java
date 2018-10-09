package com.zucc.chenfan.view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.zucc.chenfan.dao.OperatorDao;
import com.zucc.chenfan.model.Operator;
import com.zucc.chenfan.util.DbUtil;
import com.zucc.chenfan.util.StringUtil;

public class AddOperatorInterFrm extends JInternalFrame {
	DbUtil dbUtil = new DbUtil();
	OperatorDao operatorDao = new OperatorDao();
	
	private JPasswordField tx_pwd2;
	private JPasswordField tx_pwd1;
	private JTextField tx_name;
	private JComboBox cbb_group;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddOperatorInterFrm frame = new AddOperatorInterFrm();
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
	public AddOperatorInterFrm() {
		setTitle("\u65B0\u589E\u5E97\u5458");
		setIconifiable(true);
		setClosable(true);
		setBounds(100, 100, 388, 230);
		getContentPane().setLayout(null);
		
		JLabel label = new JLabel("\u5458\u5DE5\u59D3\u540D\uFF1A");
		label.setFont(new Font("黑体", Font.PLAIN, 15));
		label.setBounds(41, 39, 128, 15);
		getContentPane().add(label);
		
		JLabel label_1 = new JLabel("\u5458\u5DE5\u804C\u4F4D\uFF1A");
		label_1.setFont(new Font("黑体", Font.PLAIN, 15));
		label_1.setBounds(41, 66, 128, 15);
		getContentPane().add(label_1);
		
		JLabel label_2 = new JLabel("\u8BBE\u7F6E\u767B\u5F55\u5BC6\u7801\uFF1A");
		label_2.setFont(new Font("黑体", Font.PLAIN, 15));
		label_2.setBounds(41, 91, 128, 15);
		getContentPane().add(label_2);
		
		JLabel label_3 = new JLabel("\u91CD\u590D\u767B\u5F55\u5BC6\u7801\uFF1A");
		label_3.setFont(new Font("黑体", Font.PLAIN, 15));
		label_3.setBounds(41, 116, 128, 15);
		getContentPane().add(label_3);
		
		tx_pwd2 = new JPasswordField();
		tx_pwd2.setEchoChar('＊');
		tx_pwd2.setBounds(184, 113, 150, 21);
		getContentPane().add(tx_pwd2);
		
		tx_pwd1 = new JPasswordField();
		tx_pwd1.setEchoChar('＊');
		tx_pwd1.setBounds(184, 88, 150, 21);
		getContentPane().add(tx_pwd1);
		
		JButton btn_add = new JButton("\u6DFB\u52A0");
		btn_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*“添加”按钮监听事件*/
				if(StringUtil.isEmpty(tx_name.getText())) {
					JOptionPane.showMessageDialog(null, "请输入店员姓名");
					return;
				}
				String operator_name = tx_name.getText();
				if(StringUtil.isEmpty(tx_pwd1.getText())) {
					JOptionPane.showMessageDialog(null, "请输入密码");
					return;
				}
				String operator_pwd1 = tx_pwd1.getText();
				if(StringUtil.isEmpty(tx_pwd2.getText())) {
					JOptionPane.showMessageDialog(null, "请再次输入密码");
					return;
				}
				String operator_pwd2 = tx_pwd2.getText();
				if(!tx_pwd1.getText().equals(tx_pwd2.getText())) {
					JOptionPane.showMessageDialog(null, "两次密码不一致，请重新输入");
					tx_pwd1.setText("");
					tx_pwd2.setText("");
					return;
				}
				if(StringUtil.isEmpty((String) cbb_group.getSelectedItem())) {
					JOptionPane.showMessageDialog(null, "请选择店员类型");
					return;
				}
				String operator_group = (String)cbb_group.getSelectedItem();
				
				Connection con = null;
				try {
					con = dbUtil.getCon();
					Operator operator = new Operator();
					operator.setOperator_name(operator_name);
					operator.setPassword(operator_pwd1);
					operator.setOperator_group(operator_group);
					int judge = operatorDao.addOperator(con, operator);
					if(judge == 1) {
						JOptionPane.showMessageDialog(null, "新增成功");
						tx_name.setText("");
						tx_pwd1.setText("");
						tx_pwd2.setText("");
						cbb_group.setSelectedIndex(0);
					}
					else {
						JOptionPane.showMessageDialog(null, "数据库错误，新增失败");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "系统错误，新增失败");
				}finally {
					try {
						dbUtil.closeCon(con);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				
				
			}
		});
		btn_add.setBounds(59, 158, 95, 25);
		getContentPane().add(btn_add);
		
		JButton btn_reset = new JButton("\u91CD\u586B");
		btn_reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*“重填”按钮监听事件*/
				tx_name.setText("");
				tx_pwd1.setText("");
				tx_pwd2.setText("");
				cbb_group.setSelectedIndex(0);
			}
		});
		btn_reset.setBounds(224, 158, 95, 25);
		getContentPane().add(btn_reset);
		
		cbb_group = new JComboBox();
		cbb_group.setModel(new DefaultComboBoxModel(new String[] {"", "\u6536\u94F6\u5458", "\u6253\u5305\u5458", "\u9001\u8D27\u5458", "\u670D\u52A1\u5458"}));
		cbb_group.setBounds(184, 62, 70, 21);
		getContentPane().add(cbb_group);
		
		tx_name = new JTextField();
		tx_name.setBounds(184, 36, 70, 21);
		getContentPane().add(tx_name);
		tx_name.setColumns(10);

	}
}
