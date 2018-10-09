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

import com.zucc.chenfan.dao.GoodsDao;
import com.zucc.chenfan.dao.Goods_typeDao;
import com.zucc.chenfan.model.Goods;
import com.zucc.chenfan.model.Goods_type;
import com.zucc.chenfan.util.DbUtil;
import com.zucc.chenfan.util.IntegerUtil;
import com.zucc.chenfan.util.MyImageUtil;
import com.zucc.chenfan.util.StringUtil;

public class AddGoodsInterFrm extends JInternalFrame {
	Goods_typeDao goodsTypeDao = new Goods_typeDao();
	GoodsDao goodsDao = new GoodsDao();
	DbUtil dbUtil = new DbUtil();
	MyImageUtil myImageUtil = new MyImageUtil();
	
	private JTextField jtf_goodsName;
	private JTextField jtf_brand;
	private JTextField jtf_unitprice;
	private JTextField jtf_barcode;
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
					AddGoodsInterFrm frame = new AddGoodsInterFrm();
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
		jcb_typeName.addItem("请选择商品类别");
		try {
			con = dbUtil.getCon();
			rs = goodsTypeDao.goodsTypeList(con, new Goods_type(), false);
			int index = 0;
			while(rs.next()) {
				index ++;
				jcb_typeName.addItem(rs.getString("goods_type_name"));
				storeTypeId[index] = rs.getInt("goods_type_id");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "读取物品类别信息失败");
			e.printStackTrace();
		}
		
		
	}
	
	
	
	
	/**
	 * Create the frame.
	 */
	public AddGoodsInterFrm() {
		setClosable(true);
		setIconifiable(true);
		setTitle("\u65B0\u589E\u5546\u54C1\u7C7B\u522B");
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
		
		JLabel label_3 = new JLabel("\u5546\u54C1\u540D\uFF1A     ");
		panel.add(label_3);
		label_3.setFont(new Font("黑体", Font.PLAIN, 15));
		
		jtf_goodsName = new JTextField();
		jtf_goodsName.setBackground(Color.WHITE);
		label_3.setLabelFor(jtf_goodsName);
		panel.add(jtf_goodsName);
		jtf_goodsName.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));
		
		JLabel label_2 = new JLabel("\u5546\u54C1\u7C7B\u578B\uFF1A   ");
		panel_2.add(label_2);
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setFont(new Font("黑体", Font.PLAIN, 15));
		
		jcb_typeName = new JComboBox();
		jcb_typeName.setMaximumRowCount(5);
		jcb_typeName.setModel(new DefaultComboBoxModel(new String[] {"\u8BF7\u9009\u62E9\u5546\u54C1\u7C7B\u578B"}));
		panel_2.add(jcb_typeName);
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel = new JLabel("\u5546\u54C1\u54C1\u724C\uFF1A   ");
		lblNewLabel.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_3.add(lblNewLabel);
		
		jtf_brand = new JTextField();
		panel_3.add(jtf_brand);
		jtf_brand.setText("");
		jtf_brand.setColumns(10);
		
		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel_1 = new JLabel("\u5546\u54C1\u5355\u4EF7\uFF1A   ");
		panel_4.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("黑体", Font.PLAIN, 15));
		
		jtf_unitprice = new JTextField();
		panel_4.add(jtf_unitprice);
		jtf_unitprice.setColumns(10);
		
		JPanel panel_5 = new JPanel();
		panel_1.add(panel_5);
		panel_5.setLayout(new BoxLayout(panel_5, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel_2 = new JLabel("\u5546\u54C1\u6761\u7801\uFF1A   ");
		panel_5.add(lblNewLabel_2);
		lblNewLabel_2.setFont(new Font("黑体", Font.PLAIN, 15));
		
		jtf_barcode = new JTextField();
		panel_5.add(jtf_barcode);
		jtf_barcode.setColumns(10);
		
		JPanel panel_6 = new JPanel();
		panel_1.add(panel_6);
		panel_6.setLayout(new BoxLayout(panel_6, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel_3 = new JLabel("\u5546\u54C1\u8BE6\u60C5\uFF1A   ");
		lblNewLabel_3.setFont(new Font("黑体", Font.PLAIN, 15));
		panel_6.add(lblNewLabel_3);
		
		jta_detail = new JTextArea();
		jta_detail.setRows(2);
		panel_6.add(jta_detail);
		
		JPanel panel_7 = new JPanel();
		panel_1.add(panel_7);
		panel_7.setLayout(new BoxLayout(panel_7, BoxLayout.X_AXIS));
		
		JLabel lblNewLabel_4 = new JLabel("\u552E\u5356\u72B6\u6001\uFF1A   ");
		panel_7.add(lblNewLabel_4);
		lblNewLabel_4.setFont(new Font("黑体", Font.PLAIN, 15));
		
		JComboBox jcb_state = new JComboBox();
		panel_7.add(jcb_state);
		jcb_state.setModel(new DefaultComboBoxModel(new String[] {"\u8BF7\u9009\u62E9", "\u51FA\u552E\u4E2D", "\u4E0B\u67B6\u4E2D"}));
		
		JButton jb_add = new JButton("\u65B0\u589E");
		jb_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*①判断商品信息格式是否正确填写*/
				
				if(StringUtil.isEmpty(jtf_goodsName.getText())) {
					JOptionPane.showMessageDialog(null, "请输入商品名称");
					return;
				}
				if(jcb_typeName.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null, "请选择商品类型");
					return;
				}
				if(StringUtil.isEmpty(jtf_brand.getText())) {
					JOptionPane.showMessageDialog(null, "请输入商品品牌");
					return;
				}
				if(StringUtil.isEmpty(jtf_unitprice.getText())) {
					JOptionPane.showMessageDialog(null, "请输入商品价格");
					return;
				}
				else if(!IntegerUtil.isNumeric(jtf_unitprice.getText().replace(".", ""))){
					JOptionPane.showMessageDialog(null, "价格不应含有除数字和“.”的其他任何字符，请重新输入");
					return;
				}
				if(StringUtil.isEmpty(jtf_barcode.getText())) {
					JOptionPane.showMessageDialog(null, "请输入商品条码编号");
					return;
				}
				if(StringUtil.isEmpty(jta_detail.getText())) {
					JOptionPane.showMessageDialog(null, "请输入商品详情");
					return;
				}
				if(jcb_state.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null, "请输入商品的上架状态");
					return;
				}
				
				
				
				
				boolean state = false;
				if(jcb_state.getSelectedItem().equals("出售中")) {
					state = true;
				}
				Goods goods = new Goods(jtf_goodsName.getText(), storeTypeId[jcb_typeName.getSelectedIndex()], jtf_brand.getText(), Float.valueOf(jtf_unitprice.getText()), jtf_barcode.getText(), state, jta_detail.getText());
				
				/*②判断商品有无重复（按商品条码查询）*/
				Connection con = null;
				try {
					con = dbUtil.getCon();
					boolean hasEqual = goodsDao.isEqual(con, goods);
					if(hasEqual) {
						JOptionPane.showMessageDialog(null, "已经存在相同条码的商品，添加失败");
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
					int result = goodsDao.addGoods(con, goods);
					if(result == 1) {
						JOptionPane.showMessageDialog(null, "该条商品记录添加成功");
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
				jtf_goodsName.setText("");
				jtf_brand.setText("");
				jtf_unitprice.setText("");
				jtf_barcode.setText("");
				fillComBox();
				jta_detail.setText("");
				jcb_state.setSelectedIndex(0);
				
			}
		});
		jb_reset.setBounds(262, 207, 95, 25);
		getContentPane().add(jb_reset);
		
		/*在窗口初始化结束后完成对商品类别选择下拉框的填充*/
		fillComBox();

	}
}
