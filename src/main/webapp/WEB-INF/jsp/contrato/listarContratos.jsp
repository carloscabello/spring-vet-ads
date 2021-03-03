<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<petclinic:layout pageName="Lista Contratos">

	<table id="contractsTable" class="table table-striped">
        <thead>
        <tr>
        <sec:authorize access="hasAnyAuthority('veterinario')">
        	<th style="width: 200px;">Ganadero</th>
        </sec:authorize>
        <sec:authorize access="hasAnyAuthority('ganadero')">
        	<th style="width: 200px;">Veterinario</th>
        </sec:authorize>
            <th style="width: 160px;">Fecha de solicitud</th>
            <th style="width: 120px;">Estado</th>
            <th style="width: 120px;"></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${contratos}" var="contratos">
            <tr>
            	<td>
            	
            	<sec:authorize access="hasAnyAuthority('veterinario')">
            		<c:out value="${contratos.explotacionGanadera.ganadero.firstName}"/>&nbsp;<c:out value="${contratos.explotacionGanadera.ganadero.lastName}"/>
            	</sec:authorize>
            	
            	<sec:authorize access="hasAnyAuthority('ganadero')">
        	        <c:out value="${contratos.veterinario.firstName}"/>&nbsp;<c:out value="${contratos.veterinario.lastName}"/>
                </sec:authorize>
            	
                </td> 
                <td>
               <c:out value="${contratos.fechaPeticion}"/>
                </td>
                <td>
                    <c:out value="${contratos.estado}"/>
                </td> 
                <td>
                   <spring:url value="/contrato/{contratoId}/show" var="gestionarContrato">
                          <spring:param name="contratoId" value="${contratos.id}"/>
                     </spring:url>
                     <a href="${fn:escapeXml(gestionarContrato)}"><input type="button" class="btn btn-default" value="Gestionar"></a>
                </td>         
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>