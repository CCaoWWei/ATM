package program.atm.UI;

import program.atm.ATM_UI;
import program.atm.entity.CustAccount;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URLDecoder;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * ATM机后台管理员界面.
 *
 */
public class AdminUI extends JFrame {

    /**
     * 中间输出日志面版.
     */
    JTextArea textArea = new JTextArea();

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AdminUI() {

        //设置头部的图片
        ImageIcon img = new ImageIcon("src/image/head.png");
        JLabel headLabel = new JLabel(img);
        headLabel.setBounds(-2, 0, 584, 74);
        getContentPane().add(headLabel);

        //设置尾部的图片
        ImageIcon img2 = new ImageIcon("src/image/foot.png");
        JLabel footLabel = new JLabel(img2);
        footLabel.setLocation(0, 320);
        footLabel.setBounds(-2, 406, 584, 84);
        getContentPane().add(footLabel);

        //初始化中心面板功能
        initCenter();
        //初始化左右的按钮功能
        initBtn();

        // 设置窗口信息
        setTitle("ATM机模拟程序");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setForeground(Color.BLACK);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        //设置当前界面的信息，设置界面居中显示
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Image icon1 = Toolkit.getDefaultToolkit().getImage("src/image/icon.png"); // 设置桌面图标
        this.setIconImage(icon1);
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        int x = screenWidth / 2 - 300;
        int y = screenHeight / 2 - 265;
        setBounds(x, y, 588, 530);
        this.setResizable(false);
        setVisible(true);
    }

    /**
     * 初始化中间面板.
     */
    public void initCenter() {
        //中间输出日志面板
        textArea.setLineWrap(true);    //设置多行文本框自动换行
        textArea.setEditable(true);
        textArea.setForeground(Color.GREEN);
        textArea.setFont(new Font("黑体", Font.PLAIN, 15));
        textArea.setBackground(Color.BLACK);
        textArea.setBounds(120, 74, 344, 334);
        textArea.append("======================================================================\n");
        textArea.append("============================  ATM 机后台管理  =========================\n");
        textArea.append("======================================================================\n");
        textArea.append("【" + LocalDateTime.now().format(dateTimeFormatter) + "】 管理员登录成功...\n");
        JScrollPane scrollPane = new JScrollPane(textArea);
        //隐藏滚动条
//        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(-2, 74, 584, 278);
        getContentPane().add(scrollPane);

        //监听面板内容的变化
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            //监听面板被插入内容时
            @Override
            public void insertUpdate(DocumentEvent e) {
                //滚动条滚动到底部
                textArea.setCaretPosition(textArea.getText().length());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }

    /**
     * 初始化功能.
     */
    private void initBtn() {
        //左侧菜单栏
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(SystemColor.textInactiveText);
        leftPanel.setBounds(-2, 349, 584, 57);
        getContentPane().add(leftPanel);
        leftPanel.setLayout(null);

        //查询所有账户
        JButton searchAllAccount = new JButton("查询所有账户");
        searchAllAccount.setBounds(0, 0, 199, 56);
        //触发事件
        searchAllAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "正在输出记录...");

                Connection connection=null;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
//        2.用户信息和url
                String url = "jdbc:mysql://localhost:3306/atm";
                String username = "root";
                String password = "0000";
//        3.连接成功，数据库对象 Connection
                try {
                    connection = DriverManager.getConnection(url, username, password);
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }

                List<CustAccount> allAcount =new ArrayList<CustAccount>();
                String sql="select * from customeraccount";
                Statement stmt=null;
                ResultSet rs = null;

                try {

                stmt=connection.createStatement();
                rs = stmt.executeQuery(sql);
                CustAccount cur=new CustAccount();

                int row=0;
                while (rs.next())
                {
                    cur.id=rs.getInt(1);//第一列
                    cur.password=rs.getString(2);//第二列
                    cur.balance=rs.getFloat(3);
                    allAcount.add(cur);
                    row++;
                }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }




                //输出所有账户和其交易记录
                textArea.append("\n############################ 所有账户信息 ###########################\n");
                for (CustAccount account: allAcount) {
                    textArea.append("\n----------------账户["+account.id+"]--余额["+account.balance+"]-----------------\n");
                    List<String> operationRecod = account.getOperationRecod();
                    //判断交易记录是否为空
                    if (operationRecod.isEmpty()) {
                        textArea.append("       记录为空！\n");
                    } else {
                        for (String record : operationRecod) {
                            textArea.append(record + "\n");
                        }
                    }
//                    textArea.append("-------------------------------------------------------------------\n");
                }
                textArea.append("#####################################################################\n");
            }
        });
        leftPanel.add(searchAllAccount);

        //导出记录到文件
        JButton outToFiles = new JButton("导出账户记录");
        outToFiles.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                //获取所有账户
                Connection connection=null;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
//        2.用户信息和url
                String url = "jdbc:mysql://localhost:3306/atm";
                String username = "root";
                String password = "0000";
//        3.连接成功，数据库对象 Connection
                try {
                    connection = DriverManager.getConnection(url, username, password);
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }

                List<CustAccount> allAcount =new ArrayList<CustAccount>();
                String sql="select * from customeraccount";
                Statement stmt=null;
                ResultSet rs = null;

                try {

                    stmt=connection.createStatement();
                    rs = stmt.executeQuery(sql);
                    CustAccount cur=null;

                    int row=0;
                    while (rs.next())
                    {
                        cur.id=rs.getInt(1);//第一列
                        cur.password=rs.getString(2);//第二列
                        cur.balance=rs.getFloat(3);
                        allAcount.add(cur);
                        row++;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }



                //导出文件路径
                //获取当前jar所在路径
                String jarPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
                //避免中文情况，转码
                try {
                    jarPath = URLDecoder.decode(jarPath, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //拼接输出的目录
                String path = jarPath.substring(0, jarPath.lastIndexOf("/")) + "/AllAccount.txt";
                File file = new File(path);
                try {
                    BufferedWriter out = new BufferedWriter(new FileWriter(file));
                    out.write("\n############################ 所有账户信息 ###########################\n");
                    //遍历所有账户
                    for(CustAccount account:allAcount) {
                        out.write("\n----------------账户["+account.id+"]--余额["+account.balance+"]-----------------\n");
                        //获取账户记录
                        List<String> operationRecod = account.getOperationRecod();
                        //判断账户记录是否为空
                        if (operationRecod.isEmpty()){
                            out.write("账户记录为空！\r\n");
                        }else {
                            for (String record : operationRecod) {
                                out.write(record + "\r\n");
                            }
                        }
                    }
                    out.write("#####################################################################\n");
                    out.flush();
                    out.close();
                    JOptionPane.showMessageDialog(null, "文件成功导出至【"+path+"】目录下");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "文件导出异常", "错误消息", JOptionPane.WARNING_MESSAGE);
                    e.printStackTrace();
                }
            }
        });
        outToFiles.setBounds(194, 0, 199, 56);
        leftPanel.add(outToFiles);

        //退出
        JButton exitSystem = new JButton("退出系统");
        exitSystem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JOptionPane.showMessageDialog(null, "注销成功");
                setVisible(false);
                new ATM_UI();
            }
        });
        exitSystem.setBounds(390, 0, 194, 56);
        leftPanel.add(exitSystem);
    }

}
