package com.luv2code.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private StudentDbUtil12 studentDbUtil;
	
	@Resource(name = "jdbc/web_student_tracker")
	private DataSource datasource;
	
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();

		// Create studentDbUtil and pass in the connection pool/datasource
		try {
			studentDbUtil = new StudentDbUtil12(datasource);
		}
		catch(Exception exc)
		{
			throw new ServletException(exc);			
		}
	}
	


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			//Read the Command parameter and route to appropriate method
			String theCommand = request.getParameter("command");
			
			// if the command is missing then default to listing student
			if(theCommand==null) {
				theCommand="LIST";
			}
			//route to appropriate method
			switch(theCommand) {
			
			case "LIST":
				listStudents(request,response);
				break;
				
			case "ADD":
				addStudent(request,response);
				break;
				
			case "LOAD":
				loadStudent(request,response);
				break;
				
			case "UPDATE":
				updateStudent(request,response);
				break;
				
			case "DELETE":
				deleteStudent(request,response);
				break;
				
			default:
				listStudents(request, response);
			}
			
			//list the students in MVC fashion
			
		}
		catch(Exception exc)
		{
			throw new ServletException(exc);
		}
		
	}
	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//read student id from form data
		String theStudentId = request.getParameter("studentId");
		
		//delete student from database
		studentDbUtil.deleteStudent(theStudentId);
		
		//send the user back to List-student page
		listStudents(request,response);
		
	}



	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//read student data from form data (list-student12.jsp)
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		//create a new Student object
		Student12 theStudent = new Student12(id,firstName,lastName,email);
		
		//perform update on database
		studentDbUtil.updateStudent(theStudent);
		
		//Send them back to the list student page
		listStudents(request,response);
		
	}



	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//read student id from form data 
		String theStudentId = request.getParameter("studentId");
		
		//get student from database(db util)
		Student12 theStudent = studentDbUtil.getStudent(theStudentId);
		
		//place student in the request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		
		//send to JSP page: update-student-form.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
	}



	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
				// read student info from form data
				String firstName = request.getParameter("firstName");
				String lastName = request.getParameter("lastName");
				String email = request.getParameter("email");		
				
				// create a new student object
				Student12 theStudent = new Student12(firstName, lastName, email);
				
				// add the student to the database
				studentDbUtil.addStudent(theStudent);
						
				// send back to main page (the student list)
				listStudents(request, response);

		
	}
	
	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
				//Step1: get students from DB
				List<Student12> student = studentDbUtil.getStudents();
				
				//step2: add students to the request via attibute
				request.setAttribute("STUDENT_LIST", student);
				
				//step3: send it to JSP page (view)
				RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students12.jsp");
				dispatcher.forward(request, response);
	}

}
