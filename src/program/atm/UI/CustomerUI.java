package program.atm.UI;

import program.atm.ATM_UI;
import program.atm.entity.ATM;
import program.atm.entity.CustAccount;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 前台用户界面.
 *
 */
public class CustomerUI extends JFrame {

    /**
     * 当前登录账号信息.
     */
    private CustAccount currentAccount;

    /**
     * 中间输出日志面版.
     */
    JTextArea textArea = new JTextArea();

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CustomerUI(CustAccount custAccount) {

        //设置当前用户信息
        this.currentAccount = custAccount;
        System.out.println(currentAccount);

        //设置头部的图片
        ImageIcon img = new ImageIcon("src/image/head.png");
        JLabel headLabel = new JLabel(img);
        headLabel.setBounds(-2, 0, 584, 74);
        getContentPane().add(headLabel);

        //设置尾部的图片
        ImageIcon img2 = new ImageIcon("src/image/foot.png");
        JLabel footLabel = new JLabel(img2);
        footLabel.setLocation(0, 320);
        footLabel.setBounds(-2, 410, 584, 80);
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
        textArea.setFont(new Font("黑体", Font.PLAIN, 14));
        textArea.setBackground(Color.BLACK);
        textArea.setBounds(120, 74, 344, 334);
        textArea.append("================================================\n");
        textArea.append("============== 欢迎使用本 ATM 机 ===============\n");
        textArea.append("================================================\n");
        textArea.append("【" + LocalDateTime.now().format(dateTimeFormatter) + "】 登录成功...\n");
        JScrollPane scrollPane = new JScrollPane(textArea);
        //隐藏滚动条
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(120, 74, 344, 334);
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
        leftPanel.setBounds(-2, 75, 122, 334);
        getContentPane().add(leftPanel);
        leftPanel.setLayout(null);

        //查询余额功能
        JButton checkMoneyBtn = new JButton("查询余额");
        checkMoneyBtn.setBounds(0, 0, 122, 56);
        //触发事件
        checkMoneyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "您的余额为【" + currentAccount.balance + "】元", "提示消息", JOptionPane.WARNING_MESSAGE);
                textArea.append("【" + LocalDateTime.now().format(dateTimeFormatter) + "】您的余额为【" + currentAccount.balance + "】元\n");
            }
        });
        leftPanel.add(checkMoneyBtn);

        //atm取款功能
        JButton getMoneyBtn = new JButton("ATM取款");
        getMoneyBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                //取款触发事件
                JTextField getMoney = new JTextField();
                Object[] message = {
                        "输入取款金额:", getMoney
                };
                //弹出输入框
                int option = JOptionPane.showConfirmDialog(null, message, "ATM取款", JOptionPane.OK_CANCEL_OPTION);
                if (JOptionPane.OK_OPTION == option) {
                    String text = getMoney.getText();
                    Integer integer = Integer.valueOf(text);
                    boolean flag = false;
                    try {
                        flag = currentAccount.withdrawal(integer,1);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    List<String> operationRecod = currentAccount.getOperationRecod();
                    if (flag) {
                        //获取本账号最后一条交易记录
                        textArea.append(operationRecod.get(operationRecod.size() - 1) + "\n");
                    } else {
                        textArea.append("【" + LocalDateTime.now().format(dateTimeFormatter) + "】 取款失败.\n");
                    }
                }
            }
        });
        getMoneyBtn.setBounds(0, 92, 122, 56);
        leftPanel.add(getMoneyBtn);

        // atm存款功能
        JButton saveMoneyBtn = new JButton("ATM存款");
        saveMoneyBtn.setBounds(0, 184, 122, 56);
        saveMoneyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //存款触发事件
                JTextField getMoney = new JTextField();
                Object[] message = {
                        "输入存款金额:", getMoney
                };
                //弹出输入框
                int option = JOptionPane.showConfirmDialog(null, message, "ATM存款", JOptionPane.OK_CANCEL_OPTION);
                if (JOptionPane.OK_OPTION == option) {
                    String text = getMoney.getText();
                    Integer integer = Integer.valueOf(text);
                    boolean flag = false;
                    try {
                        flag = currentAccount.deposit(integer,1);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    if (flag) {
                        //获取本账号最后一条交易记录
                        List<String> operationRecod = currentAccount.getOperationRecod();
                        textArea.append(operationRecod.get(operationRecod.size() - 1) + "\n");
                    } else {
                        textArea.append("【" + LocalDateTime.now().format(dateTimeFormatter) + "】 存款失败..金额需为100的倍数\n");
                    }
                }
            }
        });
        leftPanel.add(saveMoneyBtn);

        //退卡功能
        JButton outToFile = new JButton("退卡");
        outToFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JOptionPane.showMessageDialog(null, "退卡成功");
                setVisible(false);
                new ATM_UI();
            }
        });
        outToFile.setBounds(0, 276, 122, 56);
        leftPanel.add(outToFile);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(null);
        rightPanel.setBackground(SystemColor.controlDkShadow);
        rightPanel.setBounds(463, 75, 122, 334);
        getContentPane().add(rightPanel);

        //修改密码功能
        JButton updatePwdBtn = new JButton("修改密码");
        updatePwdBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //修改密码触发事件
                JTextField oldPwdInput = new JTextField();
                JTextField newPwdInput = new JTextField();
                JTextField checkInput = new JTextField();
                Object[] message = {
                        "输入旧密码:", oldPwdInput,
                        "输入新密码:", newPwdInput,
                        "确认密码:", checkInput
                };
                //弹出输入框
                int option = JOptionPane.showConfirmDialog(null, message, "ATM密码修改", JOptionPane.OK_CANCEL_OPTION);
                if (JOptionPane.OK_OPTION == option) {
                    String oldPwd = oldPwdInput.getText();
                    String newPwd = newPwdInput.getText();
                    String check = checkInput.getText();
                    boolean flag = currentAccount.updatePassword(oldPwd, newPwd, check);
                    //判断修改是否成功
                    if (flag) {
                        textArea.append("【" + LocalDateTime.now().format(dateTimeFormatter) + "】 修改密码成功！\n");
                    } else {
                        textArea.append("【" + LocalDateTime.now().format(dateTimeFormatter) + "】 修改密码失败！\n");
                    }
                }
            }
        });
        updatePwdBtn.setBounds(0, 0, 122, 56);
        rightPanel.add(updatePwdBtn);

        //atm转账功能
        JButton transforBtn = new JButton("ATM转账");
        transforBtn.setBounds(0, 92, 122, 56);
        transforBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //修改密码触发事件
                JTextField targetAccount = new JTextField();
                JTextField moneyInput = new JTextField();
                Object[] message = {
                        "输入目标账号:", targetAccount,
                        "输入转账金额:", moneyInput
                };
                //弹出输入框
                int option = JOptionPane.showConfirmDialog(null, message, "ATM转账", JOptionPane.OK_CANCEL_OPTION);
                if (JOptionPane.OK_OPTION == option) {
                    String targetAccountText = targetAccount.getText();
                    String money = moneyInput.getText();
                    //调用转账功能
                    boolean flag = false;
                    try {
                        flag = ATM.transfer(currentAccount, Integer.valueOf(targetAccountText), Integer.valueOf(money));
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    //判断转账是否成功
                    if (flag) {
                        List<String> operationRecod = currentAccount.getOperationRecod();
                        textArea.append(operationRecod.get(operationRecod.size() - 1) + "\n");
                    } else {
                        textArea.append("【" + LocalDateTime.now().format(dateTimeFormatter) + "】 ATM转账给["+targetAccountText+"]失败！\n");
                    }
                }
            }
        });
        rightPanel.add(transforBtn);

        //查询记录功能
        JButton historyBtn = new JButton("查询记录");
        historyBtn.setBounds(0, 184, 122, 56);
        historyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "正在输出交易记录...");
                textArea.append("【" + LocalDateTime.now().format(dateTimeFormatter) + "】正在输出交易记录...\n");
                textArea.append("------------------交易记录----------------------\n");
                List<String> operationRecod = currentAccount.getOperationRecod();
                //判断交易记录是否为空
                if (operationRecod.isEmpty()){
                    textArea.append("       查询失败，记录为空！\n");
                }else {
                    for (String record: operationRecod){
                        textArea.append(record+"\n");
                    }
                }
                textArea.append("------------------------------------------------\n");
            }
        });
        rightPanel.add(historyBtn);

        // 导出交易记录功能
        JButton exitBtn = new JButton("导出记录");
        exitBtn.setBounds(0, 276, 122, 56);
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //调用导出记录到文件功能
                currentAccount.exportRecord();
            }
        });
        rightPanel.add(exitBtn);
    }

}
