//package server.dao.implementation;
//
//import org.springframework.stereotype.Repository;
//import server.dao.TaskDao;
//import server.dao.TaskHistoryDao;
//import server.domain.Task;
//import server.domain.TaskHistory;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public class TaskHistoryDaoImpl implements TaskHistoryDao {
//
//    private final TaskDao taskDao;
//    public TaskHistoryDaoImpl(TaskDao taskDao) {
//        this.taskDao = taskDao;
//    }
//    @Override
//    public Optional<List<TaskHistory>> getTaskHistory(String title) {
//
//        Optional<Task> optionalTask=taskDao.getTaskByTitle(title);
//        if(optionalTask.isPresent())
//        {
//            Task task=optionalTask.get();
//            return Optional.of(task.getHistory(task));
//
//        }
//        else {
//
//            return Optional.empty();
//        }
//
//    }
//@Override
//    public void setTaskHistory(TaskHistory taskHistory, String title) {
//
//        Optional<Task> optionalTask=taskDao.getTaskByTitle(title);
//        if(optionalTask.isPresent())
//        {
//            Task task=optionalTask.get();
//            task.setHistory(taskHistory);
//        }
//
//    }
//
//}
