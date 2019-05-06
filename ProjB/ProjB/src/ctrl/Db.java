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
 * Servlet implementation class Db
 */
@WebServlet("/Db.do")
public class Db extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	     this.getServletContext().getRequestDispatcher("/Db.html").forward(request, response);	
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Writer out = response.getWriter();
		response.setContentType("text/html");
		
		Brain model = Brain.getInstance();
		String itemNo = request.getParameter("itemno");
		
		
		String html;
		try
		{
			 String res = model.doDb(itemNo);
			 html = String.format("<html><body><p><a href='Dash.do'>Back to Dashboard</a>"
					+ "</p><p>Item: %s</p></body></html>", res);
			out.write(html);
			
		} catch (Exception e)
		{
			html = String.format("<html><body><p><a href='Dash.do'>Back to Dashboard</a><"
					+ "/p><p>Error For item #: \"%s\"</p", e.getMessage().toString());
			out.write(html);
		}
	}

}
