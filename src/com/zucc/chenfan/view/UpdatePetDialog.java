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

public class UpdatePetDialog extends JDialog {
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
	private JTextField jtf_id;
	private int storeCustomerId;
	private Pet storePet = new Pet();
	
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
	//关闭窗口
	private void closeDialog() {
		this.dispose();
	}
	//填写表单
	private void fillForm(int petId) {
		//填写id
		jtf_id.setText(String.valueOf(petId));
		//搜索该宠物信息
		Connection con = null;
		Pet pet = new Pet();
		pet.setPet_id(petId);
		pet.setCustomer_id(-1);
		pet.setPet_species_id(-1);
		
		try {
			con = dbUtil.getCon();
			ResultSet rs = petDao.petList(con, pet, null, null);
			if(rs.next()) {
				//填写主人姓名
				fillName(rs.getInt("customer_id"));
				storeCustomerId = rs.getInt("customer_id");
				//填写昵称
				jtf_name.setText(rs.getString("pet_name"));
				//填写路径
				jtf_imagePath.setText("");
				//填写种类
				jcb_species.removeAllItems();
				jcb_species.addItem(getPetSpeciesName(rs.getInt("pet_species_id")));
				
				//获得storePet
				storePet.setPet_id(petId);
				storePet.setPet_name(rs.getString("pet_name"));
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
	
	// 获得种类名称
	private String getPetSpeciesName(int petSpeciesId) throws Exception {
		String result = null;

		jcb_species.removeAllItems();
		Connection con = null;
		con = dbUtil.getCon();
		String sql = "select pet_species_name from pet_species where pet_species_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, petSpeciesId);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			result = rs.getString("pet_species_name");
		}
		dbUtil.closeCon(con);

		return result;
	}
	
	
	//加载主人姓名
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
	
	

	/**
	 * Create the dialog.
	 */
	public UpdatePetDialog( int petId ) {
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("\u66F4\u65B0\u5BA0\u7269\u4FE1\u606F");
		setBounds(100, 100, 377, 299);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new LineBorder(Color.BLUE));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("\u5BA0\u7269\u7F16\u53F7\uFF1A");
		lblNewLabel_1.setBounds(26, 21, 88, 15);
		contentPanel.add(lblNewLabel_1);
		
		jtf_id = new JTextField();
		jtf_id.setEditable(false);
		jtf_id.setBounds(142, 20, 66, 21);
		contentPanel.add(jtf_id);
		jtf_id.setColumns(10);
		{
			JLabel label = new JLabel("\u5BA0\u7269\u6635\u79F0\uFF1A");
			label.setBounds(27, 56, 87, 15);
			contentPanel.add(label);
		}
		
		jtf_name = new JTextField();
		jtf_name.setBounds(142, 54, 80, 21);
		contentPanel.add(jtf_name);
		jtf_name.setColumns(10);
		{
			JLabel label = new JLabel("\u5BA0\u7269\u4E3B\u4EBA\uFF1A");
			label.setBounds(27, 93, 93, 15);
			contentPanel.add(label);
		}
		{
			jtf_customer = new JTextField();
			jtf_customer.setEditable(false);
			jtf_customer.setBounds(142, 91, 80, 21);
			contentPanel.add(jtf_customer);
			jtf_customer.setColumns(10);
		}
		{
			JLabel lblNewLabel = new JLabel("\u5BA0\u7269\u54C1\u79CD\uFF1A");
			lblNewLabel.setBounds(27, 131, 89, 15);
			contentPanel.add(lblNewLabel);
		}
		
		jcb_species = new JComboBox();
		jcb_species.setModel(new DefaultComboBoxModel(new String[] {"\u8BF7\u9009\u62E9\u5BA0\u7269\u54C1\u79CD"}));
		jcb_species.setBounds(142, 127, 125, 23);
		contentPanel.add(jcb_species);
		{
			JLabel label = new JLabel("\u5BA0\u7269\u7167\u7247\uFF1A");
			label.setBounds(27, 171, 105, 15);
			contentPanel.add(label);
		}
		{
			jtf_imagePath = new JTextField();
			jtf_imagePath.setEditable(false);
			jtf_imagePath.setBounds(142, 168, 125, 21);
			contentPanel.add(jtf_imagePath);
			jtf_imagePath.setColumns(10);
		}
		
		JButton jb_addImage = new JButton("\u9009\u62E9");
		jb_addImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//获得图片path
				imagePath = myImageUtil.getPath();
				jtf_imagePath.setText(imagePath);
			}
		});
		jb_addImage.setBounds(277, 167, 59, 23);
		contentPanel.add(jb_addImage);
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
			{
				JPanel panel = new JPanel();
				buttonPane.add(panel);
				{
					JButton okButton = new JButton("\u66F4\u65B0");
					okButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							/*①判空*/
							if(StringUtil.isEmpty(jtf_name.getText())) {
								JOptionPane.showMessageDialog(null, "请填写宠物昵称");
								return;
							}
							if(storePet.getPet_name().equals(jtf_name.getText()) && StringUtil.isEmpty(jtf_imagePath.getText())) {
								JOptionPane.showMessageDialog(null, "该宠物信息没有任何改动，无需更新");
								return;
							}
							
							//②检测数据库中相同的主人有无重复的宠物呢称
							Connection con = null;
							Pet temp = new Pet();
							temp.setCustomer_id(storeCustomerId);
							temp.setPet_name(jtf_name.getText());
							temp.setPet_id(petId);
							try {
								con = dbUtil.getCon();
								boolean hasEqual = petDao.isEqual(con, temp);
								if(hasEqual) {
									JOptionPane.showMessageDialog(null, "该顾客已经有了相同名称的宠物");
									return;
								}
							} catch (SQLException e1) {
								e1.printStackTrace();
								JOptionPane.showMessageDialog(null, "系统错误");
							} catch (Exception e) {
								e.printStackTrace();
								JOptionPane.showMessageDialog(null, "系统错误");
							}finally {
								try {
									dbUtil.closeCon(con);
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
							
							//③更新数据
							con = null;
							Pet pet = new Pet();
							pet.setCustomer_id(-1);
							pet.setPet_species_id(-1);
							pet.setPet_id(petId);
							if(!StringUtil.isEmpty(jtf_imagePath.getText())) {
								try {
									pet.setPet_image(MyImageUtil.readImage(imagePath));
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							pet.setPet_name(jtf_name.getText());
							
							try {
								con = dbUtil.getCon();
								int result = petDao.updatePet(con, pet);
								if(result != 1) {
									JOptionPane.showMessageDialog(null, "系统错误，更新失败");
									return;
								}else {
									JOptionPane.showMessageDialog(null, "更新成功");
									closeDialog();
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
	
		//填充表单	
		fillForm(petId);
	
	
	}
}
