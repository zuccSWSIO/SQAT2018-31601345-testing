package com.zucc.chenfan.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.zucc.chenfan.dao.GoodsDao;
import com.zucc.chenfan.model.Goods;
import com.zucc.chenfan.model.Goods_purchase;
import com.zucc.chenfan.util.DbUtil;
import com.zucc.chenfan.util.IntegerUtil;
import com.zucc.chenfan.util.StringUtil;

import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class AddPurchaseItemDialog extends JDialog {
	DbUtil dbUtil = new DbUtil();
	GoodsDao goodsDao = new GoodsDao();
	
	
	private JTextField jtf_searchBox;
	private JTable jtb_goods;
	private JTextField jtf_amount;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		try {
			AddPurchaseItemDialog dialog = new AddPurchaseItemDialog();
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
		
		//点击某张表的某一行可以得到该行对应记录的编号
		private int getIdInTable(JTable jtable) {
			int result = -1;
			
			int row = jtable.getSelectedRow();
			if(row < 0) {
				JOptionPane.showMessageDialog(null, "请选择任何一条信息");
				return -1;
			}
			String temp = (String) jtable.getValueAt(row, 1);
			result = Integer.parseInt(temp);
			
			return result;
		}
	

	/**
	 * Create the dialog.
	 */
	public AddPurchaseItemDialog(AddPurchaseDialog addPurchaseDialog) {
		setModal(true);
		setResizable(false);
		setTitle("\u6DFB\u52A0\u5546\u54C1\u9879");
		setBounds(100, 100, 316, 281);
		getContentPane().setLayout(null);
		{
			JPanel panel = new JPanel();
			panel.setBounds(0, 23, 307, 128);
			getContentPane().add(panel);
			panel.setLayout(null);
			{
				{
					JScrollPane scrollPane = new JScrollPane();
					scrollPane.setBounds(0, 0, 307, 147);
					panel.add(scrollPane);
					jtb_goods = new JTable();
					scrollPane.setViewportView(jtb_goods);
					jtb_goods.setModel(new DefaultTableModel(
						new Object[][] {
						},
						new String[] {
							"\u5E8F\u53F7", "\u7F16\u53F7", "\u5546\u54C1\u540D", "\u5546\u54C1\u5355\u4EF7"
						}
					) {
						boolean[] columnEditables = new boolean[] {
							false, false, false, false
						};
						public boolean isCellEditable(int row, int column) {
							return columnEditables[column];
						}
					});
					jtb_goods.getColumnModel().getColumn(0).setPreferredWidth(42);
					jtb_goods.getColumnModel().getColumn(1).setPreferredWidth(43);
				}
			}
		}
		{
			JPanel jp_searchBar = new JPanel();
			jp_searchBar.setBorder(new LineBorder(new Color(0, 0, 0)));
			jp_searchBar.setBounds(0, 0, 307, 24);
			getContentPane().add(jp_searchBar);
			jp_searchBar.setLayout(null);
			{
				JButton jb_search = new JButton("\u641C\u7D22");
				jb_search.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						DefaultTableModel dtm = (DefaultTableModel) jtb_goods.getModel();
						dtm.setRowCount(0);//设置表格行数为零，即在下次查询前清空表格。
						
						String temp = jtf_searchBox.getText();
						Connection con = null;
						ResultSet rs = null;
						Goods goods = new Goods();
						goods.setGoods_name(temp);
						goods.setGoods_state(true);//搜索所有上架的商品
						if(StringUtil.isEmpty(temp)) {
							try {
								con = dbUtil.getCon();
								rs = goodsDao.findAllGoods(con, goods);
								
								int i = 0;//用于计算每一项的序号
								while(rs.next()) {
									i++;
									/*Vector和ArrayList有区别，Vector是线程安排的*/
									Vector v = new Vector();
									v.add(i);
									/*导入Vector的数据类型统一转换为String,方便在表格中点击读取数值而不会报错*/
									v.add(rs.getString("goods_id"));
									v.add(rs.getString("goods_name"));
									v.add(rs.getString("goods_unitprice"));
									
									dtm.addRow(v);
								}
								
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
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
						else {
							try {
								con = dbUtil.getCon();
								rs = goodsDao.findGoods(con, goods);
								
								int i = 0;//用于计算每一项的序号
								while(rs.next()) {
									i++;
									/*Vector和ArrayList有区别，Vector是线程安排的*/
									Vector v = new Vector();
									v.add(i);
									/*导入Vector的数据类型统一转换为String,方便在表格中点击读取数值而不会报错*/
									v.add(rs.getString("goods_id"));
									v.add(rs.getString("goods_name"));
									v.add(rs.getString("goods_unitprice"));
									
									dtm.addRow(v);
								}
								
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
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
						
						
					}
				});
				jb_search.setBounds(231, 0, 66, 24);
				jp_searchBar.add(jb_search);
			}
			{
				jtf_searchBox = new JTextField();
				jtf_searchBox.setBounds(0, 1, 234, 23);
				jp_searchBar.add(jtf_searchBox);
				jtf_searchBox.setColumns(10);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 209, 290, 33);
			getContentPane().add(buttonPane);
			buttonPane.setLayout(null);
			{
				JButton okButton = new JButton("\u786E\u8BA4");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						
						int goods_id = getIdInTable(jtb_goods);
						int amount = 0;
						if(StringUtil.isEmpty(jtf_amount.getText())) {
							JOptionPane.showMessageDialog(null, "请输入购买的数量");
							return;
						}
						if(!IntegerUtil.isNumeric(jtf_amount.getText())) {
							JOptionPane.showMessageDialog(null, "请输入正确格式的数量");
							return;
						}
						if(Integer.parseInt(jtf_amount.getText()) <= 0 ) {
							JOptionPane.showMessageDialog(null, "请输入正确的数量，应>=1");
							return;
						}
						amount = Integer.parseInt(jtf_amount.getText());
						if( goods_id != -1) {
							Goods_purchase temp = new Goods_purchase();
							temp.setGoods_id(goods_id);
							temp.setQuantity(amount);
							
							Iterator<Goods_purchase> li = addPurchaseDialog.storeGoodsItem.iterator();
							while(li.hasNext()) {
								Goods_purchase tempGoodsPurchase = li.next();
								if(tempGoodsPurchase.getGoods_id() == temp.getGoods_id()) {
									JOptionPane.showMessageDialog(null, "该订单已经存在相同商品，请返回修改数量");
									closeDialog();
									return;
								}
							}
							
							addPurchaseDialog.storeGoodsItem.add(temp);
							addPurchaseDialog.fillTable();
							closeDialog();
						}
						
					}
				});
				okButton.setBounds(42, 5, 74, 23);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("\u53D6\u6D88");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						closeDialog();
					}
				});
				cancelButton.setBounds(179, 5, 74, 23);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		{
			JLabel label = new JLabel("\u6570\u91CF\uFF1A");
			label.setBounds(22, 182, 66, 15);
			getContentPane().add(label);
		}
		{
			jtf_amount = new JTextField();
			jtf_amount.setBounds(88, 179, 86, 21);
			getContentPane().add(jtf_amount);
			jtf_amount.setColumns(10);
		}
	}

}
