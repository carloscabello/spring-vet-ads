<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<petclinic:layout pageName="Mostrar Factura">
				
				<h1><label>Factura:</label></h1>
				<label>Fecha emisión: </label><c:out value="${factura.fechaEmision}"/></br>
				<h2><label>Recetas: </label></h2><c:forEach items="${factura.recetas}" var="recetas">
                <fmt:formatDate value="${recetas.fechaRealizacion}" type="date" pattern="dd/MM/yyyy hh:mm a"/></br>
                </c:forEach></br>
			    <label>Pagada: </label>
			    <c:choose>
			    <c:when test="${factura.esPagado==true}">
			    Sí
			    </c:when>
			    <c:otherwise>
			    No
			    </c:otherwise>
			    </c:choose>
			    </br></br>
				<c:if test= "${lineasFactura.size()>0}" > 
				<h2><label>Productos:</label></h2></br>
				<c:forEach items="${lineasFactura}" var="lineaFactura">
				<label><c:out value="${lineaFactura.producto.name}"/></label></br>
				Cantidad: <c:out value="${lineaFactura.cantidad}"/> unidades </br>
				Precio: <c:out value="${lineaFactura.precioVenta}"/> por unidad </br></br>
				</c:forEach>
				</c:if>
				</br>
				<h2><label><c:out value="Precio total: ${precioTotalFactura} €"/></label></h2>
</petclinic:layout>