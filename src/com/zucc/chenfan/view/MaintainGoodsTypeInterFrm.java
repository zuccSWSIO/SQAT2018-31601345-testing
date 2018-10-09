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
	
	/*���±������ڱ�������ݿ���ȡ����Ϣ�������ڱ����º�����ʱʹ��,��ʽstoreXXX*/
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
	
	/*����������Ҫ�ķ���*/
	
	/*���������*/
	private void fillTable(Goods_type goodType, boolean hasId) {//boolean hadId�������Dao���е����ܲ�ѯ����ʹ�ã���Ҫ��ѯ����������int���͵�����ʱ����������Ĭ��ֵ0��������ֵ��0�������
		DefaultTableModel dtm = (DefaultTableModel) jtb_table.getModel();
		dtm.setRowCount(0);//���ñ������Ϊ�㣬�����´β�ѯǰ��ձ��
		/*��storeXXX��ֵΪ��,�������е��ı�������Ϊ��*/
		storeId = 0;
		storeName = null;
		storeDetail = null;
		jtf_id.setText("");
		jtf_name.setText("");
		jta_detail.setText("");
		
		Connection con = null;
		try {
			con = dbUtil.getCon();//�õ����ݿ�����
			ResultSet rs = goodsTypeDao.goodsTypeList(con, goodType, hasId);//��ѯ���ݿⲢ�õ��������ResultSet
			
			int i = 0;//���ڼ���ÿһ������
			while(rs.next()) {
				i++;
				/*Vector��ArrayList������Vector���̰߳��ŵ�*/
				Vector v = new Vector();
				v.add(i);
				/*����Vector����������ͳһת��ΪString,�����ڱ���е����ȡ��ֵ�����ᱨ��*/
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
	
	/*�������ñ�*/
	private void resetForm() {
		//�����е�����ʹ��֮ǰѡ��ʱ�����ֵ��ԭ����
		jtf_name.setText(storeName);
		jta_detail.setText(storeDetail);
		
	}
	
	/*�����������е�JRadioButton��Selected����Ϊfalse*/
	private void resetSearch() {
		//ʵ����JRadioButton�ĵ�ѡ����
		jrb_universe.setSelected(false);
		jrb_id.setSelected(false);
		jrb_name.setSelected(false);
		jrb_detail.setSelected(false);
	}
	
	/*������ѡ�б���һ�м�¼ʱ�����·�������ֵ*/
	private void fillForm() {
		/*��ȡѡ�е���*/
		int row = jtb_table.getSelectedRow();
		jtf_id.setText((String) jtb_table.getValueAt(row, 1));
		jtf_name.setText((String) jtb_table.getValueAt(row, 2));
		jta_detail.setText((String) jtb_table.getValueAt(row, 3));
		/*�����õ���Ϣ��storeXXX�������޸ĺ�����;*/
		storeId = Integer.parseInt(jtf_id.getText());
		storeName = jtf_name.getText();
		storeDetail = jta_detail.getText();
	}
	
	/*ʵ�ֵ������ֹ���ʱ������ֵ���ű�������ı仯������*/
	private void fillFormWhenMouseWheel(MouseWheelEvent mouseWheelEvent) {
		/*java������ж������������Ϲ��������¹�*/
		int beforeSelection = jtb_table.getSelectedRow();
		int afterSelection = beforeSelection;
		if(mouseWheelEvent.getWheelRotation() == 1) {//������¹�ʱ��ֵΪ1
			afterSelection ++;
			if(afterSelection >= (jtb_table.getRowCount()) ) {
				afterSelection = jtb_table.getRowCount()-1;
			}
		}
		else if(mouseWheelEvent.getWheelRotation() == -1){//������¹�ʱ��ֵΪ-1
			/*����index��0��ʼ*/
			afterSelection --;
			if(afterSelection <= -1) {
				afterSelection = 0;
			}
		}
		jtb_table.setRowSelectionInterval(afterSelection, afterSelection);
		fillForm();
	}
	
	/*�ڵ�����°�ťʱ�ж�ʱ�������˿��Ը��µı����������������ݿ���أ�*/
	private boolean allowUpdate() {
		boolean result = true;//�����Ҫ���£��������ture
		/*���ж�����ѡ�б���еļ�¼*/
		if(StringUtil.isEmpty(storeName)) {
			result = false;
			JOptionPane.showMessageDialog(null, "��ѡ���κ�һ����¼");
			return result;
		}
		
		/*���жϱ���û��ʵ���޸ģ���Ҫʹ��ѡ�б������ʱ�����ڱ����е����ݡ�storeXXX�����бȽ�*/
//		if(�������ֵ�ͱ����ֵ���) {
			/*result = false;
			JOptionPane.showMessageDialog(null, "������¼û���κ��޸ģ��������");
			return result;*/
//		}
//		else {
//		}
		if(jtf_name.getText().equals(storeName) && jta_detail.getText().equals(storeDetail)) {
			result = false;
			JOptionPane.showMessageDialog(null, "������¼û���κ��޸ģ��������");
			return result;
		}
		
		/*���жϱ��б���Ҫ��д��ֵʱ����ȷ��ʽ��д�������ж��Ƿ�Ϊ�գ�*/
		if(StringUtil.isEmpty(jtf_name.getText())) {
			//�˴���������жϸ�����д������ʹ��if-else if����ʽ�����д���ʱ������Ϣ������
			result = false;
			JOptionPane.showMessageDialog(null, "����д�µ���Ʒ��������");
			return result;
		}
		if(StringUtil.isEmpty(jta_detail.getText())) {
			result = false;
			JOptionPane.showMessageDialog(null, "����д�µ���Ʒ�������");
			return result;
		}
		
		/*���û��ٴ�ȷ���Ƿ�Ҫ���¸�����¼*/
		int choice = -1;
		choice = JOptionPane.showConfirmDialog(null, "ȷ��Ҫ����������Ʒ�����Ϣ��\n��Ʒ����ţ�" + storeId + "\n��Ʒ������ƣ�" + storeName + " ����> " + jtf_name.getText() + "\n��Ʒ������飺" + storeDetail + " ����> " + jta_detail.getText());
		if(choice != 0) {
			result = false;
			return result;
		}
		
		/*�ܸ���ʵ�ʹ������������ѯ���ݿⲢ��֤�Ƿ����ظ�ֵ������*/
		//����ѯ�����ʾ�ô���д�����ݲ�����Ҫ��ʱ��������Ϣ������false;
		/*��ѯ�Ƿ�����������Ʒ���*/
		Connection con = null;
		Goods_type temp = new Goods_type();
		temp.setGoods_type_name(jtf_name.getText());
		
		try {
			con = dbUtil.getCon();
			boolean  hasEqual = goodsTypeDao.isEqual(con, temp);
			if(hasEqual) {
				result = false;
				JOptionPane.showMessageDialog(null, "�Ѿ�������ͬ���Ƶ���Ʒ��𣬸���ʧ��");
				jtf_name.setText(storeName);
				return result;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
			JOptionPane.showMessageDialog(null, "ϵͳ�������ݿ��ѯʧ��");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			JOptionPane.showMessageDialog(null, "ϵͳ�������ݿ��ѯʧ��");
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
	
	/*����ȷ���ɷ�ɾ����¼*/
	private boolean allowDelete() {
		boolean result = true;
		
		/*���жϱ��ĵ�ǰ����Ƿ�Ϊ�գ����Ϊ��result = false����ʾ��return result*/
		if(StringUtil.isEmpty(storeName)) {
			result = false;
			JOptionPane.showMessageDialog(null, "����û��ѡ���κ�һ����¼���޷�����ɾ������");
			return result;
		}
		
		/*����ʾ�û��Ƿ�Ҫɾ��������¼��������ؽ����Ϊ0��result = false��return result*/
		int choice = -1;
		choice = JOptionPane.showConfirmDialog(null, "�Ƿ�Ҫɾ��������Ʒ���ͣ�\n��Ʒ���ͱ�ţ�" + storeId + "\n��Ʒ�������ƣ�" + storeName + "\n��Ʒ�������飺" + storeDetail);
		if(choice != 0) {
			result = false;
			return result;
		}
			
		/*�۲�ѯ���ݿ��Ƿ��������������������¼������ļ�¼����У�����ʾ�������ؽ����Ϊ0ʱresult = false��return result*/
		//�����ؽ��Ϊ0ʱ�����������ʵ���Dao���е�delete��������������ݽ���ɾ��
		/*���棡�������˴���Ϊ��û��ѧ��Ӧ����������ɾ��������������ʱ�����������Ժ�һ��Ҫ���±༭*/
		
		
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
		lblNewLabel.setFont(new Font("����", Font.PLAIN, 15));
		jp_searchBarTop.add(lblNewLabel, BorderLayout.WEST);
		
		jtf_searchBox = new JTextField();
		jtf_searchBox.setText("");
		jp_searchBarTop.add(jtf_searchBox, BorderLayout.CENTER);
		jtf_searchBox.setColumns(10);
		
		JButton jb_search = new JButton("\u641C\u7D22");
		jb_search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*ʵ��ȫ�֡�����Ʒ�����š�����Ʒ�������ơ�����Ʒ��������ģ������*/
				String temp = jtf_searchBox.getText();//��ȡ�������ı�����
				//�ж�����ѡ���Ƿ�Ϊ��
				if(!jrb_universe.isSelected() && !jrb_id.isSelected() && !jrb_name.isSelected() && !jrb_detail.isSelected()) {
					JOptionPane.showMessageDialog(null, "��ѡ���κ�һ��������ʽ");
					return;
				}
				
				/*��ʼ������������*/
				Goods_type goodsType = new Goods_type();
				boolean hasId = false;//hasId��ʾ�����������Ƿ��С���Id������,�������ֵΪtrue
				if(StringUtil.isEmpty(temp)) {
					//�����������Ϊ��ʱ��ȡ��������
				}
				else if(jrb_universe.isSelected()) {//ʵ��ȫ������
					if(IntegerUtil.isNumeric(temp)) {
						goodsType.setGoods_type_id(Integer.parseInt(temp));
						hasId = true;
					}
					goodsType.setGoods_type_name(temp);
					goodsType.setGoods_type_detail(temp);
				}
				else if(jrb_id.isSelected()) {//ʵ�ְ���Ʒ������������ģ����
					if(!IntegerUtil.isNumeric(temp)) {
						JOptionPane.showMessageDialog(null, "��Ʒ������ӦΪ�����֣�����������");
						return;
					}
					goodsType.setGoods_type_id(Integer.parseInt(temp));
					hasId = true;
				}
				else if(jrb_name.isSelected()) {//ʵ�ְ���Ʒ��������������ģ����
					goodsType.setGoods_type_name(temp);
				}
				else if(jrb_detail.isSelected()){//ʵ�ְ���Ʒ����������ѯ
					goodsType.setGoods_type_detail(temp);
				}
				
				//ִ������
				fillTable(goodsType, hasId);
			}
		});
		jb_search.setFont(new Font("����", Font.BOLD, 16));
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
				/*�������ֹ���ʱ�����±�*/
				fillFormWhenMouseWheel(arg0);
			}
		});
		jtb_table.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				/*����갴ס������϶�ʱ�����±�*/
				fillForm();
			}
		});
		jtb_table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				/*����갴ѹ����ĳһ�У����±�*/
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
		jsp_table.setViewportView(jtb_table);//�������ʹ��ͷ�ɼ�
		
		JPanel jp_form = new JPanel();
		getContentPane().add(jp_form, BorderLayout.SOUTH);
		
		JPanel jp_id = new JPanel();
		jp_form.add(jp_id);
		
		JLabel label = new JLabel("\u5546\u54C1\u79CD\u7C7B\u7F16\u53F7\uFF1A");
		label.setFont(new Font("����", Font.PLAIN, 15));
		jp_id.add(label);
		
		jtf_id = new JTextField();
		jtf_id.setEditable(false);
		jp_id.add(jtf_id);
		jtf_id.setColumns(10);
		
		JPanel jp_name = new JPanel();
		jp_form.add(jp_name);
		
		JLabel lblNewLabel_1 = new JLabel("\u5546\u54C1\u79CD\u7C7B\u540D\u79F0\uFF1A");
		lblNewLabel_1.setFont(new Font("����", Font.PLAIN, 15));
		jp_name.add(lblNewLabel_1);
		
		jtf_name = new JTextField();
		jtf_name.setText("");
		jp_name.add(jtf_name);
		jtf_name.setColumns(10);
		
		JPanel jp_detail = new JPanel();
		jp_form.add(jp_detail);
		jp_detail.setLayout(new BorderLayout(0, 0));
		
		JLabel lblS = new JLabel("\u5546\u54C1\u79CD\u7C7B\u63CF\u8FF0\uFF1A");
		lblS.setFont(new Font("����", Font.PLAIN, 15));
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
/*ʵ�ָ��¹���*/
				
				//ȷ���Ƿ���Ը���
				if(!allowUpdate()) {
					return;
				}
				
				/*�������ݿⲢˢ�±��*/
				Connection con = null;
				Goods_type goodsType = new Goods_type();//ʵ��model��
				goodsType.setGoods_type_id(storeId);//set�����б䶯������
				goodsType.setGoods_type_name(jtf_name.getText());
				goodsType.setGoods_type_detail(jta_detail.getText());
				
				try {
					con = dbUtil.getCon();
					int temp = goodsTypeDao.updateGoodsType(con, goodsType);
					if(temp == 1) {
						JOptionPane.showMessageDialog(null, "����Ʒ�����Ϣ���³ɹ�");
						//ˢ�±�������г������ָܻ�֮ǰ�����������
						fillTable(new Goods_type(), false);
						//����storeXXX
						storeId = Integer.parseInt(jtf_id.getText());
						storeName = jtf_name.getText();
						storeDetail = jta_detail.getText();
					}
					else {
						JOptionPane.showMessageDialog(null, "���ݿ��������ʱ�������⣬����ʧ��");
						/*��������*/
						resetForm();
					}
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "���ݿ��������ʱ�������⣬����ʧ��");
					/*��������*/
					resetForm();
					e.printStackTrace();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "���ݿ����ӳ��ִ��󣬸���ʧ��");
					/*��������*/
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
		jb_update.setFont(new Font("����", Font.BOLD, 16));
		jp_button.add(jb_update);
		
		JButton jb_delete = new JButton("\u5220\u9664");
		jb_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*����ʵ��ɾ����¼�Ĺ���*/
				if(!allowDelete()) {
					return;
				}
				
				Connection con = null;
				Goods_type goodsType = new Goods_type();//newһ��ʵ����
				goodsType.setGoods_type_id(storeId);//����ʵ����Ĳ�ѯ����
				try {
					con = dbUtil.getCon();
					int temp = goodsTypeDao.deleteGoodsType(con, goodsType);//����temp���ڽ����Ƿ�ɾ���ɹ�����Ϣ���������1��ɾ���ɹ�
					if(temp == 1) {
						JOptionPane.showMessageDialog(null, "������¼�ѱ��ɹ�ɾ��");
						/*ˢ�±����ձ�,������storeXXX*/
						fillTable(new Goods_type(), false);
						storeId = 0;
						storeName = null;
						storeDetail = null;
						jtf_id.setText("");
						jtf_name.setText("");
						jta_detail.setText("");
					}
					else {
						JOptionPane.showMessageDialog(null, "ϵͳ����ɾ��ʧ��");
					}
				} catch (SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "ϵͳ����ɾ��ʧ��");
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "ϵͳ����ɾ��ʧ��");
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
		jb_delete.setFont(new Font("����", Font.BOLD, 16));
		jp_button.add(jb_delete);
		
		JButton jb_reset = new JButton("\u91CD\u7F6E");
		jb_reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*ʵ�����ñ���ť�Ĺ���*/
				resetForm();
			}
		});
		jb_reset.setForeground(Color.BLUE);
		jb_reset.setFont(new Font("����", Font.BOLD, 16));
		jp_button.add(jb_reset);
		
		/*ʹ��ͷ�����϶�*/
		JTableHeader tableHeader = jtb_table.getTableHeader();
		tableHeader.setReorderingAllowed(false);
		
		/*ʹ��borderLayout���ַ������޸Ķ����ϱ��и��������С�ķ���*/
		jp_form.setPreferredSize(new Dimension(0, 100));
		
		/*ʹ�÷�����fillTable������������*/        
		fillTable(new Goods_type(), false);
		
	}

	

}
