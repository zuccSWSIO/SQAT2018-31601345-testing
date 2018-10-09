package com.zucc.chenfan.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
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

public class UpdateCustomerDialog extends JDialog {
	DbUtil dbUtil = new DbUtil();
	CustomerDao customerDao = new CustomerDao();
	
	private final JPanel contentPanel = new JPanel();
	private JTextField jtf_phone;
	private JTextField jtf_email;
	private JTextField jtf_othercontact;
	private JTextField jtf_name;
	private JTextField jtf_id;
	
	//代表更新的顾客信息
	Customer universe = new Customer();
	
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		try {
			UpdateCustomerDialog dialog = new UpdateCustomerDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	//关闭窗口
	private void closeDialog() {
		this.dispose();
	}
	//填充表单
	private void fillForm(int customer_id) {
		Connection con = null;
		Customer temp = new Customer();
		temp.setCustomer_id(customer_id);
		temp.setCustomer_phone(-1);
		try {
			con = dbUtil.getCon();
			ResultSet rs = customerDao.customerList(con, temp);
			if(rs.next()) {
				jtf_id.setText(rs.getString("customer_id"));
				universe.setCustomer_id(rs.getInt("customer_id"));
				jtf_name.setText(rs.getString("customer_name"));
				universe.setCustomer_name(rs.getString("customer_name"));
				jtf_phone.setText(rs.getString("customer_phone"));
				universe.setCustomer_phone(Long.parseLong(rs.getString("customer_phone")));
				jtf_email.setText(rs.getString("customer_email"));
				universe.setCustomer_email(rs.getString("customer_email"));
				jtf_othercontact.setText(rs.getString("customer_other_contact"));
				universe.setCustomer_other_contact(rs.getString("customer_other_contact"));
			}
			else {
				JOptionPane.showMessageDialog(null, "系统错误");
				closeDialog();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				dbUtil.closeCon(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	

	/**
	 * Create the dialog.
	 */
	public UpdateCustomerDialog(int customer_id) {
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("\u66F4\u65B0\u987E\u5BA2\u4FE1\u606F");
		setBounds(100, 100, 363, 295);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new LineBorder(Color.BLUE));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel label_1 = new JLabel("\u987E\u5BA2\u7F16\u53F7\uFF1A");
		label_1.setBounds(27, 23, 80, 15);
		contentPanel.add(label_1);
		
		jtf_id = new JTextField();
		jtf_id.setEditable(false);
		jtf_id.setBounds(142, 21, 80, 21);
		contentPanel.add(jtf_id);
		jtf_id.setColumns(10);
		{
			JLabel label = new JLabel("\u987E\u5BA2\u59D3\u540D\uFF1A");
			label.setBounds(27, 59, 87, 15);
			contentPanel.add(label);
		}
		
		jtf_name = new JTextField();
		jtf_name.setEditable(false);
		jtf_name.setBounds(142, 57, 80, 21);
		contentPanel.add(jtf_name);
		jtf_name.setColumns(10);
		{
			jtf_othercontact = new JTextField();
			jtf_othercontact.setBounds(142, 171, 163, 21);
			contentPanel.add(jtf_othercontact);
			jtf_othercontact.setColumns(10);
		}
		{
			JLabel label = new JLabel("\u8054\u7CFB\u7535\u8BDD\uFF1A");
			label.setBounds(27, 96, 93, 15);
			contentPanel.add(label);
		}
		{
			jtf_phone = new JTextField();
			jtf_phone.setBounds(142, 94, 161, 21);
			contentPanel.add(jtf_phone);
			jtf_phone.setColumns(10);
		}
		{
			JLabel lblNewLabel = new JLabel("\u7535\u5B50\u90AE\u7BB1\uFF1A");
			lblNewLabel.setBounds(27, 134, 89, 15);
			contentPanel.add(lblNewLabel);
		}
		{
			jtf_email = new JTextField();
			jtf_email.setBounds(142, 133, 162, 21);
			contentPanel.add(jtf_email);
			jtf_email.setColumns(10);
		}
		{
			JLabel label = new JLabel("\u5176\u4ED6\u8054\u7CFB\u65B9\u5F0F\uFF1A");
			label.setBounds(28, 170, 105, 15);
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
					JButton okButton = new JButton("\u66F4\u65B0");
					okButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							/*①判空*/
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
							if(  jtf_email.getText().equals(universe.getCustomer_email()) && jtf_phone.getText().equals(String.valueOf(universe.getCustomer_phone())) && jtf_othercontact.getText().equals(universe.getCustomer_other_contact()) ) {
								JOptionPane.showMessageDialog(null, "数据没有变化，无需更新");
								return;
							}
							
							//②检测数据库中有无重复的电话号码
							Connection con = null;
							Customer temp = new Customer();
							temp.setCustomer_id(universe.getCustomer_id());
							temp.setCustomer_phone(Long.parseLong(jtf_phone.getText()));
							try {
								con = dbUtil.getCon();
								boolean hasEqual = customerDao.isEqual(con, temp);
								if(hasEqual) {
									JOptionPane.showMessageDialog(null, "该联系电话已被使用，更新失败");
									jtf_phone.setText("");
									return;
								}
							} catch (SQLException e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(null, "系统错误，更新失败");
							} catch (Exception e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(null, "系统错误，更新失败");
							}finally {
								try {
									dbUtil.closeCon(con);
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
							
							//④更新数据
							con = null;
							Customer customer = new Customer();
							customer.setCustomer_id(universe.getCustomer_id());
							customer.setCustomer_name(jtf_name.getText());
							customer.setCustomer_phone(Long.parseLong(jtf_phone.getText()));
							customer.setCustomer_email(jtf_email.getText());
							customer.setCustomer_other_contact(jtf_othercontact.getText());
							try {
								con = dbUtil.getCon();
								int result = customerDao.updateCustomer(con, customer);
								if(result != 1) {
									JOptionPane.showMessageDialog(null, "系统错误，更新失败");
									return;
								}
								else {
									JOptionPane.showMessageDialog(null, "更新记录成功");
									closeDialog();
								}
							} catch (SQLException e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(null, "系统错误，更新失败");
							} catch (Exception e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(null, "系统错误，更新失败");
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
	
	fillForm(customer_id);
	
	}
}
