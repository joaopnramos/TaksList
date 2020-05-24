package org.ual.aas.tasklists.controllers;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.ual.aas.tasklists.models.Task;
import org.ual.aas.tasklists.models.TaskList;

public class TaskListController {
	private SessionFactory sessionFactory;
	
	public TaskListController() {
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure("resource/hibernate.cfg.xml")
				.build();
        try {
        	this.sessionFactory  = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch(Exception e) {
        	e.printStackTrace();
        	System.exit(1);
        }
	}
	
	private List<TaskList> readTaskLists() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List resultSet = session.createQuery("from TaskList").getResultList();
        session.flush();
        session.getTransaction().commit();
        session.close();	
        return resultSet;
	}
	
	private TaskList readTaskList(String taskListId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        TaskList t = session.get(TaskList.class, Integer.valueOf(taskListId));
        session.flush();
        session.getTransaction().commit();
        session.close();

		return t;
	}
	
	public void writeTaskList(TaskList taskList) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(taskList);
        session.getTransaction().commit();
        session.close();
	}
	
	public void close() {
		sessionFactory.close();
	}
	
	public List<TaskList> getTaskLists() {
		// TODO Auto-generated method stub
		return readTaskLists();
	}

	public boolean hasTasksLists() {
		boolean t =true;
		if (readTaskLists().equals(null)) {
			t = true;
		}
		else t = false;
		
		return t;
	}

	public boolean hasTaskList(String name) {
		// TODO Auto-generated method stub
		boolean t = true;
		List<TaskList> taskLists = null;
		taskLists = getTaskLists();
		for (TaskList taskList1 : taskLists) {
			if (String.valueOf(taskList1.getId()).equals(name)) {
				t = true;
				break;
			}
			else t = true;
		}
		return t;
	}

	public String createTaskList(String name) {
		TaskList taskList = new TaskList(name);
		this.writeTaskList(taskList);
		return ""+taskList.getId();
	}

	public List<Task> getTaskList(String name) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        TaskList t = session.get(TaskList.class, Integer.valueOf(name));
        System.out.println(t.getName());
        List<Task> w;
        for (Task task : t.getTasks()) {
        	System.out.println(task.getStatus() + "\t" + task.getDescription());
        }
        w = t.getTasks();
		session.flush();
        session.getTransaction().commit();
        session.close();
        
		return w;
	}
	
	public String changeTaskListName(String name, String newName) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        TaskList t = session.get(TaskList.class, Integer.valueOf(name));
        t.setName(newName);
        session.merge(t);
		session.flush();
        session.getTransaction().commit();
        session.close();
        return newName;
	}
	
	public String deleteTask(String taskListId, String taskId) {
		int n = Integer.parseInt(taskId);
		int n1 = Integer.parseInt(taskListId);
		Task task;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        TaskList t = session.get(TaskList.class, n1);
        task = session.get(Task.class, n);
        t.removeTask(task);
        session.saveOrUpdate(t);
        session.delete(task);
		session.flush();
        session.getTransaction().commit();
        session.close();
        return "Sucesso a apagar a tarefa!";

	}
	
	public boolean hasTask(String taskListId, String taskId) {
		return false;
	}

	public String changeTaskDescription(String taskId, String taskDescription) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Task t_description = session.get(Task.class, Integer.valueOf(taskId));
        t_description.setDescription(taskDescription);
        session.merge(t_description);
		session.flush();
        session.getTransaction().commit();
        session.close();
        return "Esta é a nova descrição: " + taskDescription;

	}

	public String changeTaskStatus(String taskListId, String taskId, String status) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Task t_status = session.get(Task.class, Integer.valueOf(taskId));
        t_status.setStatus(status);
        session.merge(t_status);
		session.flush();
        session.getTransaction().commit();
        session.close();
        return "Este é o novo estado: " + status;

	}

	public String createTask(String taskListId, String description, String status) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
		int n = Integer.parseInt(taskListId);
		TaskList t1 = session.load(TaskList.class, n);
		t1.addTask(new Task(description, status));
		session.saveOrUpdate(t1);
		session.flush();
        session.getTransaction().commit();
        session.close();
        
		return String.valueOf(t1.getId());
	}

	public Task getTask(String taskListId, String taskId) {
		int n = Integer.parseInt(taskListId);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Task t1 = new Task();
        TaskList t = session.get(TaskList.class, n);
        for (Task task : t.getTasks()) {
        	if (String.valueOf(task.getId()).equals(taskId)) {
        		t1 = task;
        	}
        }
        session.flush();
        session.getTransaction().commit();
        session.close();
		return t1;
		}

	public String deleteTaskList(String Id) {
		int n = Integer.parseInt(Id);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        TaskList t = session.get(TaskList.class, n);
        session.delete(t);
        session.flush();
        session.getTransaction().commit();
        session.close();
        return "Sucesso a apagar a Lista!";

	}

}
