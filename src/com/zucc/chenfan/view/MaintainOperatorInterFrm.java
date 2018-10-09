package com.zucc.chenfan.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.zucc.chenfan.dao.OperatorDao;
import com.zucc.chenfan.model.Operator;
import com.zucc.chenfan.util.DbUtil;
import com.zucc.chenfan.util.IntegerUtil;
import com.zucc.chenfan.util.StringUtil;
import javax.swing.ListSelectionModel;
import javax.swing.JRadioButton;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class MaintainOperatorInterFrm extends JInternalFrame {
	DbUtil dbUtil = new DbUtil();
	OperatorDao operatorDao = new OperatorDao();
	
	private JTable jtb_table;
	private JTextField jtf_searchBox;
	private JTextField jtf_id;
	private JTextField jtf_name;
	private JTextField jtf_pwd;
	private JComboBox jcb_group;
	JComboBox jcb_searchGroup;
	JRadioButton jrb_universe;
	JRadioButton jrb_id;
	JRadioButton jrb_name;
	
	/*���±������ڱ�������ݿ���ȡ����Ϣ�������ڱ����º�����ʱʹ��*/
	private int storeID = 0; 
	private String storeGroup = null;
	private String storePwd = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MaintainOperatorInterFrm frame = new MaintainOperatorInterFrm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void fillForm() {
		/*��ȡѡ�е���*/
		int row = jtb_table.getSelectedRow();
		jtf_id.setText((String) jtb_table.getValueAt(row, 1));
		jtf_name.setText((String) jtb_table.getValueAt(row, 2));
		/*�ж��Ƿ��ǵ곤*/
		String temp = (String) jtb_table.getValueAt(row, 3);
		if(temp.equals("�곤")) {//����ǵ곤������jcb_groupΪ������
			jcb_group.setSelectedIndex(0);
			jcb_group.setEnabled(false);
		}
		else {//������ǵ곤������jcb_groupΪ����
			jcb_group.setEnabled(true);
			jcb_group.setSelectedItem(temp);
		}
	
		jtf_pwd.setText((String) jtb_table.getValueAt(row, 4));
		/*�����õ���Ϣ�������޸ĺ�����;*/
		storeID = Integer.parseInt(jtf_id.getText());//����ѡ�еĲ���ԱID������������������ʹ��
		if(temp.equals("�곤")) {
			storeGroup = "�곤";
		}
		else {
			storeGroup = (String) jcb_group.getSelectedItem();
		}
		storePwd = jtf_pwd.getText();
	}
	
	
	
	/*�����������е�JRadioButton��Selected����Ϊfalse,������������jtf_searchBox*/
	private void resetSearch() {
		jrb_universe.setSelected(false);
		jrb_id.setSelected(false);
		jrb_name.setSelected(false);
		jcb_searchGroup.setSelectedIndex(0);
		jtf_searchBox.setEditable(true);
		jtf_searchBox.setEnabled(true);
	}
	
	
	/*�������ñ�*/
	private void resetForm() {
		jcb_group.setSelectedItem(storeGroup);
		jtf_pwd.setText(storePwd);
	}
	
	

	/*���������*/
	private void fillTable(Operator operator, boolean hasId) {//hasId�������OperatorDao.operatorList������ȫ����ȡ����ʱֵΪfalse�����ڰ�������ѯ��Ӧ����ѡ��ľ�������趨����ֵ
		DefaultTableModel dtm = (DefaultTableModel) jtb_table.getModel();
		dtm.setRowCount(0);//���ñ������Ϊ�㣬�����´β�ѯǰ��ձ��
		/*������storeXXX������ֵΪ��,�������е��ı�������Ϊ��*/
		storeID = 0;
		storeGroup = null;
		storePwd = null;
		jtf_id.setText("");
		jtf_name.setText("");
		jcb_group.setEnabled(true);
		jcb_group.setEditable(true);
		jcb_group.setSelectedIndex(0);
		jtf_name.setText("");
		jtf_pwd.setText("");
		
		Connection con = null;
		try {
			con = dbUtil.getCon();
			ResultSet rs = operatorDao.operatorList(con, operator,hasId);
			
			int i = 0;//���ڼ���ÿһ������
			while(rs.next()) {
				i++;
				/*Vector��ArrayList������Vector���̰߳��ŵ�*/
				Vector v = new Vector();
				v.add(i);
				/*����Vector����������ͳһת��ΪString,�����ڱ���е����ȡ��ֵ�����ᱨ��*/
				v.add(rs.getString("operator_id"));
				v.add(rs.getString("operator_name"));
				v.add(rs.getString("operator_group"));
				v.add(rs.getString("password"));
				v.add(rs.getString("operator_createdate"));
				v.add(rs.getString("operator_modifydate"));
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
	
	
	/**
	 * Create the frame.
	 */
	public MaintainOperatorInterFrm() {
		
		
		
		setIconifiable(true);
		setClosable(true);
		setResizable(true);
		setMaximizable(true);
		setTitle("\u5458\u5DE5\u4FE1\u606F\u7EF4\u62A4");
		getContentPane().setBackground(Color.CYAN);
		setBounds(100, 100, 807, 570);
		getContentPane().setLayout(new BorderLayout(0, 5));
		
		JScrollPane jsp_table = new JScrollPane();
		getContentPane().add(jsp_table, BorderLayout.CENTER);
		
		jtb_table = new JTable();
		
		jtb_table.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				/*java������ж������������Ϲ��������¹�*/
				int beforeSelection = jtb_table.getSelectedRow();
				int afterSelection = beforeSelection;
				if(arg0.getWheelRotation() == 1) {
					afterSelection ++;
					if(afterSelection >= (jtb_table.getRowCount()) ) {
						afterSelection = jtb_table.getRowCount()-1;
					}
				}
				else if(arg0.getWheelRotation() == -1){
					/*����index��0��ʼ*/
					afterSelection --;
					if(afterSelection <= -1) {
						afterSelection = 0;
					}
				}
				jtb_table.setRowSelectionInterval(afterSelection, afterSelection);
				fillForm();
			}
		});
		jtb_table.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				/*���ǣ�����±��*/
				fillForm();
			}
		});
		jtb_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jtb_table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
			
				fillForm();
			}
		});
		jtb_table.setBorder(new LineBorder(Color.RED, 2, true));
		jtb_table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null},
			},
			new String[] {
				"\u5E8F\u53F7", "\u5E97\u5458\u7F16\u53F7", "\u5E97\u5458\u59D3\u540D", "\u5E97\u5458\u804C\u52A1", "\u767B\u9646\u5BC6\u7801", "\u521B\u5EFA\u65E5\u671F", "\u6700\u540E\u4FEE\u6539\u65E5\u671F"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		jtb_table.getColumnModel().getColumn(0).setResizable(false);
		jtb_table.getColumnModel().getColumn(0).setPreferredWidth(55);
		jtb_table.getColumnModel().getColumn(1).setResizable(false);
		jtb_table.getColumnModel().getColumn(2).setResizable(false);
		jtb_table.getColumnModel().getColumn(3).setResizable(false);
		jtb_table.getColumnModel().getColumn(4).setResizable(false);
		jtb_table.getColumnModel().getColumn(5).setResizable(false);
		jtb_table.getColumnModel().getColumn(5).setPreferredWidth(100);
		jtb_table.getColumnModel().getColumn(6).setResizable(false);
		jtb_table.getColumnModel().getColumn(6).setPreferredWidth(100);
		jsp_table.setViewportView(jtb_table);//�������ʹ��ͷ�ɼ�
		
		JPanel jp_form = new JPanel();
		getContentPane().add(jp_form, BorderLayout.SOUTH);
		
		JPanel jp_id = new JPanel();
		jp_id.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblNewLabel = new JLabel("\u5E97\u5458\u7F16\u53F7\uFF1A");
		lblNewLabel.setFont(new Font("����", Font.PLAIN, 15));
		jp_id.add(lblNewLabel);
		
		jtf_id = new JTextField();
		jtf_id.setEditable(false);
		jp_id.add(jtf_id);
		jtf_id.setColumns(10);
		
		JPanel jp_name = new JPanel();
		
		JLabel lblNewLabel_1 = new JLabel("\u5E97\u5458\u59D3\u540D\uFF1A");
		lblNewLabel_1.setFont(new Font("����", Font.PLAIN, 15));
		jp_name.add(lblNewLabel_1);
		
		jtf_name = new JTextField();
		jtf_name.setEditable(false);
		jp_name.add(jtf_name);
		jtf_name.setColumns(10);
		jp_form.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 2));
		jp_form.add(jp_id);
		jp_form.add(jp_name);
		
		JPanel jp_group = new JPanel();
		jp_form.add(jp_group);
		
		JLabel lblNewLabel_2 = new JLabel("\u5E97\u5458\u804C\u52A1\uFF1A");
		lblNewLabel_2.setFont(new Font("����", Font.PLAIN, 15));
		jp_group.add(lblNewLabel_2);
		
		jcb_group = new JComboBox();
		jcb_group.setModel(new DefaultComboBoxModel(new String[] {"", "\u6536\u94F6\u5458", "\u6253\u5305\u5458", "\u9001\u8D27\u5458", "\u670D\u52A1\u5458"}));
		jp_group.add(jcb_group);
		
		JPanel jp_pwd = new JPanel();
		jp_form.add(jp_pwd);
		
		JLabel lblNewLabel_3 = new JLabel("\u767B\u5F55\u5BC6\u7801\uFF1A");
		lblNewLabel_3.setFont(new Font("����", Font.PLAIN, 15));
		jp_pwd.add(lblNewLabel_3);
		
		jtf_pwd = new JTextField();
		jp_pwd.add(jtf_pwd);
		jtf_pwd.setColumns(10);
		
		JPanel jp_button = new JPanel();
		jp_form.add(jp_button);
		
		JButton jb_update = new JButton("\u66F4\u65B0");
		jb_update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*���µ�������Ա��¼�Ĺ���*/
				if(StringUtil.isEmpty(storePwd)) {
					JOptionPane.showMessageDialog(null, "��ѡ��Ҫ���µ�Ա����¼");
					return;
				}
				/*�жϱ���û��ʵ���ϵ����ݸ���*/
				boolean isChange = false;
				
				String group1 = (String)jcb_group.getSelectedItem();
				String group2 = storeGroup;
				String pwd1 = jtf_pwd.getText();
				String pwd2 = storePwd;
				
				if(!(((String)jcb_group.getSelectedItem()).equals(storeGroup)&&jtf_pwd.getText().equals(storePwd))) {
					isChange = true;
				}
				if(storeGroup.equals("�곤")&&jtf_pwd.getText().equals(storePwd)) {//�ж��Ƿ�Ϊ�곤������ǣ��򲻸ı�
					isChange = false;
				}
				if(StringUtil.isEmpty((String) jcb_group.getSelectedItem())) {
					JOptionPane.showMessageDialog(null, "����дԱ��ְ��");
					return;
				}
				
				if(!isChange) {
					JOptionPane.showMessageDialog(null, "����Ա����Ϣû���κ��޸�");
					return;
				}
				
				int choice = -1;
				if(storeGroup.equals("�곤")) {//�ж��Ƿ�Ϊ�곤
					choice = JOptionPane.showConfirmDialog(null, "�ƹ�ģ�ȷ�����޸�����������\n�������룺" + storePwd +" ����> " + jtf_pwd.getText());
				}
				else {
					choice = JOptionPane.showConfirmDialog(null, "ȷ��Ҫ��������Ա����Ϣ��\nԱ�����:" + storeID + "    Ա��������" + jtf_name.getText() + "\nԱ��ְ��" + storeGroup + " ����> " + jcb_group.getSelectedItem() + "\n��¼���룺" + storePwd + " ����> " + jtf_pwd.getText());
				}
				if(choice == 0) {//�û�ȷ��Ҫ���¼�¼
					Connection con = null;
					Operator operator  = new Operator();
					operator.setOperator_id(storeID);
					if(storeGroup.equals("�곤")) {
						operator.setOperator_group("�곤");
					}
					else {
						operator.setOperator_group((String) jcb_group.getSelectedItem());
					}
					operator.setPassword(jtf_pwd.getText());
				
					try {
						con = dbUtil.getCon();
						int temp = operatorDao.updateOperator(con, operator);
						if(temp == 1) {
							JOptionPane.showMessageDialog(null, "��Ա����Ϣ���³ɹ�");
							/*ˢ�±�������г������ָܻ�֮ǰ�����������*/
							fillTable(new Operator(), false);
							/*����storeGroup��storePwd*/
							storeID = Integer.parseInt(jtf_id.getText());
							storeGroup = (String) jcb_group.getSelectedItem();
							storePwd = jtf_pwd.getText();
							
						}
						else {
							JOptionPane.showMessageDialog(null, "ϵͳ���󣬸���ʧ��");
							/*��������*/
							resetForm();
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "ϵͳ���󣬸���ʧ��");
						/*��������*/
						resetForm();
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "ϵͳ���󣬸���ʧ��");
						/*��������*/
						resetForm();
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
		jb_update.setFont(new Font("����", Font.BOLD, 16));
		jp_button.add(jb_update);
		
		JButton jb_delete = new JButton("\u5220\u9664");
		jb_delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*ɾ����������Ա��¼�Ĺ���*/
				String id = jtf_id.getText();
				if(StringUtil.isEmpty(id)) {
					JOptionPane.showMessageDialog(null, "��ѡ��Ҫɾ����Ա����¼");
					return;
				}
				int choice = JOptionPane.showConfirmDialog(null, "ȷ��Ҫɾ��������¼��\nԱ�����:" + storeID + "    Ա��������" + jtf_name.getText() + "\nԱ��ְλ��" + storeGroup);
				if(choice == 0) {//�û�ȷ��Ҫɾ��
					Connection con = null;
					Operator operator = new Operator();
					operator.setOperator_id(Integer.parseInt(jtf_id.getText()));
					try {
						con = dbUtil.getCon();
						int temp = operatorDao.deleteOperator(con, operator);//����temp���ڽ����Ƿ�ɾ���ɹ�����Ϣ���������1��ɾ���ɹ�
						if(temp == 1) {
							JOptionPane.showMessageDialog(null, "��Ա���ѱ��ɹ�ɾ��");
							/*ˢ�±����ձ�*/
							fillTable(new Operator(),false);
							jtf_id.setText("");
							jtf_name.setText("");
							jcb_group.setSelectedIndex(0);
							jtf_pwd.setText("");
						}
						else {
							JOptionPane.showMessageDialog(null, "ϵͳ����ɾ��ʧ��");
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, "ϵͳ����ɾ��ʧ��");
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
		jb_delete.setFont(new Font("����", Font.BOLD, 16));
		jp_button.add(jb_delete);
		
		JButton jb_reset = new JButton("\u91CD\u7F6E");
		jb_reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*������Ϣ����*/
				resetForm();
			}
		});
		jb_reset.setFont(new Font("����", Font.BOLD, 16));
		jp_button.add(jb_reset);
		
		/*ʹ��borderLayout���ַ������޸Ķ����ϱ��и��������С�ķ���*/
		jp_form.setPreferredSize(new Dimension(0,80));
		
		JPanel jp_searchBar = new JPanel();
		
		getContentPane().add(jp_searchBar, BorderLayout.NORTH);
		jp_searchBar.setLayout(new BorderLayout(0, 0));
		
		JPanel jp_searchBarTop = new JPanel();
		jp_searchBar.add(jp_searchBarTop, BorderLayout.NORTH);
		jp_searchBarTop.setToolTipText("");
		jp_searchBarTop.setLayout(new BorderLayout(5, 5));
		
		JLabel label = new JLabel("\u8F93\u5165\u641C\u7D22\u5185\u5BB9\uFF1A");
		label.setFont(new Font("����", Font.PLAIN, 15));
		jp_searchBarTop.add(label, BorderLayout.WEST);
		
		jtf_searchBox = new JTextField();
		jp_searchBarTop.add(jtf_searchBox, BorderLayout.CENTER);
		jtf_searchBox.setColumns(10);
		
		JButton jb_search = new JButton("\u641C\u7D22");
		jb_search.setFont(new Font("����", Font.BOLD, 16));
		jb_search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*��ʱ������ģ������*/
//				String operatorName = jtx_searchBox.getText();
//				Operator operator = new Operator();
//				operator.setOperator_name(operatorName);
//				fillTable(operator);
				
				/*ʵ��ȫ�֡���Ա����š���Ա����������Ա��ְ��ģ������*/
				String temp = jtf_searchBox.getText();//��ȡ�������ı�����
				//�ж�����ѡ���Ƿ�Ϊ��
				if(StringUtil.isEmpty((String) jcb_searchGroup.getSelectedItem())&&!jrb_universe.isSelected()&&!jrb_id.isSelected()&&!jrb_name.isSelected()) {
					JOptionPane.showMessageDialog(null, "��ѡ���κ�һ��������ʽ");
					return;
				}
				
				/*��ʼ������������*/
				Operator operator = new Operator();
				boolean hasId = false;//hasId��ʾ�����������Ƿ��С���ID������,�������ֵΪtrue
				if(StringUtil.isEmpty(temp) && (jcb_searchGroup.getSelectedIndex() == 0) ) {
					//�����������Ϊ����ְ��ѡ��Ϊ��ʱ����ȡ��������
				}
				else if(jrb_universe.isSelected()) {//ʵ��ȫ������
					if(IntegerUtil.isNumeric(temp)) {
						operator.setOperator_id(Integer.parseInt(temp));
						hasId = true;
					}
					operator.setOperator_name(temp);
					operator.setOperator_group(temp);
				}
				else if(jrb_id.isSelected()) {//ʵ�ְ�����Ա�����������ȷ��
					if(!IntegerUtil.isNumeric(temp)) {
						JOptionPane.showMessageDialog(null, "����Ա���ӦΪ�����֣�����������");
						return;
					}
					operator.setOperator_id(Integer.parseInt(temp));
					hasId = true;
				}
				else if(jrb_name.isSelected()) {//ʵ�ְ�����Ա����������ģ����
					operator.setOperator_name(temp);
				}
				else if(jcb_searchGroup.getSelectedIndex() != 0){//ʵ��ְ������ѯ
					operator.setOperator_group((String) jcb_searchGroup.getSelectedItem());
				}
				
				//ִ������
				fillTable(operator, hasId);
				
				
			}
		});
		jp_searchBarTop.add(jb_search, BorderLayout.EAST);
		jp_searchBarTop.setPreferredSize(new Dimension(0,30));
		
		JPanel jp_searchBarBottom = new JPanel();
		
		
		jp_searchBar.add(jp_searchBarBottom, BorderLayout.SOUTH);
		
		JPanel panel_1 = new JPanel();
		jp_searchBarBottom.add(panel_1);
		
		jrb_universe = new JRadioButton("\u5168\u5C40\u641C\u7D22");
		jrb_universe.setFont(new Font("����", Font.PLAIN, 13));
		jrb_universe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				ʵ�ֵ��������JRadioButton�����selected��������Ϊfalse;
				resetSearch();
				jrb_universe.setSelected(true);
				
			}
		});
		jrb_universe.setSelected(true);
		panel_1.add(jrb_universe);
		
		JPanel panel_2 = new JPanel();
		jp_searchBarBottom.add(panel_2);
		
		jrb_id = new JRadioButton("\u6309\u5458\u5DE5\u7F16\u53F7");
		jrb_id.setFont(new Font("����", Font.PLAIN, 13));
		jrb_id.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				ʵ�ֵ��������JRadioButton�����selected��������Ϊfalse;
				resetSearch();
				jrb_id.setSelected(true);
			}
		});
		panel_2.add(jrb_id);
		
		JPanel panel_3 = new JPanel();
		jp_searchBarBottom.add(panel_3);
		
		jrb_name = new JRadioButton("\u6309\u5458\u5DE5\u59D3\u540D");
		jrb_name.setFont(new Font("����", Font.PLAIN, 13));
		jrb_name.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				ʵ�ֵ��������JRadioButton�����selected��������Ϊfalse;
				resetSearch();
				jrb_name.setSelected(true);
			}
		});
		panel_3.add(jrb_name);
		
		JPanel panel = new JPanel();
		jp_searchBarBottom.add(panel);
		
		jcb_searchGroup = new JComboBox();
		jcb_searchGroup.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				/*ʵ�ְ�ѹ��JComBox����Ŀʱ������JRadioButton��selected��������Ϊfalse��������������Ч��*/
				jrb_universe.setSelected(false);
				jrb_id.setSelected(false);
				jrb_name.setSelected(false);
				jtf_searchBox.setText("");
				jtf_searchBox.setEditable(false);
				jtf_searchBox.setEnabled(false);
			}
		});
		jcb_searchGroup.setModel(new DefaultComboBoxModel(new String[] {"", "\u6536\u94F6\u5458", "\u6253\u5305\u5458", "\u9001\u8D27\u5458", "\u670D\u52A1\u5458"}));
		panel.add(jcb_searchGroup);
		
		JLabel lblNewLabel_4 = new JLabel("\u6309\u5458\u5DE5\u804C\u4F4D");
		panel.add(lblNewLabel_4);
		lblNewLabel_4.setFont(new Font("����", Font.PLAIN, 13));

		/*ʹ�÷�����fillTable������������*/
		fillTable(new Operator(), false);
		
		/*ʹ��ͷ�����϶�*/
		JTableHeader tableHeader = jtb_table.getTableHeader();
		tableHeader.setReorderingAllowed(false);
		
	}
}
