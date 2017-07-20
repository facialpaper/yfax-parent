package com.yfax.webapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yfax.webapi.dao.TaskDetailDao;
import com.yfax.webapi.vo.TaskDetailVo;

/**
 * 任务详情
 * @author Minbo
 */
@Service
public class TaskDetailService{
	
	@Autowired
	private TaskDetailDao dao;
	
	public TaskDetailVo selectTaskDetailByTaskId(String taskId){
		return this.dao.selectTaskDetailByTaskId(taskId);
	}

}
