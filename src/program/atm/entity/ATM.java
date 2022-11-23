package program.atm.entity;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ATM机数据.
 *
 */
public class ATM {
    static CustAccount cur_account;



    /**
     * 初始化数据.
     */

    /**
     * 登录.
     *
     * @param id
     * @param pwd
     * @return
     */
    public static CustAccount loginATM(int id, String pwd) throws SQLException {
        Connection connection=null;
        CustAccount cur=new CustAccount();
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
        String sql="select * from customeraccount where ID=?";

        PreparedStatement ps=connection.prepareStatement(sql);
        System.out.println("sql:"+sql+"id"+id);
        ps.setInt(1,id);
        ResultSet rs = ps.executeQuery();
        System.out.println();
        int row=0;
        while (rs.next())
        {
            cur.id=rs.getInt("id");//第一列
            cur.password=rs.getString("password");//第二列
            cur.balance=rs.getFloat("balance");
            row++;
        }





        //是否存在账号
        if (row!=0) {
            if (cur.password.equals(pwd)) {
                JOptionPane.showMessageDialog(null, "登录成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
                //重置错误次数
                cur_account=cur;
                return cur_account;
            }else {
                JOptionPane.showMessageDialog(null, "密码错误", "提示消息", JOptionPane.WARNING_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(null, "账号错误", "提示消息", JOptionPane.WARNING_MESSAGE);
        }
        return null;
    }

    /**
     * 转账.
     * 通过登录的用户向指定的银行账号（在系统中已经保存）转账，若银行卡不存在，
     * 则提示银行卡号输入错误，转账成功则提示“转账成功”
     *
     * @param account
     * @param targetid
     * @param money
     * @return
     */
    public static boolean transfer(CustAccount account, int targetid, int money) throws SQLException {


        CustAccount targetAccount=new CustAccount();
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
        String sql="select * from customeraccount where ID=?";

        PreparedStatement ps=connection.prepareStatement(sql);
        ps.setInt(1,targetid);
        ResultSet rs = ps.executeQuery();
        int row=0;
        while (rs.next())
        {
            targetAccount.id=rs.getInt(1);//第一列
            targetAccount.password=rs.getString(2);//第二列
            targetAccount.balance=rs.getFloat(3);
            row++;
        }

        if (row==0) {
            JOptionPane.showMessageDialog(null, "银行卡号输入错误", "提示消息", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (account.balance < money) {
            JOptionPane.showMessageDialog(null, "转账失败，余额不足", "提示消息", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        //添加目标账号金额
        targetAccount.deposit(money,0);
        //修改剩余金额
        account.withdrawal(money,0);
        //日期格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //添加交易记录
        account.getOperationRecod().add("【" + LocalDateTime.now().format(dateTimeFormatter) + "】 ATM 转账 [" + money + "]元 给账号[" + targetAccount.id + "] 当前余额[" + account.balance + "]元");
        targetAccount.getOperationRecod().add("【" + LocalDateTime.now().format(dateTimeFormatter) + "】 接收到ATM账号 [" + account.id + "] 的转账 [" + money + "]元  当前余额[" + targetAccount.balance + "]元");
        JOptionPane.showMessageDialog(null, "转账成功！", "提示消息", JOptionPane.WARNING_MESSAGE);
        return true;
    }

}
