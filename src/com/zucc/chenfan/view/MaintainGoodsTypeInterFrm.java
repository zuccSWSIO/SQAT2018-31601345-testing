package com.zucc.chenfan.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.zucc.chenfan.dao.Goods_typeDao;
import com.zucc.chenfan.model.Goods_type;
import com.zucc.chenfan.model.Operator;
import com.zucc.chenfan.util.DbUtil;
import com.zucc.chenfan.util.IntegerUtil;
import com.zucc.chenfan.util.StringUtil;

public class MaintainGoodsTypeInterFrm extends JInternalFrame {
	DbUtil dbUtil = new DbUtil();
	Goods_typeDao goodsTypeDao = new Goods_typeDao();
	
	private JTable jtb_table;
	private JTextField jtf_searchBox;
	private JTextField jtf_id;
	private JTextField jtf_name;
	private JRadioButton jrb_universe;
	private JRadioButton jrb_id;
	private JRadioButton jrb_name;
	private JRadioButton jrb_detail;
	private JTextArea jta_detail;
	
	/*以下变量用于保存从数据库拉取的信息，方便在表单更新和重置时使用,格式storeXXX*/
	private int storeId = 0;
	private String storeName = null;
	private String storeDetail = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MaintainGoodsTypeInterFrm frame = new MaintainGoodsTypeInterFrm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/*创建该类需要的方法*/
	
	/*用于填充表格*/
	private void fillTable(Goods_type goodType, boolean hasId) {//boolean hadId用于配合Dao类中的万能查询方法使用（当要查询的数据中有int类型的数据时方便区别是默认值0还是搜索值是0的情况）
		DefaultTableModel dtm = (DefaultTableModel) jtb_table.getModel();
		dtm.setRowCount(0);//设置表格行数为零，即在下次查询前清空表格。
		/*将storeXXX赋值为空,并将表单中的文本框设置为空*/
		storeId = 0;
		storeName = null;
		storeDetail = null;
		jtf_id.setText("");
		jtf_name.setText("");
		jta_detail.setText("");
		
		Connection con = null;
		try {
			con = dbUtil.getCon();//得到数据库连接
			ResultSet rs = goodsTypeDao.goodsTypeList(con, goodType, hasId);//查询数据库并得到结果集合ResultSet
			
			int i = 0;//用于计算每一项的序号
			while(rs.next()) {
				i++;
				/*Vector和ArrayList有区别，Vector是线程安排的*/
				Vector v = new Vector();
				v.add(i);
				/*导入Vector的数据类型统一转换为String,方便在表格中点击读取数值而不会报错*/
				v.add(rs.getString("goods_type_id"));
				v.add(rs.getString("goods_type_name"));
				v.add(rs.getString("goods_type_detail"));
				dtm.addRow(v);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				dbUtil.closeCon(con);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*用于重置表单*/
	private void resetForm() {
		//将表单中的数据使用之前选中时保存的值还原回来
		jtf_name.setText(storeName);
		jta_detail.setText(storeDetail);
		
	}
	
	/*用于重置所有的JRadioButton的Selected属性为false*/
	private void resetSearch() {
		//实现了JRadioButton的单选功能
		jrb_universe.setSelected(false);
		jrb_id.setSelected(false);
		jrb_name.setSelected(false);
		jrb_detail.setSelected(false);
	}
	
	/*用于在选中表格的一行记录时生成下方表单的数值*/
	private void fillForm() {
		/*获取选中的行*/
		int row = jtb_table.getSelectedRow();
		jtf_id.setText((String) jtb_table.getValueAt(row, 1));
		jtf_name.setText((String) jtb_table.getValueAt(row, 2));
		jta_detail.setText((String) jtb_table.getValueAt(row, 3));
		/*保存获得的信息到storeXXX，方便修改和重置;*/
		storeId = Integer.parseInt(jtf_id.getText());
		storeName = jtf_name.getText();
		storeDetail = jta_detail.getText();
	}
	
	/*实现当鼠标滚轮滚动时，表单的值随着表格行数的变化而更新*/
	private void fillFormWhenMouseWheel(MouseWheelEvent mouseWheelEvent) {
		/*java中如何判断鼠标滚轮是向上滚还是向下滚*/
		int beforeSelection = jtb_table.getSelectedRow();
		int afterSelection = beforeSelection;
		if(mouseWheelEvent.getWheelRotation() == 1) {//当鼠标下滚时该值为1
			afterSelection ++;
			if(afterSelection >= (jtb_table.getRowCount()) ) {
				afterSelection = jtb_table.getRowCount()-1;
			}
		}
		else if(mouseWheelEvent.getWheelRotation() == -1){//当鼠标下滚时该值为-1
			/*表格的index从0开始*/
			afterSelection --;
			if(afterSelection <= -1) {
				afterSelection = 0;
			}
		}
		jtb_table.setRowSelectionInterval(afterSelection, afterSelection);
		fillForm();
	}
	
	/*在点击更新按钮时判定时候满足了可以更新的表面条件（包括数据库查重）*/
	private boolean allowUpdate() {
		boolean result = true;//如果需要更新，结果返回ture
		/*〇判断有无选中表格中的记录*/
		if(StringUtil.isEmpty(storeName)) {
			result = false;
			JOptionPane.showMessageDialog(null, "请选择任何一条记录");
			return result;
		}
		
		/*①判断表单有没有实际修改，需要使用选中表格行数时保存在变量中的数据“storeXXX”进行比较*/
//		if(如果表单的值和保存的值相等) {
			/*result = false;
			JOptionPane.showMessageDialog(null, "该条记录没有任何修改，无需更新");
			return result;*/
//		}
//		else {
//		}
		if(jtf_name.getText().equals(storeName) && jta_detail.getText().equals(storeDetail)) {
			result = false;
			JOptionPane.showMessageDialog(null, "该条记录没有任何修改，无需更新");
			return result;
		}
		
		/*②判断表单中必须要填写的值时候按正确格式填写（包括判断是否为空）*/
		if(StringUtil.isEmpty(jtf_name.getText())) {
			//此处可以逐个判断各个填写条件，使用if-else if的形式，当有错误时给出消息并返回
			result = false;
			JOptionPane.showMessageDialog(null, "请填写新的商品类型名称");
			return result;
		}
		if(StringUtil.isEmpty(jta_detail.getText())) {
			result = false;
			JOptionPane.showMessageDialog(null, "请填写新的商品类别详情");
			return result;
		}
		
		/*③用户再次确认是否要更新该条记录*/
		int choice = -1;
		choice = JOptionPane.showConfirmDialog(null, "确定要更新这条商品类别信息吗？\n商品类别编号：" + storeId + "\n商品类别名称：" + storeName + " ——> " + jtf_name.getText() + "\n商品类别详情：" + storeDetail + " ——> " + jta_detail.getText());
		if(choice != 0) {
			result = false;
			return result;
		}
		
		/*④根据实际工程需求情况查询数据库并验证是否有重复值得问题*/
		//当查询结果显示该次填写的数据不满足要求时，给出消息并返回false;
		/*查询是否有重名的商品类别*/
		Connection con = null;
		Goods_type temp = new Goods_type();
		temp.setGoods_type_name(jtf_name.getText());
		
		try {
			con = dbUtil.getCon();
			boolean  hasEqual = goodsTypeDao.isEqual(con, temp);
			if(hasEqual) {
				result = false;
				JOptionPane.showMessageDialog(null, "已经存在相同名称的商品类别，更新失败");
				jtf_name.setText(storeName);
				return result;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
			JOptionPane.showMessageDialog(null, "系统错误，数据库查询失败");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			JOptionPane.showMessageDialog(null, "系统错误，数据库查询失败");
			return result;
		}finally {
			try {
				dbUtil.closeCon(con);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	/*用于确定可否删除记录*/
	private boolean allowDelete() {
		boolean result = true;
		
		/*①判断表单的当前情况是否为空，如果为空result = false、提示并return result*/
		if(StringUtil.isEmpty(storeName)) {
			result = false;
			JOptionPane.showMessageDialog(null, "您还没有选择任何一条记录，无法进行删除操作");
			return result;
		}
		
		/*②提示用户是否要删除该条记录，如果返回结果不为0则result = false并return result*/
		int choice = -1;
		choice = JOptionPane.showConfirmDialog(null, "是否要删除这条商品类型？\n商品类型编号：" + storeId + "\n商品类型名称：" + storeName + "\n商品类型详情：" + storeDetail);
		if(choice != 0) {
			result = false;
			return result;
		}
			
		/*③查询数据库是否在其他表中有与该条记录相关联的记录如果有，则提示。当返回结果不为0时result = false并return result*/
		//当返回结果为0时合理调用其他实体的Dao类中的delete方法，对相关数据进行删除
		/*警告！！！，此处因为还没有学对应的相关联表的删除方法，所以暂时不作处理，但以后一定要重新编辑*/
		
		
		return result;
	}
	

	/**
	 * Create the frame.
	 */
	public MaintainGoodsTypeInterFrm() {
		setResizable(true);
		setIconifiable(true);
		setMaximizable(true);
		setTitle("\u5546\u54C1\u7C7B\u522B\u4FE1\u606F\u7EF4\u62A4");
		setClosable(true);
		setBounds(100, 100, 647, 408);
		getContentPane().setLayout(new BorderLayout(0, 5));
		
		JPanel jp_searchBar = new JPanel();
		getContentPane().add(jp_searchBar, BorderLayout.NORTH);
		jp_searchBar.setLayout(new BorderLayout(0, 0));
		
		JPanel jp_searchBarTop = new JPanel();
		jp_searchBar.add(jp_searchBarTop, BorderLayout.NORTH);
		jp_searchBarTop.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("\u8BF7\u8F93\u5165\u641C\u7D22\u5185\u5BB9\uFF1A");
		lblNewLabel.setFont(new Font("黑体", Font.PLAIN, 15));
		jp_searchBarTop.add(lblNewLabel, BorderLayout.WEST);
		
		jtf_searchBox = new JTextField();
		jtf_searchBox.setText("");
		jp_searchBarTop.add(jtf_searchBox, BorderLayout.CENTER);
		jtf_searchBox.setColumns(10);
		
		JButton jb_search = new JButton("\u641C\u7D22");
		jb_search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*实现全局、按商品种类编号、按商品种类名称、按商品种类详情模糊查找*/
				String temp = jtf_searchBox.getText();//获取搜索框文本内容
				//判断搜索选项是否为空
				if(!jrb_universe.isSelected() && !jrb_id.isSelected() && !jrb_name.isSelected() && !jrb_detail.isSelected()) {
					JOptionPane.showMessageDialog(null, "请选择任何一种搜索方式");
					return;
				}
				
				/*开始解析搜索条件*/
				Goods_type goodsType = new Goods_type();
				boolean hasId = false;//hasId表示搜索条件中是否含有“按Id搜索”,如果是则值为true
				if(StringUtil.isEmpty(temp)) {
					//当输入框内容为空时拉取所有数据
				}
				else if(jrb_universe.isSelected()) {//实现全局搜索
					if(IntegerUtil.isNumeric(temp)) {
						goodsType.setGoods_type_id(Integer.parseInt(temp));
						hasId = true;
					}
					goodsType.setGoods_type_name(temp);
					goodsType.setGoods_type_detail(temp);
				}
				else if(jrb_id.isSelected()) {//实现按商品种类编号搜索（模糊）
					if(!IntegerUtil.isNumeric(temp)) {
						JOptionPane.showMessageDialog(null, "商品种类编号应为纯数字，请重新输入");
						return;
					}
					goodsType.setGoods_type_id(Integer.parseInt(temp));
					hasId = true;
				}
				else if(jrb_name.isSelected()) {//实现按商品种类名称搜索（模糊）
					goodsType.setGoods_type_name(temp);
				}
				else if(jrb_detail.isSelected()){//实现按商品种类描述查询
					goodsType.setGoods_type_detail(temp);
				}
				
				//执行搜索
				fillTable(goodsType, hasId);
			}
		});
		jb_search.setFont(new Font("黑体", Font.BOLD, 16));
		jp_searchBarTop.add(jb_search, BorderLayout.EAST);
		
		JPanel jp_searchBarBottom = new JPanel();
		jp_searchBar.add(jp_searchBarBottom);
		
		jrb_universe = new JRadioButton("\u5168\u5C40\u641C\u7D22");
		jrb_universe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetSearch();
				jrb_universe.setSelected(true);
			}
		});
		jrb_universe.setSelected(true);
		jp_searchBarBottom.add(jrb_universe);
		
		jrb_id = new JRadioButton("\u6309\u7F16\u53F7\u641C\u7D22");
		jrb_id.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetSearch();
				jrb_id.setSelected(true);
			}
		});
		jrb_id.setSelected(false);
		jrb_id.setEnabled(true);
		jp_searchBarBottom.add(jrb_id);
		
		jrb_name = new JRadioButton("\u6309\u540D\u79F0\u641C\u7D22");
		jrb_name.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetSearch();
				jrb_name.setSelected(true);
			}
		});
		jp_searchBarBottom.add(jrb_name);
		
		jrb_detail = new JRadioButton("\u6309\u63CF\u8FF0\u641C\u7D22");
		jrb_detail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetSearch();
				jrb_detail.setSelected(true);
			}
		});
		jrb_detail.setEnabled(true);
		jrb_detail.setSelected(false);
		jp_searchBarBottom.add(jrb_detail);
		
		JScrollPane jsp_table = new JScrollPane();
		getContentPane().add(jsp_table, BorderLayout.CENTER);
		
		jtb_table = new JTable();
		jtb_table.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				/*当鼠标滚轮滚动时，更新表单*/
				fillFormWhenMouseWheel(arg0);
			}
		});
		jtb_table.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				/*当鼠标按住左键并拖动时，更新表单*/
				fillForm();
			}
		});
		jtb_table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				/*当鼠标按压表格的某一行，更新表单*/
				fillForm();
			}
		});
		jtb_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jtb_table.setBorder(new LineBorder(Color.BLUE));
		jtb_table.setModel(new DefaultTableModel(
			new Object[][] {
				{"1", null, null, null},
				{"", null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
			},
			new String[] {
				"\u5E8F\u53F7", "\u5546\u54C1\u79CD\u7C7B\u7F16\u53F7", "\u5546\u54C1\u79CD\u7C7B\u540D\u79F0", "\u5546\u54C1\u79CD\u7C7B\u63CF\u8FF0"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		jtb_table.getColumnModel().getColumn(0).setResizable(false);
		jtb_table.getColumnModel().getColumn(1).setResizable(false);
		jtb_table.getColumnModel().getColumn(1).setPreferredWidth(92);
		jtb_table.getColumnModel().getColumn(2).setResizable(false);
		jtb_table.getColumnModel().getColumn(2).setPreferredWidth(99);
		jtb_table.getColumnModel().getColumn(3).setResizable(false);
		jtb_table.getColumnModel().getColumn(3).setPreferredWidth(309);
		jsp_table.setViewportView(jtb_table);//这条语句使表头可见
		
		JPanel jp_form = new JPanel();
		getContentPane().add(jp_form, BorderLayout.SOUTH);
		
		JPanel jp_id = new JPanel();
		jp_form.add(jp_id);
		
		JLabel label = new JLabel("\u5546\u54C1\u79CD\u7C7B\u7F16\u53F7\uFF1A");
		label.setFont(new Font("黑体", Font.PLAIN, 15));
		jp_id.add(label);
		
		jtf_id = new JTextField();
		jtf_id.setEditable(false);
		jp_id.add(jtf_id);
		jtf_id.setColumns(10);
		
		JPanel jp_name = new JPanel();
		jp_form.add(jp_name);
		
		JLabel lblNewLabel_1 = new JLabel("\u5546\u54C1\u79CD\u7C7B\u540D\u79F0\uFF1A");
		lblNewLabel_1.setFont(new Font("黑体", Font.PLAIN, 15));
		jp_name.add(lblNewLabel_1);
		
		jtf_name = new JTextField();
		jtf_name.setText("");
		jp_name.add(jtf_name);
		jtf_name.setColumns(10);
		
		JPanel jp_detail = new JPanel();
		jp_form.add(jp_detail);
		jp_detail.setLayout(new BorderLayout(0, 0));
		
		JLabel lblS = new JLabel("\u5546\u54C1\u79CD\u7C7B\u63CF\u8FF0\uFF1A");
		lblS.setFont(new Font("黑体", Font.PLAIN, 15));
		jp_detail.add(lblS, BorderLayout.WEST);
		
		jta_detail = new JTextArea();
		jta_detail.setRows(2);
		jta_detail.setColumns(20);
		jp_detail.add(jta_detail, BorderLayout.CENTER);
		
		JPanel jp_button = new JPanel();
		jp_form.add(jp_button);
		
		JButton jb_update = new JButton("\u66F4\u65B0");
		jb_update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
/*实现更新功能*/
				
				//确认是否可以更新
				if(!allowUpdate()) {
					return;
				}
				
				/*更新数据库并刷新表格*/
				Connection con = null;
				Goods_type goodsType = new Goods_type();//实体model类
				goodsType.setGoods_type_id(storeId);//set所有有变动的数据
				goodsType.setGoods_type_name(jtf_name.getText());
				goodsType.setGoods_type_detail(jta_detail.getText());
				
				try {
					con = dbUtil.getCon();
					int temp = goodsTypeDao.updateGoodsType(con, goodsType);
					if(temp == 1) {
						JOptionPane.showMessageDialog(null, "该商品类别信息更新成功");
						//刷新表格（完整列出，不能恢复之前的搜索结果）
						fillTable(new Goods_type(), false);
						//更新storeXXX
						storeId = Integer.parseInt(jtf_id.getText());
						storeName = jtf_name.getText();
						storeDetail = jta_detail.getText();
					}
					else {
						JOptionPane.showMessageDialog(null, "数据库更新数据时出现问题，更新失败");
						/*将表单重置*/
						resetForm();
					}
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "数据库更新数据时出现问题，更新失败");
					/*将表单重置*/
					resetForm();
					e.printStackTrace();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "数据库连接出现错误，更新失败");
					/*将表单重置*/
					resetForm();
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
		jb_update.setForeground(Color.BLACK);
		jb_update.setFont(new Font("黑体", Font.BOLD, 16));
		jp_button.add(jb_update);
		
		JButton jb_delete = new JButton("\u5220\u9664");
		jb_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*用于实现删除记录的功能*/
				if(!allowDelete()) {
					return;
				}
				
				Connection con = null;
				Goods_type goodsType = new Goods_type();//new一个实体类
				goodsType.setGoods_type_id(storeId);//设置实体类的查询条件
				try {
					con = dbUtil.getCon();
					int temp = goodsTypeDao.deleteGoodsType(con, goodsType);//变量temp用于接受是否删除成功的信息，如果返回1则删除成功
					if(temp == 1) {
						JOptionPane.showMessageDialog(null, "该条记录已被成功删除");
						/*刷新表格并清空表单,并重置storeXXX*/
						fillTable(new Goods_type(), false);
						storeId = 0;
						storeName = null;
						storeDetail = null;
						jtf_id.setText("");
						jtf_name.setText("");
						jta_detail.setText("");
					}
					else {
						JOptionPane.showMessageDialog(null, "系统错误，删除失败");
					}
				} catch (SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "系统错误，删除失败");
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "系统错误，删除失败");
				}finally {
					try {
						dbUtil.closeCon(con);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
	
			}
		});
		jb_delete.setForeground(Color.RED);
		jb_delete.setFont(new Font("黑体", Font.BOLD, 16));
		jp_button.add(jb_delete);
		
		JButton jb_reset = new JButton("\u91CD\u7F6E");
		jb_reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*实现重置表单按钮的功能*/
				resetForm();
			}
		});
		jb_reset.setForeground(Color.BLUE);
		jb_reset.setFont(new Font("黑体", Font.BOLD, 16));
		jp_button.add(jb_reset);
		
		/*使表头不可拖动*/
		JTableHeader tableHeader = jtb_table.getTableHeader();
		tableHeader.setReorderingAllowed(false);
		
		/*使用borderLayout布局方法，修改东西南北中各个区域大小的方法*/
		jp_form.setPreferredSize(new Dimension(0, 100));
		
		/*使用方法“fillTable”完成数据填充*/        
		fillTable(new Goods_type(), false);
		
	}

	

}
