package cn.com.bettle.net.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 
 * 系统角色实体类
 * 
 * @author zhangshuai
 * 
 */
public class Role {
	private String roleId;
	private String name;
	private String intro;
	private String type;
	private int sort;
	private Date createTime;
	// 角色拥有的资源
	private Collection<Res> reses;
	// 角色能够使用的功能，即相应操作
	private Collection<ModuleFun> ModuleFuns;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Collection<Res> getReses() {
		return reses;
	}

	public void setReses(Collection<Res> reses) {
		this.reses = reses;
	}

	public Collection<ModuleFun> getModuleFuns() {
		return ModuleFuns;
	}

	public void setModuleFuns(Collection<ModuleFun> moduleFuns) {
		ModuleFuns = moduleFuns;
	}

	public Collection<String> getPermissionsAsString() {
		List<String> permissions = new ArrayList<String>();
		Collection<ModuleFun> funs = this.getModuleFuns();
		if (funs != null) {
			for (ModuleFun fun : getModuleFuns()) {
				permissions.add(fun.getFunName());
			}
		}
		return permissions;
	}

}
