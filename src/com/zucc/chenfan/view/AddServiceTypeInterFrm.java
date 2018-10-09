package com.zucc.chenfan.view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.zucc.chenfan.dao.Service_typeDao;
import com.zucc.chenfan.model.Service_type;
import com.zucc.chenfan.util.DbUtil;
import com.zucc.chenfan.util.StringUtil;

public class AddServiceTypeInterFrm extends JInternalFrame {
	Service_typeDao serviceTypeDao = new Service_typeDao();
	DbUtil dbUtil = new DbUtil();
	
	private JTextField jtf_name;
	private JTextArea jta_detail;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddServiceTypeInterFrm frame = new AddServiceTypeInterFrm();
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
	public AddServiceTypeInterFrm() {
		setClosable(true);
		setIconifiable(true);
		setTitle("\u65B0\u589E\u670D\u52A1\u7C7B\u522B");
		setBounds(100, 100, 443, 292);
		getContentPane().setLayout(null);
		
		JLabel label = new JLabel("\u670D\u52A1\u7C7B\u578B\u540D\uFF1A");
		label.setFont(new Font("����", Font.PLAIN, 15));
		label.setBounds(46, 41, 105, 15);
		getContentPane().add(label);
		
		jtf_name = new JTextField();
		jtf_name.setBounds(179, 38, 95, 21);
		getContentPane().add(jtf_name);
		jtf_name.setColumns(10);
		
		JLabel label_1 = new JLabel("\u670D\u52A1\u7C7B\u578B\u63CF\u8FF0\uFF1A");
		label_1.setFont(new Font("����", Font.PLAIN, 15));
		label_1.setBounds(46, 87, 105, 15);
		getContentPane().add(label_1);
		
		jta_detail = new JTextArea();
		jta_detail.setBounds(179, 83, 202, 105);
		getContentPane().add(jta_detail);
		
		JButton jb_add = new JButton("\u65B0\u589E");
		jb_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*�������������*/
				if(StringUtil.isEmpty(jtf_name.getText())) {
					JOptionPane.showMessageDialog(null, "�������������");
					return;
				}
				String serviceTypeName = jtf_name.getText();
				if(StringUtil.isEmpty(jta_detail.getText())) {
					JOptionPane.showMessageDialog(null, "�������������");
					return;
				}
				String serviceTypeDetail = jta_detail.getText();
				
				/*�жϸ����ࣨ����service_type_name�����ݿ����Ƿ����ظ���*/
				Connection con = null;
				Service_type temp = new Service_type(serviceTypeName);
				try {
					con = dbUtil.getCon();
					boolean isEqual = serviceTypeDao.isEqual(con, temp);
					if(isEqual) {
						JOptionPane.showMessageDialog(null, "������������ʧ�ܣ����ݿ����Ѿ�������ͬ���Ƶķ������");
						jtf_name.setText("");
						return;
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "ϵͳ�������ݿ�����ʧ��");
					e1.printStackTrace();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "ϵͳ�������ݿ�����ʧ��");
					e.printStackTrace();
				}finally {
					try {
						dbUtil.closeCon(con);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				/*��ʼ�������ݿ������ķ�����ִ����������*/
				con = null;
				Service_type serviceType = new Service_type(serviceTypeName, serviceTypeDetail);
				try {
					con = dbUtil.getCon();
					int result = serviceTypeDao.addServiceType(con, serviceType);
					if(result == 1) {
						JOptionPane.showMessageDialog(null, "�÷������������ɹ�");
						jtf_name.setText("");
						jta_detail.setText("");
					}
					else {
						JOptionPane.showMessageDialog(null, "ϵͳ��������ʧ��");
					}
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "ϵͳ��������ʧ��");
					e.printStackTrace();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "ϵͳ��������ʧ��");
					e.printStackTrace();
				}finally {
					try {
						dbUtil.closeCon(con);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				
				
				
				
			}
		});
		jb_add.setFont(new Font("����", Font.BOLD, 16));
		jb_add.setBounds(67, 209, 93, 23);
		getContentPane().add(jb_add);
		
		JButton jb_reset = new JButton("\u91CD\u586B");
		jb_reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*��ձ�����*/
				jtf_name.setText("");
				jta_detail.setText("");
			}
		});
		jb_reset.setFont(new Font("����", Font.BOLD, 16));
		jb_reset.setBounds(275, 210, 93, 23);
		getContentPane().add(jb_reset);

	}
}
