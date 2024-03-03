package com.bank.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
	Connection conn;
	Scanner sc;
	public User(Connection conn, Scanner sc) {
		// TODO Auto-generated constructor stub
		this.conn=conn;
		this.sc=sc;
	}
	
	
	  public void register(){
	        sc.nextLine();
	        System.out.print("Enter your name : ");
	        String name = sc.nextLine();
	        System.out.print("Enter your email : ");
	        String email = sc.nextLine();
	        System.out.print("Enter Password: ");
	        String password = sc.nextLine();
	        System.out.print("Re-enter the password to confirm : ");
	        String pwd_confirm=sc.nextLine();
	        if(password.equals(pwd_confirm)) {
	        if(user_exist(email)) {
	            System.out.println("This Email already exists...");
	            return;
	        }
	        String register_query = "insert into user(name,email,password)values(?, ?, ?)";
	        try {
	            PreparedStatement pstmt = conn.prepareStatement(register_query);
	            pstmt.setString(1, name);
	            pstmt.setString(2, email);
	            pstmt.setString(3, password);
	            int rowsAffected = pstmt.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("Registration Successfull!");
	            } else {
	                System.out.println("Registration Failed!");
	            }
	        } catch (SQLException se) {
	            se.printStackTrace();
	        }
	    }else {
	    	System.out.println("Passwords do not match,Try Again.");
	    }
	  }
	  
	  
	    public String login(){
	        sc.nextLine();
	        System.out.print("Email: ");
	        String email = sc.nextLine();
	        System.out.print("Password: ");
	        String password = sc.nextLine();
	        String login_query = "select * from user where email = ? and password = ?";
	        try{
	            PreparedStatement pstmt = conn.prepareStatement(login_query);
	            pstmt.setString(1, email);
	            pstmt.setString(2, password);
	            ResultSet rs = pstmt.executeQuery();
	            if(rs.next()){
	                return email;
	            }else{
	                return null;
	            }
	        }catch (SQLException e){
	            e.printStackTrace();
	        }
	        return null;
	    }

	    
	    
	    public boolean user_exist(String email){
	        String query = "select * from user where email = ?";
	        try{
	            PreparedStatement pstmt = conn.prepareStatement(query);
	            pstmt.setString(1, email);
	            ResultSet rs = pstmt.executeQuery();
	            if(rs.next()){
	                return true;
	            }
	            else{
	                return false;
	            }
	        }catch (SQLException e){
	            e.printStackTrace();
	        }
	        return false;
	    }

}
