package program.atm.entity;

import javax.swing.*;
import java.io.*;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 银行卡操作类.
 *
 */
public class CustAccount extends Account {


    public CustAccount() {
    }
    /**
     *
     * @param id 账号
     * @param password 密码
     * @param balance 余额
     */
    public CustAccount(int id, String password, float balance) {
        super(id, password, balance);
    }

    public void updateaccount(int id,float new_balance) throws SQLException {
        Connection connection=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        2.用户信息和url
        String url = "jdbc:mysql://localhost:3306/atm";
        String username = "root";
        String password = "0000";
//        3.连接成功，数据库对象 Connection
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql="update customeraccount set balance=? where ID=? ";

        PreparedStatement ps=connection.prepareStatement(sql);
        ps.setInt(2,id);
        ps.setFloat(1,new_balance);
        ps.execute();
    }
    /**
     * 存款.
     * 要求存钱为100的整数倍
     *
     * @param money
     * @return
     */
    public boolean deposit(int money,int flag) throws SQLException {
        if (money % 100 != 0) {
            JOptionPane.showMessageDialog(null, "存款失败！存款数目需为100得整数倍", "提示消息", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        //余额
        this.balance= this.balance+money;


        updateaccount(this.id,this.balance);



        System.out.println(this.balance);
        //日期格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //记录交易记录
        if(flag==1) {
            List<String> operationRecod = getOperationRecod();
            operationRecod.add("【" + LocalDateTime.now().format(dateTimeFormatter) + "】 ATM 存款 [" + money + "]元，当前余额[" + this.balance + "]元");

            JOptionPane.showMessageDialog(null, "存款成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
        }
        return true;
    }

    /**
     * 取款.
     * 每次取款金额为100的倍数，
     * 总额不超过50000元，
     * 支取金额不允许透支
     */
    public boolean withdrawal(int money,int flag) throws SQLException {
        if (money % 100 != 0) {
            JOptionPane.showMessageDialog(null, "取款失败！取款数目需为100得整数倍", "提示消息", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (money > 50000) {
            JOptionPane.showMessageDialog(null, "单次取款总额不得大于5000", "提示消息", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (this.balance < money) {
            JOptionPane.showMessageDialog(null, "当前余额 【" + this.balance + "】 小于取款金额【" + money + "】，不允许透支", "提示消息", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        //取款
        this.balance= this.balance-money;
        updateaccount(this.id,this.balance);
        //日期格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //记录交易记录
        if(flag==1) {
            List<String> operationRecod = getOperationRecod();
            operationRecod.add("【" + LocalDateTime.now().format(dateTimeFormatter) + "】 ATM 取款 [" + money + "]元，当前余额[" + this.balance + "]元");
            JOptionPane.showMessageDialog(null, "取款成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
        }
        return true;
    }

    /**
     * 导出交易记录到文件.
     */
    public void exportRecord() {
        //获取记录
        List<String> operationRecod = getOperationRecod();
        if (operationRecod.isEmpty()) {
            JOptionPane.showMessageDialog(null, "当前账号交易记录为空！", "提示消息", JOptionPane.WARNING_MESSAGE);
            return;
        }
        //获取当前jar所在路径
        String jarPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        //避免中文情况，转码
        try {
            jarPath = URLDecoder.decode(jarPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //拼接输出的目录
        String path = jarPath.substring(0, jarPath.lastIndexOf("/")) + "/AccountRecord.txt";
        File file = new File(path);
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (String record : operationRecod) {
                out.write(record + "\r\n");
            }
            out.flush();
            out.close();
            JOptionPane.showMessageDialog(null, "文件成功导出至【"+path+"】目录下");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "文件导出异常", "错误消息", JOptionPane.WARNING_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * 设置新密码.
     *
     * @param new_pass 新密码
     * @param old_pass 旧密码
     * @param new_pass2  确认
     */
    public boolean updatePassword(String old_pass, String new_pass, String new_pass2) {
        if (!this.password.equals(old_pass)) {
            JOptionPane.showMessageDialog(null, "输入旧密码错误！", "提示消息", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!new_pass.equals(new_pass2)) {
            JOptionPane.showMessageDialog(null, "两次输入密码不一致！", "提示消息", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        this.password=new_pass2;

        Connection connection=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        2.用户信息和url
        String url = "jdbc:mysql://localhost:3306/atm";
        String username = "root";
        String password = "0000";
//        3.连接成功，数据库对象 Connection
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql="update customeraccount set password=? where ID=? ";

        PreparedStatement ps= null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(2,this.id);
            ps.setString(1,this.password);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }




        JOptionPane.showMessageDialog(null, "设置新密码成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
        return true;
    }



}
