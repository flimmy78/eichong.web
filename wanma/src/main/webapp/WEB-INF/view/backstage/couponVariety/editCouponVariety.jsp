<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="/WEB-INF/mytag.tld" prefix="my"%>
<%@ taglib uri="/WEB-INF/bluemobi-tag.tld" prefix="bmtag"%>

<link href="<%=request.getContextPath()%>/res/commen.css"
	rel="stylesheet" />
<script type="text/javascript">
</script>
<h2 class="contentTitle">
	<bmtag:message messageKey="修改现金券品种" />
</h2>
<div class="pageContent">
	<form method="post" action="couponVariety/changeCouponVariety.do"
		class="pageForm required-validate" enctype="multipart/form-data"
		onsubmit="return iframeCallback(this, navTabAjaxDone)">
	 <input type="hidden" name="pkCouponVariety" value="${tblCouponVariety.pkCouponVariety}" />
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>
					<bmtag:message messageKey="现金券名称" />
				</dt>
				<dd>
					<input name="covaActivityName"
						value="${tblCouponVariety.covaActivityName}"
						class="textInput " maxlength="20" style="width: 165px;"  readOnly="readOnly"/>
					<span class="info"></span>
				</dd>
			</dl>
			<dl>
				<dt>
					<bmtag:message messageKey="电桩限制" />
				</dt>
				<dd>
					<select name="covaLimitation" class="select_Style">
							<option value="1" ${tblCouponVariety.covaLimitation==1 ? 'selected="selected"' : ''} >
								仅限交流充电桩
							</option>
							<option value="2" ${tblCouponVariety.covaLimitation==2 ? 'selected="selected"' : ''} >
								仅限直流充电桩
							</option>
							<option value="3" ${tblCouponVariety.covaLimitation==3 ? 'selected="selected"' : ''} >
								不限充电桩
							</option>
					</select> <span class="info"></span>
				</dd>
			</dl>
			<dl>
				<dt>
					<bmtag:message messageKey="面值" />
				</dt>
				<dd>
					<input name="covaCouponValue"
						value="${tblCouponVariety.covaCouponValue}" id="covaCouponValue"
						class="textInput required number" maxlength="4" style="width: 165px;" />元
					<span class="info"></span>
				</dd>
			</dl>
			<dl>
				<dt>
					<bmtag:message messageKey="使用条件" />
				</dt>
				<dd>
					<span style="display:block;float:left;padding-top:5px;padding-right:5px;">满</span><input name="covaCouponCondition" id="covaCouponCondition"
						value="${tblCouponVariety.covaCouponCondition}"
						class="textInput required number" maxlength="4" style="width: 40px;text-align:center;" />元可使用
					<span class="info"></span>
				</dd>
			</dl>
			<!--  <dl>
				<dt>
					<bmtag:message messageKey="有效期" />
				</dt>
				<dd>
					<input name="covaCouponTerm"
						value="${tblCouponVariety.covaCouponTerm}" max="365"
						class="textInput required number" maxlength="20" style="width: 165px;" />天
					<span class="info"></span>
				</dd>
			</dl>-->
			 <dl>
				<dt>
					<bmtag:message messageKey="状态" />
				</dt>
				<dd>
					<select name="covaStutas" class="select_Style" id="covaStutasValue">
							<option value="0" ${tblCouponVariety.covaStutas==0 ? 'selected="selected"' : ''} >
								上架
							</option>
							<option value="1" ${tblCouponVariety.covaStutas==1 ? 'selected="selected"' : ''} >
								下架
							</option>
					</select> <span class="info"></span>
				</dd>
			</dl> 
			<dl>
				<dt>
					<bmtag:message messageKey="标签" />
				</dt>
				<dd>
					<input name="covaLabel" class="textInput " value="${tblCouponVariety.covaLabel}"
						maxlength="20" style="width: 165px;" /> <span class="info"></span>
					<span class="info"></span>
				</dd>
			</dl>
			<dl>
				<dt>
					<bmtag:message messageKey="备注" />
				</dt>
				<dd>
				<textarea name="covaRemark" id="raInChange_raInRemarks"  maxlength="100" cols="25" rows="4">${tblCouponVariety.covaRemark}</textarea>
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
			<div style="display:none" > <bmtag:button type="submit"  id="hideFormSubmitter"/>
			</div>
		</div>
	</form>
</div>
<script type="text/javascript">
	function bombBox() {
		var covaCouponCondition = parseInt($("#covaCouponCondition").val());
		var covaCouponValue = parseInt($("#covaCouponValue").val());
		var covaStutas = $('#covaStutasValue option:selected').val();
		if(covaCouponCondition!=0 && covaCouponValue>=covaCouponCondition){
			alertMsg.error("现金券面值要小于使用条件的值");
			return;
		}
		if(covaStutas==1){
			alertMsg.confirm("下架提示：当现金劵下架后，如果已经配置的活动中有这个现金劵品种，该现金劵品种会自动从这些活动奖品中消失。"
					+"该现金劵品种的兑换码，已经兑换成劵的，可以继续使用；如果还没兑换，兑换码会失效",{
				okCall: function(){
					$("#hideFormSubmitter").click();
				}
			})
		}else{
					$("#hideFormSubmitter").click();
		}
		
	}
</script>