package ctrl;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Brain;

/**
 * Servlet implementation class Prime
 */
@WebServlet("/Prime.do")
public class Prime extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		this.getServletContext().getRequestDispatcher("/Prime.html").forward(request, response);
		
	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Writer out = response.getWriter();
		response.setContentType("text/html");
		
		Brain model = Brain.getInstance();
		String primeNum = request.getParameter("digits");
		
		
		String html;
		try
		{
			 String res = model.doPrime(primeNum);
			 html = String.format("<html><body><p><a href='Dash.do'>Back to Dashboard</a>"
					+ "</p><p>Prime: %s</p></body></html>", res);
			out.write(html);
			
		} catch (Exception e)
		{
			html = String.format("<html><body><p><a href='Dash.do'>Back to Dashboard</a><"
					+ "/p><p>Error For input string: \"%s\"</p", primeNum);
			out.write(html);
		}
		
	
	}



}
