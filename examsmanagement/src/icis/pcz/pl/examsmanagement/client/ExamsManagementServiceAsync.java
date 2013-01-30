package icis.pcz.pl.examsmanagement.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExamsManagementServiceAsync 
{
	void getOwnExams(String authorName, AsyncCallback<Exam[]> callback);

	void editExam(Exam exam, String userName, AsyncCallback<Boolean> callback);

	void getAllUsers(AsyncCallback<Group[]> callback);

	void getRegisteredUsers(int examId, AsyncCallback<User[]> callback);

	void deleteExam(int examId, String userName,
			AsyncCallback<Boolean> callback);

	void registerUser(String userName, int examId,
			AsyncCallback<Boolean> callback);

	void unRegisterUser(String userName, int examId,
			AsyncCallback<Boolean> callback);

	void changeRegisterStatus(String[] userNames, int examId, Status status,
			AsyncCallback<Boolean> callback);

	void getResults(int examId, AsyncCallback<Result[]> callback);

	void getExamWithAnswers(int examId, String userName, int resultId,
			AsyncCallback<Exam> callback);

	void registerGroup(Group group, int examId, AsyncCallback<Boolean> callback);

}
