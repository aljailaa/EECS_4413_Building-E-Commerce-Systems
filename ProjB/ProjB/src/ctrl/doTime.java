package ctrl;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Brain;

/**
 * Servlet implementation class Time
 */
@WebServlet("/Time.do")
public class doTime extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//		response.getWriter().append("Served at: ").append(request.getContextPath());
		 if (request.getParameter("calc") == null)
		  {
		     this.getServletContext().getRequestDispatcher("/Time.html").forward(request, response);
		  }
		  else
		  {
		     Brain model = Brain.getInstance();
		     try
		     {
		    	String advancedAttr = request.getParameter("advanced");
		        ArrayList<String> responseList = model.doTime(advancedAttr);
		
		        response.setContentType("text/html");
		        Writer out = response.getWriter();
		        String html = "<html><body>";
		        html += "<p><a href='Dash.do'>Back to Dashboard</a></p>";
		        
		        if (responseList.size() == 1)
		        	html += "<h2>Server Time: \n" + responseList.get(0) + "</h2>";
		        else  {
		        	html += "<h2>Server Time:</h2>";
		        	for (String item: responseList) 
		        		html += "<h2>"+ item+"</h2>";
		        }
		        		
		        html += "</body></html>";
		        out.write(html);
		     }
		     catch (Exception e)
		     {
		        response.setContentType("text/html");
		        Writer out = response.getWriter();
		        String html = "<html><body>";
		        html += "<p><a href=' Dash.do'>Back to Dashboard</a></p>";
		        html += "<p>Error: The number of hours must be int.</p>";
		        out.write(html);
		     }
		  }

	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
