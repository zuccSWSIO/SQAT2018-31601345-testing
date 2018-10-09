package com.zucc.chenfan.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.zucc.chenfan.dao.CustomerDao;
import com.zucc.chenfan.dao.PetDao;
import com.zucc.chenfan.model.Customer;
import com.zucc.chenfan.model.Pet;
import com.zucc.chenfan.util.DbUtil;
import com.zucc.chenfan.util.MyImageUtil;
import com.zucc.chenfan.util.StringUtil;

public class AddPetDialog extends JDialog {
	DbUtil dbUtil = new DbUtil();
	CustomerDao customerDao = new CustomerDao();
	PetDao petDao = new PetDao();
	MyImageUtil myImageUtil = new MyImageUtil();
	
	private final JPanel contentPanel = new JPanel();
	private JTextField jtf_customer;
	private JTextField jtf_imagePath;
	private JTextField jtf_name;
	private JComboBox jcb_species;
	private String imagePath;
	
	//storeXXX
	private int[] storeSpeciesId = new int[20];
	private String[] storeSpeciesName = new String[20];
	
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		try {
			AddPetDialog dialog = new AddPetDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	//�رմ���
	private void closeDialog() {
		this.dispose();
	}
	//���س������
	private void fillCB() throws Exception {
		jcb_species.removeAllItems();
		jcb_species.addItem("��ѡ�����Ʒ��");
		Connection con = null;
		con = dbUtil.getCon();
		String sql = "select * from pet_species";
		PreparedStatement pstmt = con.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		int index = -1;
		while(rs.next()) {
			index++;
			storeSpeciesId[index] = rs.getInt("pet_species_id");
			storeSpeciesName[index] = rs.getString("pet_species_name");
			jcb_species.addItem(rs.getString("pet_species_name"));
		}
		dbUtil.closeCon(con);
	}
	//������������
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
				JOptionPane.showMessageDialog(null, "ϵͳ����");
				closeDialog();
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "ϵͳ����");
			e.printStackTrace();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ϵͳ����");
			e.printStackTrace();
		}finally {
			try {
				dbUtil.closeCon(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	

	/**
	 * Create the dialog.
	 */
	public AddPetDialog( int customer_id ) {
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("\u65B0\u589E\u5BA0\u7269");
		setBounds(100, 100, 377, 263);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new LineBorder(Color.BLUE));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel label = new JLabel("\u5BA0\u7269\u6635\u79F0\uFF1A");
			label.setBounds(28, 23, 87, 15);
			contentPanel.add(label);
		}
		
		jtf_name = new JTextField();
		jtf_name.setBounds(143, 21, 80, 21);
		contentPanel.add(jtf_name);
		jtf_name.setColumns(10);
		{
			JLabel label = new JLabel("\u5BA0\u7269\u4E3B\u4EBA\uFF1A");
			label.setBounds(28, 60, 93, 15);
			contentPanel.add(label);
		}
		{
			jtf_customer = new JTextField();
			jtf_customer.setEditable(false);
			jtf_customer.setBounds(143, 58, 80, 21);
			contentPanel.add(jtf_customer);
			jtf_customer.setColumns(10);
		}
		{
			JLabel lblNewLabel = new JLabel("\u5BA0\u7269\u54C1\u79CD\uFF1A");
			lblNewLabel.setBounds(28, 98, 89, 15);
			contentPanel.add(lblNewLabel);
		}
		
		jcb_species = new JComboBox();
		jcb_species.setModel(new DefaultComboBoxModel(new String[] {"\u8BF7\u9009\u62E9\u5BA0\u7269\u54C1\u79CD"}));
		jcb_species.setBounds(143, 94, 125, 23);
		contentPanel.add(jcb_species);
		{
			JLabel label = new JLabel("\u5BA0\u7269\u7167\u7247\uFF1A");
			label.setBounds(28, 138, 105, 15);
			contentPanel.add(label);
		}
		{
			jtf_imagePath = new JTextField();
			jtf_imagePath.setEditable(false);
			jtf_imagePath.setBounds(143, 135, 125, 21);
			contentPanel.add(jtf_imagePath);
			jtf_imagePath.setColumns(10);
		}
		
		JButton jb_addImage = new JButton("\u9009\u62E9");
		jb_addImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//���ͼƬpath
				imagePath = myImageUtil.getPath();
				jtf_imagePath.setText(imagePath);
			}
		});
		jb_addImage.setBounds(278, 134, 59, 23);
		contentPanel.add(jb_addImage);
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
			{
				JPanel panel = new JPanel();
				buttonPane.add(panel);
				{
					JButton okButton = new JButton("\u65B0\u589E");
					okButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							/*���п�*/
							if(StringUtil.isEmpty(jtf_name.getText())) {
								JOptionPane.showMessageDialog(null, "����д�����ǳ�");
								return;
							}
							if(jcb_species.getSelectedIndex() <= 0) {
								JOptionPane.showMessageDialog(null, "��ѡ���������");
								return;
							}
							if(StringUtil.isEmpty(jtf_imagePath.getText())) {
								JOptionPane.showMessageDialog(null, "��ѡ�����ͼƬ");
								return;
							}
							
							
							//�ڼ�����ݿ�����ͬ�����������ظ��ĳ����س�
							Connection con = null;
							Pet temp = new Pet();
							temp.setCustomer_id(customer_id);
							temp.setPet_name(jtf_name.getText());
							temp.setPet_id(-1);
							try {
								con = dbUtil.getCon();
								boolean hasEqual = petDao.isEqual(con, temp);
								if(hasEqual) {
									JOptionPane.showMessageDialog(null, "�ù˿��Ѿ�������ͬ���Ƶĳ���");
									return;
								}
							} catch (SQLException e1) {
								e1.printStackTrace();
								JOptionPane.showMessageDialog(null, "ϵͳ����");
							} catch (Exception e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(null, "ϵͳ����");
							}finally {
								try {
									dbUtil.closeCon(con);
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
							
							//����������
							con = null;
							Pet pet = new Pet();
							pet.setCustomer_id(customer_id);
							pet.setPet_name(jtf_name.getText());
							for(int i=0; i<storeSpeciesId.length; i++) {
								if(jcb_species.getSelectedItem().equals(storeSpeciesName[i])) {
									pet.setPet_species_id(storeSpeciesId[i]);
									break;
								}
							}
							try {
								pet.setPet_image(myImageUtil.readImage(imagePath));
							} catch (IOException e) {
								JOptionPane.showMessageDialog(null, "ͼƬ�ڴ������ݿ�ʱ���ִ�������ʧ��");
								e.printStackTrace();
							}
							
							try {
								con = dbUtil.getCon();
								int result = petDao.addPet(con, pet);
								if(result != 1) {
									JOptionPane.showMessageDialog(null, "ϵͳ��������ʧ��");
									return;
								}
								else{
									JOptionPane.showMessageDialog(null, "������¼�ɹ�");
									closeDialog();
								}
								
							} catch (SQLException e) {
								JOptionPane.showMessageDialog(null, "ϵͳ��������ʧ��");
								e.printStackTrace();
							} catch (Exception e) {
								JOptionPane.showMessageDialog(null, "ϵͳ��������ʧ��");
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
					panel.add(okButton);
					okButton.setActionCommand("OK");
					getRootPane().setDefaultButton(okButton);
				}
			}
			{
				JPanel panel = new JPanel();
				buttonPane.add(panel);
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
		}
	
		//����jcb_species
		try {
			fillCB();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//������������
		fillName(customer_id);
	
	}
}
