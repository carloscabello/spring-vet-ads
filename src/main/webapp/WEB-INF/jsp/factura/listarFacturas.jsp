<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<petclinic:layout pageName="Lista productos">

	<c:if test="${message != null}"><c:out value="${message}"/></c:if>

	<table id="productosTable" class="table table-striped">
        <thead>
        <tr>
        	<th style="width: 240px;">Fecha emisión</th>
            <th style="width: 400px;">Pagado</th>
            <th style="width: 160px;"></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${facturas}" var="factura">
            <tr>
            	<td>
                    <fmt:formatDate value="${factura.fechaEmision}" type="date" pattern="dd/MM/yyyy hh:mm a"/>
                </td> 
                <td>
                    <c:choose>
                    <c:when test="${factura.esPagado==true}">
                    <img height="25px" alt="Sí" src="/resources/images/tickverde.png"/>
                    </c:when>
                    <c:otherwise>
                    <img height="25px" alt="No" src="/resources/images/cruzroja.png"/>
                    </c:otherwise>
                    </c:choose>
                </td>
                <td>
                <spring:url value="/factura/{facturaId}/show" var="showFacturaUrl">
                <spring:param name="facturaId" value="${factura.id}"/>
                </spring:url>
                <a href="${fn:escapeXml(showFacturaUrl)}"><input class="btn btn-default" type="button" value="Mostrar factura"/></a>
                </td>
                <sec:authorize access="hasAnyAuthority('veterinario')">
                <td>
                	<c:if test="${factura.esPagado==false}">
                    <spring:url value="/contrato/{contratoId}/factura/{facturaId}/edit" var="facturaEditUrl">
                        <spring:param name="facturaId" value="${factura.id}"/>
                        <spring:param name="contratoId" value="${contratoId}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(facturaEditUrl)}"><input class="btn btn-default" type="button" value="Confirmar pago"/></a>
                    </c:if>
                </td>
                </sec:authorize>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <sec:authorize access="hasAnyAuthority('veterinario')">
    <spring:url value="/contrato/{contratoId}/factura/new" var="facturaNewUrl">
                        <spring:param name="contratoId" value="${contratoId}"/>
                    </spring:url>
    <a href="${fn:escapeXml(facturaNewUrl)}"><input class="btn btn-default" type="button" value="Añadir una nueva factura"/></a>
    </sec:authorize>
</petclinic:layout>