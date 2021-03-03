<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="producto">

<jsp:body>
Producto: <c:out value="${producto.name}"/>

<form:form modelAttribute="producto" class="form-horizontal" id="edit-producto-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Cantidad" name="cantidad"/>
            <petclinic:inputField label="Precio" name="precio"/>
            <br></br>
            <label>¿El producto necesita receta para ser vendido?</label><input <c:if test="${producto.necesitaReceta}">checked</c:if> type="checkbox" id="necesitaReceta" name="necesitaReceta"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            <button class="btn btn-default" type="submit">Editar producto</button>    
            </div>
        </div>
    </form:form>
</jsp:body>
</petclinic:layout>