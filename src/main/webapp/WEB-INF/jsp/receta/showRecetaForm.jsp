<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<petclinic:layout pageName="Mostrar Receta">
				
				<h1><label>Receta:</label></h1>
				
				<sec:authorize access="hasAnyAuthority('ganadero')">
				<label>Cita sobre la que se hizo la receta:</label><c:out value="${receta.cita.fechaHoraInicio} con ${receta.cita.contrato.veterinario.firstName} ${receta.cita.contrato.veterinario.lastName}"/></br>
				</sec:authorize>
				<sec:authorize access="hasAnyAuthority('veterinario')">
				<label>Cita sobre la que se hizo la receta:</label><c:out value="${receta.cita.fechaHoraInicio} con ${receta.cita.contrato.explotacionGanadera.ganadero.firstName} ${receta.cita.contrato.explotacionGanadera.ganadero.lastName}"/></br>
				</sec:authorize>
				<label>Fecha realización: </label><c:out value="${receta.fechaRealizacion}"/></br>
				<label>Descripción: </label><c:out value="${receta.descripcion}"/></br>
			    <label>Facturada: </label>
			    <c:choose>
			    <c:when test="${receta.esFacturado==true}">
			    Sí
			    </c:when>
			    <c:otherwise>
			    No
			    </c:otherwise>
			    </c:choose>
			    </br></br>
				
				<h2><label>Productos:</label></h2>
				<c:forEach items="${lineasReceta}" var="lineaReceta">
				<label><c:out value="${lineaReceta.producto.name}"/></label></br>
				Cantidad: <c:out value="${lineaReceta.cantidad}"/> unidades </br>
				Precio: <c:out value="${lineaReceta.precioVenta}"/> por unidad </br></br>
				</c:forEach>
				</br>
				<h2><label><c:out value="Precio total: ${precioTotalReceta} €"/></label></h2>
</petclinic:layout>