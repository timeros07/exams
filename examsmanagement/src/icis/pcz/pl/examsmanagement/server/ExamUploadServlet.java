package icis.pcz.pl.examsmanagement.server;





import icis.pcz.pl.examsmanagement.client.Duration;
import icis.pcz.pl.examsmanagement.client.Exam;
import icis.pcz.pl.examsmanagement.client.Question;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;




public class ExamUploadServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
		// Create a factory for disk-based file items
		FileItemFactory factory = new DiskFileItemFactory();
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		//response.setCharacterEncoding("UTF-8");
		//request.setCharacterEncoding("UTF-8");
		
		//response.setHeader("Accept-Encoding", "gzip,deflate,sdch");
		//response.setHeader("Acept-Language", "pl-PL,pl;q=0.8,en-US;q=0.6,en;q=0.4");
		
		String name = "";
		String description = "";
		int duration = 0;
		String userName = "";
		try 
		{
			if(ServletFileUpload.isMultipartContent(request))
			{
				@SuppressWarnings("unchecked")
				List<FileItem> items  = upload.parseRequest(request);
			
				for(FileItem f : items)
				{
					if(f.getFieldName().equals("name"))
					{
						name= f.getString();
					}
					else if(f.getFieldName().equals("description"))
					{
						description = f.getString();
					}
					else if(f.getFieldName().equals("duration"))
					{
						duration = Integer.parseInt(f.getString());
					}
					else if(f.getFieldName().equals("userName"))
					{
						userName = f.getString();
					}
					else if(f.getFieldName().equals("examFile"))
					{
						if(name.equals("") || description.equals("") || duration == 0 || userName.equals(""))
						{
							response.getWriter().write("server otrzymał niepoprawne dane");
						}
						else
						{
							if(f.getSize() > 544023 )
							{
								response.getWriter().write("plik jest za duży");
							}
							else if(!isCorrectXMLFile(f))
							{
								response.getWriter().write("plik jest niepoprawny");
							}
							else
							{
								if(createExam(name, description, duration, userName, f.getString("UTF-8")) == 0)
								{
									response.getWriter().write("nie udało się zapisać testu");
								}
								else
								{
										response.getWriter().write("ok");
								}
								
							}
								
						}
					}
				}
			}
		}
		catch(IOException e)
		{
			System.out.println("Błąd podczas wysyłania odpowiedzi HTTP");	
			e.printStackTrace();
		} 
		catch (FileUploadException e)
		{
			System.out.println("Błąd podczas pobierania pliku");
			e.printStackTrace();
		}
		
	} 
	private int createExam(String name, String description, int duration, String userName, String xmlContent)
	{
		Exam exam = new Exam();
		exam.setName(name);
		exam.setDescription(description);
		exam.setDuration(new Duration(duration, 0));
		
		loadQuestions(exam, xmlContent);
		
		if(exam.getQuestionCount() <= 0)
			return 0;
		
		Connection connection = getConnection();
		try
		{
			//zapisujemy dane do bazy danych
		
			
			Statement statement = connection.createStatement();
			
			statement.executeUpdate("INSERT INTO exams(name, description, duration, author, question_count) VALUES ('" 
			+ exam.getName() + "', '" + exam.getDescription() + "', " + exam.getDuration().getMinutes()
			+ ", '" + userName + "', " + exam.getQuestionCount() + ")");
			
			statement = connection.createStatement();
			ResultSet result =  statement.executeQuery("SELECT LAST_INSERT_ID()");
			result.next();
			exam.setId(Integer.parseInt(result.getString(1)));
			result.close();
			
			for(int i=0 ; i<exam.getQuestionCount() ; i++)
			{
				Question question = exam.getQuestion(i);
				statement.executeUpdate("INSERT INTO questions(exam_id, question_nr, content) VALUES(" +
					+ exam.getId() + ", " + i + ", '" + question.getContent() + "' )");
				
				for(int j=0 ; j<question.getAnswerCount() ; j++)
				{
					int correct = question.getAnswer(j).isCorrect() ? 1:0 ; 
					
					statement.executeUpdate("INSERT INTO answers(exam_id, question_nr, answer_nr, content, correct) VALUES(" +
						exam.getId() + ", " + i + ", " + j + ", '" + question.getAnswer(j).getContent() + "', " + correct + ")");
				}
			}
			return exam.getId();
		}
		catch(SQLException e)
		{
			System.out.println("Błąd SQL");
			e.printStackTrace();
		}

		finally
		{
			closeConnection(connection);
		}
		return 0;
				
	}
	private Connection getConnection()
	{
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection
					(getServletConfig().getInitParameter("mySqlConnectionString").replace(';', '&'));
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void closeConnection(Connection connection)
	{
		if(connection != null)
		{
			try
			{
				connection.close();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	private boolean isCorrectXMLFile(FileItem file)
	{
		try 
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			
			Document doc = builder.parse(file.getInputStream());
		
			Element root = doc.getDocumentElement();
		
			NodeList mainNodeList = root.getChildNodes();
			
			for(int i=0 ; i<mainNodeList.getLength() ; i++)
			{
				if(mainNodeList.item(i) instanceof Element)
	            {
	               Element mainElement = (Element) mainNodeList.item(i);
	               
	               if(!mainElement.getNodeName().equals("question"))
	               {
	            	   return false;
	               }
	               else
	               {
	            	   NodeList nodeList = mainElement.getChildNodes();
	            	   for(int j=0 ; j<nodeList.getLength() ; j++)
	                   {
	                       if(nodeList.item(j) instanceof Element)
	                       {
	                            Element element = (Element) nodeList.item(j);
	                            if(!(element.getNodeName().equals("content") || element.getNodeName().equals("answer")))
	                            {
	                            	return false;
	                            }
	                            else
	                            {
	                            	if(element.getNodeName().equals("content"))
	                                {
	                                    Text textNode = (Text) element.getFirstChild();
	                                    if(textNode.getData().trim().equals(""))
	                                    {
	                                    	return false;
	                                    }
	                                }
	                                else if(element.getNodeName().equals("answer"))
	                                {
	                                    Text textNode = (Text) element.getFirstChild();
	                                    if(textNode.getData().trim().equals(""))
	                                    {
	                                    	return false;
	                                    }
	                                    else
	                                    {
	                                    	if(element.getAttribute("correct").equals(""))
	                                    		return false;
	                                    }
	                                }
	                            }
	                       }
	                   }
	               }
	            }
			}
			return true;
		} 
		catch (ParserConfigurationException e) 
		{
			System.out.println("Błędna konfiguracja parsera XML");
			e.printStackTrace();
		}
		catch (SAXException e) 
		{
			System.out.println("Błąd podczas parsowania");
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			System.out.println("Błąd podczas parsowania");
			e.printStackTrace();
		}
		return false;
	}
	
	private void loadQuestions(Exam exam, String xmlString)
	{
		try 
		{
		javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
	    javax.xml.parsers.DocumentBuilder db = factory.newDocumentBuilder();
	    org.xml.sax.InputSource inStream = new org.xml.sax.InputSource();
	 
	    inStream.setCharacterStream(new java.io.StringReader(xmlString));
	    Document doc = db.parse(inStream);
		
	
		Element root = doc.getDocumentElement();
		
		NodeList mainNodeList = root.getChildNodes();
		for(int i=0 ; i<mainNodeList.getLength() ; i++)
		{
			if(mainNodeList.item(i) instanceof Element)
            {
               Element mainElement = (Element) mainNodeList.item(i);
               
               if(mainElement.getNodeName().equals("question"))
               {
            	   Question q = new Question();
                   NodeList nodeList = mainElement.getChildNodes();
                   for(int j=0 ; j<nodeList.getLength() ; j++)
                   {
                       if(nodeList.item(j) instanceof Element)
                       {
                            Element element = (Element) nodeList.item(j);
                            if(element.getNodeName().equals("content"))
                            {
                                Text textNode = (Text) element.getFirstChild();
                                q.setContent(textNode.getData().trim());
                            }
                            else if(element.getNodeName().equals("answer"))
                            {
                                Text textNode = (Text) element.getFirstChild();
                                q.addAnswer(textNode.getData().trim(), 
                                		Boolean.parseBoolean(element.getAttribute("correct")));
                            }
                       	}
                   }
                   exam.addQuestion(q);
               }
            }
		}
		} 
		catch (ParserConfigurationException e) 
		{
			System.out.println("Błędna konfiguracja parsera XML");
			e.printStackTrace();
		}
		catch (SAXException e) 
		{
			System.out.println("Błąd podczas parsowania");
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			System.out.println("Błąd podczas parsowania");
			e.printStackTrace();
		}
	}
}



