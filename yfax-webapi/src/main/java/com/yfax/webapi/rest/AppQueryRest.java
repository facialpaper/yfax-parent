package com.yfax.webapi.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yfax.webapi.service.ApkUrlService;
import com.yfax.webapi.service.IncomeHisService;
import com.yfax.webapi.service.IncomeSetService;
import com.yfax.webapi.service.TaskDetailService;
import com.yfax.webapi.service.UserTaskListService;
import com.yfax.webapi.service.UsersService;
import com.yfax.webapi.service.WithdrawHisService;
import com.yfax.webapi.utils.DateUtil;
import com.yfax.webapi.utils.JsonResult;
import com.yfax.webapi.utils.ResultCode;
import com.yfax.webapi.utils.StrUtil;
import com.yfax.webapi.vo.ApkUrlVo;
import com.yfax.webapi.vo.IncomeHisVo;
import com.yfax.webapi.vo.IncomeSetVo;
import com.yfax.webapi.vo.TaskDetailVo;
import com.yfax.webapi.vo.UserTaskListVo;
import com.yfax.webapi.vo.UsersVo;
import com.yfax.webapi.vo.WithdrawHisVo;

/**
 * @author Minbo.He
 * 查询接口
 */
@RestController
@RequestMapping("/api/cfdb")
public class AppQueryRest {

	protected static Logger logger = LoggerFactory.getLogger(AppQueryRest.class);

	@Autowired
	private IncomeHisService incomeHisService;
	@Autowired
	private WithdrawHisService withdrawHisService;
	@Autowired
	private UsersService usersService;
	@Autowired
	private UserTaskListService userTaskListService;
	@Autowired
	private IncomeSetService incomeSetService;
	@Autowired
	private ApkUrlService apkUrlService;
	@Autowired
	private TaskDetailService taskDetailService;
	
	@RequestMapping("/")
	public String greeting() {
		return "Greetings from 冲返单包 开放Api!";
	}

	/**
	 * 收益记录接口
	 */
	@RequestMapping("/queryIncomeHis")
	public JsonResult queryIncomeHis(String phoneId) {
		if(!StrUtil.null2Str(phoneId).equals("")) {
			UsersVo users = this.usersService.selectUsersByPhoneId(phoneId);
			if(users != null) {
				List<IncomeHisVo> incomeList = this.incomeHisService.selectIncomeHis(users.getPhoneId());
				return new JsonResult(ResultCode.SUCCESS, "成功", incomeList);
			}else {
				return new JsonResult(ResultCode.SUCCESS_NO_USER, "用户不存在", users);
			}
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR, "参数错误", null);
		}
	}

	/**
	 * 兑换提现记录接口
	 */
	@RequestMapping("/queryWithdrawHis")
	public JsonResult queryWithdrawHis(String phoneId) {
		if(!StrUtil.null2Str(phoneId).equals("")) {
			UsersVo users = this.usersService.selectUsersByPhoneId(phoneId);
			if(users != null) {
				List<WithdrawHisVo> withdrawHis = this.withdrawHisService.selectWithdrawHis(users.getPhoneId());
				return new JsonResult(ResultCode.SUCCESS, "成功", withdrawHis);
			}else {
				return new JsonResult(ResultCode.SUCCESS_NO_USER, "用户不存在", users);
			}
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR, "参数错误", null);
		}
	}
	
	/**
	 * 个人资产接口
	 */
	@RequestMapping("/queryOwnInfo")
	public JsonResult queryOwnInfo(String phoneId) {
		if(!StrUtil.null2Str(phoneId).equals("")) {
			UsersVo users = this.usersService.selectUsersByPhoneId(phoneId);
			if(users != null) {
				//TODO 临时写死配合APP测试
				users.setTotalncome("1099");
				users.setTodayIncome("345");
				users.setCurrentTime(DateUtil.getCurrentLongDateTime());
				return new JsonResult(ResultCode.SUCCESS, "成功", users);
			}else {
				return new JsonResult(ResultCode.SUCCESS_NO_USER, "用户不存在", users);
			}
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR, "参数错误", null);
		}
	}
	
	/**
	 * 个人任务列表接口
	 */
	@RequestMapping("/queryTaskList")
	public JsonResult queryTaskList(String phoneId) {
		if(!StrUtil.null2Str(phoneId).equals("")) {
			UsersVo users = this.usersService.selectUsersByPhoneId(phoneId);
			if(users != null) {
				List<UserTaskListVo> userTaskList = this.userTaskListService.selectUserTaskListByPhoneId(users.getPhoneId());
				return new JsonResult(ResultCode.SUCCESS, "成功", userTaskList);
			}else {
				return new JsonResult(ResultCode.SUCCESS_NO_USER, "用户不存在", null);
			}
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR, "参数错误", null);
		}
	}
	
	/**
	 * 任务详情接口
	 */
	@RequestMapping("/queryTaskDetail")
	public JsonResult queryTaskDetail(String phoneId, String taskId) {
		if(!StrUtil.null2Str(phoneId).equals("")) {
			UsersVo users = this.usersService.selectUsersByPhoneId(phoneId);
			if(users != null) {
				TaskDetailVo taskDetailVo = this.taskDetailService.selectTaskDetailByTaskId(taskId);
				return new JsonResult(ResultCode.SUCCESS, "成功", taskDetailVo);
			}else {
				return new JsonResult(ResultCode.SUCCESS_NO_USER, "用户不存在", null);
			}
		}else {
			return new JsonResult(ResultCode.PARAMS_ERROR, "参数错误", null);
		}
	}
	
	/**
	 * APP初始化配置接口
	 */
	@RequestMapping("/queryAppConfig")
	public JsonResult queryAppConfig() {
		Map<String,List<IncomeSetVo>> allMap = new HashMap<String,List<IncomeSetVo>>();
		//提现金额配置
		List<IncomeSetVo> incomeSetlist = this.incomeSetService.selectIncomeSetList();
		allMap.put("incomeSet", incomeSetlist);
		return new JsonResult(ResultCode.SUCCESS, "成功", allMap);
	}
	
	/**
	 * 获取分享链接接口（APP下载链接）
	 */
	@RequestMapping("/querySharingUrl")
	public JsonResult getSharingUrl() {
		ApkUrlVo apkUrlVo = this.apkUrlService.selectApkUrl();
		return new JsonResult(ResultCode.SUCCESS, "成功", apkUrlVo);
	}
}