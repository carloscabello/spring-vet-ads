<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="path" required="true"
              description="Name of corresponding property in bean object" %>
<%@ attribute name="items" required="true" type="java.util.List"
              description="Lista de elementos que se pasarán como opciones al selector" %>
<%@ attribute name="label" required="true"
description="Label appears in red color if input is considered as invalid after submission" %>
<%@ attribute name="itemValue" required="true"
description="Name of the property mapped to 'value' attribute of the 'option' tag" %>
<%@ attribute name="itemLabel" required="true"
description="Name of the property mapped to the inner text of the 'option' tag" %>
<%@ attribute name="itemClassPropertyName" required="true"
description="Nombre de la propiedad que determina la clase que recibe el tag 'option'" %>
<%@ attribute name="itemClassPrefix" required="true"
description="Prefijo que se coloca a la clase que recibe el tag 'option'" %>

<c:set var="itemClassPropertyName" value="${(empty itemClassPropertyName) ? '' : itemClassPropertyName}" />
<c:set var="itemClassPrefix" value="${(empty itemClassPrefix) ? '' : itemClassPrefix}" />
<spring:bind path="${path}">
	<c:set var="cssGroup" value="form-group ${status.error ? 'has-error' : '' }"/>
	<c:set var="valid" value="${not status.error and not empty status.actualValue}"/>
    <div class="${cssGroup}">
        <label class="col-sm-2 control-label">${label}</label>

        <div class="col-sm-10">
            <select name="${path}">
            	<c:forEach items="${items}" var="item">
            		<option 
            		class="${item[itemClassPropertyName]}" 
            		value="${item[itemValue]}">
            			<c:out value="${item[itemLabel]}"/></option>
            	</c:forEach>
            </select>
            <c:if test="${valid}">
                <span class="glyphicon glyphicon-ok form-control-feedback" aria-hidden="true"></span>
            </c:if>
            <c:if test="${status.error}">
                <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                <span class="help-inline">${status.errorMessage}</span>
            </c:if>
        </div>
    </div> 
</spring:bind>

