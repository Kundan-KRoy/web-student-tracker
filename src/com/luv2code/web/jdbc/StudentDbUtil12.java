package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil12 {
	
	private DataSource datasource;
	public StudentDbUtil12(DataSource datasource)
	{
		this.datasource=datasource;
	}
	
	public List<Student12> getStudents() throws Exception{
		
		List<Student12> students = new ArrayList<>();
		
		Connection myConn=null;
		Statement myStmt=null;
		ResultSet myRs=null;
		try {
			// get a connection
			myConn = datasource.getConnection();
			
			//create SQL statement
			String sql = "select * from student order by last_name";
			myStmt = myConn.createStatement();
			
			//execute query
			myRs = myStmt.executeQuery(sql);
			
			//process result set
			while(myRs.next()) {
				
				// retrieve data from result set row
				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				//create new student object
				 Student12 tempStudent = new Student12(id,firstName,lastName,email);
				 	
				//add it to the list of students
				students.add(tempStudent);
			}
			return students;
		}
		
		finally {
			//close JDBC objects
			close(myConn,myStmt,myRs);
		}
		
		
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		
		try {
			if(myRs!=null) {
				myRs.close();
			}
			if(myStmt!=null) {
				myStmt.close();
			}
			if(myConn!=null) {
				myConn.close();//does not really close the connection..just puts back in connection pool so other u
			}
		}
		catch(Exception exc) {
			exc.printStackTrace();
		}
		
	}

	public void addStudent(Student12 theStudent) throws SQLException {
		

		Connection myConn = null;
		PreparedStatement myStmt = null;
		
		try {
			// get db connection
			myConn = datasource.getConnection();
			
			// create sql for insert
			String sql = "insert into student "
					   + "(first_name, last_name, email) "
					   + "values (?, ?, ?)";
			
			myStmt = myConn.prepareStatement(sql);
			
			// set the param values for the student
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			
			// execute sql insert
			myStmt.execute();
		}
		finally {
			// clean up JDBC objects
			close(myConn, myStmt, null);
		}

	
	}

	public Student12 getStudent(String theStudentId) throws Exception {
		Student12 theStudent = null;
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentId;
		try {
			
			
			//convert student id to int
			studentId = Integer.parseInt(theStudentId);
			
			//get connection to database
			myConn = datasource.getConnection();
			
			String sql = "select * from student where id=?";			
			//create preparedStatement
			myStmt = myConn.prepareStatement(sql);			
			
			//set params
			myStmt.setInt(1, studentId);
			
			//execute statement
			myRs = myStmt.executeQuery();
			
			//retrieve data from result set row
			if(myRs.next())
			{
				String firstName=myRs.getString("first_name");
				String lastName=myRs.getString("last_name");
				String email=myRs.getString("email");
				
				// use the studentId during construction
				theStudent = new Student12(studentId, firstName, lastName, email);
			}
			else {
				throw new Exception ("could not find student id" + studentId);
			}
			
			
		return theStudent;
		}
		finally {
			//clean  up JDBC objects
			close(myConn, myStmt, myRs);
		}
	}

	public void updateStudent(Student12 theStudent) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		try {
			//get DB Connection
			myConn = datasource.getConnection();
			
			//Create SQL update Statement
			String sql = "update student "+
					     "set first_name=?, last_name=?,email=? "+
					     "where id=?";
			
			//prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			//set params
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			
			//execute SQL statement
			myStmt.execute();
		}
		finally{
			//close JDBC objects
			close(myConn,myStmt,null);
		}
		
		
		
	}

	public void deleteStudent(String theStudentId) throws Exception {
		
		Connection myConn =null;
		PreparedStatement myStmt = null;
		
		try {
			//convert student id to int
			int studentId=Integer.parseInt((theStudentId));
			
			//get DB connection
			myConn = datasource.getConnection();
			
			//create delete query
			String sql = "delete from Student where id=?";
			
			//prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			//set params
			myStmt.setInt(1, studentId);
			
			//execute sql Statement
			myStmt.execute();

		}
		finally {
			//Clean up JDBC objects
			close(myConn,myStmt,null);
		}
		
	}

}
