package com.zucc.chenfan.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.zucc.chenfan.dao.ServiceDao;
import com.zucc.chenfan.dao.Service_typeDao;
import com.zucc.chenfan.model.Service;
import com.zucc.chenfan.model.Service_type;
import com.zucc.chenfan.util.DbUtil;
import com.zucc.chenfan.util.IntegerUtil;
import com.zucc.chenfan.util.MyImageUtil;
import com.zucc.chenfan.util.StringUtil;

public class AddServiceInterFrm extends JInternalFrame {
	Service_typeDao serviceTypeDao = new Service_typeDao();
	ServiceDao serviceDao = new ServiceDao();
	DbUtil dbUtil = new DbUtil();
	MyImageUtil myImageUtil = new MyImageUtil();
	
	private JTextField jtf_serviceName;
	private JTextField jtf_price;
	private JComboBox<String> jcb_typeName;
	private JTextArea jta_detail;
	
	
	private int[] storeTypeId = new int[100];
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddServiceInterFrm frame = new AddServiceInterFrm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void fillComBox() {
		Connection con = null;
		ResultSet rs = null;
		jcb_typeName.removeAllItems();
		jcb_typeName.addItem("请选择服务类别");
		try {
			con = dbUtil.getCon();
			rs = serviceTypeDao.serviceTypeList(con, new Service_type(), false);
			int index = 0;
			while(rs.next()) {
				index ++;
				jcb_typeName.addItem(rs.getString("service_type_name"));
				storeTypeId[index] = rs.getInt("service_type_id");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "读取服务类别信息失败");
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	/**
	 * Create the frame.
	 */
	public AddServiceInterFrm() {
		setClosable(true);
		setIconifiable(true);
		setTitle("\u65B0\u589E\u670D\u52A1\u7C7B\u522B");
		setBounds(100, 100, 442, 277);
		getContentPane().setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(UIManager.getBorder("Tree.editorBorder"));
		panel_1.setBounds(0, 0, 435, 184);
		getContentPane().add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.PAGE_AXIS));
		
		JPanel panel = new JPanel();
		panel_1.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel label_3 = new JLabel("\u670D\u52A1\u540D\uFF1A     ");
		panel.add(label_3);
		label_3.setFont(new Font("黑体", Font.PLAIN, 15));
		
		jtf_serviceName = new JTextField();
		jtf_serviceName.setBackground(Color.WHITE);
		label_3.setLabelFor(jtf_serviceName);
		panel.add(jtf_serviceName);
		jtf_serviceName.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));
		
		JLabel label_2 = new JLabel("\u670D\u52A1\u7C7B\u578B\uFF1A   ");
		panel_2.add(label_2);
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setFont(new Font("黑体", Font.PLAIN, 15));
		
		jcb_typeName = new JComboBox();
		jcb_typeName.setMaximumRowCount(5);
		jcb_typeName.setModel(new DefaultComboBoxModel(new String[] {"\u8BF7\u9009\u62E9\u670D\u52A1\u7C7B\u578B"}));
		panel_2.add(jcb_typeName);
		
		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel_1 = new JLabel("\u670D\u52A1\u4F63\u91D1\uFF1A   ");
		panel_4.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("黑体", Font.PLAIN, 15));
		
		jtf_price = new JTextField();
		panel_4.add(jtf_price);
		jtf_price.setColumns(10);
		
		JPanel panel_6 = new JPanel();
		panel_1.add(panel_6);
		panel_6.setLayout(new BoxLayout(panel_6, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel_3 = new JLabel("\u670D\u52A1\u8BE6\u60C5\uFF1A   ");
		lblNewLabel_3.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_6.add(lblNewLabel_3);
		
		jta_detail = new JTextArea();
		jta_detail.setRows(2);
		panel_6.add(jta_detail);
		
		JPanel panel_7 = new JPanel();
		panel_1.add(panel_7);
		panel_7.setLayout(new BoxLayout(panel_7, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel_4 = new JLabel("\u4E0A\u7EBF\u72B6\u6001\uFF1A   ");
		panel_7.add(lblNewLabel_4);
		lblNewLabel_4.setFont(new Font("黑体", Font.PLAIN, 15));
		
		JComboBox jcb_state = new JComboBox();
		panel_7.add(jcb_state);
		jcb_state.setModel(new DefaultComboBoxModel(new String[] {"\u8BF7\u9009\u62E9", "\u670D\u52A1\u4E0A\u7EBF\u4E2D", "\u670D\u52A1\u4E0B\u7EBF\u4E2D"}));
		
		JButton jb_add = new JButton("\u65B0\u589E");
		jb_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*①判断服务信息格式是否正确填写*/
				
				if(StringUtil.isEmpty(jtf_serviceName.getText())) {
					JOptionPane.showMessageDialog(null, "请输入服务名称");
					return;
				}
				if(jcb_typeName.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null, "请选择服务类型");
					return;
				}
				if(StringUtil.isEmpty(jtf_price.getText())) {
					JOptionPane.showMessageDialog(null, "请输入服务价格");
					return;
				}
				else if(!IntegerUtil.isNumeric(jtf_price.getText().replace(".", ""))){
					JOptionPane.showMessageDialog(null, "价格不应含有除数字和“.”的其他任何字符，请重新输入");
					return;
				}
				if(StringUtil.isEmpty(jta_detail.getText())) {
					JOptionPane.showMessageDialog(null, "请输入服务详情");
					return;
				}
				if(jcb_state.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null, "请输入服务的上线状态");
					return;
				}
				
				
				
				
				boolean state = false;
				if(jcb_state.getSelectedItem().equals("服务上线中")) {
					state = true;
				}
				Service service = new Service(jtf_serviceName.getText(), storeTypeId[jcb_typeName.getSelectedIndex()], Float.valueOf(jtf_price.getText()), jta_detail.getText(), state);
				
				/*②判断服务有无重复（按服务条码查询）*/
				Connection con = null;
				try {
					con = dbUtil.getCon();
					boolean hasEqual = serviceDao.isEqual(con, service);
					if(hasEqual) {
						JOptionPane.showMessageDialog(null, "已经存在相同名称的服务，添加失败");
						dbUtil.closeCon(con);
						return;
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "系统错误，添加失败");
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "系统错误，添加失败");
				}finally {
					try {
						dbUtil.closeCon(con);
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(null, "系统错误，添加失败");
						e.printStackTrace();
					}
				}
				
				/*③新增数据*/
				con = null;
				
				try {
					con = dbUtil.getCon();
					int result = serviceDao.addService(con, service);
					if(result == 1) {
						JOptionPane.showMessageDialog(null, "该条服务记录添加成功");
					}
					else {
						JOptionPane.showMessageDialog(null, "系统错误，添加失败");
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
			}
		});
		jb_add.setBounds(83, 207, 95, 25);
		getContentPane().add(jb_add);
		
		JButton jb_reset = new JButton("\u91CD\u7F6E");
		jb_reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*清空表单信息*/
				jtf_serviceName.setText("");
				jtf_price.setText("");
				fillComBox();
				jta_detail.setText("");
				jcb_state.setSelectedIndex(0);
				
			}
		});
		jb_reset.setBounds(262, 207, 95, 25);
		getContentPane().add(jb_reset);
		
		/*在窗口初始化结束后完成对服务类别选择下拉框的填充*/
		fillComBox();

	}
}
