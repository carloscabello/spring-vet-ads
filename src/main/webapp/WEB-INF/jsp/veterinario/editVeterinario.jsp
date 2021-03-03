<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

<petclinic:layout pageName="veterinario">

<form:form modelAttribute="veterinario" class="form-horizontal" id="add-veterinario-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Dni" name="dni"/>
            <petclinic:inputField label="Nombre" name="firstName"/>
            <petclinic:inputField label="Apellidos" name="lastName"/>
            <petclinic:inputField label="Teléfono" name="telephone"/>
            <petclinic:inputField label="Correo" name="mail"/>
            <petclinic:inputField label="Provincia" name="province"/>
            <petclinic:inputField label="Ciudad" name="city"/>
            <petclinic:inputField label="Nombre de usuario" name="user.username"/>
            <petclinic:inputField label="Contraseña" name="user.password"/>
            <label>Tipo/s de ganado:</label>
            <select id="tiposGanado" name="tiposGanado" multiple required>
               <c:forEach items="${todosTiposG}" var="ganado">
               <option value="${ganado.id}"><c:out value="${ganado.tipoGanado}"/></option>
               </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                        <button class="btn btn-default" type="submit">Crear cuenta</button>
            </div>
        </div>
    </form:form>
</petclinic:layout>