package org.ual.aas.tasklists.views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ual.aas.tasklists.controllers.TaskListController;
import org.ual.aas.tasklists.models.Task;
import org.ual.aas.tasklists.models.TaskList;

import com.google.gson.Gson;


@WebServlet("/lists/*")

public class TaskListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Gson gson = new Gson();
	
	private void sendAsJson(HttpServletResponse resp, Object obj) throws IOException{
		resp.setContentType("application/json");
		String m = gson.toJson(obj);
		PrintWriter n  = resp.getWriter();
		n.print(m+"\n");
		n.flush();
		n.close();		
		}

	//GET
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	TaskListController controller = new TaskListController();
        String uri = req.getRequestURI();
        String[] splits = uri.split("/");

        if(splits.length == 3) { // GET /lists/
        	List<TaskList> taskList = controller.getTaskLists();
        	sendAsJson(resp, taskList);
            controller.close();
        }
        else if(splits.length == 4) { // GET /lists/<list_id>/
        	String listId = splits[3];
        	List<Task> taskLists = controller.getTaskList(listId);
        	sendAsJson(resp, taskLists);
            controller.close();
        }
    	else if(splits.length == 5) { // GET /lists/<list_id>/<task_id>
    		String listId = splits[3];
    		String taskId = splits[4];
        	Task taskLists = controller.getTask(listId, taskId);
        	sendAsJson(resp, taskLists);
            controller.close();
    	}
    	else {
  	      resp.sendError(404, "NOT FOUND!!!" );
    	}
	}
	
	//POST
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		TaskListController controller = new TaskListController();
		String uri = req.getRequestURI();
		String[] splits = uri.split("/");

		if(splits.length == 3) { // POST /lists/
			//String name = "";
			//String name = splits[4];
			StringBuilder buffer = new StringBuilder();
			BufferedReader reader = req.getReader();
			String name = reader.readLine();	        	
			String taskListId = controller.createTaskList(name);
			PrintWriter writer = resp.getWriter();
			writer.println("Criou-se com sucesso a lista: " + name);
			writer.close();
			controller.close();
		}
		else if(splits.length == 4) { // POST /lists/<list_id>/
			String listId = splits[3];
			String taskStatus = "Em processo";
			StringBuilder buffer = new StringBuilder();
			BufferedReader reader = req.getReader();
			String taskDescription = reader.readLine();
			String taskListId = controller.createTask(listId, taskDescription, taskStatus);
			PrintWriter writer = resp.getWriter();´
			//"{\"id\":" + taskListId + "}"
			writer.println("Tarefa Criada!");
			writer.close();
			controller.close();
		}
		else if(splits.length == 5) { // POST /lists/<list_id>/<task_id>/
			String listId = splits[3];
			String taskId = splits[4];
			StringBuilder buffer = new StringBuilder();
			BufferedReader reader = req.getReader();
			String taskStatus = reader.readLine();
			String taskListId = controller.changeTaskStatus(listId, taskId, taskStatus);
			PrintWriter writer = resp.getWriter();
			writer.println("Alterou-se com sucesso o estado da Tarefa!");
			writer.close();
			controller.close();
		}
		else {
			resp.sendError(404, "NOT FOUND!!!" );
		}
	}

	//PUT
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		TaskListController controller = new TaskListController();
		String uri = req.getRequestURI();
		String[] splits = uri.split("/");
		PrintWriter writer = resp.getWriter();

		if(splits.length == 4) { // PUT /lists/
			String listId = splits[3];
			String name = splits[4];
			if(!controller.hasTaskList(listId)) {
				writer.println("ERRO!\n Não Existe!");
			}
			else {
				controller.changeTaskListName(listId, name);
				writer.println("A lista" + listId + " mudou de nome!");
			}
			writer.close();
			controller.close();
		}
		
		else if(splits.length == 5) { // PUT /lists/<list_id>/<task_id>/
			String taskId = splits[4];
			StringBuilder buffer = new StringBuilder();
			BufferedReader reader = req.getReader();
			String taskDescription = reader.readLine();
			String taskListId = controller.changeTaskDescription(taskId, taskDescription);
			writer.println("Alterou-se a Descrição da Tarefa!");
			writer.close();
			controller.close();
		}
		else {
			resp.sendError(404, "NOT FOUND!!!" );
		}
	}

	//DELETE
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		TaskListController controller = new TaskListController();
		String uri = req.getRequestURI();
		String[] splits = uri.split("/");

		if(splits.length == 4) { // GET /lists/
			String listId = splits[3];
			String taskListId = controller.deleteTaskList(listId);
			PrintWriter writer = resp.getWriter();
			writer.println("Eliminou-se a Lista!");
			writer.close();
			controller.close();
		}
		else if(splits.length == 5) { // GET /lists/<list_id>/<task_id>/
			String listId = splits[3];
			String taskId = splits[4];
			String taskListId = controller.deleteTask(listId, taskId);
			PrintWriter writer = resp.getWriter();
			writer.println("Eliminou-se a Tarefa!");
			writer.close();
			controller.close();
		}
		else {
			resp.sendError(404, "NOT FOUND!!!" );
		}
	}
}