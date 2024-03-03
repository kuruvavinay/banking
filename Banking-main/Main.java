package com.bank.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

	    public static void main(String[] args) throws ClassNotFoundException, SQLException {
	        try{
	            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","Root@123");
	            Scanner sc =  new Scanner(System.in);
	            User user = new User(conn, sc);
	            Accounts accounts = new Accounts(conn, sc);
	            AccountMgr accountMgr = new AccountMgr(conn, sc);

	            String email;
	            long acc_no;

	            while(true){
	            	ScheduledExecutorService scheduler1=Executors.newScheduledThreadPool(1);
	            	scheduler1.schedule(()->{
	            	System.out.println();
	                System.out.println("WELCOME TO BANKING APPLICATION");
	                System.out.println("------------------------------");
	                System.out.println("1.REGISTER");
	                System.out.println("2.LOGIN");
	                System.out.println("3.EXIT");
	                System.out.println("Enter your choice: ");
	            	},2,TimeUnit.SECONDS);
	                int choice1 = sc.nextInt();
	                switch (choice1){
	                    case 1:
	                        user.register();
	                        break;
	                    case 2:
	                        email = user.login();
	                        if(email!=null){
	                            System.out.println();
	                            System.out.println("User Logged In!");
	                            if(!accounts.account_exist(email)){
	                                System.out.println();
	                                System.out.println("1.Open a New Bank Account");
	                                System.out.println("2 Exit");
	                                if(sc.nextInt() == 1) {
	                                    acc_no = accounts.open_account(email);
	                                    System.out.println("Account Created Successfully");
	                                    System.out.println("Your Account Number is: " + acc_no);
	                                }else{
	                                    break;
	                                }

	                            }
	                            acc_no = accounts.getAccount_number(email);
	                            int choice2 = 0;
	                            while (choice2 != 5) {
	                               ScheduledExecutorService scheduler=Executors.newScheduledThreadPool(1);
	                            	   scheduler.schedule(()->{
	                            		   services();
	                            	   },3,TimeUnit.SECONDS);
	                                choice2 = sc.nextInt();
	                                switch (choice2) {
	                                    case 1:
	                                        accountMgr.debit_money(acc_no);
	                                        break;
	                                    case 2:
	                                        accountMgr.credit_money(acc_no);
	                                        break;
	                                    case 3:
	                                        accountMgr.transfer_money(acc_no);
	                                        break;
	                                    case 4:
	                                        accountMgr.getBalance(acc_no);
	                                        break;
	                                    case 5:
	                                        break;
	                                    default:
	                                        System.out.println("Enter Valid Choice!");
	                                        break;
	                                }
	                            }

	                        }
	                        else{
	                            System.out.println("Incorrect Email or Password!");
	                        }
	                    case 3:
	                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
	                        System.out.println("Exiting System!");
	                        return;
	                    default:
	                        System.out.println("Enter Valid Choice");
	                        break;
	                }
	            }
	        }catch (SQLException e){
	            e.printStackTrace();
	        }
	    }
	    public static void services() {
	    	System.out.println();
	    	System.out.println("What do you want to do now?");
	    	System.out.println("1.DEBIT MONEY");
	    	System.out.println("2.Credit Money");
            System.out.println("3.Transfer Money");
            System.out.println("4.Check Balance");
            System.out.println("5.Log Out");
            System.out.println("Enter your choice: ");
	    }
}
