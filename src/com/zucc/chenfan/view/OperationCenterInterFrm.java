package com.zucc.chenfan.view;

import java.awt.EventQueue;
import java.beans.PropertyVetoException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JInternalFrame;
import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

import java.awt.Font;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.zucc.chenfan.dao.CustomerDao;
import com.zucc.chenfan.dao.Goods_purchaseDao;
import com.zucc.chenfan.dao.PetDao;
import com.zucc.chenfan.model.Customer;
import com.zucc.chenfan.model.Goods;
import com.zucc.chenfan.model.Goods_purchase;
import com.zucc.chenfan.model.Goods_type;
import com.zucc.chenfan.model.Pet;
import com.zucc.chenfan.model.Service_type;
import com.zucc.chenfan.util.DbUtil;
import com.zucc.chenfan.util.FileUtil;
import com.zucc.chenfan.util.IntegerUtil;
import com.zucc.chenfan.util.MyImageUtil;
import com.zucc.chenfan.util.StringUtil;

import java.awt.FlowLayout;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ListSelectionModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OperationCenterInterFrm extends JInternalFrame {
	DbUtil dbUtil = new DbUtil();
	CustomerDao customerDao = new CustomerDao();
	PetDao petDao = new PetDao();
	FileUtil fileUtil = new FileUtil();
	Goods_purchaseDao goodsPurchaseDao = new Goods_purchaseDao();
	
	private JTextField jtf_searchBox;
	private JTable jtb_tableCustomer;
	private JTable jtb_tablePurchase;
	private JTable jtb_tablePet;
	private JTable jtb_tableOrder;
	private JLabel jlb_petImage;
	
	//storeXXX
	//��store for pet'table
	private int[] storePetSpeciesId = new int[20];
	private String[] storePetSpeciesName = new String[20];
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OperationCenterInterFrm frame = new OperationCenterInterFrm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	
	//���˿���Ϣ��
	private void fillTableCustomer(Customer customer) {
		DefaultTableModel dtm = (DefaultTableModel) jtb_tableCustomer.getModel();
		dtm.setRowCount(0);//���ñ������Ϊ�㣬�����´β�ѯǰ��ձ��
		
		Connection con = null;
		try {
			con = dbUtil.getCon();//�õ����ݿ�����
			ResultSet rs = customerDao.customerList(con, customer);//��ѯ���ݿⲢ�õ��������ResultSet
			
			int i = 0;//���ڼ���ÿһ������
			while(rs.next()) {
				i++;
				/*Vector��ArrayList������Vector���̰߳��ŵ�*/
				Vector v = new Vector();
				v.add(i);//��ʾ���
				/*����Vector����������ͳһת��ΪString,�����ڱ���е����ȡ��ֵ�����ᱨ��*/
				v.add(rs.getString("customer_id"));
				v.add(rs.getString("customer_name"));
				v.add(rs.getString("customer_phone"));
				v.add(rs.getString("customer_email"));
				v.add(rs.getString("customer_other_contact"));
				v.add(rs.getString("customer_createdate"));
				v.add(rs.getString("customer_modifydate"));
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
	
	// ��������Ϣ��
	private void fillTablePet(Pet pet, int[] customerId, int[] petSpeciesId) {// boolean
		DefaultTableModel dtm = (DefaultTableModel) jtb_tablePet.getModel();
		dtm.setRowCount(0);// ���ñ������Ϊ�㣬�����´β�ѯǰ��ձ��

		Connection con = null;
		try {
			con = dbUtil.getCon();// �õ����ݿ�����
			ResultSet rs = petDao.petList(con, pet, customerId, petSpeciesId);// ��ѯ���ݿⲢ�õ��������ResultSet

			int i = 0;// ���ڼ���ÿһ������
			while (rs.next()) {
				i++;
				/* Vector��ArrayList������Vector���̰߳��ŵ� */
				Vector v = new Vector();
				v.add(i);// ��ʾ���
				/* ����Vector����������ͳһת��ΪString,�����ڱ���е����ȡ��ֵ�����ᱨ�� */
				v.add(rs.getString("pet_id"));
				v.add(rs.getString("pet_name"));
				//��������������
				String petSpeciesName = petSpeciesId2Name(rs.getInt("pet_species_id"));
				v.add(petSpeciesName);
				v.add(rs.getString("pet_createdate"));
				v.add(rs.getString("pet_modifydate"));
				dtm.addRow(v);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//�����Ʒ������Ϣ��
		private void fillTablePurchase(Goods_purchase goods_purchase) {
			DefaultTableModel dtm = (DefaultTableModel) jtb_tablePurchase.getModel();
			dtm.setRowCount(0);//���ñ������Ϊ�㣬�����´β�ѯǰ��ձ��
			
			Connection con = null;
			try {
				con = dbUtil.getCon();//�õ����ݿ�����
				ResultSet rs = goodsPurchaseDao.goods_purchaseList(con, goods_purchase);//��ѯ���ݿⲢ�õ��������ResultSet
				
				int i = -1;//���ڼ���ÿһ������
				int changedPurchaseId = -1;
				float changedtotalprice = 0;
				while(rs.next()) {
					if(rs.getInt("purchase_id") == changedPurchaseId) {
						changedtotalprice = changedtotalprice + rs.getFloat("totalprice");
					}
					else {
						i++;
						if(i > 0) {
							Vector v = new Vector();
							v.add(i);
							v.add(rs.getInt("purchase_id"));
							v.add(changedtotalprice);
							v.add(rs.getString("address"));
							v.add(rs.getString("state"));
							v.add(rs.getString("purchase_createdate"));
							v.add(rs.getString("purchase_updatedate"));
							dtm.addRow(v);
						}
						
						
						changedtotalprice = rs.getFloat("totalprice");
					}
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
	
	
	
	//���ĳ�ű��ĳһ�п��Եõ����ж�Ӧ��¼�ı��
	private int getIdInTable(JTable jtable) {
		int result = -1;
		
		int row = jtable.getSelectedRow();
		if(row < 0) {
			JOptionPane.showMessageDialog(null, "��ѡ���κ�һ����Ϣ");
			return -1;
		}
		String temp = (String) jtable.getValueAt(row, 1);
		result = Integer.parseInt(temp);
		
		return result;
	}
	
	
	//���pet�йص�storeXXX
	private void getStorePetSpecies() throws Exception {
		Connection con = null;
		con = dbUtil.getCon();
		String sql = "select * from pet_species";
		PreparedStatement pstmt = con.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		int index = -1;
		while(rs.next()) {
			index++;
			storePetSpeciesId[index] = rs.getInt("pet_species_id");
			storePetSpeciesName[index] = rs.getString("pet_species_name");
		}
		dbUtil.closeCon(con);
	}
	
	//ͨ������customer_name���customer_id�Ŀ���ֵ
	private int[] customerName2Id(String customerName) {
		int[] result= new int[1000];
		Connection con = null;
		Customer customer = new Customer();
		customer.setCustomer_name(customerName);
		customer.setCustomer_phone(-1);
		customer.setCustomer_id(-1);
		ResultSet rs;
		try {
			con = dbUtil.getCon();
			rs = customerDao.customerList(con, customer);
			int index = -1;
			while(rs.next()) {
				index++;
				result[index] = rs.getInt("customer_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			try {
				dbUtil.closeCon(con);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return result;
	}

	// ͨ������pet_species_name���pet_species_id�Ŀ���ֵ
	private int[] petSpeciesName2Id(String petSpeciesName) throws Exception {
		int[] result = new int[20];
		Connection con = null;
		con = dbUtil.getCon();
		String sql = "select pet_species_id from pet_species where pet_species_name = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, petSpeciesName);
		ResultSet rs = pstmt.executeQuery();
		int index = -1;
		while (rs.next()) {
			index++;
			result[index] = rs.getInt("pet_species_id");
		}
		dbUtil.closeCon(con);
		return result;
	}

	// ͨ������pet_species_id���pet_species_Name�Ŀ���ֵ
	private String petSpeciesId2Name(int petSpeciesId) throws Exception {
		String result = null;
		Connection con = null;
		con = dbUtil.getCon();
		String sql = "select pet_species_name from pet_species where pet_species_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, petSpeciesId);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			result = rs.getString("pet_species_name");
		}else {
			JOptionPane.showMessageDialog(null, "����IDת��������ʧ��");
		}
		dbUtil.closeCon(con);
		return result;
	}
	
	/**
	 * Create the frame.
	 * @throws PropertyVetoException 
	 */
	public OperationCenterInterFrm() throws PropertyVetoException {
		setClosable(true);
		setTitle("\u8FD0\u8425\u4E2D\u5FC3");
		setResizable(true);
		setMaximizable(true);
		setIconifiable(true);
		setBounds(100, 100, 1371, 797);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JComboBox jcb_universe = new JComboBox();
		jcb_universe.setFont(new Font("����", Font.PLAIN, 15));
		jcb_universe.setModel(new DefaultComboBoxModel(new String[] {"\u8BF7\u9009\u62E9\u641C\u7D22\u7684\u8868", "\u641C\u7D22\u987E\u5BA2", "\u641C\u7D22\u5BA0\u7269", "\u641C\u7D22\u5546\u54C1\u8BA2\u5355", "\u641C\u7D22\u670D\u52A1\u9884\u7EA6"}));
		panel.add(jcb_universe, BorderLayout.WEST);
		
		jtf_searchBox = new JTextField();
		panel.add(jtf_searchBox);
		jtf_searchBox.setColumns(10);
		
		JButton jb_search = new JButton("\u5168\u5C40\u641C\u7D22");
		jb_search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*ȫ������*/
				String temp = jtf_searchBox.getText();
				//����û��ѡ���κ�Ҫ�����ı��ʱ
				if(jcb_universe.getSelectedIndex() == 0 ){
					JOptionPane.showMessageDialog(null, "��ѡ��Ҫ�����ı��");
					return;
				}
				
				//�ٵ�ѡ�������˿���Ϣ��ʱ
				else if(jcb_universe.getSelectedIndex() == 1) {
					/*�Թ˿���Ϣ���ݿ����ȫ�ֲ���*/
					Connection con = null;
					Customer customer = new Customer();
					//�����������ı�
					if(StringUtil.isEmpty(temp)) {
						customer.setCustomer_id(-1);
						customer.setCustomer_phone(-1);
					}
					else {
						if(IntegerUtil.isNumeric(temp)) {
							customer.setCustomer_id(Integer.parseInt(temp));
							customer.setCustomer_phone(Long.parseLong(temp));
						}
						else {
							customer.setCustomer_id(-1);
							customer.setCustomer_phone(-1);
						}
						customer.setCustomer_email(temp);
						customer.setCustomer_other_contact(temp);
						customer.setCustomer_name(temp);
					}
					//ִ������
					try {
						con = dbUtil.getCon();
						fillTableCustomer(customer);
					} catch (SQLException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, "ϵͳ���󣬲�ѯʧ��");
					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, "ϵͳ���󣬲�ѯʧ��");
					}finally {
						try {
							dbUtil.closeCon(con);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
				
				//�ڵ�ѡ������������Ϣ��ʱ
				else if(jcb_universe.getSelectedIndex() == 2) {
					/*�Գ�����Ϣ�����ݿ��н���ȫ�ֲ���*/
					Connection con = null;
					int[] customerId = null;
					int[] petSpeciesId = null;
					Pet pet = new Pet();
					//����������ı�
					if(StringUtil.isEmpty(temp)) {
						pet.setCustomer_id(-1);
						pet.setPet_id(-1);
						pet.setPet_species_id(-1);
					}
					else {
						if(IntegerUtil.isNumeric(temp)) {
							pet.setPet_id(Integer.parseInt(temp));//����pet_id��ѯ
						}
						else {
							pet.setPet_id(-1);
						}
						//����pet_species_id��ѯ
						try {
							petSpeciesId = petSpeciesName2Id(temp);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//����customer_id��ѯ
						customerId = customerName2Id(temp);
						//����customer_name��ѯ
						pet.setPet_name(temp);
					}
					//ִ������
					fillTablePet(pet, customerId, petSpeciesId);
				}
				
				
				
				
			}
		});
		jb_search.setFont(new Font("����", Font.BOLD, 16));
		panel.add(jb_search, BorderLayout.EAST);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.PAGE_AXIS));
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));
		
		JPanel jp_customer = new JPanel();
		jp_customer.setBorder(new LineBorder(Color.ORANGE, 2));
		panel_2.add(jp_customer);
		jp_customer.setLayout(new BorderLayout(0, 0));
		
		JPanel jp_barCustomer = new JPanel();
		jp_customer.add(jp_barCustomer, BorderLayout.NORTH);
		jp_barCustomer.setLayout(new BoxLayout(jp_barCustomer, BoxLayout.X_AXIS));
		
		JPanel panel_5 = new JPanel();
		jp_barCustomer.add(panel_5);
		
		JLabel lblNewLabel = new JLabel("\u987E\u5BA2\u4FE1\u606F\u8868");
		panel_5.add(lblNewLabel);
		lblNewLabel.setFont(new Font("����", Font.BOLD, 16));
		
		JPanel panel_4 = new JPanel();
		jp_barCustomer.add(panel_4);
		
		JButton jb_addCustomer = new JButton("\u65B0\u589E");
		jb_addCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AddCustomerDialog addCustomerDialog = new AddCustomerDialog();
				addCustomerDialog.setVisible(true);
			}
		});
		jb_addCustomer.setFont(new Font("����", Font.PLAIN, 14));
		panel_4.add(jb_addCustomer);
		
		JButton jb_updateCustomer = new JButton("\u66F4\u65B0");
		jb_updateCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//�õ�ѡ��Ĺ˿ͱ��
				int row = jtb_tableCustomer.getSelectedRow();
				if(row < 0) {
					JOptionPane.showMessageDialog(null, "��ѡ���κ�һ���˿���Ϣ");
					return;
				}
				String temp = (String) jtb_tableCustomer.getValueAt(row, 1);
				int id = Integer.parseInt(temp);
				//�½�UpdateCustomerDialog
				UpdateCustomerDialog updateCustomerDialog = new UpdateCustomerDialog(id);
				updateCustomerDialog.setVisible(true);
			}
		});
		jb_updateCustomer.setFont(new Font("����", Font.PLAIN, 14));
		panel_4.add(jb_updateCustomer);
		
		JScrollPane scrollPane = new JScrollPane();
		jp_customer.add(scrollPane);
		
		jtb_tableCustomer = new JTable();
		jtb_tableCustomer.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				//�����ˢ�±�jtb_tablePet
				int customerId = getIdInTable(jtb_tableCustomer); 
				if(customerId <= 0) {
					return;
				}
				Pet pet = new Pet();
				pet.setCustomer_id(customerId);
				pet.setPet_id(-1);
				pet.setPet_species_id(-1);
				fillTablePet(pet, null, null);
				//����ͼƬ
				jlb_petImage.setIcon(null);
				
			}
		});
		jtb_tableCustomer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(jtb_tableCustomer);
		jtb_tableCustomer.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
			},
			new String[] {
				"\u5E8F\u53F7", "\u7F16\u53F7", "\u987E\u5BA2\u59D3\u540D", "\u8054\u7CFB\u7535\u8BDD", "\u7535\u5B50\u90AE\u7BB1", "\u5176\u4ED6\u8054\u7CFB\u65B9\u5F0F", "\u521B\u5EFA\u65E5\u671F", "\u4FEE\u6539\u65E5\u671F"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		jtb_tableCustomer.getColumnModel().getColumn(0).setPreferredWidth(41);
		jtb_tableCustomer.getColumnModel().getColumn(1).setPreferredWidth(46);
		jtb_tableCustomer.getColumnModel().getColumn(2).setPreferredWidth(55);
		jtb_tableCustomer.getColumnModel().getColumn(3).setPreferredWidth(66);
		jtb_tableCustomer.getColumnModel().getColumn(4).setPreferredWidth(80);
		jtb_tableCustomer.getColumnModel().getColumn(5).setPreferredWidth(84);
		jtb_tableCustomer.getColumnModel().getColumn(6).setPreferredWidth(60);
		jtb_tableCustomer.getColumnModel().getColumn(7).setPreferredWidth(58);
		
		JPanel jp_purchase = new JPanel();
		jp_purchase.setBorder(new LineBorder(Color.ORANGE, 2));
		panel_2.add(jp_purchase);
		jp_purchase.setLayout(new BorderLayout(0, 0));
		
		JPanel jp_barPurchase = new JPanel();
		jp_purchase.add(jp_barPurchase, BorderLayout.NORTH);
		jp_barPurchase.setLayout(new BoxLayout(jp_barPurchase, BoxLayout.X_AXIS));
		
		JPanel panel_6 = new JPanel();
		jp_barPurchase.add(panel_6);
		
		JLabel lblNewLabel_1 = new JLabel("\u5546\u54C1\u8BA2\u5355\u4FE1\u606F\u8868");
		panel_6.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("����", Font.BOLD, 16));
		
		JPanel panel_7 = new JPanel();
		jp_barPurchase.add(panel_7);
		
		JButton jb_addPurchase = new JButton("\u65B0\u589E");
		jb_addPurchase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//��ù˿�id
				int customerId = getIdInTable(jtb_tableCustomer);
				if(customerId != -1) {
					//����AddPurchaseDialog
					AddPurchaseDialog addPurchaseDialog = new AddPurchaseDialog(customerId);
					addPurchaseDialog.setVisible(true);
				}
			}
		});
		jb_addPurchase.setFont(new Font("����", Font.PLAIN, 14));
		panel_7.add(jb_addPurchase);
		
		JButton jb_updatePurchase = new JButton("\u66F4\u65B0");
		jb_updatePurchase.setFont(new Font("����", Font.PLAIN, 14));
		panel_7.add(jb_updatePurchase);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		jp_purchase.add(scrollPane_1);
		
		jtb_tablePurchase = new JTable();
		jtb_tablePurchase.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_1.setViewportView(jtb_tablePurchase);
		jtb_tablePurchase.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
			},
			new String[] {
				"\u5E8F\u53F7", "\u8BA2\u5355\u7F16\u53F7", "\u603B\u4EF7", "\u6536\u8D27\u5730\u5740", "\u8BA2\u5355\u72B6\u6001", "\u8BA2\u5355\u521B\u5EFA\u65E5\u671F", "\u8BA2\u5355\u66F4\u65B0\u65E5\u671F"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		jtb_tablePurchase.getColumnModel().getColumn(0).setPreferredWidth(43);
		jtb_tablePurchase.getColumnModel().getColumn(1).setPreferredWidth(59);
		jtb_tablePurchase.getColumnModel().getColumn(2).setPreferredWidth(59);
		jtb_tablePurchase.getColumnModel().getColumn(3).setPreferredWidth(74);
		jtb_tablePurchase.getColumnModel().getColumn(5).setPreferredWidth(96);
		jtb_tablePurchase.getColumnModel().getColumn(6).setPreferredWidth(89);
		
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.X_AXIS));
		
		JPanel jp_pet = new JPanel();
		jp_pet.setBorder(new LineBorder(Color.ORANGE, 2));
		panel_3.add(jp_pet);
		jp_pet.setLayout(new BoxLayout(jp_pet, BoxLayout.Y_AXIS));
		
		JPanel jp_barPet = new JPanel();
		jp_pet.add(jp_barPet);
		jp_barPet.setLayout(new BoxLayout(jp_barPet, BoxLayout.X_AXIS));
		
		JPanel panel_9 = new JPanel();
		jp_barPet.add(panel_9);
		
		JLabel lblNewLabel_2 = new JLabel("\u5BA0\u7269\u4FE1\u606F\u8868");
		panel_9.add(lblNewLabel_2);
		lblNewLabel_2.setFont(new Font("����", Font.BOLD, 16));
		
		JPanel panel_8 = new JPanel();
		jp_barPet.add(panel_8);
		
		JButton jb_addPet = new JButton("\u65B0\u589E");
		jb_addPet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//���������¼
				//�������customer_id
				int row = jtb_tableCustomer.getSelectedRow();
				if(row < 0) {
					JOptionPane.showMessageDialog(null, "��ѡ���κ�һ���˿���Ϣ");
					return;
				}
				String temp = (String) jtb_tableCustomer.getValueAt(row, 1);
				int id = Integer.parseInt(temp);
				//�½�AddPetDialog
				AddPetDialog addPetDialog = new AddPetDialog(id);
				addPetDialog.setVisible(true);
			}
		});
		jb_addPet.setFont(new Font("����", Font.PLAIN, 14));
		panel_8.add(jb_addPet);
		
		JButton jb_updatePet = new JButton("\u66F4\u65B0");
		jb_updatePet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//���³�����Ϣ
				int petId = getIdInTable(jtb_tablePet);
				UpdatePetDialog updatePetDialog = new UpdatePetDialog(petId);
				updatePetDialog.setVisible(true);
			}
		});
		jb_updatePet.setFont(new Font("����", Font.PLAIN, 14));
		panel_8.add(jb_updatePet);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		jp_pet.add(scrollPane_2);
		
		jtb_tablePet = new JTable();
		jtb_tablePet.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				//�õ���������id
				int row = jtb_tablePet.getSelectedRow();
				if(row < 0) {
					JOptionPane.showMessageDialog(null, "��ѡ���κ�һ��������Ϣ");
					return;
				}
				String temp = (String) jtb_tablePet.getValueAt(row, 1);
				int id = Integer.parseInt(temp);
				//��ʾ����ͼƬ
				Connection con = null;
				Pet tempPet = new Pet();
				tempPet.setPet_id(id);
				tempPet.setCustomer_id(-1);
				tempPet.setPet_species_id(-1);
				try {
					con = dbUtil.getCon();
					ResultSet rs = petDao.petList(con, tempPet, null, null);
					if(rs.next()) {
						MyImageUtil.showImageInJLabel(jlb_petImage, rs.getBlob("pet_image"));
					}
					else {
						JOptionPane.showMessageDialog(null, "ϵͳ����");
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
				
				//ȷ���Ƿ���jtb_tableCustomer��ѡ�е����Ƿ�Ϊ��������ˣ�������ǣ��г�ȫ���˿ͣ�������jtb_tableCustomer��ѡ����Ϊ�ó��������
				
				
				
			}
		});
		jtb_tablePet.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_2.setViewportView(jtb_tablePet);
		jtb_tablePet.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
			},
			new String[] {
				"\u5E8F\u53F7", "\u7F16\u53F7", "\u5462\u79F0", "\u79CD\u7C7B", "\u521B\u5EFA\u65E5\u671F", "\u4FEE\u6539\u65E5\u671F"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		jtb_tablePet.getColumnModel().getColumn(0).setPreferredWidth(38);
		jtb_tablePet.getColumnModel().getColumn(1).setPreferredWidth(43);
		jtb_tablePet.getColumnModel().getColumn(2).setPreferredWidth(64);
		jtb_tablePet.getColumnModel().getColumn(3).setPreferredWidth(49);
		jtb_tablePet.getColumnModel().getColumn(4).setPreferredWidth(90);
		jtb_tablePet.getColumnModel().getColumn(5).setPreferredWidth(91);
		JTableHeader tableHeader = jtb_tablePet.getTableHeader();
		
		JScrollPane jsp_petImage = new JScrollPane();
		jp_pet.add(jsp_petImage);
		
		jlb_petImage = new JLabel("");
		jsp_petImage.setViewportView(jlb_petImage);
		jsp_petImage.setPreferredSize(new Dimension(0,300));
		
		JPanel jp_order = new JPanel();
		jp_order.setBorder(new LineBorder(Color.ORANGE, 2));
		panel_3.add(jp_order);
		jp_order.setLayout(new BoxLayout(jp_order, BoxLayout.PAGE_AXIS));
		
		JPanel jp_barOrder = new JPanel();
		jp_order.add(jp_barOrder);
		jp_barOrder.setLayout(new BoxLayout(jp_barOrder, BoxLayout.X_AXIS));
		
		JPanel panel_11 = new JPanel();
		jp_barOrder.add(panel_11);
		
		JLabel lblNewLabel_3 = new JLabel("\u670D\u52A1\u9884\u7EA6\u4FE1\u606F\u8868");
		panel_11.add(lblNewLabel_3);
		lblNewLabel_3.setFont(new Font("����", Font.BOLD, 16));
		
		JPanel panel_10 = new JPanel();
		jp_barOrder.add(panel_10);
		
		JButton jb_addOrder = new JButton("\u65B0\u589E");
		jb_addOrder.setFont(new Font("����", Font.PLAIN, 14));
		panel_10.add(jb_addOrder);
		
		JButton jb_updateOrder = new JButton("\u66F4\u65B0");
		jb_updateOrder.setFont(new Font("����", Font.PLAIN, 14));
		panel_10.add(jb_updateOrder);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		jp_order.add(scrollPane_3);
		
		jtb_tableOrder = new JTable();
		jtb_tableOrder.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_3.setViewportView(jtb_tableOrder);
		jtb_tableOrder.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null},
			},
			new String[] {
				"\u5E8F\u53F7", "\u9884\u7EA6\u7F16\u53F7", "\u603B\u4F63\u91D1", "\u5B8C\u6210\u72B6\u6001", "\u521B\u5EFA\u65F6\u95F4", "\u72B6\u6001\u66F4\u65B0\u65F6\u95F4", "\u5B8C\u6210\u65F6\u95F4"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		jtb_tableOrder.getColumnModel().getColumn(0).setPreferredWidth(46);
		jtb_tableOrder.getColumnModel().getColumn(1).setPreferredWidth(67);
		jtb_tableOrder.getColumnModel().getColumn(2).setPreferredWidth(59);
		jtb_tableOrder.getColumnModel().getColumn(3).setPreferredWidth(70);
		jtb_tableOrder.getColumnModel().getColumn(4).setPreferredWidth(67);
		jtb_tableOrder.getColumnModel().getColumn(5).setPreferredWidth(87);
		
		/*ʹ��ͷ�����϶�*/
		JTableHeader tableHeader1 = jtb_tableCustomer.getTableHeader();
		tableHeader1.setReorderingAllowed(false);
		tableHeader1 = jtb_tablePet.getTableHeader();
		tableHeader1.setReorderingAllowed(false);
		tableHeader1 = jtb_tableOrder.getTableHeader();
		tableHeader1.setReorderingAllowed(false);
		tableHeader1.setReorderingAllowed(false);
		tableHeader1 = jtb_tablePurchase.getTableHeader();
		tableHeader1.setReorderingAllowed(false);
		
		
		//���˿���Ϣ��
		Customer tempCustomer = new Customer();
		tempCustomer.setCustomer_id(-1);
		tempCustomer.setCustomer_phone(-1);
		fillTableCustomer(tempCustomer);
		

	}

}
