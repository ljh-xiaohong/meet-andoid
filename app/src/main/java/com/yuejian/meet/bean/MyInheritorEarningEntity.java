package com.yuejian.meet.bean;

/**
 * @author : g000gle
 * @time : 2019/6/19 10:49
 * @desc : 钱包 - VIP收益 实体类
 */
public class MyInheritorEarningEntity {

    public int giving_package;
    public Inheritor_earnings_list inheritor_earnings_list;
    public Account_earnings_list account_earnings_list;
    public int inheritor_id;
    public double inheritor_bal;
    public double total_inheritor_amt;
    public int count_vip;
    public int all_earnings_count;
    public int residue_package;
    public int inheritor_count;
    public int all_package;

    public int getGiving_package() {
        return giving_package;
    }

    public void setGiving_package(int giving_package) {
        this.giving_package = giving_package;
    }

    public Inheritor_earnings_list getInheritor_earnings_list() {
        return inheritor_earnings_list;
    }

    public void setInheritor_earnings_list(Inheritor_earnings_list inheritor_earnings_list) {
        this.inheritor_earnings_list = inheritor_earnings_list;
    }

    public Account_earnings_list getAccount_earnings_list() {
        return account_earnings_list;
    }

    public void setAccount_earnings_list(Account_earnings_list account_earnings_list) {
        this.account_earnings_list = account_earnings_list;
    }

    public int getInheritor_id() {
        return inheritor_id;
    }

    public void setInheritor_id(int inheritor_id) {
        this.inheritor_id = inheritor_id;
    }

    public double getInheritor_bal() {
        return inheritor_bal;
    }

    public void setInheritor_bal(double inheritor_bal) {
        this.inheritor_bal = inheritor_bal;
    }

    public double getTotal_inheritor_amt() {
        return total_inheritor_amt;
    }

    public void setTotal_inheritor_amt(double total_inheritor_amt) {
        this.total_inheritor_amt = total_inheritor_amt;
    }

    public int getCount_vip() {
        return count_vip;
    }

    public void setCount_vip(int count_vip) {
        this.count_vip = count_vip;
    }

    public int getAll_earnings_count() {
        return all_earnings_count;
    }

    public void setAll_earnings_count(int all_earnings_count) {
        this.all_earnings_count = all_earnings_count;
    }

    public int getResidue_package() {
        return residue_package;
    }

    public void setResidue_package(int residue_package) {
        this.residue_package = residue_package;
    }

    public int getInheritor_count() {
        return inheritor_count;
    }

    public void setInheritor_count(int inheritor_count) {
        this.inheritor_count = inheritor_count;
    }

    public int getAll_package() {
        return all_package;
    }

    public void setAll_package(int all_package) {
        this.all_package = all_package;
    }

    public class Inheritor_earnings_list {
        public int inheritor_earnings_today;
        public String inheritor_name;
        public int inheritor_earnings;

        public int getInheritor_earnings_today() {
            return inheritor_earnings_today;
        }

        public void setInheritor_earnings_today(int inheritor_earnings_today) {
            this.inheritor_earnings_today = inheritor_earnings_today;
        }

        public String getInheritor_name() {
            return inheritor_name;
        }

        public void setInheritor_name(String inheritor_name) {
            this.inheritor_name = inheritor_name;
        }

        public int getInheritor_earnings() {
            return inheritor_earnings;
        }

        public void setInheritor_earnings(int inheritor_earnings) {
            this.inheritor_earnings = inheritor_earnings;
        }
    }

    public class Account_earnings_list {
        public int account_earnings_today;
        public String account_name;
        public int account_earnings;

        public int getAccount_earnings_today() {
            return account_earnings_today;
        }

        public void setAccount_earnings_today(int account_earnings_today) {
            this.account_earnings_today = account_earnings_today;
        }

        public String getAccount_name() {
            return account_name;
        }

        public void setAccount_name(String account_name) {
            this.account_name = account_name;
        }

        public int getAccount_earnings() {
            return account_earnings;
        }

        public void setAccount_earnings(int account_earnings) {
            this.account_earnings = account_earnings;
        }
    }



}
