package com.bank.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;

public class Accounts {
	Connection conn;
	Scanner sc;
	public Accounts(Connection conn, Scanner sc) {
		// TODO Auto-generated constructor stub
		this.conn=conn;
		this.sc=sc;
	}
	 public long open_account(String email){
	        if(!account_exist(email)) {
	            String open_account_query = "insert into accounts(acc_no,name,email,balance,pin) values(?,?,?,?,?)";
	            sc.nextLine();
	            System.out.print("Enter Your Name: ");
	            String name = sc.nextLine();
	            System.out.print("Enter Initial Amount: ");
	            double balance = sc.nextDouble();
	            sc.nextLine();
	            System.out.print("Enter the Pin: ");
	            String pin = sc.nextLine();
	            try {
	                long acc_no = generateAccountNumber();
	                PreparedStatement pstmt = conn.prepareStatement(open_account_query);
	                pstmt.setLong(1, acc_no);
	                pstmt.setString(2, name);
	                pstmt.setString(3, email);
	                pstmt.setDouble(4, balance);
	                pstmt.setString(5, pin);
	                int rowsAffected = pstmt.executeUpdate();
	                if (rowsAffected > 0) {
	                    return acc_no;
	                } else {
	                    throw new RuntimeException("Account Creation failed!!");
	                }
	            } catch (SQLException se) {
	                se.printStackTrace();
	            }
	        }
	        throw new RuntimeException("Account Already Exist");

	    }

	 
	 
	    public long getAccount_number(String email) {
	        String query = "select acc_no from accounts where email = ?";
	        try{
	            PreparedStatement pstmt = conn.prepareStatement(query);
	            pstmt.setString(1, email);
	            ResultSet rs = pstmt.executeQuery();
	            if(rs.next()){
	                return rs.getLong("acc_no");
	            }
	        }catch (SQLException se){
	            se.printStackTrace();
	        }
	        throw new RuntimeException("Account Number Doesn't Exist!");
	    }



	    private long generateAccountNumber() {
	        int inc=10000;
	        int exc=99999;
	        int random_num=generateRandomNumber(inc,exc);
	        return random_num;
	    }
	    private int generateRandomNumber(int inc,int exc) {
	    	Random random=new Random();
	    	return random.nextInt(exc-inc)+inc;
	    }

	    
	    
	    public boolean account_exist(String email){
	        String query = "select acc_no from accounts where email = ?";
	        try{
	            PreparedStatement pstmt = conn.prepareStatement(query);
	            pstmt.setString(1, email);
	            ResultSet rs = pstmt.executeQuery();
	            if(rs.next()){
	                return true;
	            }else{
	                return false;
	            }
	        }catch (SQLException e){
	            e.printStackTrace();
	        }
	        return false;

	    }

}
