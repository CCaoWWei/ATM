package program.atm;

import program.atm.UI.AdminUI;
import program.atm.UI.CustomerUI;
import program.atm.entity.ATM;
import program.atm.entity.CustAccount;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * atm机主界面.
 *
 */
public class ATM_UI extends JFrame implements ActionListener {

    /**
     * 用户模式按钮.
     */
    JButton userLogin;

    /**
     * 管理员模式按钮.
     */
    JButton adminLogin;


    /**
     * 初始化界面.
     */
	public ATM_UI() {
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 603, 490);
		getContentPane().add(panel);
		
		//设置背景图
		ImageIcon icon = new ImageIcon("src/image/atm.png");
		panel.setLayout(null);
		JLabel iconLabel = new JLabel(icon);

		iconLabel.setBounds(0, 0, 603, 451);
		panel.add(iconLabel);
		
		userLogin = new JButton("用户模式");
		userLogin.setBounds(313, 449, 290, 41);
		//监听事件
		userLogin.addActionListener(this);
        panel.add(userLogin);

        adminLogin = new JButton("管理员界面");
        adminLogin.setBounds(0, 449, 315, 41);
        //监听事件
        adminLogin.addActionListener(this);
		panel.add(adminLogin);

        // 设置窗口大小，屏幕居中
        setTitle("ATM机模拟程序");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().add(panel);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 获取屏幕大小
        Image icon1 = Toolkit.getDefaultToolkit().getImage("src/image/icon.png"); // 设置桌面图标
        this.setIconImage(icon1);
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        int x = screenWidth / 2 - 300;
        int y = screenHeight / 2 - 265;
        setBounds(x, y, 600, 530);
        this.setResizable(false);
		setVisible(true);
	}


    /**
     * 监听触发事件.
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("-----------"+e.getSource());
        JTextField number = new JTextField();
        JTextField password = new JTextField();
        //是否是管理员登录
        boolean isAdmin = false;
        //选择结果
        int option = 1;
        //管理员
        if (e.getSource()==adminLogin){
            Object[] message = {
                    "管理员账号:", number,
                    "密码:", password
            };
            isAdmin = true;
            //弹出输入框
            option = JOptionPane.showConfirmDialog(null, message, "管理员登录", JOptionPane.OK_CANCEL_OPTION);
            //用户
        }else if (e.getSource()==userLogin){
            Object[] message = {
                    "输入卡号:", number,
                    "密码:", password
            };
            //弹出输入框
            option = JOptionPane.showConfirmDialog(null, message, "请插入您的卡片", JOptionPane.OK_CANCEL_OPTION);
        }
        //是否点击了确定按钮
        if (JOptionPane.OK_OPTION == option){
            //获取用户名
            String num = number.getText();
            //获取密码
            String pwd = password.getText();

            //判断管理员登录
            if (isAdmin){
                if ("admin".equals(num)&&"admin".equals(pwd)){
                    //隐藏登录页面
                    JOptionPane.showMessageDialog(null, "登录成功","提示消息", JOptionPane.WARNING_MESSAGE);
                    setVisible(false);
                    new AdminUI();
                }else {
                    JOptionPane.showMessageDialog(null, "登录失败 (测试账号密码均为admin)", "提示消息", JOptionPane.WARNING_MESSAGE);
                }
            }else {
                //用户登录逻辑
                CustAccount account = null;
                try {
                    System.out.println(num);

                    account = ATM.loginATM(Integer.valueOf(num), pwd);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                if (account!=null){
                    //登录成功
                    setVisible(false);
                    new CustomerUI(account);
                }
            }
        }else {
            System.out.println("取消了输入...");
        }
    }

    public static void main(String[] args) {
        new ATM_UI();
    }
}
