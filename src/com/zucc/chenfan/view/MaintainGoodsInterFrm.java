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
	/*���±������ڱ�������ݿ���ȡ����Ϣ�������ڱ����º�����ʱʹ��,��ʽstoreXXX*/
	private int storeId = 0;
	private String storeName = null;
	private String storeDetail = null;
	private String storeBarcode = null;
	private String storeBrand = null;
	private float storeUnitprice = -1;
	private int storeTypeIdSelected = -1;
	private String storeTypeNameSelected = null;
	/*���ﱣ�����Ʒ״̬��Ϣʹ���ַ�����ʽ���ڴ������ݿ�ǰҪת��Ϊboolean��ʽ*/
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
	
	/*����������Ҫ�ķ���*/
	
	/*���������*/
	private void fillTable(Goods goods, boolean hasState, int[] typeIdSet) {//boolean hasState�������Dao���е����ܲ�ѯ����ʹ�ã���Ҫ��ѯ����������boolean���͵�����ʱ���������Ƿ�������;typeIdSet�������浱������������ʱ��õ�����ID
		DefaultTableModel dtm = (DefaultTableModel) jtb_table.getModel();
		dtm.setRowCount(0);//���ñ������Ϊ�㣬�����´β�ѯǰ��ձ��
		/*��storeXXX��ֵΪ��,�������е��ı�������Ϊ��*/
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
			con = dbUtil.getCon();//�õ����ݿ�����
			ResultSet rs = goodsDao.goodsList(con, goods, hasState, typeIdSet);//��ѯ���ݿⲢ�õ��������ResultSet
			
			int i = 0;//���ڼ���ÿһ������
			while(rs.next()) {
				i++;
				/*Vector��ArrayList������Vector���̰߳��ŵ�*/
				Vector v = new Vector();
				v.add(i);
				/*����Vector����������ͳһת��ΪString,�����ڱ���е����ȡ��ֵ�����ᱨ��*/
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
					v.add("������");
				}
				else{
					v.add("�¼���");
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
	
	/*�������ñ�*/
	private void resetForm() {
		//�����е�����ʹ��֮ǰѡ��ʱ�����ֵ��ԭ����
		jtf_name.setText(storeName);
		jtf_barcode.setText(storeBarcode);
		jtf_brand.setText(storeBrand);
		jtf_unitprice.setText(String.valueOf(storeUnitprice));
		jta_detail.setText(storeDetail);
		jcb_stateModify.setSelectedItem(storeState);
		jcb_typeNameModify.setSelectedItem(storeTypeNameSelected);
		
	}
	/*�������¼�����Ʒ����jcb*/
	private void resetType() {
		Connection con = null;
		
		Goods_type goodsType = new Goods_type();
		try {
			con = dbUtil.getCon();
			ResultSet rs = goodsTypeDao.goodsTypeList(con, goodsType, false);
			jcb_typeName.removeAllItems();
			jcb_typeNameModify.removeAllItems();
			jcb_typeName.addItem("���������");
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
			JOptionPane.showMessageDialog(null, "��Ʒ�������ݼ���ʧ��");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "��Ʒ�������ݼ���ʧ��");
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
	
	/*�����������е�JRadioButton��Selected����Ϊfalse*/
	private void resetSearch() {
		//ʵ����JRadioButton�ĵ�ѡ����
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
	
	/*������ѡ�б���һ�м�¼ʱ�����·�������ֵ*/
	private void fillForm() {
		/*��ȡѡ�е���*/
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
		
		/*�����õ���Ϣ��storeXXX�������޸ĺ�����;*/
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
		boolean result = true;//�����Ҫ���£��������true
		/*���ж�����ѡ�б���еļ�¼*/
		if(storeId == -1) {
			result = false;
			JOptionPane.showMessageDialog(null, "��ѡ���κ�һ����¼");
			return result;
		}
		
		/*���жϱ���û��ʵ���޸ģ���Ҫʹ��ѡ�б������ʱ�����ڱ����е����ݡ�storeXXX�����бȽ�*/

		if(jtf_name.getText().equals(storeName) && jta_detail.getText().equals(storeDetail) && jcb_typeNameModify.getSelectedItem().equals(storeTypeNameSelected) && jtf_brand.getText().equals(storeBrand) && (Float.valueOf(jtf_unitprice.getText())==storeUnitprice) && jtf_barcode.getText().equals(storeBarcode) && jcb_stateModify.getSelectedItem().equals(storeState) ){
			result = false;
			JOptionPane.showMessageDialog(null, "������¼û���κ��޸ģ��������");
			return result;
		}
	
		/*���жϱ��б���Ҫ��д��ֵʱ����ȷ��ʽ��д�������ж��Ƿ�Ϊ�գ�*/
		if(StringUtil.isEmpty(jtf_name.getText())) {
			//�˴���������жϸ�����д������ʹ��if-else if����ʽ�����д���ʱ������Ϣ������
			result = false;
			JOptionPane.showMessageDialog(null, "����д�µ���Ʒ����");
			return result;
		}
		if(jcb_typeNameModify.getSelectedIndex() == 0) {
			result = false;
			JOptionPane.showMessageDialog(null, "��ѡ����Ʒ����");
			return result;
		}
		if(StringUtil.isEmpty(jtf_brand.getText())) {
			result = false;
			JOptionPane.showMessageDialog(null, "����д�µ���ƷƷ��");
			return result;
		}
		if(StringUtil.isEmpty(jtf_unitprice.getText())) {
			result = false;
			JOptionPane.showMessageDialog(null, "����д�µ���Ʒ����");
			return result;
		}
		if(StringUtil.isEmpty(jtf_barcode.getText())) {
			result = false;
			JOptionPane.showMessageDialog(null, "����д�µ���Ʒ����");
			return result;
		}
		if(StringUtil.isEmpty(jta_detail.getText())) {
			result = false;
			JOptionPane.showMessageDialog(null, "����д�µ���Ʒ����");
			return result;
		}
		if(jcb_stateModify.getSelectedIndex() == 0) {
			result = false;
			JOptionPane.showMessageDialog(null, "��ѡ����Ʒ�ϼ�״̬");
			return result;
		}
		
		
		/*���û��ٴ�ȷ���Ƿ�Ҫ���¸�����¼*/
		int choice = -1;
		choice = JOptionPane.showConfirmDialog(null, "ȷ��Ҫ����������Ʒ��Ϣ��\n��Ʒ��ţ�" + storeId + "\n��Ʒ���ƣ�" + storeName + " ����> " + jtf_name.getText() + "\n��Ʒ���ͣ�" + storeTypeNameSelected + " ����> " + jcb_typeNameModify.getSelectedItem() + "\n��ƷƷ�ƣ�" + storeBrand + " ����> " + jtf_brand.getText() + "\n��Ʒ���ۣ�" + storeUnitprice + " ����> " + jtf_unitprice.getText() + "\n��Ʒ���룺" + storeBarcode + " ����> " + jtf_barcode.getText() + "\n��Ʒ������" + storeDetail + " ����> " + jta_detail.getText() + "\n��Ʒ�ϼ�״̬��" + storeState + " ����> " + jcb_stateModify.getSelectedItem());
		if(choice != 0) {
			result = false;
			return result;
		}
		
		/*�ܸ���ʵ�ʹ������������ѯ���ݿⲢ��֤�Ƿ����ظ�ֵ������*/
		//����ѯ�����ʾ�ô���д�����ݲ�����Ҫ��ʱ��������Ϣ������false;
		/*��ѯ�Ƿ�����ͬ�������Ʒ���*/
		Goods goods = new Goods();
		goods.setGoods_barcode(jtf_barcode.getText());
		goods.setGoods_id(storeId);
		Connection con = null;
		try {
			con = dbUtil.getCon();
			boolean hasEqual = goodsDao.isEqual(con, goods);
			if(hasEqual) {
				result = false;
				JOptionPane.showMessageDialog(null, "�Ѿ�������ͬ�������Ʒ������ʧ��");
				dbUtil.closeCon(con);
				return result;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "ϵͳ�������ʧ��");
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "ϵͳ�������ʧ��");
			return false;
		}finally {
			try {
				dbUtil.closeCon(con);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "ϵͳ�������ʧ��");
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	/*����ȷ���ɷ�ɾ����¼*/
	private boolean allowDelete() {
		boolean result = true;
		
		/*���жϱ��ĵ�ǰ����Ƿ�Ϊ�գ����Ϊ��result = false����ʾ��return result*/
		if(storeId == -1) {
			result = false;
			JOptionPane.showMessageDialog(null, "����û��ѡ���κ�һ����¼���޷�����ɾ������");
			return result;
		}
		
		/*����ʾ�û��Ƿ�Ҫɾ��������¼��������ؽ����Ϊ0��result = false��return result*/
		int choice = -1;
		choice = JOptionPane.showConfirmDialog(null, "�Ƿ�Ҫɾ��������Ʒ��\n��Ʒ��ţ�" + storeId + "\n��Ʒ���ƣ�" + storeName + "\n��Ʒ���ͣ�" + storeTypeNameSelected+ "\n��ƷƷ�ƣ�" + storeBrand + "\n��Ʒ���ۣ�" + storeUnitprice + "\n��Ʒ���룺" + storeBarcode + "\n��Ʒ������" + storeDetail + "\n��Ʒ�ϼ�״̬��" + storeState);
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
		lblNewLabel.setFont(new Font("����", Font.PLAIN, 15));
		jp_searchBarTop.add(lblNewLabel, BorderLayout.WEST);
		
		jtf_searchBox = new JTextField();
		jtf_searchBox.setText("");
		jp_searchBarTop.add(jtf_searchBox, BorderLayout.CENTER);
		jtf_searchBox.setColumns(10);
		
		JButton jb_search = new JButton("\u641C\u7D22");
		jb_search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*ʵ��ȫ�֡�����Ʒ��š�����Ʒ���ơ�����Ʒ�������ơ�����Ʒ���ۡ�����ƷƷ�ơ�����Ʒ���롢���ϼ�״̬��������ģ������*/
				String temp = jtf_searchBox.getText();//��ȡ�������ı�����
				boolean isTypeId = false;//��ʾ�����Ƿ������������
				boolean hasState = false;//hasState��ʾ�����������Ƿ��С�����Ʒ״̬������,�������ֵΪtrue
				//�ж�����ѡ���Ƿ�Ϊ��
				if(!jrb_universe.isSelected() && !jrb_id.isSelected() && !jrb_name.isSelected() && !jrb_detail.isSelected() && !jrb_barcode.isSelected() && !jrb_brand.isSelected() && (jcb_state.getSelectedIndex() == 0) && (jcb_typeName.getSelectedIndex() == 0)) {
					JOptionPane.showMessageDialog(null, "��ѡ���κ�һ��������ʽ");
					return;
				}
				
				//��ʼ������������
				Goods goods = new Goods();
				goods.setGoods_id(-1);
				goods.setGoods_type_id(-1);
				goods.setGoods_unitprice(-1);
				
				/*���goods_type_id�Ŀ���ֵ����String --> int��*/
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
					//�����������Ϊ��ʱ��ȡ��������
				}
				else if(jrb_universe.isSelected()) {//ʵ��ȫ������
					if(IntegerUtil.isNumeric(temp)) {
						goods.setGoods_id(Integer.parseInt(temp));
					}
					
					goods.setGoods_barcode(temp);
					goods.setGoods_name(temp);
					goods.setGoods_detail(temp);
					goods.setGoods_brand(temp);
					if("������".indexOf(temp) != -1) {
						hasState = true;
						goods.setGoods_state(true);
					}
					else if("�¼���".indexOf(temp) != -1) {
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
				else if(jrb_id.isSelected()) {//ʵ�ְ���Ʒ���������ģ����
					if(!IntegerUtil.isNumeric(temp)) {
						JOptionPane.showMessageDialog(null, "��Ʒ���ӦΪ�����֣�����������");
						return;
					}
					goods.setGoods_id(Integer.parseInt(temp));
				}
				else if(jrb_name.isSelected()) {//ʵ�ְ���Ʒ����������ģ����
					goods.setGoods_name(temp);
				}
				else if(jrb_detail.isSelected()){//ʵ�ְ���Ʒ������ѯ
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
						JOptionPane.showMessageDialog(null, "�۸��Ӧ���ԡ�__.xx������ʽ����");
						return;
					}
					goods.setGoods_unitprice(Float.valueOf(temp));
				}*/
				else if(jcb_state.getSelectedIndex() >= 1) {
					hasState = true;
					if(jcb_state.getSelectedItem().equals("������")) {
						goods.setGoods_state(true);
					}
					else if(jcb_state.getSelectedItem().equals("�¼���")) {
						goods.setGoods_state(false);
					}
					else {
						JOptionPane.showMessageDialog(null, "δ֪��Ʒ״̬����");
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
				
				//ִ������
				if(isTypeId) {
					fillTable(goods, hasState, tempTypeId);
				}
				else {
					fillTable(goods, hasState, null);
				}
				
				
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
		jsp_table.setViewportView(jtb_table);//�������ʹ��ͷ�ɼ�
		
		JPanel jp_form = new JPanel();
		getContentPane().add(jp_form, BorderLayout.SOUTH);
		
		JPanel jp_id = new JPanel();
		jp_form.add(jp_id);
		
		JLabel label = new JLabel("\u5546\u54C1\u7F16\u53F7\uFF1A");
		label.setFont(new Font("����", Font.PLAIN, 15));
		jp_id.add(label);
		
		jtf_id = new JTextField();
		jtf_id.setEditable(false);
		jp_id.add(jtf_id);
		jtf_id.setColumns(10);
		
		JPanel jp_name = new JPanel();
		jp_form.add(jp_name);
		
		JLabel lblNewLabel_1 = new JLabel("\u5546\u54C1\u540D\u79F0\uFF1A");
		lblNewLabel_1.setFont(new Font("����", Font.PLAIN, 15));
		jp_name.add(lblNewLabel_1);
		
		jtf_name = new JTextField();
		jtf_name.setText("");
		jp_name.add(jtf_name);
		jtf_name.setColumns(10);
		
		JPanel jp_typeNameModify = new JPanel();
		jp_form.add(jp_typeNameModify);
		
		JLabel lblNewLabel_6 = new JLabel("\u5546\u54C1\u7C7B\u522B\uFF1A");
		lblNewLabel_6.setFont(new Font("����", Font.PLAIN, 15));
		jp_typeNameModify.add(lblNewLabel_6);
		
		jcb_typeNameModify = new JComboBox();
		jcb_typeNameModify.setModel(new DefaultComboBoxModel(new String[] {""}));
		jp_typeNameModify.add(jcb_typeNameModify);
		
		JPanel jp_brand = new JPanel();
		jp_form.add(jp_brand);
		
		JLabel lblNewLabel_2 = new JLabel("\u5546\u54C1\u54C1\u724C\uFF1A");
		lblNewLabel_2.setFont(new Font("����", Font.PLAIN, 15));
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
		lblNewLabel_3.setFont(new Font("����", Font.PLAIN, 15));
		jp_unitprice.add(lblNewLabel_3);
		
		jtf_unitprice = new JTextField();
		jp_unitprice.add(jtf_unitprice);
		jtf_unitprice.setColumns(10);
		
		JPanel jp_barcode = new JPanel();
		jp_form.add(jp_barcode);
		
		JLabel lblNewLabel_4 = new JLabel("\u6761\u7801\u53F7\uFF1A");
		lblNewLabel_4.setFont(new Font("����", Font.PLAIN, 15));
		jp_barcode.add(lblNewLabel_4);
		
		jtf_barcode = new JTextField();
		jp_barcode.add(jtf_barcode);
		jtf_barcode.setColumns(10);
		
		JPanel jp_detail = new JPanel();
		jp_form.add(jp_detail);
		jp_detail.setLayout(new BorderLayout(0, 0));
		
		JLabel lblS = new JLabel("\u5546\u54C1\u63CF\u8FF0\uFF1A");
		lblS.setFont(new Font("����", Font.PLAIN, 15));
		jp_detail.add(lblS, BorderLayout.WEST);
		
		jta_detail = new JTextArea();
		jta_detail.setRows(2);
		jta_detail.setColumns(20);
		jp_detail.add(jta_detail, BorderLayout.CENTER);
		
		JPanel jp_state = new JPanel();
		jp_form.add(jp_state);
		
		JLabel lblNewLabel_5 = new JLabel("\u4E0A\u67B6\u72B6\u6001\uFF1A");
		lblNewLabel_5.setFont(new Font("����", Font.PLAIN, 15));
		jp_state.add(lblNewLabel_5);
		
		jcb_stateModify = new JComboBox();
		jcb_stateModify.setModel(new DefaultComboBoxModel(new String[] {"", "\u51FA\u552E\u4E2D", "\u4E0B\u67B6\u4E2D"}));
		jp_state.add(jcb_stateModify);
		
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
				Goods goods = new Goods();//ʵ��model��
				goods.setGoods_id(storeId);//set�����б䶯������
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
				if(jcb_stateModify.getSelectedItem().equals("������")) {
					goods.setGoods_state(true);
				}
				else if(jcb_stateModify.getSelectedItem().equals("�¼���")) {
					goods.setGoods_state(false);
				}
				
				try {
					con = dbUtil.getCon();
					int temp = goodsDao.updateGoods(con, goods);
					if(temp == 1) {
						JOptionPane.showMessageDialog(null, "����Ʒ�����Ϣ���³ɹ�");
						//ˢ�±�������г������ָܻ�֮ǰ�����������
						Goods tempFill = new Goods();
						tempFill.setGoods_id(-1);
						tempFill.setGoods_type_id(-1);
						tempFill.setGoods_unitprice(-1);
						//fillTable��������storeXXX�Լ���ձ��Ĳ���
						fillTable(tempFill, false, null);
					}
					else {
						JOptionPane.showMessageDialog(null, "���ݿ��������ʱ�������⣬����ʧ��");
						//��������
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
				Goods goods = new Goods();//newһ��ʵ����
				goods.setGoods_id(storeId);//����ʵ����Ĳ�ѯ����
				try {
					con = dbUtil.getCon();
					int temp = goodsDao.deleteGoods(con, goods);//����temp���ڽ����Ƿ�ɾ���ɹ�����Ϣ���������1��ɾ���ɹ�
					if(temp == 1) {
						JOptionPane.showMessageDialog(null, "������¼�ѱ��ɹ�ɾ��");
						//ˢ�±����ձ�,������storeXXX
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
		
		
		/*��ʼ��jcb_typeName�Լ�jcb_typeNameModify*/
		resetType();
		
		/*ʹ�÷�����fillTable������������*/
		Goods tempFill = new Goods();
		tempFill.setGoods_id(-1);
		tempFill.setGoods_type_id(-1);
		tempFill.setGoods_unitprice(-1);
		fillTable(tempFill, false, null);
		
		
	}

	

}
