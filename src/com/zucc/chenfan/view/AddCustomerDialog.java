package com.zucc.chenfan.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.zucc.chenfan.dao.CustomerDao;
import com.zucc.chenfan.model.Customer;
import com.zucc.chenfan.util.DbUtil;
import com.zucc.chenfan.util.IntegerUtil;
import com.zucc.chenfan.util.StringUtil;

public class AddCustomerDialog extends JDialog {
	DbUtil dbUtil = new DbUtil();
	CustomerDao customerDao = new CustomerDao();
	
	private final JPanel contentPanel = new JPanel();
	private JTextField jtf_phone;
	private JTextField jtf_email;
	private JTextField jtf_othercontact;
	private JTextField jtf_name;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AddCustomerDialog dialog = new AddCustomerDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//关闭窗口
	private void closeDialog() {
		this.dispose();
	}
	

	/**
	 * Create the dialog.
	 */
	public AddCustomerDialog() {
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("\u65B0\u589E\u5BA2\u6237");
		setBounds(100, 100, 363, 261);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new LineBorder(Color.BLUE));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel label = new JLabel("\u987E\u5BA2\u59D3\u540D\uFF1A");
			label.setBounds(28, 23, 87, 15);
			contentPanel.add(label);
		}
		
		jtf_name = new JTextField();
		jtf_name.setBounds(143, 21, 80, 21);
		contentPanel.add(jtf_name);
		jtf_name.setColumns(10);
		{
			jtf_othercontact = new JTextField();
			jtf_othercontact.setBounds(143, 135, 163, 21);
			contentPanel.add(jtf_othercontact);
			jtf_othercontact.setColumns(10);
		}
		{
			JLabel label = new JLabel("\u8054\u7CFB\u7535\u8BDD\uFF1A");
			label.setBounds(28, 60, 93, 15);
			contentPanel.add(label);
		}
		{
			jtf_phone = new JTextField();
			jtf_phone.setBounds(143, 58, 161, 21);
			contentPanel.add(jtf_phone);
			jtf_phone.setColumns(10);
		}
		{
			JLabel lblNewLabel = new JLabel("\u7535\u5B50\u90AE\u7BB1\uFF1A");
			lblNewLabel.setBounds(28, 98, 89, 15);
			contentPanel.add(lblNewLabel);
		}
		{
			jtf_email = new JTextField();
			jtf_email.setBounds(143, 97, 162, 21);
			contentPanel.add(jtf_email);
			jtf_email.setColumns(10);
		}
		{
			JLabel label = new JLabel("\u5176\u4ED6\u8054\u7CFB\u65B9\u5F0F\uFF1A");
			label.setBounds(29, 134, 105, 15);
			contentPanel.add(label);
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
			{
				JPanel panel = new JPanel();
				buttonPane.add(panel);
				{
					JButton okButton = new JButton("\u65B0\u589E");
					okButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							/*①判空*/
							if(StringUtil.isEmpty(jtf_name.getText())) {
								JOptionPane.showMessageDialog(null, "请填写顾客姓名");
								return;
							}
							if(StringUtil.isEmpty(jtf_phone.getText())) {
								JOptionPane.showMessageDialog(null, "请填写联系电话");
								return;
							}
							else {
								if(!IntegerUtil.isNumeric(jtf_phone.getText())) {
									JOptionPane.showMessageDialog(null, "联系电话应为纯数字");
									return;
								}
							}
							//以下两项允许空值
							/*if(StringUtil.isEmpty(jtf_email.getText())) {
								JOptionPane.showMessageDialog(null, "请填写电子邮箱");
								return;
							}
							if(StringUtil.isEmpty(jtf_othercontact.getText())) {
								JOptionPane.showMessageDialog(null, "请填写其他联系方式");
								return;
							}*/
							
							//②检测数据库中有无重复的电话号码
							Connection con = null;
							Customer temp = new Customer();
							temp.setCustomer_id(-1);
							temp.setCustomer_phone(Long.parseLong(jtf_phone.getText()));
							try {
								con = dbUtil.getCon();
								boolean hasEqual = customerDao.isEqual(con, temp);
								if(hasEqual) {
									JOptionPane.showMessageDialog(null, "该联系电话已被使用，新增失败");
									jtf_phone.setText("");
									return;
								}
							} catch (SQLException e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(null, "系统错误，添加失败");
							} catch (Exception e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(null, "系统错误，添加失败");
							}finally {
								try {
									dbUtil.closeCon(con);
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
							
							//③新增数据
							con = null;
							Customer customer = new Customer();
							customer.setCustomer_name(jtf_name.getText());
							customer.setCustomer_phone(Long.parseLong(jtf_phone.getText()));
							customer.setCustomer_email(jtf_email.getText());
							customer.setCustomer_other_contact(jtf_othercontact.getText());
							try {
								con = dbUtil.getCon();
								int result = customerDao.addCustomer(con, customer);
								if(result != 1) {
									JOptionPane.showMessageDialog(null, "系统错误，添加失败");
									return;
								}
								else {
									JOptionPane.showMessageDialog(null, "新增记录成功");
									closeDialog();
								}
							} catch (SQLException e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(null, "系统错误，添加失败");
							} catch (Exception e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(null, "系统错误，添加失败");
							}finally {
								try {
									dbUtil.closeCon(con);
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
							
						}
					});
					panel.add(okButton);
					okButton.setActionCommand("OK");
					getRootPane().setDefaultButton(okButton);
				}
			}
			{
				JPanel panel = new JPanel();
				buttonPane.add(panel);
				{
					JButton cancelButton = new JButton("\u53D6\u6D88");
					cancelButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							closeDialog();
						}
					});
					panel.add(cancelButton);
					cancelButton.setActionCommand("Cancel");
				}
			}
		}
	}
}
