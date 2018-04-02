<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="/WEB-INF/mytag.tld" prefix="my"%>
<%@ taglib uri="/WEB-INF/bluemobi-tag.tld" prefix="bmtag"%>
<link href="<%=request.getContextPath()%>/res/commen.css"
	rel="stylesheet" />
<h2 class="contentTitle">
	<bmtag:message messageKey="新增现金券品种" />
</h2>
<div class="pageContent">
	<form method="post" action="couponVariety/addCouponVariety.do"
		class="pageForm required-validate" enctype="multipart/form-data"
		onsubmit="return iframeCallback(this, navTabAjaxDone)">
		<input type="hidden" name="elpiPoweruser" value="3"/>
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>
					<bmtag:message messageKey="现金券名称" />
				</dt>
				<dd>
					<input name="covaActivityName" class="textInput required"
						maxlength="20" style="width: 165px;" /> <span class="info"></span>
				</dd>
			</dl>
			<dl>
				<dt>
					<bmtag:message messageKey="电桩限制" />
				</dt>
				<dd>		 
						<select name="covaLimitation" class="select_Style ">
							<option value="1">仅限交流充电桩</option>							
							<option value="2" >仅限直流充电桩</option>
							<option value="3" >不限充电桩</option>
						</select>
						<span class="info"></span>	
				</dd>
			</dl>
			<dl>
				<dt>
					<bmtag:message messageKey="面值" />
				</dt>
				<dd>
					<input name="covaCouponValue" class="textInput required number digits" id="covaCouponValue" 
						maxlength="4" style="width: 65px;" />元 <span class="info"></span>
				</dd>
			</dl>
			<dl>
				<dt>
					<bmtag:message messageKey="使用条件" />
				</dt>
				<dd>
					<span style="display:block;float:left;padding-top:5px;">满</span><input name="covaCouponCondition" class="textInput required number digits" id="covaCouponCondition"
						maxlength="20" style="width: 60px;" />元可使用 <span class="info"></span>
				</dd>
			</dl>
			<!-- <dl>
				<dt>
					<bmtag:message messageKey="有效期" />
				</dt>
				<dd>
					<input name="covaCouponTerm" class="textInput required number" max="365"
						maxlength="20" style="width: 65px;" />天 <span class="info"></span>
				</dd>
			</dl> -->
			<dl>
				<dt>
					<bmtag:message messageKey="状态" />
				</dt>
				<dd>
					<select name="covaStutas" class="select_Style ">
							<option value="0">已上架</option>							
							<option value="1" >已下架</option>
					</select>
					<span class="info"></span>
				</dd>
			</dl>
			<dl>
				<dt>
					<bmtag:message messageKey="标签" />
				</dt>
				<dd>
					<input name="covaLabel" class="textInput "
						maxlength="20" style="width: 165px;" /> <span class="info"></span>
					<span class="info"></span>
				</dd>
			</dl>
			<dl>
				<dt><bmtag:message messageKey=""/>备注</dt>
				<dd>
					<textarea name="covaRemark" id="raInChange_raInRemarks"  maxlength="100" cols="25" rows="4"></textarea>
					<span class="info"></span>
				</dd>
			</dl>
		</div>
		
		<div class="formBar">
			<ul>
				<li><bmtag:button messageKey="common.button.submit" onclick="bombBox();"
						type="button"  />
				</li>
				<li><bmtag:button messageKey="common.button.back" type="button"
						dwzClass="close" />
				</li>
			</ul>
			<div style="display:none"><bmtag:button type="submit" id="hideFormSubmitter" />
			</div>
		</div>
	</form>
</div>
<script type="text/javascript">
	function bombBox() {
		var covaCouponCondition = parseInt($("#covaCouponCondition").val());
		var covaCouponValue = parseInt($("#covaCouponValue").val());
		if(covaCouponCondition!=0 && covaCouponValue>=covaCouponCondition){
			alertMsg.error("现金券面值要小于使用条件的值");
			return;
		}
		alertMsg.confirm("添加后立刻生效，请确认是否添加？",{
			okCall: function(){
				$("#hideFormSubmitter").click();
			}
		})
	}
	
</script>
