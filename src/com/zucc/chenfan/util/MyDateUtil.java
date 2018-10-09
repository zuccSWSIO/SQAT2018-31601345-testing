package com.zucc.chenfan.util;

/**   
*    
* ��Ŀ���ƣ�PetServiceManagementSystem   
* �����ƣ�MyDateUtil   
* ��������   
* �����ˣ�Administrator   
* ����ʱ�䣺2018��9��8�� ����11:02:19   
* �޸��ˣ�Administrator   
* �޸�ʱ�䣺2018��9��8�� ����11:02:19   
* �޸ı�ע��   ��swing�����Դ�ڣ�https://blog.csdn.net/jianggujin/article/details/53544141
* @version    
*    
*/
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * ����ѡ�����ؼ�
 * 
 * @author jianggujin
 * 
 */
@SuppressWarnings("serial")
public final class MyDateUtil extends JDialog
{

   // ������ز���
   /**
    * ���
    */
   private int year = 0;
   /**
    * �·�
    */
   private int month = 0;
   /**
    * ��
    */
   private int date = 0;

   /**
    * ����ѡ�񱳾�ɫ
    */
   private Color selectColor = Color.green;
   /**
    * ���ڱ���ɫ
    */
   private Color dateColor = Color.white;
   /**
    * ���������뱳��ɫ
    */
   private Color dateHoverColor = Color.lightGray;
   /**
    * ���ڱ��ⱳ��ɫ
    */
   private Color dateTitleColor = Color.gray;
   /**
    * ���ڱ���������ɫ
    */
   private Color dateTitleFontColor = Color.black;
   /**
    * ����������ɫ
    */
   private Color dateFontColor = Color.black;

   /**
    * �����Ƿ���Ч��־
    */
   private boolean flag = false;

   /**
    * ��С���
    */
   private int minYear = 1900;
   /**
    * ������
    */
   private int maxYear = 2050;

   // �����������
   /**
    * ��һ��
    */
   private JButton jbYearPre;
   /**
    * ��һ��
    */
   private JButton jbYearNext;
   /**
    * ��һ��
    */
   private JButton jbMonthPre;
   /**
    * ��һ��
    */
   private JButton jbMonthNext;
   /**
    * �������ѡ���
    */
   private JComboBox<String> jcbYear;
   /**
    * �·�����ѡ���
    */
   private JComboBox<String> jcbMonth;
   /**
    * ���ǩ
    */
   private JLabel[][] jlDays;
   /**
    * ѡ��
    */
   private JButton jbChoose;
   /**
    * ����
    */
   private JButton jbToday;
   /**
    * ȡ��
    */
   private JButton jbCancel;

   /**
    * ����������
    * 
    * @param args
    *           �������
    */
   public static void main(String[] args)
   {
      try
      {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch (Exception e)
      {
      }
      MyDateUtil gg = new MyDateUtil();
      gg.showDateChooser();
      System.out.println(gg.getDateFormat("yyyy-MM-dd"));
   }

   /**
    * ��ʾ�Ի���
    */
   public void showDateChooser()
   {
      setVisible(true);
   }

   /**
    * �رնԻ���
    */
   public void closeDateChooser()
   {
      this.dispose();
   }

   /**
    * ����ʱ��
    * 
    * @param year
    *           ��� 1900-2050
    * @param month
    *           �·� 1-12
    * @param date
    *           ��
    */
   public void setDate(int year, int month, int date)
   {
      if (year >= minYear && year <= maxYear)
      {
         this.year = year;
      }
      else
      {
         return;
      }
      if (month >= 1 && month <= 12)
      {
         this.month = month;
      }
      else
      {
         return;
      }
      if (date > 0 && date <= getDaysInMonth(year, month))
      {
         this.date = date;
      }
      else
      {
         return;
      }
   }

   /**
    * ����û������Ƿ���Ч��־
    * 
    * @return �¼��Ƿ���Ч
    */
   public boolean getFlag()
   {
      return flag;
   }

   /**
    * ���췽��
    */
   public MyDateUtil()
   {
      initComponent();
      initComponentData();
      addComponent();
      addListener();
      setDialogAttribute();
   }

   /**
    * ʵ�������
    */
   private void initComponent()
   {
      jbYearPre = new JButton();
      jbYearNext = new JButton();
      jbMonthPre = new JButton();
      jbMonthNext = new JButton();
      jcbYear = new JComboBox<String>();
      jcbMonth = new JComboBox<String>();

      jlDays = new JLabel[7][7];

      jbChoose = new JButton();
      jbToday = new JButton();
      jbCancel = new JButton();
   }

   /**
    * ��ʼ���������
    */
   private void initComponentData()
   {
      jbYearPre.setText("��");
      jbYearNext.setText("��");
      jbMonthPre.setText("��");
      jbMonthNext.setText("��");
      Calendar calendar = Calendar.getInstance();
      if (year != 0 && month != 0 && date != 0)
      {
         calendar.set(year, month - 1, date);
      }
      else
      {
         year = calendar.get(Calendar.YEAR);
         month = calendar.get(Calendar.MONTH) + 1;
         date = calendar.get(Calendar.DAY_OF_MONTH);
      }
      initYear();
      jcbYear.setSelectedItem(year + "��");
      for (int i = 1; i <= 12; i++)
      {
         jcbMonth.addItem(i + "��");
      }
      jcbMonth.setSelectedItem(month + "��");
      for (int i = 0; i < 7; i++)
      {
         JLabel temp = new JLabel();
         temp.setHorizontalAlignment(JLabel.CENTER);
         temp.setVerticalAlignment(JLabel.CENTER);
         temp.setOpaque(true);
         temp.setBackground(dateTitleColor);
         temp.setForeground(dateTitleFontColor);
         jlDays[0][i] = temp;
      }
      for (int i = 1; i < 7; i++)
      {
         for (int j = 0; j < 7; j++)
         {
            JLabel temp = new JLabel();
            temp.setHorizontalAlignment(JLabel.CENTER);
            temp.setVerticalAlignment(JLabel.CENTER);
            temp.setOpaque(true);
            temp.setForeground(dateFontColor);
            jlDays[i][j] = temp;
         }
      }

      String[] days = { "��", "һ", "��", "��", "��", "��", "��" };
      for (int i = 0; i < 7; i++)
      {
         jlDays[0][i].setText(days[i]);
      }

      jbChoose.setText("ѡ��");
      jbToday.setText("����");
      jbCancel.setText("ȡ��");

      changeDate();
   }

   /**
    * ��ʼ����ʾ��ݷ�Χ
    */
   private void initYear()
   {
      jcbYear.removeAllItems();
      for (int i = minYear; i <= maxYear; i++)
      {
         jcbYear.addItem(i + "��");
      }
   }

   /**
    * �������
    */
   private void addComponent()
   {
      // ���ӱ������
      JPanel north = new JPanel();
      north.add(jbYearPre);
      north.add(jbMonthPre);
      north.add(jcbYear);
      north.add(jcbMonth);
      north.add(jbMonthNext);
      north.add(jbYearNext);
      this.add(north, "North");

      // �����м����
      JPanel center = new JPanel(new GridLayout(7, 7));
      for (int i = 0; i < 7; i++)
      {
         for (int j = 0; j < 7; j++)
         {
            center.add(jlDays[i][j]);
         }
      }
      this.add(center);

      // �����ϲ����
      JPanel jpSouth = new JPanel();
      jpSouth.add(jbChoose);
      jpSouth.add(jbToday);
      jpSouth.add(jbCancel);
      this.add(jpSouth, "South");
   }

   /**
    * ���ָ����ָ���·ݵ�����
    * 
    * @param year
    *           ���
    * @param month
    *           �·�
    * @return ����
    */
   private int getDaysInMonth(int year, int month)
   {
      switch (month)
      {
      case 1:
      case 3:
      case 5:
      case 7:
      case 8:
      case 10:
      case 12:
         return 31;
      case 4:
      case 6:
      case 9:
      case 11:
         return 30;
      case 2:
         if (isLeapYear(year))
         {
            return 29;
         }
         return 28;
      default:
         return 0;
      }
   }

   /**
    * �������
    */
   private void clearDate()
   {
      for (int i = 1; i < 7; i++)
      {
         for (int j = 0; j < 7; j++)
         {
            jlDays[i][j].setText("");
         }
      }
   }

   /**
    * ��������
    * 
    */
   private void changeDate()
   {
      refreshLabelColor();
      clearDate();
      Calendar calendar = Calendar.getInstance();
      calendar.set(year, month - 1, 1);
      int day_in_week = calendar.get(Calendar.DAY_OF_WEEK);
      int days = getDaysInMonth(year, month);
      if (date > days)
      {
         date = 1;
      }
      int temp = 0;
      for (int i = day_in_week - 1; i < 7; i++)
      {
         temp++;
         jlDays[1][i].setText(temp + "");
         if (temp == date)
         {
            jlDays[1][i].setBackground(selectColor);
         }
      }
      for (int i = 2; i < 7; i++)
      {
         for (int j = 0; j < 7; j++)
         {
            temp++;
            if (temp > days)
            {
               return;
            }
            jlDays[i][j].setText(temp + "");
            if (temp == date)
            {
               jlDays[i][j].setBackground(selectColor);
            }
         }
      }
   }

   /**
    * ���Ӽ���
    */
   private void addListener()
   {
      LabelMouseListener labelMouseListener = new LabelMouseListener();
      for (int i = 1; i < 7; i++)
      {
         for (int j = 0; j < 7; j++)
         {
            jlDays[i][j].addMouseListener(labelMouseListener);
         }
      }
      ButtonActionListener buttonActionListener = new ButtonActionListener();
      jbYearPre.addActionListener(buttonActionListener);
      jbYearNext.addActionListener(buttonActionListener);
      jbMonthPre.addActionListener(buttonActionListener);
      jbMonthNext.addActionListener(buttonActionListener);
      jbChoose.addActionListener(buttonActionListener);
      jbToday.addActionListener(buttonActionListener);
      jbCancel.addActionListener(buttonActionListener);

      ComboBoxItemListener comboBoxItemListener = new ComboBoxItemListener();
      jcbYear.addItemListener(comboBoxItemListener);
      jcbMonth.addItemListener(comboBoxItemListener);
   }

   /**
    * ������ݻ��·�
    * 
    * @param yearOrMonth
    *           ��ݻ��·��ַ���
    * @return ��ݻ��·�
    */
   private int parseYearOrMonth(String yearOrMonth)
   {
      return Integer.parseInt(yearOrMonth.substring(0, yearOrMonth.length() - 1));
   }

   /**
    * �ж��Ƿ�Ϊ����
    * 
    * @param year
    *           ���
    * @return true ����<br/>
    *         false ƽ��
    */
   private boolean isLeapYear(int year)
   {
      return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
   }

   /**
    * ���öԻ�������
    */
   private void setDialogAttribute()
   {
      this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      this.setSize(400, 300);
      this.setLocationRelativeTo(null);
      // ��ʾΪģ̬�Ի���
      this.setModal(true);
      this.setTitle("����ѡ����");
      this.setIconImage((new ImageIcon(this.getClass().getResource("/calendar.png"))).getImage());
   }

   /**
    * ˢ�����ڱ�ǩ������ɫ
    */
   private void refreshLabelColor()
   {
      for (int i = 1; i < 7; i++)
      {
         for (int j = 0; j < 7; j++)
         {
            jlDays[i][j].setBackground(dateColor);
         }
      }
   }

   /**
    * �����ʾ��С���
    * 
    * @return ��ʾ��С���
    */
   public int getMinYear()
   {
      return minYear;
   }

   /**
    * �����ʾ������
    * 
    * @return ��ʾ������
    */
   public int getMaxYear()
   {
      return maxYear;
   }

   /**
    * ������ʾ��С��ݺ�������(1900-9999)
    * 
    * @param minYear
    *           ��С���
    * @param maxYear
    *           ������
    */
   public void setMinAndMaxYear(int minYear, int maxYear)
   {
      if (minYear > maxYear || minYear < 1900 || maxYear > 9999)
      {
         return;
      }
      this.minYear = minYear;
      this.maxYear = maxYear;
      initYear();
   }

   /**
    * ���ѡ�б�����ɫ
    * 
    * @return ѡ�б�����ɫ
    */
   public Color getSelectColor()
   {
      return selectColor;
   }

   /**
    * ����ѡ�б�����ɫ
    * 
    * @param selectColor
    *           ѡ�б�����ɫ
    */
   public void setSelectColor(Color selectColor)
   {
      this.selectColor = selectColor;
   }

   /**
    * ������ڱ�����ɫ
    * 
    * @return ���ڱ�����ɫ
    */
   public Color getDateColor()
   {
      return dateColor;
   }

   /**
    * �������ڱ�����ɫ
    * 
    * @param dateColor
    *           ���ڱ�����ɫ
    */
   public void setDateColor(Color dateColor)
   {
      this.dateColor = dateColor;
   }

   /**
    * ������������뱳����ɫ
    * 
    * @return ���������뱳����ɫ
    */
   public Color getDetaHoverColor()
   {
      return dateHoverColor;
   }

   /**
    * �������������뱳����ɫ
    * 
    * @param dateHoverColor
    *           ���������뱳����ɫ
    */
   public void setDateHoverColor(Color dateHoverColor)
   {
      this.dateHoverColor = dateHoverColor;
   }

   /**
    * ������ڱ��ⱳ����ɫ
    * 
    * @return ���ڱ��ⱳ����ɫ
    */
   public Color getDateTitleColor()
   {
      return dateTitleColor;
   }

   /**
    * �������ڱ��ⱳ����ɫ
    * 
    * @param dateTitleColor
    *           ���ڱ��ⱳ����ɫ
    */
   public void setDateTitleColor(Color dateTitleColor)
   {
      this.dateTitleColor = dateTitleColor;
   }

   /**
    * ������ڱ���������ɫ
    * 
    * @return ���ڱ���������ɫ
    */
   public Color getDateTitleFontColor()
   {
      return dateTitleFontColor;
   }

   /**
    * �������ڱ���������ɫ
    * 
    * @param dateTitleFontColor
    *           ���ڱ���������ɫ
    */
   public void setDateTitleFontColor(Color dateTitleFontColor)
   {
      this.dateTitleFontColor = dateTitleFontColor;
   }

   /**
    * �������������ɫ
    * 
    * @return ����������ɫ
    */
   public Color getDateFontColor()
   {
      return dateFontColor;
   }

   /**
    * ��������������ɫ
    * 
    * @param dateFontColor
    *           ����������ɫ
    */
   public void setDateFontColor(Color dateFontColor)
   {
      this.dateFontColor = dateFontColor;
   }

   /**
    * ���ѡ�����
    * 
    * @return ѡ�����
    */
   public int getYear()
   {
      return year;
   }

   /**
    * ���ѡ���·�
    * 
    * @return ѡ���·�
    */
   public int getMonth()
   {
      return month;
   }

   /**
    * ���ѡ����Ϊ���µڼ���
    * 
    * @return ѡ����Ϊ���µڼ���
    */
   public int getDate()
   {
      return date;
   }

   /**
    * ���ѡ����Ϊһ���еڼ���
    * 
    * @return ѡ����Ϊһ���еڼ���
    */
   public int getDayOfWeek()
   {
      return getCalendar().get(Calendar.DAY_OF_WEEK);
   }

   /**
    * ���ѡ����Ϊһ���еڼ���
    * 
    * @return ѡ����Ϊһ���еڼ���
    */
   public int getDayOfYear()
   {
      return getCalendar().get(Calendar.DAY_OF_YEAR);
   }

   /**
    * ������ڶ���
    * 
    * @return ���ڶ���
    */
   public Date getDateObject()
   {
      return getCalendar().getTime();
   }

   /**
    * �����ָ�������ʽ���������ַ���
    * 
    * @param format
    *           ��ʽ������
    * @return �����ַ���
    */
   public String getDateFormat(String format)
   {
      return new SimpleDateFormat(format).format(getDateObject());
   }

   /**
    * ���Calendar����
    * 
    * @return Calendar����
    */
   private Calendar getCalendar()
   {
      Calendar calendar = Calendar.getInstance();
      calendar.set(year, month - 1, date);
      return calendar;
   }

   /**
    * ��ǩ������
    * 
    * @author jianggujin
    * 
    */
   final class LabelMouseListener extends MouseAdapter
   {

      @Override
      public void mouseClicked(MouseEvent e)
      {
         JLabel temp = (JLabel) e.getSource();
         if (!temp.getText().equals(""))
         {
            int date = Integer.parseInt(temp.getText());
            {
               if (date != MyDateUtil.this.date)
               {
                  MyDateUtil.this.date = date;
                  refreshLabelColor();
                  temp.setBackground(selectColor);
               }
            }
         }
      }

      @Override
      public void mouseEntered(MouseEvent e)
      {
         JLabel temp = (JLabel) e.getSource();
         if (!temp.getText().equals(""))
         {
            temp.setBackground(dateHoverColor);
         }
      }

      @Override
      public void mouseExited(MouseEvent e)
      {
         JLabel temp = (JLabel) e.getSource();
         if (!temp.getText().equals(""))
         {
            if (Integer.parseInt(temp.getText()) != date)
            {
               temp.setBackground(dateColor);
            }
            else
            {
               temp.setBackground(selectColor);
            }
         }
      }

   }

   /**
    * ��ť��������
    * 
    * @author jianggujin
    * 
    */
   final class ButtonActionListener implements ActionListener
   {

      public void actionPerformed(ActionEvent e)
      {
         if (e.getSource() == jbYearPre)
         {
            int select = jcbYear.getSelectedIndex();
            if (select > 0)
            {
               jcbYear.setSelectedIndex(select - 1);
            }
         }
         else if (e.getSource() == jbYearNext)
         {
            int select = jcbYear.getSelectedIndex();
            if (select < jcbYear.getItemCount() - 1)
            {
               jcbYear.setSelectedIndex(select + 1);
            }
         }
         else if (e.getSource() == jbMonthPre)
         {
            int select = jcbMonth.getSelectedIndex();
            if (select > 0)
            {
               jcbMonth.setSelectedIndex(select - 1);
            }
         }
         else if (e.getSource() == jbMonthNext)
         {
            int select = jcbMonth.getSelectedIndex();
            if (select < jcbMonth.getItemCount() - 1)
            {
               jcbMonth.setSelectedIndex(select + 1);
            }
         }
         else if (e.getSource() == jbChoose)
         {
            flag = true;
            closeDateChooser();
         }
         else if (e.getSource() == jbToday)
         {
            flag = true;
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            date = calendar.get(Calendar.DATE);
            closeDateChooser();
         }
         else if (e.getSource() == jbCancel)
         {
            flag = false;
            closeDateChooser();
         }
      }
   }

   /**
    * ����ѡ�����ı����
    * 
    * @author jianggujin
    * 
    */
   final class ComboBoxItemListener implements ItemListener
   {

      public void itemStateChanged(ItemEvent e)
      {
         if (e.getSource() == jcbYear)
         {
            int year = parseYearOrMonth(jcbYear.getSelectedItem().toString());
            if (year != MyDateUtil.this.year)
            {
               MyDateUtil.this.year = year;
               changeDate();
            }
         }
         else if (e.getSource() == jcbMonth)
         {
            int month = parseYearOrMonth(jcbMonth.getSelectedItem().toString());
            if (month != MyDateUtil.this.month)
            {
               MyDateUtil.this.month = month;
               changeDate();
            }
         }
      }
   }
}