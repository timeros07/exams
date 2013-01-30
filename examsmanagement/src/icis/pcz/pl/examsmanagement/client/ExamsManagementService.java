package icis.pcz.pl.examsmanagement.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
/**
 * 
 * @author tomek
 *
 */

@RemoteServiceRelativePath("examsManagement")
public interface ExamsManagementService extends RemoteService
{
	/**
	 * loads exams which user created
	 * @param authorName the name of the user who created exams
	 * @return exams which user created
	 */
	Exam[] getOwnExams(String authorName);
	/**
	 * edit exam's properties such as name, description and duration
	 * @param exam exam that will be edited
	 * @param userName the name of the user
	 * @return true if everything goes correct, false if not
	 */
	boolean editExam(Exam exam, String userName);
	/**
	 * returns All users from LDAP server
	 * @return All users divided by group
	 */
	Group[] getAllUsers();
	/**
	 * returns users which became registered to the exam with given id
	 * @param examId identify number of the exam
	 * @return registered users
	 */
	User[] getRegisteredUsers(int examId);
	/**
	 * delete exam with given id
	 * @param examId identify number of the exam
	 * @param userName the name of the user
	 * @return true if everything goes correct, false if not
	 */
	boolean deleteExam(int examId, String userName);
	/**
	 * register user to the exam by given id
	 * @param userName the name of the user which became registered
	 * @param examId identify number of the exam
	 * @return true if everything goes correct, false if not
	 */
	boolean registerUser(String userName, int examId);
	/**
	 * register all users which belongs to group by the given name
	 *  to the exam by given id
	 * @param group the group
	 * @param examId identify number of the exam
	 * @return true if everything goes correct, false if not
	 */
	boolean registerGroup(Group group, int examId);
	/**
	 * unregister user from exam with a given id
	 * @param userName the name of the user which was registered
	 * @param examId identify number of the exam
	 * @return true if everything goes correct, false if not
	 */
	boolean unRegisterUser(String userName, int examId);
	/**
	 * change register status for user
	 * @param userNames the names of the users which were registered
	 * @param examId identify number of the exam
	 * @param status the status of register
	 * @return true if everything goes correct, false if not
	 */
	boolean changeRegisterStatus(String[] userNames, int examId, Status status);
	/**
	 * returns results of the exam, results contains scores all users which finished exam
	 * @param examId identify number of the exam
	 * @return results of the exam  
	 */
	Result[] getResults(int examId);
	/**
	 * return Exam contain all questions and user's answers
	 * @param examId identify number of the exam
	 * @param userName the name of the user which finished exam
	 * @param resultId identify number of the result
	 * @return Exam
	 */
	Exam getExamWithAnswers(int examId, String userName, int resultId);
}
