package com.zucc.chenfan.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.zucc.chenfan.dao.CustomerDao;
import com.zucc.chenfan.dao.GoodsDao;
import com.zucc.chenfan.dao.Goods_purchaseDao;
import com.zucc.chenfan.model.Customer;
import com.zucc.chenfan.model.Goods;
import com.zucc.chenfan.model.Goods_purchase;
import com.zucc.chenfan.util.DbUtil;
import com.zucc.chenfan.util.IntegerUtil;
import com.zucc.chenfan.util.StringUtil;

import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class AddPurchaseDialog extends JDialog {
	DbUtil dbUtil = new DbUtil();
	CustomerDao customerDao = new CustomerDao();
	GoodsDao goodsDao = new GoodsDao();
	Goods_purchaseDao goodsPurchaseDao = new Goods_purchaseDao();
//	Goods_purchase temp = new Goods_purchase();

	private final JPanel contentPanel = new JPanel();
	private JTextField jtf_customer;
	private JTextField jtf_total;
	private JTextField jtf_address;
	private JTable jtb_table;
	private JTextField jtf_amount;

	
	//storeXXX
	int storeCustomerId;
	float storeTotal;
	List<Goods_purchase> storeGoodsItem = new ArrayList<Goods_purchase>();
	AddPurchaseDialog itself = null;
	
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		try {
			AddPurchaseDialog dialog = new AddPurchaseDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	//关闭窗口
		void closeDialog() {
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
	
	//加载顾客姓名
		private void fillName(int customer_id) {
			Connection con = null;
			
			Customer customer = new Customer();
			customer.setCustomer_id(customer_id);
			customer.setCustomer_phone(-1);
			
			try {
				con = dbUtil.getCon();
				ResultSet rs = customerDao.customerList(con, customer);
				if(rs.next()) {
					jtf_customer.setText(rs.getString("customer_id") + "||" + rs.getString("customer_name"));
				}
				else {
					JOptionPane.showMessageDialog(null, "系统错误");
					closeDialog();
				}
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "系统错误");
				e.printStackTrace();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "系统错误");
				e.printStackTrace();
			}finally {
				try {
					dbUtil.closeCon(con);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	// 得到商品名
	private Goods getGoodsFromId(int goods_id) {
		Goods result = null; 
		
		Connection con = null;
		Goods goods = new Goods();
		goods.setGoods_type_id(-1);
		goods.setGoods_id(goods_id);

		try {
			con = dbUtil.getCon();
			ResultSet rs = goodsDao.goodsList(con, goods, false, null);
			if (rs.next()) {
				result = new Goods();
				result.setGoods_name(rs.getString("goods_name"));
				result.setGoods_id(goods_id);
				result.setGoods_barcode(rs.getString("goods_barcode"));
				result.setGoods_brand(rs.getString("goods_brand"));
				result.setGoods_detail(rs.getString("goods_detail"));
				result.setGoods_state(rs.getBoolean("goods_state"));
				result.setGoods_unitprice(rs.getFloat("goods_unitprice"));
			} else {
				JOptionPane.showMessageDialog(null, "系统错误");
				closeDialog();
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "系统错误");
			e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "系统错误");
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;

	}
		
		
		
	//填充商品项表格
		void fillTable() {
			DefaultTableModel dtm = (DefaultTableModel) jtb_table.getModel();
			dtm.setRowCount(0);//设置表格行数为零，即在下次查询前清空表格。
			
			float total = 0;
			Iterator<Goods_purchase> li = storeGoodsItem.iterator();
			int index = 0;
			while(li.hasNext()) {
				Goods_purchase temp = li.next();
				index++;
				/*Vector和ArrayList有区别，Vector是线程安排的*/
				Vector v = new Vector();
				v.add(index);//表示序号
				/*导入Vector的数据类型统一转换为String,方便在表格中点击读取数值而不会报错*/
				v.add(String.valueOf(temp.getGoods_id()));
				v.add(getGoodsFromId(temp.getGoods_id()).getGoods_name());
				//商品单价
				float unitprice = getGoodsFromId(temp.getGoods_id()).getGoods_unitprice(); 
				v.add(String.valueOf(unitprice));
				v.add(String.valueOf(temp.getQuantity()));
				dtm.addRow(v);
				total = total + (unitprice * temp.getQuantity());
				
			}
			
			//显示总价格
			jtf_total.setText(String.valueOf(total));
		}
		
	
	/**
	 * Create the dialog.
	 */
	public AddPurchaseDialog(int customerId) {
		storeCustomerId = customerId;
		storeTotal = 0;
		itself = this;
		
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setTitle("\u65B0\u589E\u5546\u54C1\u8D2D\u4E70\u8BA2\u5355");
		setBounds(100, 100, 464, 314);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 567, 80);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		{
			JPanel panel = new JPanel();
			panel.setBorder(new LineBorder(Color.BLUE));
			panel.setBounds(0, 0, 447, 70);
			contentPanel.add(panel);
			panel.setLayout(null);
			{
				JLabel lblNewLabel = new JLabel("\u987E\u5BA2\uFF1A");
				lblNewLabel.setBounds(10, 10, 48, 28);
				panel.add(lblNewLabel);
			}
			
			jtf_customer = new JTextField();
			jtf_customer.setEditable(false);
			jtf_customer.setBounds(68, 14, 107, 21);
			panel.add(jtf_customer);
			jtf_customer.setColumns(10);
			
			JLabel label = new JLabel("\u603B\u91D1\u989D\uFF1A");
			label.setBounds(215, 17, 54, 15);
			panel.add(label);
			
			jtf_total = new JTextField();
			jtf_total.setEditable(false);
			jtf_total.setBounds(279, 14, 84, 21);
			panel.add(jtf_total);
			jtf_total.setColumns(10);
			
			JLabel label_1 = new JLabel("\u6536\u8D27\u5730\u5740\uFF1A");
			label_1.setBounds(10, 48, 84, 15);
			panel.add(label_1);
			
			jtf_address = new JTextField();
			jtf_address.setBounds(78, 45, 352, 21);
			panel.add(jtf_address);
			jtf_address.setColumns(10);
		}
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(285, 73, 163, 167);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JButton btnNewButton = new JButton("\u65B0\u589E\u5546\u54C1\u9879");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AddPurchaseItemDialog addPurchaseItemDialog = new AddPurchaseItemDialog(itself);
				addPurchaseItemDialog.setVisible(true);
				//如果放弃该项，则在关闭addPurchaseItemDialog前将tempItem赋值为null
				/*if(tempItem != null) {
					storeGoodsItem.add(tempItem);
					fillTable();
				}*/
				
			}
		});
		btnNewButton.setBounds(49, 10, 108, 23);
		panel_1.add(btnNewButton);
		
		jtf_amount = new JTextField();
		jtf_amount.setBounds(10, 55, 41, 21);
		panel_1.add(jtf_amount);
		jtf_amount.setColumns(10);
		
		JButton button = new JButton("\u4FEE\u6539\u6570\u91CF");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int goods_id = getIdInTable(jtb_table);
				
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
				Iterator<Goods_purchase> li = storeGoodsItem.iterator();
				while(li.hasNext()) {
					Goods_purchase temp = li.next();
					if(temp.getGoods_id() == goods_id ) {
						temp.setQuantity(Integer.parseInt(jtf_amount.getText()));
						fillTable();
						JOptionPane.showMessageDialog(null, "数量修改成功");
						break;
					}
					
				}
			}
		});
		button.setBounds(64, 54, 93, 23);
		panel_1.add(button);
		
		JButton button_1 = new JButton("\u5220\u9664");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int goods_id = getIdInTable(jtb_table);
				
				if(goods_id != -1) {
					Iterator<Goods_purchase> li = storeGoodsItem.iterator();
					while(li.hasNext()) {
						Goods_purchase temp = li.next();
						if(temp.getGoods_id() == goods_id ) {
							storeGoodsItem.remove(temp);

							fillTable();
							JOptionPane.showMessageDialog(null, "删除成功");
							break;
						}
						
					}
					
				}
				
				
			}
		});
		button_1.setBounds(64, 100, 93, 23);
		panel_1.add(button_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new LineBorder(Color.RED, 2));
		scrollPane.setBounds(0, 73, 286, 167);
		getContentPane().add(scrollPane);
		
		jtb_table = new JTable();
		jtb_table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				int goods_id = getIdInTable(jtb_table);
				if(goods_id != -1) {
					Iterator<Goods_purchase> li = storeGoodsItem.iterator();
					while(li.hasNext()) {
						Goods_purchase temp = li.next();
						if(temp.getGoods_id() == goods_id) {
							jtf_amount.setText(String.valueOf(temp.getQuantity()));
						}
					
						
					}
					
					
				}
				
			}
		});
		scrollPane.setViewportView(jtb_table);
		jtb_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jtb_table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"\u5E8F\u53F7", "\u5546\u54C1\u7F16\u53F7", "\u540D\u79F0", "\u5355\u4EF7", "\u6570\u91CF"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, true, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		jtb_table.getColumnModel().getColumn(0).setPreferredWidth(38);
		jtb_table.getColumnModel().getColumn(1).setPreferredWidth(60);
		jtb_table.getColumnModel().getColumn(2).setPreferredWidth(60);
		jtb_table.getColumnModel().getColumn(3).setPreferredWidth(50);
		jtb_table.getColumnModel().getColumn(4).setPreferredWidth(52);
		{
			JPanel panel = new JPanel();
			panel.setBounds(218, 239, 230, 33);
			getContentPane().add(panel);
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
		{
			JPanel panel = new JPanel();
			panel.setBounds(0, 239, 218, 33);
			getContentPane().add(panel);
			{
				JButton okButton = new JButton("\u786E\u8BA4");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						//判空
						if(StringUtil.isEmpty(jtf_address.getText())) {
							JOptionPane.showMessageDialog(null, "请输入地址");
							return;
						}
						if(Float.valueOf(jtf_total.getText()) <= 0) {
							JOptionPane.showMessageDialog(null, "请选择商品");
							return;
						}
						
						//录入信息到数据库
						int purchaseId = 1;
						Connection con = null;
						try {
							con = dbUtil.getCon();
							purchaseId = goodsPurchaseDao.getPurchaseId(con);
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
						Iterator<Goods_purchase> li = storeGoodsItem.iterator();
						while(li.hasNext()) {
							Goods_purchase temp = li.next();
							temp.setPurchase_id(purchaseId);
							temp.setCustomer_id(customerId);
							//算总价
							con = null;
							float unitprice = 0;
							Goods goods = new Goods();
							ResultSet rs;
							try {
								con = dbUtil.getCon();
								rs = goodsDao.goodsList(con, goods, false, null);
								if(rs.next()) {
									unitprice = rs.getFloat("goods_unitprice");
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
							temp.setTotalprice(unitprice * temp.getQuantity());
							//地址
							temp.setAddress(jtf_address.getText());
							//状态
							temp.setState("已下单");
							
							con = null;
							try {
								con = dbUtil.getCon();
								int result = goodsPurchaseDao.addPurchase(con, temp);
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
						
						JOptionPane.showMessageDialog(null, "录入结束");
						closeDialog();
						
					}
				});
				panel.add(okButton);
				okButton.setActionCommand("OK");
				getRootPane().setDefaultButton(okButton);
			}
		}
	
		//加载顾客
		fillName(storeCustomerId);
		
		//加载总价钱
		jtf_total.setText(String.valueOf(storeTotal));
	
	
	}
}
