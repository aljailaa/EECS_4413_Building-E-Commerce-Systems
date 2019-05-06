package ctrl;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import  org.w3c.dom.Document;
import org.xml.sax.InputSource;

import model.Brain;

/**
 * Servlet implementation class Roster
 */
@WebServlet("/Roster.do")
public class Roster extends HttpServlet {
	private static final long serialVersionUID = 1L;
      

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	     this.getServletContext().getRequestDispatcher("/Roster.html").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Writer out = response.getWriter();
		response.setContentType("text/html");
		
		Brain model = Brain.getInstance();
		String course = request.getParameter("course");
	
		String html;
		try
		{
 			 String xmlString = model.doRoster(course); 
 			 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			 DocumentBuilder builder = factory.newDocumentBuilder();
			 InputSource is = new InputSource(new StringReader(xmlString));
			 Document doc = builder.parse(is);
			 Element parent = doc.getDocumentElement();
			 
			
 			 
			 String htmlString = "<html>";
			 htmlString += "<head><title>Head Roster<</title></head><body>";
			 htmlString += "<h1>Course Roster via HTTP</h1>";
			 htmlString += "<p><a href='Dash.do'>Back to Dashboard</a></p>";
			 
			 htmlString += "<table border='1' cellpadding='4'>";	
			 htmlString += " <tr>\n<th>ID</th><th>Last Name</th><th>First Name</th><th>City</th>"
			 		+ "<th>Program</th><th>Hours</th> <th>GPA</th></tr>";
			
			 
			 NodeList elements = parent.getElementsByTagName("students");
			//out.write(res);
			for (int i = 0; i < elements.getLength(); i++) {
				NodeList childNodes = elements.item(i).getChildNodes();
				htmlString += "<tr>";
				for (int j = 0; j < childNodes.getLength(); j++) {
					htmlString += "<td>";
					htmlString += childNodes.item(j).getTextContent();
					htmlString += "</td>";
				}
				htmlString += "</tr>";
			}
			
			htmlString += "</table></body></html>";
			out.write(htmlString);
		} catch (Exception e)
		{
			html = String.format("<html><body><p><a href='Dash.do'>Back to Dashboard</a><"
					+ "/p><p>Could not find course: \"%s\"</p", e.getMessage().toString());
			out.write(html);
		}
	}
	

}
