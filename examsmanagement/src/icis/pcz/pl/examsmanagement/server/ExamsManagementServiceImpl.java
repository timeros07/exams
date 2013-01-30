package icis.pcz.pl.examsmanagement.server;

import icis.pcz.pl.examsmanagement.client.Duration;
import icis.pcz.pl.examsmanagement.client.Exam;
import icis.pcz.pl.examsmanagement.client.ExamsManagementService;
import icis.pcz.pl.examsmanagement.client.Group;
import icis.pcz.pl.examsmanagement.client.Result;
import icis.pcz.pl.examsmanagement.client.Status;
import icis.pcz.pl.examsmanagement.client.User;
import icis.pcz.pl.examsmanagement.client.Question;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ExamsManagementServiceImpl extends RemoteServiceServlet implements ExamsManagementService
{
	private static final long serialVersionUID = 2L;
	
	@Override
	public Exam[] getOwnExams(String authorName) 
	{
		return null;
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

	@Override
	public boolean editExam(Exam exam, String userName)
	{
		Connection connection = getConnection();
		
		try
		{
			Statement statement = connection.createStatement();
			statement.executeUpdate("UPDATE exams SET name = '" + exam.getName() + 
				"', description = '" + exam.getDescription() +  
				"', duration = " + exam.getDuration().getMinutes() + 
				" WHERE exam_id = " + exam.getId() + " AND author = '" + userName + "'");
			return true;
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		} 
		finally
		{
			closeConnection(connection);
		}
		return false;
	}
	@Override
	public Group[] getAllUsers()
	{
		/*try
		{
			DirContext initial = new InitialDirContext();
			DirContext context = (DirContext) initial.lookup(getServletConfig().getInitParameter("ldapConnectionString"));
			
			@SuppressWarnings("rawtypes")
			NamingEnumeration list = context.list("ou=usergroups,dc=icis,dc=pcz,dc=pl");
			
			List<Group> groupList = new ArrayList<Group>();
			while (list.hasMore()) 
			{
				NameClassPair nc = (NameClassPair)list.next();
				Attributes attr = context.getAttributes(nc.getName() + ",ou=usergroups,dc=icis,dc=pcz,dc=pl");
				
				Group group = new Group(nc.getName().substring(nc.getName().indexOf("=")+1));
				
				if(containAttr("memberUid", attr.getIDs()))
				{
					@SuppressWarnings("rawtypes")
					NamingEnumeration innerList = attr.get("memberUid").getAll();
								
					while(innerList.hasMore())
					{
						group.addUserName(innerList.next().toString());
					}
				}
				groupList.add(group);
			}
				
			@SuppressWarnings("rawtypes")
			NamingEnumeration allUserslist = context.list("ou=users,dc=icis,dc=pcz,dc=pl");
			Group group = new Group("pozostali użytkownicy");
			while(allUserslist.hasMore())
			{
				NameClassPair nc = (NameClassPair)allUserslist.next();
				String name = nc.getName().substring(nc.getName().indexOf("=")+1);

				if(!contains(groupList,name))
				{
					group.addUserName(name);
				}
			}
			groupList.add(group);
			
			Group table[] = new Group[groupList.size()];
			for(int i=0 ; i<groupList.size() ; i++)
				table[i] = groupList.get(i);
			if(table.length == 0)
				return null;
			else
				return table;
			
		}
		catch(NamingException ex)
		{
			ex.printStackTrace();
		}*/
		return null;
	}
	private static boolean contains(List<Group> list,String name)
	{
		for(Group g : list)
		{
			if(g.contain(name))
				return true;
		}
		return false;
	}
	/*boolean  containAttr(String attrName, NamingEnumeration<String> attrNames)
	{
		try 
		{
			while(attrNames.hasMore())
			{
				if(attrNames.next().equals(attrName))
					return true;
			}
			return false;
		} 
		catch (NamingException e)
		{
			e.printStackTrace();
		}
		return false;
	}*/
	
	
	@Override
	public User[] getRegisteredUsers(int examId) 
	{
		List<User> users = new ArrayList<User>();
		Connection connection = getConnection();
		
		try
		{
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery
					("SELECT * FROM registrations WHERE exam_id = " + examId);
			
			while(resultSet.next())
			{
				users.add(new User(resultSet.getString("username"), Status.valueOf(resultSet.getString("status"))));
			}
			resultSet.close();
			
			if(users.size() > 0)
			{
				User table[] = new User[users.size()];
				for (int i=0 ; i<users.size() ; i++)
				{
					table[i] = users.get(i);
				}
				return table;
			}
			else
				return null;
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		} 
		finally
		{
			closeConnection(connection);
		}
		return null;
	}

	@Override
	public boolean deleteExam(int examId, String userName) 
	{
		Connection connection = getConnection();
		
		try
		{
			Statement statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM exams WHERE exam_id = " + examId + " AND author = '" + userName + "'");
			statement.executeUpdate("DELETE FROM registrations WHERE exam_id = " + examId);
			statement.executeUpdate("DELETE FROM answers WHERE exam_id = " + examId);
			statement.executeUpdate("DELETE FROM results WHERE exam_id = " + examId);
			statement.executeUpdate("DELETE FROM selected_answers WHERE exam_id = " + examId);
			
			return true;
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		} 
		finally
		{
			closeConnection(connection);
		}
		return false;
	}

	@Override
	public boolean registerUser(String userName, int examId)
	{
		Connection connection = getConnection();
		
		try
		{
			Statement statement = connection.createStatement();
			statement.executeUpdate("INSERT INTO registrations(exam_id, username, status) VALUES " +
					"(" + examId + ", '" + userName + "', '" + Status.nieaktywny.name() + "')");
			
			return true;
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		} 
		finally
		{
			closeConnection(connection);
		}
		return false;
	}

	@Override
	public boolean unRegisterUser(String userName, int examId) 
	{
		Connection connection = getConnection();
		
		try
		{
			Statement statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM registrations WHERE exam_id = " + examId +
					" AND username = '" + userName + "'");
			
			return true;
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		} 
		finally
		{
			closeConnection(connection);
		}
		return false;
	}

	@Override
	public boolean changeRegisterStatus(String[] userNames, int examId, Status status) 
	{
		Connection connection = getConnection();
		
		try
		{
			for(int i=0 ; i<userNames.length ; i++)
			{
				Statement statement = connection.createStatement();
				statement.executeUpdate("UPDATE registrations SET status = '" + status.name() + "' WHERE exam_id = " + examId +
					" AND username = '" + userNames[i] + "'");
			}
			return true;
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		} 
		finally
		{
			closeConnection(connection);
		}
		return false;
	}

	@Override
	public Result[] getResults(int examId) 
	{
		Connection connection = getConnection();
		List<Result> results = new ArrayList<Result>();
		
		try
		{
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM results NATURAL JOIN exams WHERE exam_id = " + examId 
					+ " AND result IS NOT NULL ORDER BY username, date");
			
			while(result.next())
			{
				results.add(new Result(result.getInt("result_id")
						,Integer.parseInt(result.getString("result"))
						,result.getString("username"),
						result.getString("date").substring(0, result.getString("date").length()-5),
						result.getInt("question_count")));
			}
			
			if(results.size() != 0)
			{
				Result[] table = new Result[results.size()];
				for(int i=0 ; i<results.size() ; i++)
				{
					table[i] = results.get(i);
				}
				return table;
			}
			else
				return null;
			

		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		} 
		finally
		{
			closeConnection(connection);
		}
		return null;
	}

	@Override
	public Exam getExamWithAnswers(int examId, String userName, int resultId) 
	{
		Connection connection = getConnection();
		try
		{
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery
				("SELECT * FROM exams WHERE exam_id = " + examId);
			resultSet.next();
			
			Exam exam = new Exam(resultSet.getInt("exam_id"),
				resultSet.getString("name"),
				resultSet.getString("description"),
				new Duration(resultSet.getInt("duration"), 0),
				Status.nieaktywny);
			
	
			int questionsCount = resultSet.getInt("question_count");
			
			//loading questions
			for(int i=0 ; i<questionsCount ; i++)
			{
				
				resultSet = statement.executeQuery("SELECT * FROM questions WHERE exam_id = " +
				exam.getId() + " AND question_nr = " + i);
				resultSet.next();
				
				Question q = new Question(resultSet.getString("content"));
					
				ResultSet innerResultSet = statement.executeQuery("SELECT * FROM answers WHERE exam_id" +
							" = " + exam.getId() + " AND question_nr = " + i);
					
				while(innerResultSet.next())
				{
					q.addAnswer(innerResultSet.getString("content"), innerResultSet.getInt("correct")==1 );
				}
				exam.addQuestion(q);
			}
			
			//ładowanie odpowiedzi
			
			statement = connection.createStatement();
			for(int i=0 ; i<exam.getQuestionCount() ; i++)
			{
				for(int j=0 ; j<exam.getQuestion(i).getAnswerCount() ; j++)
				{
					resultSet = statement.executeQuery("SELECT marked FROM selected_answers WHERE exam_id = " + 
					exam.getId() + " AND result_id = " + resultId +  " AND username = '" + userName
					+ "' AND question_nr = " + i + " AND answer_nr = " + j);
					resultSet.next();
							
					if(resultSet.getInt("marked") == 1)
						exam.getQuestion(i).getAnswer(j).setMarked(true);
					else
						exam.getQuestion(i).getAnswer(j).setMarked(false);
				}
			}
			return exam;

		}
		catch(SecurityException ex)
		{
			System.out.println("błąd bezpieczeństwa: " + ex.getMessage());
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	
		return null;
	}

	@Override
	public boolean registerGroup(Group group, int examId) 
	{
		for(String userName : group.getUserNames())
		{
			if(!registerUser(userName, examId))
				return false;
		}
		return true;
	}

}
