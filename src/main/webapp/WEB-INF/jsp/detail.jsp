<%--
  Created by IntelliJ IDEA.
  User: Chenzhimei
  Date: 2020/10/27
  Time: 2:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="common/tag.jsp" %>
<html>
<head>
    <title>秒杀商品详情页面</title>
    <%@include file="common/head.jsp" %>
</head>
<body>
<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h1>${seckill.name}</h1>
        </div>
        <div class="panel-body">
            <h2 class="text-danger">
                <span class="glyphicon glyphicon-time"></span>
                <span class="glyphicon" id="seckill-box"></span>
            </h2>
        </div>
    </div>
</div>

<div id="killPhoneModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title text-center">
                    <span class="glyphicon glyphicon-phone"></span>秒杀电话:
                </h3>
            </div>
        </div>

        <div class="modal-body">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2">
                    <input type="text" name="killPhone" id="killPhoneKey" placeholder="填写手机号码" class="form-control">
                </div>
            </div>
        </div>

        <div class="modal-footer">
            <span id="killPhoneMessage" class="glyphicon"></span>
            <button type="button" id="killPhoneBtn" class="btn btn-success">
                <span class="glyphicon glyphicon-phone"></span>
                提交
            </button>
        </div>
    </div>
</div>
</body>
<script src="${pageContext.request.contextPath}/static/plugins/jquery.js"></script>
<script src="${pageContext.request.contextPath}/static/plugins/bootstrap-3.3.0/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/static/plugins/jquery.cookie.min.js"></script>
<script src="${pageContext.request.contextPath}/static/plugins/jquery.countdown.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/seckill.js"></script>
<script type="text/javascript">
    $(function () {
        var startTimeVal = "${seckill.startTime.toLocalDate()} " + seckill.cloneZero("${seckill.startTime.toLocalTime()}");
        var endTimeVal = "${seckill.endTime.toLocalDate()} " + seckill.cloneZero("${seckill.endTime.toLocalTime()}");
        <%--var startTimeVal = seckill.convertTime(${seckill.startTime});--%>
        <%--var endTimeVal = seckill.convertTime(${seckill.endTime});--%>
        console.log("${seckill.startTime.year}-" + "${seckill.startTime.monthValue}-" + "${seckill.startTime.dayOfMonth}" +
            " ${seckill.startTime.hour}:" + "${seckill.startTime.minute}:" + "${seckill.startTime.second}");
        console.log("seckill.startTime.toLocalTime()=======" + "${seckill.startTime.toLocalTime()}");
        console.log("startTimeVal========" + startTimeVal);
        console.log("endTimeVal========" + endTimeVal);
        console.log(seckill);
        // 传入参数
        seckill.detail.init({
            seckillId:${seckill.seckillId},//seckill是后台传来的
            startTime: startTimeVal,
            endTime: endTimeVal
        })
    })
</script>

</html>
