<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<petclinic:layout pageName="Lista productos">

<c:if test="${message != null}"><c:out value="${message}"/></c:if>

	<table id="productosTable" class="table table-striped">
        <thead>
        <tr>
        	<th style="width: 240px;">Nombre</th>
            <th style="width: 180px;">Precio actual</th>
            <th style="width: 200px;">Cantidad en almacén</th>
            <th style="width: 120px;">Disponible</th>
            <th style="width: 160px;">Necesita receta</th>
            <th style="width: 160px;"></th>
            <th style="width: 160px;"></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${productos}" var="producto">
            <tr>
            	<td>
                    <c:out value="${producto.name} "/>
                </td> 
                <td>
                    <c:out value="${producto.precio}"/>
                </td>
                <td>
                    <c:out value="${producto.cantidad}"/>
                </td>
                <td>
                    <c:choose>
                    <c:when test="${producto.esDisponible==true}">
                    <img height="25px" alt="Sí" src="/resources/images/tickverde.png"/>
                    </c:when>
                    <c:otherwise>
                    <img height="25px" alt="No" src="/resources/images/cruzroja.png"/>
                    </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                    <c:when test="${producto.necesitaReceta==true}">
                    <img height="25px" alt="Sí" src="/resources/images/tickverde.png"/>
                    </c:when>
                    <c:otherwise>
                    <img height="25px" alt="No" src="/resources/images/cruzroja.png"/>
                    </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <spring:url value="/producto/{productoId}/edit" var="productoEditUrl">
                        <spring:param name="productoId" value="${producto.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(productoEditUrl)}"><input class="btn btn-default" type="button" value="Editar producto"/></a>
                </td>
                <td>
                    <spring:url value="/producto/{productoId}/delete" var="productoDeleteUrl">
                        <spring:param name="productoId" value="${producto.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(productoDeleteUrl)}"><input class="btn btn-default" type="button" value="Borrar producto"/></a>
                </td>   
            </tr>
        </c:forEach>
        </tbody>
    </table>
    
    <a href="/producto/new"><input class="btn btn-default" type="button" value="Añadir un nuevo producto"/></a>
</petclinic:layout>