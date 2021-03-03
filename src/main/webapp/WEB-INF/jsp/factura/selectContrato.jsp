<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.*" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<petclinic:layout pageName="SelectContrato">
	<jsp:body>		        	
		<form:form modelAttribute="selectContrato" class="form-horizontal" id="select-contrato">
		   <div class="form-group has-feedback">  
		   	<label>Seleccione el contrato del que quiere ver facturas:</label>      		 
				<select id="contratoId" name="contrato" required>
				<c:forEach items="${contratos}" var="contrato">
				<option value="${contrato.id}"><c:out value="Contrato con ${contrato.explotacionGanadera.ganadero.firstName} ${contrato.explotacionGanadera.ganadero.lastName}"/></option>
				</c:forEach>
				</select>
		        </div>
		        <div class="form-group">
		            <div class="col-sm-offset-2 col-sm-10">
		                        <button class="btn btn-default" type="submit">Mostrar facturas</button>
		            </div>
		        </div>
		    </form:form>
	</jsp:body>
</petclinic:layout>