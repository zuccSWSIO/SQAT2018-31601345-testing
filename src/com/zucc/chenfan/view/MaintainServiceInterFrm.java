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

import com.zucc.chenfan.dao.ServiceDao;
import com.zucc.chenfan.dao.Service_typeDao;
import com.zucc.chenfan.model.Service;
import com.zucc.chenfan.model.Service_type;
import com.zucc.chenfan.util.DbUtil;
import com.zucc.chenfan.util.IntegerUtil;
import com.zucc.chenfan.util.StringUtil;

public class MaintainServiceInterFrm extends JInternalFrame {
	DbUtil dbUtil = new DbUtil();
	Service_typeDao serviceTypeDao = new Service_typeDao();
	ServiceDao serviceDao = new ServiceDao();
	
	private JTable jtb_table;
	private JTextField jtf_searchBox;
	private JTextField jtf_id;
	private JTextField jtf_name;
	private JRadioButton jrb_universe;
	private JRadioButton jrb_id;
	private JRadioButton jrb_name;
	private JRadioButton jrb_detail;
	private JTextArea jta_detail;
	private JTextField jtf_price;
	private JComboBox jcb_typeName;
	private JComboBox jcb_state;
	private JComboBox jcb_typeNameModify;
	private JComboBox jcb_stateModify;
	/*���±������ڱ�������ݿ���ȡ����Ϣ�������ڱ����º�����ʱʹ��,��ʽstoreXXX*/
	private int storeId = 0;
	private String storeName = null;
	private String storeDetail = null;
	private float storePrice = -1;
	private int storeTypeIdSelected = -1;
	private String storeTypeNameSelected = null;
	/*���ﱣ��ķ���״̬��Ϣʹ���ַ�����ʽ���ڴ������ݿ�ǰҪת��Ϊboolean��ʽ*/
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
					MaintainServiceInterFrm frame = new MaintainServiceInterFrm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/*����������Ҫ�ķ���*/
	
	/*���������*/
	private void fillTable(Service service, boolean hasState, int[] typeIdSet) {//boolean hasState�������Dao���е����ܲ�ѯ����ʹ�ã���Ҫ��ѯ����������boolean���͵�����ʱ���������Ƿ�������;typeIdSet�������浱������������ʱ��õ�����ID
		DefaultTableModel dtm = (DefaultTableModel) jtb_table.getModel();
		dtm.setRowCount(0);//���ñ������Ϊ�㣬�����´β�ѯǰ��ձ��
		/*��storeXXX��ֵΪ��,�������е��ı�������Ϊ��*/
		storeId = -1;
		storeName = null;
		storeDetail = null;
		storeState = null;
		storeTypeIdSelected = -1;
		storeTypeNameSelected = null;
		storePrice = -1;
		
		jtf_id.setText("");
		jtf_name.setText("");
		jta_detail.setText("");
		jtf_price.setText("");
		jcb_stateModify.setSelectedIndex(0);
		jcb_typeNameModify.setSelectedIndex(0);
		
		
		Connection con = null;
		try {
			con = dbUtil.getCon();//�õ����ݿ�����
			ResultSet rs = serviceDao.serviceList(con, service, hasState, typeIdSet);//��ѯ���ݿⲢ�õ��������ResultSet
			
			int i = 0;//���ڼ���ÿһ������
			while(rs.next()) {
				i++;
				/*Vector��ArrayList������Vector���̰߳��ŵ�*/
				Vector v = new Vector();
				v.add(i);
				/*����Vector����������ͳһת��ΪString,�����ڱ���е����ȡ��ֵ�����ᱨ��*/
				v.add(rs.getString("service_id"));
				v.add(rs.getString("service_name"));
				
				for(int j=0 ; j < storeTypeId.length; j++) {
					if(rs.getInt("service_type_id") == storeTypeId[j]) {
						v.add(storeTypeName[j]);
						break;
					}
				}
				v.add(rs.getString("service_price"));
				v.add(rs.getString("service_detail"));
				if(rs.getBoolean("service_state")) {
					v.add("����������");
				}
				else{
					v.add("����������");
				}
				v.add(rs.getString("service_createdate"));
				v.add(rs.getString("service_modifydate"));
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
		jtf_price.setText(String.valueOf(storePrice));
		jta_detail.setText(storeDetail);
		jcb_stateModify.setSelectedItem(storeState);
		jcb_typeNameModify.setSelectedItem(storeTypeNameSelected);
		
	}
	/*�������¼��ط�������jcb*/
	private void resetType() {
		Connection con = null;
		
		Service_type serviceType = new Service_type();
		try {
			con = dbUtil.getCon();
			ResultSet rs = serviceTypeDao.serviceTypeList(con, serviceType, false);
			jcb_typeName.removeAllItems();
			jcb_typeNameModify.removeAllItems();
			jcb_typeName.addItem("���������");
			jcb_typeNameModify.addItem("");
			int index = 0;
			while(rs.next()) {
				index ++;
				jcb_typeName.addItem(rs.getString("service_type_name"));
				jcb_typeNameModify.addItem(rs.getString("service_type_name"));
				storeTypeName[index-1] = rs.getString("service_type_name");
				storeTypeId[index-1] = rs.getInt("service_type_id");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "�����������ݼ���ʧ��");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "�����������ݼ���ʧ��");
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
//		jrb_price.setSelected(false);
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
		jtf_price.setText((String) jtb_table.getValueAt(row, 4));
		jta_detail.setText((String) jtb_table.getValueAt(row, 5));
		jcb_stateModify.setSelectedItem(jtb_table.getValueAt(row, 6));
		
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
		storePrice = Float.valueOf(jtf_price.getText());
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

		if(jtf_name.getText().equals(storeName) && jta_detail.getText().equals(storeDetail) && jcb_typeNameModify.getSelectedItem().equals(storeTypeNameSelected)  && (Float.valueOf(jtf_price.getText())==storePrice) && jcb_stateModify.getSelectedItem().equals(storeState) ){
			result = false;
			JOptionPane.showMessageDialog(null, "������¼û���κ��޸ģ��������");
			return result;
		}
	
		/*���жϱ��б���Ҫ��д��ֵʱ����ȷ��ʽ��д�������ж��Ƿ�Ϊ�գ�*/
		if(StringUtil.isEmpty(jtf_name.getText())) {
			//�˴���������жϸ�����д������ʹ��if-else if����ʽ�����д���ʱ������Ϣ������
			result = false;
			JOptionPane.showMessageDialog(null, "����д�µķ�������");
			return result;
		}
		if(jcb_typeNameModify.getSelectedIndex() == 0) {
			result = false;
			JOptionPane.showMessageDialog(null, "��ѡ���������");
			return result;
		}
		if(StringUtil.isEmpty(jtf_price.getText())) {
			result = false;
			JOptionPane.showMessageDialog(null, "����д�µķ��񵥼�");
			return result;
		}
		if(StringUtil.isEmpty(jta_detail.getText())) {
			result = false;
			JOptionPane.showMessageDialog(null, "����д�µķ�������");
			return result;
		}
		if(jcb_stateModify.getSelectedIndex() == 0) {
			result = false;
			JOptionPane.showMessageDialog(null, "��ѡ������ϼ�״̬");
			return result;
		}
		
		
		/*���û��ٴ�ȷ���Ƿ�Ҫ���¸�����¼*/
		int choice = -1;
		choice = JOptionPane.showConfirmDialog(null, "ȷ��Ҫ��������������Ϣ��\n�����ţ�" + storeId + "\n�������ƣ�" + storeName + " ����> " + jtf_name.getText() + "\n�������ͣ�" + storeTypeNameSelected + " ����> " + jcb_typeNameModify.getSelectedItem() + "\n����Ӷ��" + storePrice + " ����> " + jtf_price.getText() + "\n����������" + storeDetail + " ����> " + jta_detail.getText() + "\n�����ϼ�״̬��" + storeState + " ����> " + jcb_stateModify.getSelectedItem());
		if(choice != 0) {
			result = false;
			return result;
		}
		
		/*�ܸ���ʵ�ʹ������������ѯ���ݿⲢ��֤�Ƿ����ظ�ֵ������*/
		//����ѯ�����ʾ�ô���д�����ݲ�����Ҫ��ʱ��������Ϣ������false;
		/*��ѯ�Ƿ�����ͬ���Ƶķ������*/
		Service service = new Service();
		service.setService_id(storeId);
		service.setService_name(jtf_name.getText());
		Connection con = null;
		try {
			con = dbUtil.getCon();
			boolean hasEqual = serviceDao.isEqual(con, service);
			if(hasEqual) {
				result = false;
				JOptionPane.showMessageDialog(null, "�Ѿ�������ͬ���Ƶķ��񣬸���ʧ��");
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
		choice = JOptionPane.showConfirmDialog(null, "�Ƿ�Ҫɾ����������\n�����ţ�" + storeId + "\n�������ƣ�" + storeName + "\n�������ͣ�" + storeTypeNameSelected + "\n���񵥼ۣ�" + storePrice + "\n����������" + storeDetail + "\n�����ϼ�״̬��" + storeState);
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
	public MaintainServiceInterFrm() {
		setResizable(true);
		setIconifiable(true);
		setMaximizable(true);
		setTitle("\u670D\u52A1\u4FE1\u606F\u7EF4\u62A4");
		setClosable(true);
		setBounds(100, 100, 868, 481);
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
				/*ʵ��ȫ�֡��������š����������ơ��������������ơ������񵥼ۡ�������Ʒ�ơ����������롢���ϼ�״̬��������ģ������*/
				String temp = jtf_searchBox.getText();//��ȡ�������ı�����
				boolean isTypeId = false;//��ʾ�����Ƿ������������
				boolean hasState = false;//hasState��ʾ�����������Ƿ��С�������״̬������,�������ֵΪtrue
				//�ж�����ѡ���Ƿ�Ϊ��
				if(!jrb_universe.isSelected() && !jrb_id.isSelected() && !jrb_name.isSelected() && !jrb_detail.isSelected() && (jcb_state.getSelectedIndex() == 0) && (jcb_typeName.getSelectedIndex() == 0)) {
					JOptionPane.showMessageDialog(null, "��ѡ���κ�һ��������ʽ");
					return;
				}
				
				//��ʼ������������
				Service service = new Service();
				service.setService_id(-1);
				service.setService_type_id(-1);
				service.setService_price(-1);
				
				/*���service_type_id�Ŀ���ֵ����String --> int��*/
				int[] tempTypeId = new int[100];
				Connection con = null;
				Service_type serviceType  = new Service_type();
				serviceType.setService_type_name(temp);
				ResultSet rs;
				try {
					con = dbUtil.getCon();
					rs = serviceTypeDao.serviceTypeList(con, serviceType, false);
					int index = -1;
					while(rs.next()) {
						index++;
						tempTypeId[index] = rs.getInt("service_type_id");
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
						service.setService_id(Integer.parseInt(temp));
					}
					
					service.setService_name(temp);
					service.setService_detail(temp);
					if("����������".indexOf(temp) != -1) {
						hasState = true;
						service.setService_state(true);
					}
					else if("����������".indexOf(temp) != -1) {
						hasState = true;
						service.setService_state(false);
					}
					if(tempTypeId[0] != 0) {
						isTypeId = true;
					}
					/*if(IntegerUtil.isNumeric(temp.replaceFirst(".", ""))) {
						service.setService_price(Float.parseFloat(temp));
					}*/
					
				}
				else if(jrb_id.isSelected()) {//ʵ�ְ�������������ģ����
					if(!IntegerUtil.isNumeric(temp)) {
						JOptionPane.showMessageDialog(null, "������ӦΪ�����֣�����������");
						return;
					}
					service.setService_id(Integer.parseInt(temp));
				}
				else if(jrb_name.isSelected()) {//ʵ�ְ���������������ģ����
					service.setService_name(temp);
				}
				else if(jrb_detail.isSelected()){//ʵ�ְ�����������ѯ
					service.setService_detail(temp);
				}
				/*else if(jrb_price.isSelected()) {
					if(!IntegerUtil.isNumeric(temp.replaceFirst(".", "0"))) {
						System.out.print(temp);
						System.out.print("|||" + temp.replace(".", ""));
						JOptionPane.showMessageDialog(null, "�۸��Ӧ���ԡ�__.xx������ʽ����");
						return;
					}
					service.setService_price(Float.valueOf(temp));
				}*/
				else if(jcb_state.getSelectedIndex() >= 1) {
					hasState = true;
					if(jcb_state.getSelectedItem().equals("����������")) {
						service.setService_state(true);
					}
					else if(jcb_state.getSelectedItem().equals("����������")) {
						service.setService_state(false);
					}
					else {
						JOptionPane.showMessageDialog(null, "δ֪����״̬����");
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
					fillTable(service, hasState, tempTypeId);
				}
				else {
					fillTable(service, hasState, null);
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
//				jrb_price.setSelected(false);
				jcb_state.setSelectedIndex(0);
				jtf_searchBox.setText("");
				jtf_searchBox.setEnabled(false);
				jtf_searchBox.setEditable(false);
			}
		});
		jcb_typeName.setModel(new DefaultComboBoxModel(new String[] {"\u6309\u7C7B\u522B\u641C\u7D22"}));
		jp_searchBarBottom.add(jcb_typeName);
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
//				jrb_price.setSelected(false);
				jcb_typeName.setSelectedIndex(0);
				jtf_searchBox.setText("");
				jtf_searchBox.setEnabled(false);
				jtf_searchBox.setEditable(false);
			}
		});
		
		jcb_state.setModel(new DefaultComboBoxModel(new String[] {"\u6309\u4E0A\u7EBF\u72B6\u6001\u641C\u7D22", "\u670D\u52A1\u4E0A\u7EBF\u4E2D", "\u670D\u52A1\u4E0B\u7EBF\u4E2D"}));
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
				{"1", null, null, null, null, null, null, null, null},
				{"", null, "", null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, ""},
				{null, null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null, null},
			},
			new String[] {
				"\u5E8F\u53F7", "\u7F16\u53F7", "\u540D\u79F0", "\u7C7B\u578B", "\u670D\u52A1\u4F63\u91D1", "\u8BE6\u60C5\u63CF\u8FF0", "\u4E0A\u7EBF\u72B6\u6001", "\u521B\u5EFA\u65E5\u671F", "\u4FEE\u6539\u65E5\u671F"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		jtb_table.getColumnModel().getColumn(0).setPreferredWidth(37);
		jtb_table.getColumnModel().getColumn(1).setPreferredWidth(48);
		jtb_table.getColumnModel().getColumn(2).setPreferredWidth(47);
		jtb_table.getColumnModel().getColumn(3).setPreferredWidth(47);
		jtb_table.getColumnModel().getColumn(4).setPreferredWidth(62);
		jtb_table.getColumnModel().getColumn(5).setPreferredWidth(82);
		jtb_table.getColumnModel().getColumn(7).setPreferredWidth(109);
		jtb_table.getColumnModel().getColumn(8).setPreferredWidth(100);
		jsp_table.setViewportView(jtb_table);//�������ʹ��ͷ�ɼ�
		
		JPanel jp_form = new JPanel();
		getContentPane().add(jp_form, BorderLayout.SOUTH);
		
		JPanel jp_id = new JPanel();
		jp_form.add(jp_id);
		
		JLabel label = new JLabel("\u670D\u52A1\u7F16\u53F7\uFF1A");
		label.setFont(new Font("����", Font.PLAIN, 15));
		jp_id.add(label);
		
		jtf_id = new JTextField();
		jtf_id.setEditable(false);
		jp_id.add(jtf_id);
		jtf_id.setColumns(10);
		
		JPanel jp_name = new JPanel();
		jp_form.add(jp_name);
		
		JLabel lblNewLabel_1 = new JLabel("\u670D\u52A1\u540D\u79F0\uFF1A");
		lblNewLabel_1.setFont(new Font("����", Font.PLAIN, 15));
		jp_name.add(lblNewLabel_1);
		
		jtf_name = new JTextField();
		jtf_name.setText("");
		jp_name.add(jtf_name);
		jtf_name.setColumns(10);
		
		JPanel jp_typeNameModify = new JPanel();
		jp_form.add(jp_typeNameModify);
		
		JLabel lblNewLabel_6 = new JLabel("\u670D\u52A1\u7C7B\u522B\uFF1A");
		lblNewLabel_6.setFont(new Font("����", Font.PLAIN, 15));
		jp_typeNameModify.add(lblNewLabel_6);
		
		jcb_typeNameModify = new JComboBox();
		jcb_typeNameModify.setModel(new DefaultComboBoxModel(new String[] {""}));
		jp_typeNameModify.add(jcb_typeNameModify);
		
		JPanel jp_price = new JPanel();
		jp_form.add(jp_price);
		
		JLabel lblNewLabel_3 = new JLabel("\u670D\u52A1\u4F63\u91D1\uFF1A");
		lblNewLabel_3.setFont(new Font("����", Font.PLAIN, 15));
		jp_price.add(lblNewLabel_3);
		
		jtf_price = new JTextField();
		jp_price.add(jtf_price);
		jtf_price.setColumns(10);
		
		JPanel jp_detail = new JPanel();
		jp_form.add(jp_detail);
		jp_detail.setLayout(new BorderLayout(0, 0));
		
		JLabel lblS = new JLabel("\u670D\u52A1\u63CF\u8FF0\uFF1A");
		lblS.setFont(new Font("����", Font.PLAIN, 15));
		jp_detail.add(lblS, BorderLayout.WEST);
		
		jta_detail = new JTextArea();
		jta_detail.setRows(2);
		jta_detail.setColumns(20);
		jp_detail.add(jta_detail, BorderLayout.CENTER);
		
		JPanel jp_state = new JPanel();
		jp_form.add(jp_state);
		
		JLabel lblNewLabel_5 = new JLabel("\u4E0A\u7EBF\u72B6\u6001\uFF1A");
		lblNewLabel_5.setFont(new Font("����", Font.PLAIN, 15));
		jp_state.add(lblNewLabel_5);
		
		jcb_stateModify = new JComboBox();
		jcb_stateModify.setModel(new DefaultComboBoxModel(new String[] {"", "\u670D\u52A1\u4E0A\u7EBF\u4E2D", "\u670D\u52A1\u4E0B\u7EBF\u4E2D"}));
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
				Service service = new Service();//ʵ��model��
				service.setService_id(storeId);//set�����б䶯������
				service.setService_name(jtf_name.getText());
				for(int i=0; i<storeTypeId.length; i++) {
					if(storeTypeName[i].equals(jcb_typeNameModify.getSelectedItem())) {
						service.setService_type_id(storeTypeId[i]);
						break;
					}
				}
				service.setService_price(Float.valueOf(jtf_price.getText()));
				service.setService_detail(jta_detail.getText());
				if(jcb_stateModify.getSelectedItem().equals("����������")) {
					service.setService_state(true);
				}
				else if(jcb_stateModify.getSelectedItem().equals("����������")) {
					service.setService_state(false);
				}
				
				try {
					con = dbUtil.getCon();
					int temp = serviceDao.updateService(con, service);
					if(temp == 1) {
						JOptionPane.showMessageDialog(null, "�÷��������Ϣ���³ɹ�");
						//ˢ�±�������г������ָܻ�֮ǰ�����������
						Service tempFill = new Service();
						tempFill.setService_id(-1);
						tempFill.setService_type_id(-1);
						tempFill.setService_price(-1);
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
				Service service = new Service();//newһ��ʵ����
				service.setService_id(storeId);//����ʵ����Ĳ�ѯ����
				try {
					con = dbUtil.getCon();
					int temp = serviceDao.deleteService(con, service);//����temp���ڽ����Ƿ�ɾ���ɹ�����Ϣ���������1��ɾ���ɹ�
					if(temp == 1) {
						JOptionPane.showMessageDialog(null, "������¼�ѱ��ɹ�ɾ��");
						//ˢ�±����ձ�,������storeXXX
						Service tempFill = new Service();
						tempFill.setService_id(-1);
						tempFill.setService_type_id(-1);
						tempFill.setService_price(-1);
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
		Service tempFill = new Service();
		tempFill.setService_id(-1);
		tempFill.setService_type_id(-1);
		tempFill.setService_price(-1);
		fillTable(tempFill, false, null);
		
		
	}

	

}
