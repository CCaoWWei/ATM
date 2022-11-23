package program.atm.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 银行卡实体.
 */
public class Account {

    /**
     * 卡号 10位.
     */
    public int id;

    /**
     * 密码 6位.
     */
    public String password;

    /**
     * 账户余额.
     */
    public float balance;

    /**
     * 账号收入支出记录.
     */
    public List<String> operationRecod;

    public Account() {this.operationRecod = new ArrayList<>(4);
    }

    public Account(int id, String password, float balance) {
        this.id = id;
        this.password = password;
        this.balance = balance;
        this.operationRecod = new ArrayList<>(4);
    }
    
    public List<String> getOperationRecod() {
        return operationRecod;
    }

    public void setOperationRecod(List<String> operationRecod) {
        this.operationRecod = operationRecod;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", operationRecod=" + operationRecod +
                '}';
    }
}
