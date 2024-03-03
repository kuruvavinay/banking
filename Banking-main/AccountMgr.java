package com.bank.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountMgr {

	Connection conn;
	Scanner sc;
	public AccountMgr(Connection conn,Scanner sc) {
		this.conn=conn;
		this.sc=sc;
	}
	
	
	  public void credit_money(long acc_no)throws SQLException {
	        sc.nextLine();
	        System.out.print("Enter Amount: ");
	        double amount = sc.nextDouble();
	        sc.nextLine();
	        System.out.print("Enter the Pin: ");
	        String pin = sc.nextLine();

	        try {
	            conn.setAutoCommit(false);
	            if(acc_no != 0) {
	                PreparedStatement pstmt = conn.prepareStatement("select * from accounts where acc_no = ? and pin = ?");
	                pstmt.setLong(1, acc_no);
	                pstmt.setString(2, pin);
	                ResultSet rs = pstmt.executeQuery();

	                if (rs.next()) {
	                    String credit_query = "update accounts set balance = balance + ? where acc_no = ?";
	                    PreparedStatement pstmt1 = conn.prepareStatement(credit_query);
	                    pstmt1.setDouble(1, amount);
	                    pstmt1.setLong(2, acc_no);
	                    int rowsAffected = pstmt1.executeUpdate();
	                    if (rowsAffected > 0) {
	                        System.out.println("Rs."+amount+" credited Successfully");
	                        conn.commit();
	                        conn.setAutoCommit(true);
	                        return;
	                    } else {
	                        System.out.println("Transaction Failed!");
	                        conn.rollback();
	                        conn.setAutoCommit(true);
	                    }
	                }else{
	                    System.out.println("Invalid Security Pin!");
	                }
	            }
	        }catch (SQLException e){
	            e.printStackTrace();
	        }
	        conn.setAutoCommit(true);
	    }
	  
	  

	    public void debit_money(long acc_no) throws SQLException {
	        sc.nextLine();
	        System.out.print("Enter Amount: ");
	        double amount = sc.nextDouble();
	        sc.nextLine();
	        System.out.print("Enter Security Pin: ");
	        String pin = sc.nextLine();
	        try {
	            conn.setAutoCommit(false);
	            if(acc_no!=0) {
	                PreparedStatement pstmt = conn.prepareStatement("select * from accounts where acc_no = ? and pin = ?");
	                pstmt.setLong(1, acc_no);
	                pstmt.setString(2, pin);
	                ResultSet rs = pstmt.executeQuery();

	                if (rs.next()) {
	                    double current_balance = rs.getDouble("balance");
	                    if (amount<=current_balance){
	                        String debit_query = "update accounts set balance = balance - ? where acc_no = ?";
	                        PreparedStatement preparedStatement1 = conn.prepareStatement(debit_query);
	                        preparedStatement1.setDouble(1, amount);
	                        preparedStatement1.setLong(2, acc_no);
	                        int rowsAffected = preparedStatement1.executeUpdate();
	                        if (rowsAffected > 0) {
	                            System.out.println("Rs."+amount+" debited Successfully");
	                            conn.commit();
	                            conn.setAutoCommit(true);
	                            return;
	                        } else {
	                            System.out.println("Transaction Failed!");
	                            conn.rollback();
	                            conn.setAutoCommit(true);
	                        }
	                    }else{
	                        System.out.println("Insufficient Balance!");
	                    }
	                }else{
	                    System.out.println("Invalid Pin!");
	                }
	            }
	        }catch (SQLException e){
	                e.printStackTrace();
	            }
	        conn.setAutoCommit(true);
	    }
	    
	    
	    

	    public void transfer_money(long sender_accno) throws SQLException {
	        sc.nextLine();
	        System.out.print("Enter Receiver Account Number: ");
	        long receiver_accno = sc.nextLong();
	        System.out.print("Enter Amount: ");
	        double amount = sc.nextDouble();
	        sc.nextLine();
	        System.out.print("Enter the Pin: ");
	        String pin = sc.nextLine();

	        try {
	            conn.setAutoCommit(false);

	            String sender_query = "SELECT * FROM accounts WHERE acc_no = ? AND pin = ?";
	            PreparedStatement sendpstmt = conn.prepareStatement(sender_query);
	            sendpstmt.setLong(1, sender_accno);
	            sendpstmt.setString(2, pin);
	            ResultSet sendrs = sendpstmt.executeQuery();

	            if (sendrs.next()) {
	                String receiver_query = "SELECT * FROM accounts WHERE acc_no = ?";
	                PreparedStatement receivepstmt = conn.prepareStatement(receiver_query);
	                receivepstmt.setLong(1, receiver_accno);
	                ResultSet receivers = receivepstmt.executeQuery();

	                if (receivers.next()) {
	                    double current_balance = sendrs.getDouble("balance");

	                    if (amount <= current_balance) {
	                        String credit_query = "UPDATE accounts SET balance = balance + ? WHERE acc_no = ?";
	                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE acc_no = ?";

	                        PreparedStatement credit_pstmt = conn.prepareStatement(credit_query);
	                        PreparedStatement debit_pstmt = conn.prepareStatement(debit_query);

	                        credit_pstmt.setDouble(1, amount);
	                        credit_pstmt.setLong(2, receiver_accno);
	                        debit_pstmt.setDouble(1, amount);
	                        debit_pstmt.setLong(2, sender_accno);

	                        int rowsAffected1 = credit_pstmt.executeUpdate();
	                        int rowsAffected2 = debit_pstmt.executeUpdate();

	                        if (rowsAffected1 > 0 && rowsAffected2 > 0) {
	                            System.out.println("Transaction Successful!");
	                            System.out.println("Rs." + amount + " transferred successfully");
	                            conn.commit();
	                        } else {
	                            System.out.println("Transaction Failed");
	                            conn.rollback();
	                        }
	                    } else {
	                        System.out.println("Insufficient Balance!");
	                    }
	                } else {
	                    System.out.println("Invalid Receiver Account Number!");
	                }
	            } else {
	                System.out.println("Invalid Sender Security Pin!");
	            }
	        } catch (SQLException se) {
	            se.printStackTrace();
	        } finally {
	            conn.setAutoCommit(true);
	        }
	    }

	    
	    public static String convertNumberToWords(double balance) {
	        String[] units = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};
	        String[] teens = {"", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
	        String[] tens = {"", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};

	        if (balance == 0) {
	            return "Zero";
	        }

	        return convertToWords(balance, units, teens, tens);
	    }

	    private static String convertToWords(double balance, String[] units, String[] teens, String[] tens) {
	        if (balance < 10) {
	            return units[(int) balance];
	        } else if (balance < 20) {
	            return teens[(int) (balance - 10)];
	        } else if (balance < 100) {
	            return tens[(int) (balance / 10)] + " " + convertToWords(balance % 10, units, teens, tens);
	        } else if (balance < 1000) {
	            return units[(int) (balance / 100)] + " Hundred " + convertToWords(balance % 100, units, teens, tens);
	        } else if (balance < 1000000) {
	            return convertToWords(balance / 1000, units, teens, tens) + " Thousand " + convertToWords(balance % 1000, units, teens, tens);
	        } else if (balance < 1000000000) {
	            return convertToWords(balance / 1000000, units, teens, tens) + " Million " + convertToWords(balance % 1000000, units, teens, tens);
	        } else {
	            return convertToWords(balance / 1000000000, units, teens, tens) + " Billion " + convertToWords(balance % 1000000000, units, teens, tens);
	        }
	    }
	    

	    public void getBalance(long acc_no){
	        sc.nextLine();
	        System.out.print("Enter the Pin: ");
	        String pin = sc.nextLine();
	        try{
	            PreparedStatement pstmt = conn.prepareStatement("select balance from accounts where acc_no = ? and pin = ?");
	            pstmt.setLong(1, acc_no);
	            pstmt.setString(2,pin);
	            ResultSet rs = pstmt.executeQuery();
	            if(rs.next()){
	                double balance = rs.getDouble("balance");
	                System.out.println("Balance: "+balance);
	                System.out.println("IN WORDS : "+convertNumberToWords(balance)+"Rupees");
	            }else{
	                System.out.println("Invalid Pin!");
	            }
	        }catch (SQLException se){
	            se.printStackTrace();
	        }
	    }
}
