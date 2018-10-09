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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

import com.zucc.chenfan.dao.GoodsDao;
import com.zucc.chenfan.dao.Goods_typeDao;
import com.zucc.chenfan.model.Goods;
import com.zucc.chenfan.model.Goods_type;
import com.zucc.chenfan.util.DbUtil;
import com.zucc.chenfan.util.IntegerUtil;
import com.zucc.chenfan.util.StringUtil;

public class MaintainGoodsInterFrm extends JInternalFrame {
	DbUtil dbUtil = new DbUtil();
	Goods_typeDao goodsTypeDao = new Goods_typeDao();
	GoodsDao goodsDao = new GoodsDao();
	
	private JTable jtb_table;
	private JTextField jtf_searchBox;
	private JTextField jtf_id;
	private JTextField jtf_name;
	private JRadioButton jrb_universe;
	private JRadioButton jrb_id;
	private JRadioButton jrb_name;
	private JRadioButton jrb_detail;
	private JTextArea jta_detail;
	private JTextField jtf_brand;
	private JTextField jtf_unitprice;
	private JTextField jtf_barcode;
	private JComboBox jcb_typeName;
	private JRadioButton jrb_brand;
	private JRadioButton jrb_barcode;
	private JComboBox jcb_state;
	private JComboBox jcb_typeNameModify;
	private JComboBox jcb_stateModify;
	/*以下变量用于保存从数据库拉取的信息，方便在表单更新和重置时使用,格式storeXXX*/
	private int storeId = 0;
	private String storeName = null;
	private String storeDetail = null;
	private String storeBarcode = null;
	private String storeBrand = null;
	private float storeUnitprice = -1;
	private int storeTypeIdSelected = -1;
	private String storeTypeNameSelected = null;
	/*这里保存的商品状态信息使用字符串形式，在存入数据库前要转化为boolean形式*/
	private String storeState = null;
	private int[] storeTypeId = new int[100];
	private String[] storeTypeName = new String[100]; 
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MaintainGoodsInterFrm frame = new MaintainGoodsInterFrm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/*创建该类需要的方法*/
	
	/*用于填充表格*/
	private void fillTable(Goods goods, boolean hasState, int[] typeIdSet) {//boolean hasState用于配合Dao类中的万能查询方法使用（当要查询的数据中有boolean类型的数据时方便区别是否搜索）;typeIdSet用来保存当搜索类型名称时获得的类型ID
		DefaultTableModel dtm = (DefaultTableModel) jtb_table.getModel();
		dtm.setRowCount(0);//设置表格行数为零，即在下次查询前清空表格。
		/*将storeXXX赋值为空,并将表单中的文本框设置为空*/
		storeId = -1;
		storeName = null;
		storeDetail = null;
		storeBarcode = null;
		storeBrand = null;
		storeState = null;
		storeTypeIdSelected = -1;
		storeTypeNameSelected = null;
		storeUnitprice = -1;
		
		jtf_id.setText("");
		jtf_name.setText("");
		jta_detail.setText("");
		jtf_brand.setText("");
		jtf_barcode.setText("");
		jtf_unitprice.setText("");
		jcb_stateModify.setSelectedIndex(0);
		jcb_typeNameModify.setSelectedIndex(0);
		
		
		Connection con = null;
		try {
			con = dbUtil.getCon();//得到数据库连接
			ResultSet rs = goodsDao.goodsList(con, goods, hasState, typeIdSet);//查询数据库并得到结果集合ResultSet
			
			int i = 0;//用于计算每一项的序号
			while(rs.next()) {
				i++;
				/*Vector和ArrayList有区别，Vector是线程安排的*/
				Vector v = new Vector();
				v.add(i);
				/*导入Vector的数据类型统一转换为String,方便在表格中点击读取数值而不会报错*/
				v.add(rs.getString("goods_id"));
				v.add(rs.getString("goods_name"));
				
				for(int j=0 ; j < storeTypeId.length; j++) {
					if(rs.getInt("goods_type_id") == storeTypeId[j]) {
						v.add(storeTypeName[j]);
						break;
					}
				}
				v.add(rs.getString("goods_brand"));
				v.add(rs.getString("goods_unitprice"));
				v.add(rs.getString("goods_barcode"));
				v.add(rs.getString("goods_detail"));
				if(rs.getBoolean("goods_state")) {
					v.add("出售中");
				}
				else{
					v.add("下架中");
				}
				v.add(rs.getString("goods_createdate"));
				v.add(rs.getString("goods_modifydate"));
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
		jtf_barcode.setText(storeBarcode);
		jtf_brand.setText(storeBrand);
		jtf_unitprice.setText(String.valueOf(storeUnitprice));
		jta_detail.setText(storeDetail);
		jcb_stateModify.setSelectedItem(storeState);
		jcb_typeNameModify.setSelectedItem(storeTypeNameSelected);
		
	}
	/*用于重新加载商品类型jcb*/
	private void resetType() {
		Connection con = null;
		
		Goods_type goodsType = new Goods_type();
		try {
			con = dbUtil.getCon();
			ResultSet rs = goodsTypeDao.goodsTypeList(con, goodsType, false);
			jcb_typeName.removeAllItems();
			jcb_typeNameModify.removeAllItems();
			jcb_typeName.addItem("按类别搜索");
			jcb_typeNameModify.addItem("");
			int index = 0;
			while(rs.next()) {
				index ++;
				jcb_typeName.addItem(rs.getString("goods_type_name"));
				jcb_typeNameModify.addItem(rs.getString("goods_type_name"));
				storeTypeName[index-1] = rs.getString("goods_type_name");
				storeTypeId[index-1] = rs.getInt("goods_type_id");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "商品类型数据加载失败");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "商品类型数据加载失败");
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
	
	/*用于重置所有的JRadioButton的Selected属性为false*/
	private void resetSearch() {
		//实现了JRadioButton的单选功能
		jrb_universe.setSelected(false);
		jrb_id.setSelected(false);
		jrb_name.setSelected(false);
		jrb_detail.setSelected(false);
		resetType();
		jcb_typeName.setSelectedIndex(0);
		jrb_brand.setSelected(false);
//		jrb_unitprice.setSelected(false);
		jrb_barcode.setSelected(false);
		jcb_state.setSelectedIndex(0);
	}
	
	/*用于在选中表格的一行记录时生成下方表单的数值*/
	private void fillForm() {
		/*获取选中的行*/
		int row = jtb_table.getSelectedRow();
		jtf_id.setText((String) jtb_table.getValueAt(row, 1));
		jtf_name.setText((String) jtb_table.getValueAt(row, 2));
//		jta_detail.setText((String) jtb_table.getValueAt(row, 3));
		jcb_typeNameModify.setSelectedItem(jtb_table.getValueAt(row, 3));
		jtf_brand.setText((String) jtb_table.getValueAt(row, 4));
		jtf_unitprice.setText((String) jtb_table.getValueAt(row, 5));
		jtf_barcode.setText((String) jtb_table.getValueAt(row, 6));
		jta_detail.setText((String) jtb_table.getValueAt(row, 7));
		jcb_stateModify.setSelectedItem(jtb_table.getValueAt(row, 8));
		
		/*保存获得的信息到storeXXX，方便修改和重置;*/
		storeId = Integer.parseInt(jtf_id.getText());
		storeName = jtf_name.getText();
//		storeDetail = jta_detail.getText();
		storeTypeNameSelected = (String) jcb_typeNameModify.getSelectedItem();
		for(int i=0; i<storeTypeId.length; i++) {
			if(storeTypeNameSelected.equals(storeTypeName[i])) {
				storeTypeIdSelected = storeTypeId[i];
				break;
			}
		}
		storeBrand = jtf_brand.getText();
		storeUnitprice = Float.valueOf(jtf_unitprice.getText());
		storeBarcode = jtf_barcode.getText();
		storeDetail = jta_detail.getText();
		storeState = (String) jcb_stateModify.getSelectedItem();
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
		boolean result = true;//如果需要更新，结果返回true
		/*〇判断有无选中表格中的记录*/
		if(storeId == -1) {
			result = false;
			JOptionPane.showMessageDialog(null, "请选择任何一条记录");
			return result;
		}
		
		/*①判断表单有没有实际修改，需要使用选中表格行数时保存在变量中的数据“storeXXX”进行比较*/

		if(jtf_name.getText().equals(storeName) && jta_detail.getText().equals(storeDetail) && jcb_typeNameModify.getSelectedItem().equals(storeTypeNameSelected) && jtf_brand.getText().equals(storeBrand) && (Float.valueOf(jtf_unitprice.getText())==storeUnitprice) && jtf_barcode.getText().equals(storeBarcode) && jcb_stateModify.getSelectedItem().equals(storeState) ){
			result = false;
			JOptionPane.showMessageDialog(null, "该条记录没有任何修改，无需更新");
			return result;
		}
	
		/*②判断表单中必须要填写的值时候按正确格式填写（包括判断是否为空）*/
		if(StringUtil.isEmpty(jtf_name.getText())) {
			//此处可以逐个判断各个填写条件，使用if-else if的形式，当有错误时给出消息并返回
			result = false;
			JOptionPane.showMessageDialog(null, "请填写新的商品名称");
			return result;
		}
		if(jcb_typeNameModify.getSelectedIndex() == 0) {
			result = false;
			JOptionPane.showMessageDialog(null, "请选择商品类型");
			return result;
		}
		if(StringUtil.isEmpty(jtf_brand.getText())) {
			result = false;
			JOptionPane.showMessageDialog(null, "请填写新的商品品牌");
			return result;
		}
		if(StringUtil.isEmpty(jtf_unitprice.getText())) {
			result = false;
			JOptionPane.showMessageDialog(null, "请填写新的商品单价");
			return result;
		}
		if(StringUtil.isEmpty(jtf_barcode.getText())) {
			result = false;
			JOptionPane.showMessageDialog(null, "请填写新的商品条码");
			return result;
		}
		if(StringUtil.isEmpty(jta_detail.getText())) {
			result = false;
			JOptionPane.showMessageDialog(null, "请填写新的商品详情");
			return result;
		}
		if(jcb_stateModify.getSelectedIndex() == 0) {
			result = false;
			JOptionPane.showMessageDialog(null, "请选择商品上架状态");
			return result;
		}
		
		
		/*③用户再次确认是否要更新该条记录*/
		int choice = -1;
		choice = JOptionPane.showConfirmDialog(null, "确定要更新这条商品信息吗？\n商品编号：" + storeId + "\n商品名称：" + storeName + " ——> " + jtf_name.getText() + "\n商品类型：" + storeTypeNameSelected + " ——> " + jcb_typeNameModify.getSelectedItem() + "\n商品品牌：" + storeBrand + " ——> " + jtf_brand.getText() + "\n商品单价：" + storeUnitprice + " ——> " + jtf_unitprice.getText() + "\n商品条码：" + storeBarcode + " ——> " + jtf_barcode.getText() + "\n商品描述：" + storeDetail + " ——> " + jta_detail.getText() + "\n商品上架状态：" + storeState + " ——> " + jcb_stateModify.getSelectedItem());
		if(choice != 0) {
			result = false;
			return result;
		}
		
		/*④根据实际工程需求情况查询数据库并验证是否有重复值得问题*/
		//当查询结果显示该次填写的数据不满足要求时，给出消息并返回false;
		/*查询是否有相同条码的商品类别*/
		Goods goods = new Goods();
		goods.setGoods_barcode(jtf_barcode.getText());
		goods.setGoods_id(storeId);
		Connection con = null;
		try {
			con = dbUtil.getCon();
			boolean hasEqual = goodsDao.isEqual(con, goods);
			if(hasEqual) {
				result = false;
				JOptionPane.showMessageDialog(null, "已经存在相同条码的商品，更新失败");
				dbUtil.closeCon(con);
				return result;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "系统错误，添加失败");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "系统错误，添加失败");
			return false;
		}finally {
			try {
				dbUtil.closeCon(con);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "系统错误，添加失败");
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	/*用于确定可否删除记录*/
	private boolean allowDelete() {
		boolean result = true;
		
		/*①判断表单的当前情况是否为空，如果为空result = false、提示并return result*/
		if(storeId == -1) {
			result = false;
			JOptionPane.showMessageDialog(null, "您还没有选择任何一条记录，无法进行删除操作");
			return result;
		}
		
		/*②提示用户是否要删除该条记录，如果返回结果不为0则result = false并return result*/
		int choice = -1;
		choice = JOptionPane.showConfirmDialog(null, "是否要删除这条商品？\n商品编号：" + storeId + "\n商品名称：" + storeName + "\n商品类型：" + storeTypeNameSelected+ "\n商品品牌：" + storeBrand + "\n商品单价：" + storeUnitprice + "\n商品条码：" + storeBarcode + "\n商品描述：" + storeDetail + "\n商品上架状态：" + storeState);
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
	public MaintainGoodsInterFrm() {
		setResizable(true);
		setIconifiable(true);
		setMaximizable(true);
		setTitle("\u5546\u54C1\u4FE1\u606F\u7EF4\u62A4");
		setClosable(true);
		setBounds(100, 100, 1029, 481);
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
				/*实现全局、按商品编号、按商品名称、按商品种类名称、按商品单价、按商品品牌、按商品条码、按上架状态、按详情模糊查找*/
				String temp = jtf_searchBox.getText();//获取搜索框文本内容
				boolean isTypeId = false;//表示最终是否会以类型搜索
				boolean hasState = false;//hasState表示搜索条件中是否含有“按商品状态搜索”,如果有则值为true
				//判断搜索选项是否为空
				if(!jrb_universe.isSelected() && !jrb_id.isSelected() && !jrb_name.isSelected() && !jrb_detail.isSelected() && !jrb_barcode.isSelected() && !jrb_brand.isSelected() && (jcb_state.getSelectedIndex() == 0) && (jcb_typeName.getSelectedIndex() == 0)) {
					JOptionPane.showMessageDialog(null, "请选择任何一种搜索方式");
					return;
				}
				
				//开始解析搜索条件
				Goods goods = new Goods();
				goods.setGoods_id(-1);
				goods.setGoods_type_id(-1);
				goods.setGoods_unitprice(-1);
				
				/*获得goods_type_id的可能值（由String --> int）*/
				int[] tempTypeId = new int[100];
				Connection con = null;
				Goods_type goodsType  = new Goods_type();
				goodsType.setGoods_type_name(temp);
				ResultSet rs;
				try {
					con = dbUtil.getCon();
					rs = goodsTypeDao.goodsTypeList(con, goodsType, false);
					int index = -1;
					while(rs.next()) {
						index++;
						tempTypeId[index] = rs.getInt("goods_type_id");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (Exception e) {
					try {
						dbUtil.closeCon(con);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
				
				
				if(StringUtil.isEmpty(temp) &&  (jcb_state.getSelectedIndex()==0) && (jcb_typeName.getSelectedIndex()==0) )  {
					//当输入框内容为空时拉取所有数据
				}
				else if(jrb_universe.isSelected()) {//实现全局搜索
					if(IntegerUtil.isNumeric(temp)) {
						goods.setGoods_id(Integer.parseInt(temp));
					}
					
					goods.setGoods_barcode(temp);
					goods.setGoods_name(temp);
					goods.setGoods_detail(temp);
					goods.setGoods_brand(temp);
					if("出售中".indexOf(temp) != -1) {
						hasState = true;
						goods.setGoods_state(true);
					}
					else if("下架中".indexOf(temp) != -1) {
						hasState = true;
						goods.setGoods_state(false);
					}
					if(tempTypeId[0] != 0) {
						isTypeId = true;
					}
					/*if(IntegerUtil.isNumeric(temp.replaceFirst(".", ""))) {
						goods.setGoods_unitprice(Float.parseFloat(temp));
					}*/
					
				}
				else if(jrb_id.isSelected()) {//实现按商品编号搜索（模糊）
					if(!IntegerUtil.isNumeric(temp)) {
						JOptionPane.showMessageDialog(null, "商品编号应为纯数字，请重新输入");
						return;
					}
					goods.setGoods_id(Integer.parseInt(temp));
				}
				else if(jrb_name.isSelected()) {//实现按商品名称搜索（模糊）
					goods.setGoods_name(temp);
				}
				else if(jrb_detail.isSelected()){//实现按商品描述查询
					goods.setGoods_detail(temp);
				}
				else if(jrb_barcode.isSelected()) {
					goods.setGoods_barcode(temp);
				}
				else if(jrb_brand.isSelected()) {
					goods.setGoods_brand(temp);
				}
				/*else if(jrb_unitprice.isSelected()) {
					if(!IntegerUtil.isNumeric(temp.replaceFirst(".", "0"))) {
						System.out.print(temp);
						System.out.print("|||" + temp.replace(".", ""));
						JOptionPane.showMessageDialog(null, "价格的应该以‘__.xx’的形式输入");
						return;
					}
					goods.setGoods_unitprice(Float.valueOf(temp));
				}*/
				else if(jcb_state.getSelectedIndex() >= 1) {
					hasState = true;
					if(jcb_state.getSelectedItem().equals("出售中")) {
						goods.setGoods_state(true);
					}
					else if(jcb_state.getSelectedItem().equals("下架中")) {
						goods.setGoods_state(false);
					}
					else {
						JOptionPane.showMessageDialog(null, "未知商品状态参数");
						return;
					}
				}
				else if(jcb_typeName.getSelectedIndex() >= 1) {
					isTypeId = true;
					tempTypeId = new int[1];
					for(int i=0; i<storeTypeId.length; i++) {
						if(storeTypeName[i].equals(jcb_typeName.getSelectedItem())) {
							tempTypeId[0] = storeTypeId[i];
							break;
						}
					}
				}
				
				//执行搜索
				if(isTypeId) {
					fillTable(goods, hasState, tempTypeId);
				}
				else {
					fillTable(goods, hasState, null);
				}
				
				
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
				jtf_searchBox.setEnabled(true);
				jtf_searchBox.setEditable(true);
			}
		});
		jrb_universe.setSelected(true);
		jp_searchBarBottom.add(jrb_universe);
		
		jrb_id = new JRadioButton("\u6309\u7F16\u53F7\u641C\u7D22");
		jrb_id.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetSearch();
				jrb_id.setSelected(true);
				jtf_searchBox.setEnabled(true);
				jtf_searchBox.setEditable(true);
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
				jtf_searchBox.setEnabled(true);
				jtf_searchBox.setEditable(true);
			}
		});
		jp_searchBarBottom.add(jrb_name);
		
		jrb_detail = new JRadioButton("\u6309\u63CF\u8FF0\u641C\u7D22");
		jrb_detail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetSearch();
				jrb_detail.setSelected(true);
				jtf_searchBox.setEnabled(true);
				jtf_searchBox.setEditable(true);
			}
		});
		
		jcb_typeName = new JComboBox();
		jcb_typeName.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				jrb_universe.setSelected(false);
				jrb_id.setSelected(false);
				jrb_name.setSelected(false);
				jrb_detail.setSelected(false);
				jrb_brand.setSelected(false);
//				jrb_unitprice.setSelected(false);
				jrb_barcode.setSelected(false);
				jcb_state.setSelectedIndex(0);
				jtf_searchBox.setText("");
				jtf_searchBox.setEnabled(false);
				jtf_searchBox.setEditable(false);
			}
		});
		jcb_typeName.setModel(new DefaultComboBoxModel(new String[] {"\u6309\u7C7B\u522B\u641C\u7D22"}));
		jp_searchBarBottom.add(jcb_typeName);
		
		jrb_brand = new JRadioButton("\u6309\u54C1\u724C\u641C\u7D22");
		jrb_brand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetSearch();
				jrb_brand.setSelected(true);
				jtf_searchBox.setEnabled(true);
				jtf_searchBox.setEditable(true);
			}
		});
		jp_searchBarBottom.add(jrb_brand);
		
		jrb_barcode = new JRadioButton("\u6309\u6761\u7801\u53F7\u641C\u7D22");
		jrb_barcode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetSearch();
				jrb_barcode.setSelected(true);
				jtf_searchBox.setEnabled(true);
				jtf_searchBox.setEditable(true);
			}
		});
		jp_searchBarBottom.add(jrb_barcode);
		jrb_detail.setEnabled(true);
		jrb_detail.setSelected(false);
		jp_searchBarBottom.add(jrb_detail);
		
		jcb_state = new JComboBox();
		jcb_state.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				jrb_universe.setSelected(false);
				jrb_id.setSelected(false);
				jrb_name.setSelected(false);
				jrb_detail.setSelected(false);
				jrb_brand.setSelected(false);
//				jrb_unitprice.setSelected(false);
				jrb_barcode.setSelected(false);
				jcb_typeName.setSelectedIndex(0);
				jtf_searchBox.setText("");
				jtf_searchBox.setEnabled(false);
				jtf_searchBox.setEditable(false);
			}
		});
		
		jcb_state.setModel(new DefaultComboBoxModel(new String[] {"\u6309\u4E0A\u67B6\u72B6\u6001\u641C\u7D22", "\u51FA\u552E\u4E2D", "\u4E0B\u67B6\u4E2D"}));
		jp_searchBarBottom.add(jcb_state);
		
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
				{"1", null, null, null, null, null, null, null, null, null, null},
				{"", null, "", null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, ""},
				{null, null, null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null, null, null},
			},
			new String[] {
				"\u5E8F\u53F7", "\u7F16\u53F7", "\u540D\u79F0", "\u7C7B\u578B", "\u54C1\u724C", "\u5355\u4EF7", "\u6761\u7801\u53F7", "\u8BE6\u60C5\u63CF\u8FF0", "\u4E0A\u67B6\u72B6\u6001", "\u521B\u5EFA\u65E5\u671F", "\u4FEE\u6539\u65E5\u671F"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		jtb_table.getColumnModel().getColumn(0).setPreferredWidth(37);
		jtb_table.getColumnModel().getColumn(1).setPreferredWidth(48);
		jtb_table.getColumnModel().getColumn(2).setPreferredWidth(47);
		jtb_table.getColumnModel().getColumn(3).setPreferredWidth(47);
		jtb_table.getColumnModel().getColumn(4).setPreferredWidth(49);
		jtb_table.getColumnModel().getColumn(5).setPreferredWidth(62);
		jtb_table.getColumnModel().getColumn(6).setPreferredWidth(74);
		jtb_table.getColumnModel().getColumn(7).setPreferredWidth(82);
		jtb_table.getColumnModel().getColumn(9).setPreferredWidth(109);
		jtb_table.getColumnModel().getColumn(10).setPreferredWidth(100);
		jsp_table.setViewportView(jtb_table);//这条语句使表头可见
		
		JPanel jp_form = new JPanel();
		getContentPane().add(jp_form, BorderLayout.SOUTH);
		
		JPanel jp_id = new JPanel();
		jp_form.add(jp_id);
		
		JLabel label = new JLabel("\u5546\u54C1\u7F16\u53F7\uFF1A");
		label.setFont(new Font("黑体", Font.PLAIN, 15));
		jp_id.add(label);
		
		jtf_id = new JTextField();
		jtf_id.setEditable(false);
		jp_id.add(jtf_id);
		jtf_id.setColumns(10);
		
		JPanel jp_name = new JPanel();
		jp_form.add(jp_name);
		
		JLabel lblNewLabel_1 = new JLabel("\u5546\u54C1\u540D\u79F0\uFF1A");
		lblNewLabel_1.setFont(new Font("黑体", Font.PLAIN, 15));
		jp_name.add(lblNewLabel_1);
		
		jtf_name = new JTextField();
		jtf_name.setText("");
		jp_name.add(jtf_name);
		jtf_name.setColumns(10);
		
		JPanel jp_typeNameModify = new JPanel();
		jp_form.add(jp_typeNameModify);
		
		JLabel lblNewLabel_6 = new JLabel("\u5546\u54C1\u7C7B\u522B\uFF1A");
		lblNewLabel_6.setFont(new Font("黑体", Font.PLAIN, 15));
		jp_typeNameModify.add(lblNewLabel_6);
		
		jcb_typeNameModify = new JComboBox();
		jcb_typeNameModify.setModel(new DefaultComboBoxModel(new String[] {""}));
		jp_typeNameModify.add(jcb_typeNameModify);
		
		JPanel jp_brand = new JPanel();
		jp_form.add(jp_brand);
		
		JLabel lblNewLabel_2 = new JLabel("\u5546\u54C1\u54C1\u724C\uFF1A");
		lblNewLabel_2.setFont(new Font("黑体", Font.PLAIN, 15));
		jp_brand.add(lblNewLabel_2);
		
		jtf_brand = new JTextField();
		jtf_brand.setEnabled(true);
		jtf_brand.setEditable(true);
		jtf_brand.setText("");
		jp_brand.add(jtf_brand);
		jtf_brand.setColumns(10);
		
		JPanel jp_unitprice = new JPanel();
		jp_form.add(jp_unitprice);
		
		JLabel lblNewLabel_3 = new JLabel("\u5546\u54C1\u5355\u4EF7\uFF1A");
		lblNewLabel_3.setFont(new Font("黑体", Font.PLAIN, 15));
		jp_unitprice.add(lblNewLabel_3);
		
		jtf_unitprice = new JTextField();
		jp_unitprice.add(jtf_unitprice);
		jtf_unitprice.setColumns(10);
		
		JPanel jp_barcode = new JPanel();
		jp_form.add(jp_barcode);
		
		JLabel lblNewLabel_4 = new JLabel("\u6761\u7801\u53F7\uFF1A");
		lblNewLabel_4.setFont(new Font("黑体", Font.PLAIN, 15));
		jp_barcode.add(lblNewLabel_4);
		
		jtf_barcode = new JTextField();
		jp_barcode.add(jtf_barcode);
		jtf_barcode.setColumns(10);
		
		JPanel jp_detail = new JPanel();
		jp_form.add(jp_detail);
		jp_detail.setLayout(new BorderLayout(0, 0));
		
		JLabel lblS = new JLabel("\u5546\u54C1\u63CF\u8FF0\uFF1A");
		lblS.setFont(new Font("黑体", Font.PLAIN, 15));
		jp_detail.add(lblS, BorderLayout.WEST);
		
		jta_detail = new JTextArea();
		jta_detail.setRows(2);
		jta_detail.setColumns(20);
		jp_detail.add(jta_detail, BorderLayout.CENTER);
		
		JPanel jp_state = new JPanel();
		jp_form.add(jp_state);
		
		JLabel lblNewLabel_5 = new JLabel("\u4E0A\u67B6\u72B6\u6001\uFF1A");
		lblNewLabel_5.setFont(new Font("黑体", Font.PLAIN, 15));
		jp_state.add(lblNewLabel_5);
		
		jcb_stateModify = new JComboBox();
		jcb_stateModify.setModel(new DefaultComboBoxModel(new String[] {"", "\u51FA\u552E\u4E2D", "\u4E0B\u67B6\u4E2D"}));
		jp_state.add(jcb_stateModify);
		
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
				Goods goods = new Goods();//实体model类
				goods.setGoods_id(storeId);//set所有有变动的数据
				goods.setGoods_name(jtf_name.getText());
				for(int i=0; i<storeTypeId.length; i++) {
					if(storeTypeName[i].equals(jcb_typeNameModify.getSelectedItem())) {
						goods.setGoods_type_id(storeTypeId[i]);
						break;
					}
				}
				goods.setGoods_brand(jtf_brand.getText());
				goods.setGoods_unitprice(Float.valueOf(jtf_unitprice.getText()));
				goods.setGoods_barcode(jtf_barcode.getText());
				goods.setGoods_detail(jta_detail.getText());
				if(jcb_stateModify.getSelectedItem().equals("出售中")) {
					goods.setGoods_state(true);
				}
				else if(jcb_stateModify.getSelectedItem().equals("下架中")) {
					goods.setGoods_state(false);
				}
				
				try {
					con = dbUtil.getCon();
					int temp = goodsDao.updateGoods(con, goods);
					if(temp == 1) {
						JOptionPane.showMessageDialog(null, "该商品类别信息更新成功");
						//刷新表格（完整列出，不能恢复之前的搜索结果）
						Goods tempFill = new Goods();
						tempFill.setGoods_id(-1);
						tempFill.setGoods_type_id(-1);
						tempFill.setGoods_unitprice(-1);
						//fillTable中有重置storeXXX以及清空表单的操作
						fillTable(tempFill, false, null);
					}
					else {
						JOptionPane.showMessageDialog(null, "数据库更新数据时出现问题，更新失败");
						//将表单重置
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
				Goods goods = new Goods();//new一个实体类
				goods.setGoods_id(storeId);//设置实体类的查询条件
				try {
					con = dbUtil.getCon();
					int temp = goodsDao.deleteGoods(con, goods);//变量temp用于接受是否删除成功的信息，如果返回1则删除成功
					if(temp == 1) {
						JOptionPane.showMessageDialog(null, "该条记录已被成功删除");
						//刷新表格并清空表单,并重置storeXXX
						Goods tempFill = new Goods();
						tempFill.setGoods_id(-1);
						tempFill.setGoods_type_id(-1);
						tempFill.setGoods_unitprice(-1);
						fillTable(tempFill, false, null);
						/*storeId = 0;
						storeName = null;
						storeDetail = null;
						jtf_id.setText("");
						jtf_name.setText("");
						jta_detail.setText("");*/
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
		
		
		/*初始化jcb_typeName以及jcb_typeNameModify*/
		resetType();
		
		/*使用方法“fillTable”完成数据填充*/
		Goods tempFill = new Goods();
		tempFill.setGoods_id(-1);
		tempFill.setGoods_type_id(-1);
		tempFill.setGoods_unitprice(-1);
		fillTable(tempFill, false, null);
		
		
	}

	

}
